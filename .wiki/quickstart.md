# Quick Start
easyjson 致力于打造JSON界的slf4j，为了方便JSON相关的开发而生。既然是类似于slf4j，目前也做到了。

提供了如下特性：
1. 提供了JSON的门面，具体实现交给现有的JSON库
2. 自动查找JSON库实现。目前已实现的底层JSON库有 fastjson, gson, jackson。
3. 支持将某个JSON通过easyjson适配。支持将 fastjson, boonjson, android-json, org.json, json-simple, moshi, progsbase-json, minimal-json, json-smart, json-lib等众多JSON库通过easyjson适配给fastjson, gson, jackson

基于上述特性，做到了：
1. 随时随地切换JSON库，再有安全漏洞时，也不用加班了。
2. 已有的使用fastjson的项目，不必修改代码，无缝切换到gson, jackson
3. 编码时，可以使用easyjson的API，也可以使用任何一种你喜欢的且支持适配的json库的API。

## Modules
1. easyjson-core: core api, 提供了最基础最核心的抽象，类似于 slf4j-api模块。
2. easyjson-XXX： 提供了基于XXX JSON的实现。
    + easyjson-gson: 使用gson作为底层实现
    + easyjson-fastjson: 使用fastjson作为底层实现
    + easyjson-jackson: 使用jackson作为底层实现
3. XXX-to-easyjson: 提供了将XXX json库通过easyjson适配给其他的JSON库。
    + fastjson-to-easyjson: 将fastjson 适配给其他的JSON库
    + androidjson-to-easyjson: 将Android下常用的JSON库适配给其他的JSON库
    + boonjson-to-easyjson: 将boon json适配给其他的JSON库
    + jsonlib-to-easyjson: 将json-lib适配给其他的JSON库
    + jsonsmart-to-easyjson: 将json smart适配给其他的JSON库
    + minimaljson-to-easyjson: 将minimal json适配给其他的JSON库
    + moshi-to-easyjson: 将moshi 适配给其他的JSON库
    + orgjson-to-easyjson: 将org.json适配给其他的JSON库
    + progsbase-to-easyjson: 将progsbase适配给其他的JSON库
    + simplejson-to-easyjson: 将simple json适配给其他的JSON库

## Core API

### com.jn.easyjson.core.JSON

API上参考了GSON的API，采用极简主义。

```text
/**
 * 把Java Object 转换为 json String
 */   
public String toJson(Object obj);
public String toJson(Object obj, Type type);


/**
 * 将json string 转换为Java对象
 */
public <T> T fromJson(String json, Class<T> classOfT);
public <T> T fromJson(String json, Type typeOfT);
public JsonTreeNode fromJson(String json);

```
### JSONBuilder & JSONFactory 

JSONBuilder用于构建JSON对象，提供了类似于Gson的里的GsonBuilder的功能。
所以这里就不过多的阐述了。

在JSONFactory API的基础上，又提供了更为简便的JSONFactory API，支持创建singleton, prototype 两种模式的JSON对象。

### JsonTreeNode
树结构的API抽象。提供了 ObjectNode, ArrayNode, NullNode, PrimitiveNode等基本结构。


## 安装说明

由于easyjson本身不提供JSON解析、序列化操作，而是依赖于其他JSON库实现。
所以要选择好你要用的JSON实现库，然后引入相应的适配器包即可，easyjson-core会被自动的引入。

```text
如果是 要使用jackson， 那么项目中肯定要自行引入 jackson相关的jar，另外需要引入 easyjson-jackson。
如果是 要使用fastjson，那么项目中肯定要自行引入fastjson相关的jar，另外需要引入 easyjson-fastjson。
如果是 要使用gson，那么项目中肯定要自行引入gson相关的jar，另外需要引入 easyjson-gson。
```

如果项目中已经用了一个json库，想要在不修改代码的情况下，切换底层实现的话，需要在上面的基础上，额外引入 xxx-to-easyjson。
```text
例如，fastjson可能在某一段时间内，爆出来的漏洞比较多，你想要切换为gson，或者jackson。
那么就先引入 gson（或者jackson）、 easyjon-gson （或者easyjson-jackson）,然后引入 fastjson-to-easyjson，最后移除fastjson本身的包。
```



