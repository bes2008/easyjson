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

package com.alibaba.fastjson.support.spring.annotation;

import java.lang.annotation.*;

/**
 * <pre>
 * 一个放置到 {@link org.springframework.stereotype.Controller Controller}方法上的注解.
 * 设置返回对象针对某个类需要排除或者包括的字段
 * 例如：
 * <code>&#064;FastJsonView(exclude = {&#064;FastJsonFilter(clazz = JSON.class,props = {"data"})})</code>
 *
 * </pre>
 *
 * @author yanquanyu
 * @author liuming
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FastJsonView {
    FastJsonFilter[] include() default {};

    FastJsonFilter[] exclude() default {};
}
