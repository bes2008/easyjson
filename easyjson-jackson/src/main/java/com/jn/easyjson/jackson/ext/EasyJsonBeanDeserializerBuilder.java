package com.jn.easyjson.jackson.ext;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.jn.easyjson.jackson.deserializer.CustomizedBeanDeserializer;

import java.util.Collection;

public class EasyJsonBeanDeserializerBuilder extends BeanDeserializerBuilder {

    public EasyJsonBeanDeserializerBuilder(BeanDescription beanDesc, DeserializationConfig config) {
        super(beanDesc, config);
    }

    public EasyJsonBeanDeserializerBuilder(BeanDeserializerBuilder src) {
        super(src);
    }

    @Override
    public JsonDeserializer<?> build() {
        Collection<SettableBeanProperty> props = _properties.values();
        BeanPropertyMap propertyMap = BeanPropertyMap.construct(props, _caseInsensitivePropertyComparison);
        propertyMap.assignIndexes();

        // view processing must be enabled if:
        // (a) fields are not included by default (when deserializing with view), OR
        // (b) one of properties has view(s) to included in defined
        boolean anyViews = !_defaultViewInclusion;

        if (!anyViews) {
            for (SettableBeanProperty prop : props) {
                if (prop.hasViews()) {
                    anyViews = true;
                    break;
                }
            }
        }

        // one more thing: may need to create virtual ObjectId property:
        if (_objectIdReader != null) {
            /* 18-Nov-2012, tatu: May or may not have annotations for id property;
             *   but no easy access. But hard to see id property being optional,
             *   so let's consider required at this point.
             */
            ObjectIdValueProperty prop = new ObjectIdValueProperty(_objectIdReader, PropertyMetadata.STD_REQUIRED);
            propertyMap = propertyMap.withProperty(prop);
        }

        return new CustomizedBeanDeserializer(this,
                _beanDesc, propertyMap, _backRefProperties, _ignorableProps, _ignoreAllUnknown,
                anyViews);
    }
}
