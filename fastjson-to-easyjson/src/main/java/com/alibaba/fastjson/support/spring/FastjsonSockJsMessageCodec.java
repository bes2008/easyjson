/*
 * Copyright 2019 the original author or authors.
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

package com.alibaba.fastjson.support.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import org.springframework.web.socket.sockjs.frame.AbstractSockJsMessageCodec;

import java.io.IOException;
import java.io.InputStream;

public class FastjsonSockJsMessageCodec extends AbstractSockJsMessageCodec {

    public String[] decode(String content) throws IOException {
        return JSON.parseObject(content, String[].class);
    }

    public String[] decodeInputStream(InputStream content) throws IOException {
        return JSON.parseObject(content, String[].class);
    }

    @Override
    protected char[] applyJsonQuoting(String content) {
        SerializeWriter out = new SerializeWriter();
        try {
            JSONSerializer serializer = new JSONSerializer(out);
            serializer.write(content);
            return out.toCharArrayForSpringWebSocket();
        } finally {
            out.close();
        }
    }

}
