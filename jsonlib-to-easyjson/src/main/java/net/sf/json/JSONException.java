/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sf.json;

public class JSONException extends RuntimeException {
    private static final long serialVersionUID = 6995087065217051815L;

    public JSONException() {
        super();
    }

    public JSONException(String msg) {
        super(msg, null);
    }

    public JSONException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JSONException(Throwable cause) {
        super((cause == null ? null : cause.toString()), cause);
    }
}