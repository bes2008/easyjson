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

package com.jn.easyjson.core.node;

import com.jn.easyjson.core.JsonTreeNode;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

/**
 * 和JsonTreeNodes 配合使用，用来自定义如何将一个对象转换为JsonTreeNode
 */
public interface ToJsonTreeNodeMapper {

    JsonTreeNode mapping(@Nullable Object object);

    boolean isAcceptable(@NonNull Object object);
}
