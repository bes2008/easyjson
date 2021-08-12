# Fastjson Filter

有几大类过滤，它们的作用分别是：

```
    PropertyPreFilter：根据 PropertyName 判断是否序列化
    PropertyFilter：根据 PropertyName 和 PropertyValue 来判断是否序列化

    NameFilter: 用于对JavaBean的属性名进行处理。譬如说 首字母大写，驼峰命名法
    ValueFilter: 用于对 Java Bean的值进行处理

    BeforeFilter：序列化时在最前添加内容
    AfterFilter：序列化时在最后添加内容

```




