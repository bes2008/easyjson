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

package net.minidev.json.writer;


public class FakeMapper extends JsonReaderI<Object> {
	private FakeMapper() {
		super(null);
	}

	public static JsonReaderI<Object> DEFAULT = new FakeMapper();

	@Override
	public JsonReaderI<?> startObject(String key) {
		return this;
	}

	@Override
	public JsonReaderI<?> startArray(String key) {
		return this;
	}

	@Override
	public void setValue(Object current, String key, Object value) {
	}

	@Override
	public void addValue(Object current, Object value) {
	}

	@Override
	public Object createObject() {
		return null;
	}

	@Override
	public Object createArray() {
		return null;
	}
}
