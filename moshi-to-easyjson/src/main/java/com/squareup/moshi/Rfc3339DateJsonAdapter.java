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
package com.squareup.moshi;

import java.io.IOException;
import java.util.Date;

/**
 * @deprecated this class moved to avoid a package name conflict in the Java Platform Module System.
 * The new class is {@code com.squareup.moshi.adapters.Rfc3339DateJsonAdapter}.
 */
public final class Rfc3339DateJsonAdapter extends JsonAdapter<Date> {
    com.squareup.moshi.adapters.Rfc3339DateJsonAdapter delegate
            = new com.squareup.moshi.adapters.Rfc3339DateJsonAdapter();

    @Override
    public Date fromJson(JsonReader reader) throws IOException {
        return delegate.fromJson(reader);
    }

    @Override
    public void toJson(JsonWriter writer, Date value) throws IOException {
        delegate.toJson(writer, value);
    }
}
