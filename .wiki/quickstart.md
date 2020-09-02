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
3. 

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
树结构的API抽象。提供了 



