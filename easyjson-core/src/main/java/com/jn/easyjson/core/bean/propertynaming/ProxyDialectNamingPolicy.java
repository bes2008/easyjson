package com.jn.easyjson.core.bean.propertynaming;

import com.jn.easyjson.core.codec.dialect.CodecConfigurationRepository;
import com.jn.easyjson.core.codec.dialect.CodecConfigurationRepositoryService;
import com.jn.easyjson.core.codec.dialect.DialectIdentify;
import com.jn.easyjson.core.codec.dialect.PropertyCodecConfiguration;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;

import java.lang.reflect.Member;

public class ProxyDialectNamingPolicy implements BeanPropertyNamingPolicy {
    private DialectIdentify proxyDialect;
    private BeanPropertyNamingPolicy defaultPolicy;

    public ProxyDialectNamingPolicy(@NonNull DialectIdentify proxyDialect, @NonNull BeanPropertyNamingPolicy defaultPolicy) {
        Preconditions.checkNotNull(proxyDialect);
        Preconditions.checkNotNull(defaultPolicy);
        this.proxyDialect = proxyDialect;
        this.defaultPolicy = defaultPolicy;
    }

    @Override
    public String translateName(Member member, String property) {
        if (member != null) {
            CodecConfigurationRepository configurationRepository = CodecConfigurationRepositoryService.getInstance().getCodecConfigurationRepository(proxyDialect);
            if (configurationRepository != null) {
                PropertyCodecConfiguration propertyCodecConfiguration = configurationRepository.getPropertyCodeConfiguration(member.getDeclaringClass(), property);
                if (propertyCodecConfiguration != null) {
                    String alias = propertyCodecConfiguration.getAlias();
                    if (Strings.isNotBlank(alias)) {
                        return alias;
                    }
                }
            }
        }
        return defaultPolicy.translateName(member, property);
    }

    @Override
    public String getName() {
        return StringTemplates.formatWithPlaceholder("Proxy Dialect ({})", defaultPolicy.getName());
    }
}
