/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the LGPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.easyjson.core.codec.config;

import com.jn.langx.configuration.AbstractConfigurationRepository;
import com.jn.langx.configuration.ConfigurationWriter;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.ConcurrentReferenceHashMap;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.reflect.reference.ReferenceType;

public class CodecConfigurationRepository<T extends CodecConfiguration> extends AbstractConfigurationRepository<T, ClassLoaderCodecConfigurationLoader<T>, ConfigurationWriter<T>> {
    private ConcurrentReferenceHashMap<ClassLoader, AbstractConfigurationRepository<T, ClassLoaderCodecConfigurationLoader<T>, ConfigurationWriter<T>>> repositories = new ConcurrentReferenceHashMap<ClassLoader, AbstractConfigurationRepository<T, ClassLoaderCodecConfigurationLoader<T>, ConfigurationWriter<T>>>(1000, 0.95f, Runtime.getRuntime().availableProcessors(), ReferenceType.WEAK,ReferenceType.WEAK);

    @Override
    public void setReloadIntervalInSeconds(int reloadIntervalInSeconds) {
        super.setReloadIntervalInSeconds(-1);
        Collects.forEach(repositories, new Consumer2<ClassLoader, AbstractConfigurationRepository>() {
            @Override
            public void accept(ClassLoader cl, AbstractConfigurationRepository repository) {
                repository.setReloadIntervalInSeconds(-1);
            }
        });
    }

}
