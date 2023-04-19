package com.jn.easyjson.jackson.ext;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.jn.easyjson.jackson.serializer.NullSerializer;
import com.jn.langx.annotation.Order;
import com.jn.langx.util.comparator.OrderedComparator;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.reflect.Reflects;

public class EasyJsonJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {
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
        Class<?> cls1 = setter1.getRawParameterType(0);
        Class<?> cls2 = setter2.getRawParameterType(0);

        int orderDelta = setterOrderComparator.compare(setter1, setter2);
        if (orderDelta == 0) {
            if (cls1 == cls2) {
                return setter1;
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
