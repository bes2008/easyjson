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

package com.jn.easyjson.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class IOs {
    public static String readAsString(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(256);
        char[] chars = new char[1024];
        int length = -1;
        while ((length = reader.read(chars)) != -1) {
            stringBuilder.append(chars, 0, length);
        }
        return stringBuilder.toString();
    }

    public static String readAsString(InputStream reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(256);
        byte[] bytes = new byte[1024];
        int length = -1;
        while ((length = reader.read(bytes)) != -1) {
            stringBuilder.append(new String(bytes, 0, length));
        }
        return stringBuilder.toString();
    }

}
