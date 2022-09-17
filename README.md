[![JSON](https://www.json.org/img/json160.gif)](http://www.json.org/)


[![License](https://img.shields.io/badge/license-Apache2.0-green.svg)](https://github.com/fangjinuo/sqlhelper/blob/master/LICENSE)

[![Build Status](https://www.travis-ci.com/fangjinuo/easyjson.svg?branch=master)](https://travis-ci.com/fangjinuo/easyjson)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/dbf315b97bf443f8815c8dbaf6bbf358)](https://www.codacy.com/manual/fs1194361820/easyjson?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=fangjinuo/easyjson&amp;utm_campaign=Badge_Grade)

[![gson](https://img.shields.io/badge/gson-2.x-green.svg)](https://github.com/google/gson)
[![fastjson](https://img.shields.io/badge/fastjson-1.2.x-green.svg)](https://github.com/alibaba/fastjson)
[![jackson](https://img.shields.io/badge/jackson-2.6.x+-green.svg)](https://github.com/FasterXML/jackson)

[![JDK](https://img.shields.io/badge/JDK-1.6+-green.svg)](https://www.oracle.com/technetwork/java/javase/downloads/index.html)

## [Github地址](https://github.com/fangjinuo/easyjson)
## [Gitee地址](https://gitee.com/fangjinuo/easyjson)

[![maven](https://img.shields.io/badge/maven-3.2.26-green.svg)](https://search.maven.org/search?q=g:io.github.bes2008.solutioneasyjson%20AND%20v:3.2.26)



# easyjson
Provides a unified JSON access API, you can adapter any JSON library to Gson, Jackson, FastJson with easyjson.

## Modules

+ **easyjson-core**: 这是个核心模块，可以直接去用这个模块的API，就像使用slf4j的logger API一样。
+ **easyjson-XXX**: 使用它可以将其他的JSON库适配给XXX，由XXX来做具体的json操作。
    1. **easyjson-gson**: 使用它可以将其他的JSON库适配给Gson
    2. **easyjson-jackson**: 使用它可以将其他的JSON库适配给Jackson
    3. **easyjson-fastjson**: 使用它可以将其他的JSON库适配给 Fastjson
+ **XXXX-to-easyjson**: 有很多这样的模块，它们是用于将xxx JSON库通过easyjson-core，适配给底层JSON实现库。
    1. **fast-to-easyjson**: 使用它可以将fastjson库通过easyjson适配给 gson, jackson
    2. **gson-to-easyjson**: 使用它可以将fastjson库通过easyjson适配给 fastjson, jackson
    3. **android-to-easyjson**: 使用它可以将其他的Android下的org.json库通过easyjson适配给fastjson, gson, jackson
    4. **boonjson-to-easyjson**: 使用它可以将安卓下的boon-json（io.advantageous.boon.json）库通过easyjson适配给fastjson, gson, jackson
    5. **jsonlib-to-easyjson**: 使用它可以将json-lib （net.sf.json）通过easyjson适配给fastjson, gson, jackson
    6. **jsonsmart-to-easyjson**: 使用它可以将json-smart （net.minidev.json）通过easyjson适配给fastjson, gson, jackson
    7. **minimaljson-to-easyjson**: 使用它可以将minimal-json （com.eclipsesource.json）通过easyjson适配给fastjson, gson, jackson
    8. **moshi-to-easyjson**: 使用它可以将moshi-json （com.squareup.moshi）通过easyjson适配给fastjson, gson, jackson
    9. **orgjson-to-easyjson**: 使用它可以将org.json通过easyjson适配给fastjson, gson, jackson
    10. **progsbase-to-easyjson**: 使用它可以将progsbase json通过easyjson适配给fastjson, gson, jackson
    11. **simplejson-to-easyjson**: 使用它可以将json-simple通过easyjson适配给fastjson, gson, jackson
+ **easyjson-examples**: test cases
+ **easyjson-test**: test cases v2


## Usage
### Key Features
1. Supports **gson**, **fastjson**, **jackson**, **Progsbase** **...**
2. Supports tree model, stream model
3. Control whether serialize null
4. Convert ON|OFF, 1|0 to Boolean
5. Serialize or deserialize a Enum based on custom field
6. Serialize or deserialize a Date using specified pattern | date format
7. Supports custom exclusion rule, for examples: @Ignore, transient, specified modifier ...
8. Get the JSONBuilder with zero configuration
9. Switch the underlying implement without any code change 
10. Supports JDK 1.6+

### Quick Start
#### installation


<table style="border:1px solid #ACE;">
    <thead style="border:1px solid #ACE;">
        <tr>
            <th>easyjson facade</th>
            <th>JSON Implementation</th>
        </tr>
    </thead>
    <tbody style="border:1px solid #ACE;">
        <tr>
           <td>
                <pre>
&lt;dependency>
    &lt;groupId>io.github.bes2008.solutioneasyjson&lt;/groupId>
    &lt;artifactId>easyjson-core&lt;/artifactId>
    &lt;version>${version}/version>
&lt;/dependency>
                </pre>            
            </td>
            <td>
                <pre>
&lt;dependency>
    &lt;groupId>io.github.bes2008.solutioneasyjson&lt;/groupId>
    &lt;artifactId>easyjson-gson&lt;/artifactId>
    &lt;version>${version}</version>
&lt;/dependency>
                </pre>     
                <pre>
&lt;dependency>
    &lt;groupId>io.github.bes2008.solutioneasyjson&lt;/groupId>
    &lt;artifactId>easyjson-jackson&lt;/artifactId>
    &lt;version>${version}/version>
&lt;/dependency>
                </pre>     
                <pre>
&lt;dependency>
    &lt;groupId>io.github.bes2008.solutioneasyjson&lt;/groupId>
    &lt;artifactId>easyjson-fastjson&lt;/artifactId>
    &lt;version>${version}/version>
&lt;/dependency>
                </pre>                                                 
            </td>
        </tr>
    </tbody>
</table>

<table style="border:1px solid #ACE;">
    <thead style="border:1px solid #ACE;">
        <tr>
            <th>JSON Library</th>
            <th>Maven</th>
            <th>version</th>
            <th>Adapter</th>
        </tr>
    </thead>
    <tbody style="border:1px solid #ACE;">
        <tr>
            <td>Android json</td>
            <td>
                <pre>
&lt;dependency>
    &lt;groupId>com.vaadin.external.google&lt;/groupId>
    &lt;artifactId>android-json&lt;/artifactId>
    &lt;version>0.0.20131108.vaadin1&lt;/version>
&lt;/dependency>
                </pre>
            </td>
            <td>0.0.20131108.vaadin</td>
            <td>
                <pre>
&lt;dependency>
    &lt;groupId>io.github.bes2008.solutioneasyjson&lt;/groupId>
    &lt;artifactId>android-to-easyjson&lt;/artifactId>
    &lt;version>${version}&lt;/version>
&lt;/dependency>
                </pre>
            </td>            
        </tr>
        <tr>
            <td>fastjson</td>
            <td>
                <pre>
&lt;dependency>
    &lt;groupId>com.alibaba&lt;/groupId>
    &lt;artifactId>fastjson&lt;/artifactId>
    &lt;version>1.2.58&lt;/version>
&lt;/dependency>
                </pre>
            </td>
            <td>1.2.58</td>
            <td>
                <pre>
&lt;dependency>
    &lt;groupId>io.github.bes2008.solutioneasyjson&lt;/groupId>
    &lt;artifactId>fastjson-to-easyjson&lt;/artifactId>
    &lt;version>${version}&lt;/version>
&lt;/dependency>
                </pre>
            </td> 
        </tr>
        <tr>
            <td>json-lib</td>
            <td>
                <pre>
&lt;dependency>
    &lt;groupId>com.hynnet&lt;/groupId>
    &lt;artifactId>json-lib&lt;/artifactId>
    &lt;version>2.4&lt;/version>
&lt;/dependency>
                </pre>
            </td>
            <td>2.4</td>
            <td>
                <pre>
&lt;dependency>
    &lt;groupId>io.github.bes2008.solutioneasyjson&lt;/groupId>
    &lt;artifactId>jsonlib-to-easyjson&lt;/artifactId>
    &lt;version>${version}&lt;/version>
&lt;/dependency>
                </pre>
            </td>          
        </tr>
        <tr>
            <td>json-smart</td>
            <td>
                <pre>
&lt;dependency>
    &lt;groupId>net.minidev&lt;/groupId>
    &lt;artifactId>json-smart&lt;/artifactId>
    &lt;version>2.3&lt;/version>
&lt;/dependency>
                </pre>
            </td>
            <td>2.3</td>
            <td>
                <pre>
&lt;dependency>
    &lt;groupId>io.github.bes2008.solutioneasyjson&lt;/groupId>
    &lt;artifactId>jsonsmart-to-easyjson&lt;/artifactId>
    &lt;version>${version}&lt;/version>
&lt;/dependency>
                </pre>
            </td>          
        </tr> 
        <tr>
            <td>Eclipse minimal-json</td>
            <td>
                <pre>
&lt;dependency>
    &lt;groupId>com.eclipsesource.minimal-json&lt;/groupId>
    &lt;artifactId>minimal-json&lt;/artifactId>
    &lt;version>0.9.5&lt;/version>
&lt;/dependency>
                </pre>
            </td>
            <td>0.9.5</td>
            <td>
                <pre>
&lt;dependency>
    &lt;groupId>io.github.bes2008.solutioneasyjson&lt;/groupId>
    &lt;artifactId>minimaljson-to-easyjson&lt;/artifactId>
    &lt;version>${version}&lt;/version>
&lt;/dependency>
                </pre>
            </td>          
        </tr>    
        <tr>
            <td>org.json</td>
            <td>
                <pre>
&lt;dependency>
    &lt;groupId>org.json&lt;/groupId>
    &lt;artifactId>json&lt;/artifactId>
    &lt;version>20180813&lt;/version>
&lt;/dependency>
                </pre>
            </td>
            <td>20180813</td>
            <td>
                <pre>
&lt;dependency>
    &lt;groupId>io.github.bes2008.solutioneasyjson&lt;/groupId>
    &lt;artifactId>orgjson-to-easyjson&lt;/artifactId>
    &lt;version>${version}&lt;/version>
&lt;/dependency>
                </pre>
            </td>          
        </tr>           
        <tr>
            <td>Progsbase JSON</td>
            <td>
                <pre>
&lt;dependency>
    &lt;groupId>com.progsbase.libraries&lt;/groupId>
    &lt;artifactId>JSON&lt;/artifactId>
    &lt;version>0.3.9&lt;/version>
&lt;/dependency>
                </pre>
            </td>
            <td>0.3.9</td>
            <td>
                <pre>
&lt;dependency>
    &lt;groupId>io.github.bes2008.solutioneasyjson&lt;/groupId>
    &lt;artifactId>progsbase-to-easyjson&lt;/artifactId>
    &lt;version>${version}&lt;/version>
&lt;/dependency>
                </pre>
            </td>          
        </tr> 
        <tr>
            <td>json-simple</td>
            <td>
                <pre>
&lt;dependency>
    &lt;groupId>com.googlecode.json-simple&lt;/groupId>
    &lt;artifactId>json-simple&lt;/artifactId>
    &lt;version>1.1.1&lt;/version>
&lt;/dependency>
                </pre>
            </td>
            <td>1.1.1</td>
            <td>
                <pre>
&lt;dependency>
    &lt;groupId>io.github.bes2008.solutioneasyjson&lt;/groupId>
    &lt;artifactId>simplejson-to-easyjson&lt;/artifactId>
    &lt;version>${version}&lt;/version>
&lt;/dependency>
                </pre>
            </td>          
        </tr>  
        <tr>
            <td>Boon-json</td>
            <td><pre>
        &lt;dependency>
            &lt;groupId>com.github.advantageous&lt;/groupId>
            &lt;artifactId>boon-json&lt;/artifactId>
            &lt;version>0.5.7-RC1&lt;/version>
        &lt;/dependency>
                </pre></td>
            <td>0.5.7-RC1</td>
            <td><pre>
        &lt;dependency>
            &lt;groupId>io.github.bes2008.solutioneasyjson&lt;/groupId>
            &lt;artifactId>boonjson-to-easyjson&lt;/artifactId>
            &lt;version>${version}&lt;/version>
        &lt;/dependency>
                </pre></td>          
        </tr>    
        <tr>
            <td>Moshi</td>
            <td><pre>
        &lt;dependency>
            &lt;groupId>com.squareup.moshi&lt;/groupId>
            &lt;artifactId>moshi&lt;/artifactId>
            &lt;version>1.9.2&lt;/version>
        &lt;/dependency>
                </pre></td>
            <td>1.8.6+</td>
            <td><pre>
        &lt;dependency>
            &lt;groupId>io.github.bes2008.solutioneasyjson&lt;/groupId>
            &lt;artifactId>moshi-to-easyjson&lt;/artifactId>
            &lt;version>${version}&lt;/version>
        &lt;/dependency>
                </pre></td>          
        </tr>                       
    </tbody>
</table>


#### How to:
   ***@see*** easyjson-examples testcases
   
   
### Contact:
如果遇到问题，可以在Github, Gitee 上提出issue, 也可以在QQ群里询问。

QQ Group: 750929088   
![QQ Group](https://github.com/fangjinuo/sqlhelper/blob/master/_images/qq_group.png)



##  [推广](https://github.com/fangjinuo)
+ langx 系列
    - [langx-js](https://github.com/fangjinuo/langx-js)：TypeScript, JavaScript tools
    - [langx-java](https://github.com/fangjinuo/langx-java): Java tools ，可以替换guava, apache commons-lang,io, hu-tool等
+ [easyjson](https://github.com/fangjinuo/easyjson): 一个通用的JSON库门面，可以无缝的在各个JSON库之间切换，就像slf4j那样。
+ [sqlhelper](https://github.com/fangjinuo/sqlhelper): SQL工具套件（通用分页、DDL Dump、SQLParser、URL Parser、批量操作工具等）。
+ [esmvc](https://github.com/fangjinuo/es-mvc): ElasticSearch 通用客户端，就像MyBatis Mapper那样顺滑
+ [redisclient](https://github.com/fangjinuo/redisclient): 基于Spring RestTemplate提供的客户端
+ [audit](https://github.com/fangjinuo/audit)：通用的Java应用审计框架


## 鸣谢
最后，感谢 Jetbrains 提供免费License，方便了开源项目的发展。

[![Jetbrains](https://github.com/fangjinuo/sqlhelper/blob/master/_images/jetbrains.png)](https://www.jetbrains.com/zh-cn/)
