package com.jn.easyjson.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Predicate;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CustomizedBeanDeserializer extends BeanDeserializer {
    public CustomizedBeanDeserializer(BeanDeserializerBuilder builder, BeanDescription beanDesc, BeanPropertyMap properties, Map<String, SettableBeanProperty> backRefs, HashSet<String> ignorableProps, boolean ignoreAllUnknown, boolean hasViews) {
        super(builder, beanDesc, properties, backRefs, ignorableProps, ignoreAllUnknown, hasViews);
    }

    public CustomizedBeanDeserializer(BeanDeserializerBase src) {
        super(src);
    }

    public CustomizedBeanDeserializer(BeanDeserializerBase src, boolean ignoreAllUnknown) {
        super(src, ignoreAllUnknown);
    }

    public CustomizedBeanDeserializer(BeanDeserializerBase src, NameTransformer unwrapper) {
        super(src, unwrapper);
    }

    public CustomizedBeanDeserializer(BeanDeserializerBase src, ObjectIdReader oir) {
        super(src, oir);
    }

    public CustomizedBeanDeserializer(BeanDeserializerBase src, HashSet<String> ignorableProps) {
        super(src, ignorableProps);
    }

    @Override
    protected void handleUnknownVanilla(JsonParser parser, DeserializationContext ctxt, Object bean, final String propName) throws IOException {

        List<SettableBeanProperty> props = Pipeline.of(_beanProperties).filter(new Predicate<SettableBeanProperty>() {
            @Override
            public boolean test(SettableBeanProperty settableBeanProperty) {
                PropertyName propertyName = settableBeanProperty.getFullName();
                return Strings.equalsIgnoreCase(propertyName.getSimpleName(), propName);
            }
        }).asList();
        SettableBeanProperty prop = null;
        if (props.size() == 1) {
            prop = props.get(0);
        }
        if (prop != null) { // normal case
            try {
                bean = prop.deserializeSetAndReturn(parser, ctxt, bean);
            } catch (Exception e) {
                wrapAndThrow(e, bean, propName, ctxt);
            }
        } else {
            super.handleUnknownVanilla(parser, ctxt, bean, propName);
        }
    }
}
