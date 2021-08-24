package com.jn.easyjson.jackson.ext;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.jn.easyjson.jackson.Jacksons;
import com.jn.easyjson.jackson.deserializer.CustomizedBeanDeserializer;
import com.jn.langx.util.jar.JarVersionMismatchedException;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @since 3.2.2
 */
public class EasyJsonBeanDeserializerBuilder extends BeanDeserializerBuilder {
    private static final Logger logger = LoggerFactory.getLogger(EasyJsonBeanDeserializerBuilder.class);
    /**
     * @since 3.2.3
     */
    private DeserializationConfig __config;
    /**
     * @since 3.2.3
     */
    private DeserializationContext __context;
    /**
     * jackson.version >= 2.9.0 时有值
     *
     * @since 3.2.3
     */
    private static final Field _config_field;
    /**
     * jackson.version >= 2.9.0 时有值
     *
     * @since 3.2.3
     */
    private static final Field _context_field;
    /**
     * jackson.version >= 2.9.0 时有值
     *
     * @since 3.2.3
     */
    private static final Method _collectAliases_method;
    /**
     * jackson.version >= 2.9.0 时有值
     *
     * @since 3.2.3
     */
    private static final Method BeanPropertyMap_construct_3args_method;

    static {
        _config_field = Reflects.getDeclaredField(BeanDeserializerBuilder.class, "_config");
        if (_config_field != null) {
            _config_field.setAccessible(true);
        }
        _context_field = Reflects.getDeclaredField(BeanDeserializerBuilder.class, "_context");
        if (_context_field != null) {
            _context_field.setAccessible(true);
        }
        _collectAliases_method = Reflects.getDeclaredMethod(BeanDeserializerBuilder.class, "_collectAliases", new Class[]{Collection.class});
        if (_collectAliases_method != null) {
            _collectAliases_method.setAccessible(true);
        }
        BeanPropertyMap_construct_3args_method = Reflects.getDeclaredMethod(BeanPropertyMap.class, "construct", new Class[]{Collection.class, boolean.class, Map.class});
        if (BeanPropertyMap_construct_3args_method != null) {
            BeanPropertyMap_construct_3args_method.setAccessible(true);
        }
    }


    public EasyJsonBeanDeserializerBuilder(BeanDescription beanDesc, DeserializationContext ctx) {
        this(createBeanDeserializerBuilder(beanDesc, ctx));
        setDeserializationContext(ctx);
        setDeserializationConfig(ctx.getConfig());
    }

    /**
     * jackson v2.9.0 +
     * BeanDeserializerBuilder(BeanDescription beanDesc, DeserializationContext ctx)
     *
     * <p>
     * jackson v2.6.0 ~ v2.8.x
     * BeanDeserializerBuilder(BeanDescription beanDesc,  DeserializationConfig config)
     */
    private static BeanDeserializerBuilder createBeanDeserializerBuilder(BeanDescription beanDesc, DeserializationContext ctx) {
        BeanDeserializerBuilder builder = null;
        boolean greatThan_version2_9_0 = Jacksons.getCurrentVersion().compareTo(new Version(2, 9, 0, null)) >= 0;
        if (greatThan_version2_9_0) {
            builder = Reflects.newInstance(BeanDeserializerBuilder.class, new Class[]{BeanDescription.class, DeserializationContext.class}, beanDesc, ctx);
        } else {
            builder = Reflects.newInstance(BeanDeserializerBuilder.class, new Class[]{BeanDescription.class, DeserializationConfig.class}, beanDesc, ctx.getConfig());
        }
        if (builder == null) {
            logger.error("please check versions of the jackson libraries: jackson-core, jackson-databind");
            throw new JarVersionMismatchedException("jackson-*");
        }
        return builder;
    }

    /**
     * @since 3.2.3
     */

    public EasyJsonBeanDeserializerBuilder(BeanDeserializerBuilder src) {
        super(src);
    }

    /**
     * @since 3.2.3
     */
    private void setDeserializationConfig(DeserializationConfig config) {
        this.__config = config;
        if (_config_field != null) {
            Reflects.setFieldValue(_config_field, this, this.__config, true, true);
        }
    }

    /**
     * @since 3.2.3
     */
    private DeserializationConfig getDeserializationConfig() {
        return this.__config;
    }

    /**
     * @since 3.2.3
     */
    private void setDeserializationContext(DeserializationContext context) {
        this.__context = context;
        if (_context_field != null) {
            Reflects.setFieldValue(_context_field, this, this.__context, true, true);
        }
    }

    /**
     * @since 3.2.3
     */
    private DeserializationContext getDeserializationContext() {
        return this.__context;
    }

    /**
     * @since 3.2.2
     */
    @Override
    public JsonDeserializer<?> build() {
        Collection<SettableBeanProperty> props = _properties.values();
        BeanPropertyMap propertyMap = createPropertyMap(props);

        propertyMap.assignIndexes();

        // view processing must be enabled if:
        // (a) fields are not included by default (when deserializing with view), OR
        // (b) one of properties has view(s) to included in defined
        boolean anyViews = !getDeserializationConfig().isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION);

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

    /**
     * @since 3.2.3
     */
    private BeanPropertyMap createPropertyMap(Collection<SettableBeanProperty> props) {
        boolean caseInsensitive = getDeserializationConfig().isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        if (BeanPropertyMap_construct_3args_method != null) {
            Reflects.invoke(BeanPropertyMap_construct_3args_method, null, new Object[]{props, caseInsensitive, constructBeanPropertyMap(props)}, true, true);
        }
        return BeanPropertyMap.construct(props, getDeserializationConfig().isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES));
    }

    /**
     * @since 3.2.3
     */
    protected Map<String, List<PropertyName>> constructBeanPropertyMap(Collection<SettableBeanProperty> props) {
        if (_collectAliases_method != null) {
            return (Map<String, List<PropertyName>>) Reflects.invoke(_collectAliases_method, this, new Object[]{props}, true, true);
        }
        return Collections.emptyMap();
    }
}
