package com.jn.easyjson.jackson.ext;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.jn.easyjson.jackson.serializer.NullSerializer;
import com.jn.langx.annotation.Order;
import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.comparator.OrderedComparator;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Function2;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.spi.CommonServiceProvider;

public class EasyJsonJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {
    private static GenericRegistry<JacksonSetterConflictSelector> setterConflictSelectorGenericRegistry = new GenericRegistry<JacksonSetterConflictSelector>();

    static {
        Pipeline.<JacksonSetterConflictSelector>of(new CommonServiceProvider<JacksonSetterConflictSelector>().get(JacksonSetterConflictSelector.class)).forEach(new Consumer<JacksonSetterConflictSelector>() {
            @Override
            public void accept(JacksonSetterConflictSelector selector) {
                setterConflictSelectorGenericRegistry.register(selector);
            }
        });
    }

    @Override
    public Object findNullSerializer(Annotated a) {
        JsonSerialize ann = _findAnnotation(a, JsonSerialize.class);
        if (ann != null) {
            @SuppressWarnings("rawtypes")
            Class<? extends JsonSerializer> serClass = ann.nullsUsing();
            if (serClass != JsonSerializer.None.class) {
                return serClass;
            }
        } else {
            return new NullSerializer();
        }
        return null;
    }

    private OrderedComparator<AnnotatedMethod> setterOrderComparator = new OrderedComparator<AnnotatedMethod>(new Supplier<AnnotatedMethod, Integer>() {
        @Override
        public Integer get(AnnotatedMethod annotatedMethod) {
            Order order = Reflects.getAnnotation(annotatedMethod.getAnnotated(), Order.class);
            if (order != null) {
                return order.value();
            }
            return Integer.MAX_VALUE;
        }
    });


    @Override
    public AnnotatedMethod resolveSetterConflict(MapperConfig<?> config, AnnotatedMethod setter1, AnnotatedMethod setter2) {
        final Class<?> cls1 = setter1.getRawParameterType(0);
        final Class<?> cls2 = setter2.getRawParameterType(0);

        int orderDelta = setterOrderComparator.compare(setter1, setter2);
        if (orderDelta == 0) {
            if (cls1 == cls2) {
                return setter1;
            }

            Class exceptedClass = Pipeline.of(setterConflictSelectorGenericRegistry.instances())
                    .firstMap(new Function2<Integer, JacksonSetterConflictSelector, Class>() {
                        @Override
                        public Class apply(Integer index, JacksonSetterConflictSelector selector) {
                            return selector.select(cls1, cls2);
                        }
                    });
            if (exceptedClass != null) {
                return exceptedClass == cls1 ? setter1 : setter2;
            }

            if (cls1.isPrimitive()) {
                if (!cls2.isPrimitive()) {
                    return setter1;
                }
            } else if (cls2.isPrimitive()) {
                return setter2;
            }

            if (cls1 == String.class) {
                return setter1;
            } else if (cls2 == String.class) {
                return setter2;
            }

            return null;
        } else if (orderDelta > 0) {
            return setter2;
        } else {
            return setter1;
        }
    }
}
