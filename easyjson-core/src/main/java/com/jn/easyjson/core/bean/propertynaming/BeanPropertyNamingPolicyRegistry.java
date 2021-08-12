package com.jn.easyjson.core.bean.propertynaming;

import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.registry.GenericRegistry;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;

import java.util.ServiceLoader;

/**
 * @since 3.2.2
 */
public class BeanPropertyNamingPolicyRegistry extends GenericRegistry<BeanPropertyNamingPolicy> {

    public BeanPropertyNamingPolicyRegistry() {
        init();
    }

    @Override
    protected void doInit() throws InitializationException {
        Collects.forEach(ServiceLoader.load(BeanPropertyNamingPolicy.class), new Consumer<BeanPropertyNamingPolicy>() {
            @Override
            public void accept(BeanPropertyNamingPolicy fieldNamingPolicy) {
                register(fieldNamingPolicy);
            }
        });
    }

    public static final BeanPropertyNamingPolicyRegistry INSTANCE = new BeanPropertyNamingPolicyRegistry();
}
