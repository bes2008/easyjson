package com.jn.easyjson.tests.cases.fastjson_to_jackson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.easyjson.FastEasyJsons;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.jn.easyjson.core.JsonHandler;
import com.jn.easyjson.jackson.JacksonAdapter;
import org.junit.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <h3></h3>
 *
 * @author jiaxiaotong
 * @version 1.0.0
 * @createTime 2020/6/22 12:22 下午
 */
public class NoSpringTest1 {


    public static void main(String[] args) throws Exception {
        com.jn.easyjson.core.JSON json = FastEasyJsons.getJSONFactory(JSON.DEFAULT_GENERATE_FEATURE).get();
        JsonHandler jsonHandler = json.getJsonHandler();
        JacksonAdapter jacksonAdapter = (JacksonAdapter) jsonHandler;

        ObjectMapper objectMapper = jacksonAdapter.getDelegate();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setSerializerProvider(new DefaultSerializerProvider.Impl());

        String jsonStr = "{\"date\":\"1630484219.328283\",\"date1\":\"2021-07-10 01:01:01\"}";

        Test test = JSON.parseObject(jsonStr, Test.class);

        ObjectMapper objectMapper1 = new ObjectMapper();
        objectMapper1.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper1.setSerializerProvider(new DefaultSerializerProvider.Impl());

        Test test1 = objectMapper1.readValue(jsonStr, Test.class);

        Assert.assertEquals(test.getDate(), test1.getDate());
        Assert.assertEquals(test.getDate1(), test1.getDate1());

    }

    public static class Test {

        @JsonDeserialize(converter = StringToLocalDatetimeConverter.class)
        private Date date;

        private Date date1;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Date getDate1() {
            return date1;
        }

        public void setDate1(Date date1) {
            this.date1 = date1;
        }
    }


}

class StringToLocalDatetimeConverter extends StdConverter<String, Date> {
    @Override
    public Date convert(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (value.contains(".")) {
            value = value.split("\\.")[0];
            return new Date(Long.parseLong(value));
        } else {
            try {
                return sdf.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
