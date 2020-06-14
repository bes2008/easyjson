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

package com.jn.easyjson.core.codec.dialect;

import com.jn.langx.annotation.Singleton;
import com.jn.langx.lifecycle.Initializable;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

@Singleton
public class CodecConfigurationRepositoryService implements Initializable {
    private static final Map<JsonLibraryIdentify, CodecConfigurationRepository> repositoryMap = new HashMap<JsonLibraryIdentify, CodecConfigurationRepository>();
    private static final CodecConfigurationRepositoryService INSTANCE = new CodecConfigurationRepositoryService();

    private volatile boolean inited = false;
    private CodecConfigurationRepositoryService(){
        init();
    }

    public static CodecConfigurationRepositoryService getInstance(){
        return INSTANCE;
    }

    @Override
    public void init() throws InitializationException {
        if(!inited){
            inited = true;
            ServiceLoader<CodecConfigurationRepository> serviceLoader = ServiceLoader.load(CodecConfigurationRepository.class);
            Collects.forEach(serviceLoader, new Consumer<CodecConfigurationRepository>() {
                @Override
                public void accept(CodecConfigurationRepository codecConfigurationRepository) {
                    register(codecConfigurationRepository);
                }
            });
        }
    }

    public void register(CodecConfigurationRepository codecConfigurationRepository){
        repositoryMap.put(codecConfigurationRepository.getJsonLibraryIdentify(), codecConfigurationRepository);
    }
}
