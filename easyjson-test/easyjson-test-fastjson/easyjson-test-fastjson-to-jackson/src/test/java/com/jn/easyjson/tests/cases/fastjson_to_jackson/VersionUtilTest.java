package com.jn.easyjson.tests.cases.fastjson_to_jackson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.easyjson.FastEasyJsons;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.jn.easyjson.core.JsonHandler;
import com.jn.easyjson.jackson.JacksonAdapter;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author shenxiuqiang
 * @date 2019/5/309:40 AM
 */
public class VersionUtilTest {
    private static final String STANDARD_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @org.testng.annotations.Test
    public void test() throws Exception {

        Test test = new Test();
        test.setName("name");
    //    test.setNow(LocalDateTime.now());
        System.out.println("not config objectmapper:" + JSON.toJSONString(test));

        com.jn.easyjson.core.JSON json = FastEasyJsons.getJSONFactory(JSON.DEFAULT_GENERATE_FEATURE).get();
        JsonHandler jsonHandler = json.getJsonHandler();
        JacksonAdapter jsonHandler1 = (JacksonAdapter) jsonHandler;
        ObjectMapper objectMapper = jsonHandler1.getDelegate();

        // 初始化JavaTimeModule
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        //处理LocalDateTime
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(STANDARD_PATTERN);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

        objectMapper.registerModule(javaTimeModule);
        objectMapper.setSerializerProvider(new DefaultSerializerProvider.Impl());
        test.setNow(LocalDateTime.now());
        String jsonstr = JSON.toJSONString(test);
        System.out.println("get objectmapper from easyjson and config:" + jsonstr);

    }

    public static class Test {

        @JSONField(name = "name1")
        private String name;

        private LocalDateTime now;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public LocalDateTime getNow() {
            return now;
        }

        public void setNow(LocalDateTime now) {
            this.now = now;
        }
    }


}