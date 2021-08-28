package com.jn.easyjson.fastjson.filter.namefilter;

import com.alibaba.fastjson.serializer.NameFilter;
import com.jn.easyjson.core.bean.propertynaming.BeanPropertyNamingPolicy;
/**
 * @since 3.2.2
 */
public class FastjsonPropertyNamingFilter implements NameFilter {
    private BeanPropertyNamingPolicy policy;

    public FastjsonPropertyNamingFilter(BeanPropertyNamingPolicy propertyNamingPolicy) {
        this.policy = propertyNamingPolicy;
    }

    @Override
    public String process(Object object, String fieldName, Object fieldValue) {
        return this.policy.translateName(null,fieldName);
    }
}
