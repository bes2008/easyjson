/*
 *  Copyright 2019 the original author or authors.
 *
 *  Licensed under the LGPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.github.fangjinuo.easyjson.fastjson.ext;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.util.JavaBeanInfo;

import java.lang.reflect.Type;

public class EasyJsonJavaBeanDeserializer extends JavaBeanDeserializer {
    public EasyJsonJavaBeanDeserializer(ParserConfig config, Class<?> clazz) {
        this(config, clazz, clazz);
    }

    public EasyJsonJavaBeanDeserializer(ParserConfig config, Class<?> clazz, Type type) {
        super(config, clazz, type);
    }

    public EasyJsonJavaBeanDeserializer(ParserConfig config, JavaBeanInfo beanInfo) {
        super(config, config instanceof EasyJsonParserConfig ? ((EasyJsonParserConfig)config).filterFields(beanInfo): beanInfo);
    }
}
