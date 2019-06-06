package com.github.fangjinuo.easyjson.tests.original;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fangjinuo.easyjson.tests.struct.Contact;
import com.github.fangjinuo.easyjson.tests.struct.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.*;

public class SimpleModelTests {
    private static Person person;
    private static final List<Person> persons = new ArrayList<Person>();
    private static final Map<String, Person> nameToPersonMap = new HashMap<String, Person>();
    private static final Map<Integer, Person> idToPersonMap = new HashMap<Integer, Person>();

    static {
        for(int i=1; i<= 10; i++){
            Person p = new Person();
            p.setId(i);
            p.setName("name_"+i);
            p.setBirthday(new Date());

            Contact c = new Contact();
            p.setContact(c);
            c.setEmail(p.getName()+"@gmail.com");
            c.setMobilePhone("mphone"+ i);
            c.setPhone("phone"+ i);
            c.setQq("qq"+i);
            c.setWebchat("webchat"+i);

            if(i==1){
                person = p;
            }

            persons.add(person);
            nameToPersonMap.put(p.getName(), p);
            idToPersonMap.put(p.getId(), p);
        }
    }

    @Test
    public void testGson(){
        System.out.println("=====================Gson test start =============================");
        Gson gson = new GsonBuilder().serializeNulls().create();

        // test simple object
        String str1 = gson.toJson(person, person.getClass());
        System.out.println(str1);
        Person p1 = gson.fromJson(str1, Person.class);
        System.out.println(p1.equals(person));
        System.out.println(gson.toJson(person));

        // test list
        String str2 = gson.toJson(persons);
        System.out.println(str2);
        List<Person> persons2 = gson.fromJson(str2, TypeToken.getParameterized(List.class, Person.class).getType());
        System.out.println(gson.toJson(persons2));
        // test map
        String str3 = gson.toJson(idToPersonMap);
        System.out.println(str3);
        Map<Integer, Person> personMap = gson.fromJson(str3, TypeToken.getParameterized(Map.class, Integer.class, Person.class).getType());
        System.out.println(gson.toJson(personMap, TypeToken.getParameterized(Map.class, Integer.class, Person.class).getType()));
        System.out.println("=====================Gson test end =============================");
    }


    @Test
    public void testJackson() throws Exception{
        System.out.println("=====================Jackson test start =============================");
        ObjectMapper objectMapper = new ObjectMapper();

        // test simple object
        String str1 = objectMapper.writeValueAsString(person);
        System.out.println(str1);
        Person p1 = objectMapper.readValue(str1, Person.class);
        System.out.println(p1.equals(person));
        System.out.println(objectMapper.writeValueAsString(person));

        // test list
        String str2 = objectMapper.writeValueAsString(persons);
        System.out.println(str2);
        List<Person> persons2 = objectMapper.readValue(str2, objectMapper.getTypeFactory().constructParametricType(List.class, Person.class));
        System.out.println(objectMapper.writeValueAsString(persons2));
        // test map
        String str3 = objectMapper.writeValueAsString(idToPersonMap);
        System.out.println(str3);
        Map<Integer, Person> personMap = objectMapper.readValue(str3, objectMapper.getTypeFactory().constructParametricType(Map.class, Integer.class, Person.class));
        System.out.println(objectMapper.writeValueAsString(personMap));
        System.out.println("=====================Jackson test end =============================");
    }

    @Test
    public void testFastJson() throws Exception{
        System.out.println("=====================FastJson test start =============================");

        // test simple object
        String str1 = JSON.toJSONString(person, SerializerFeature.WriteMapNullValue);
        System.out.println(str1);
        Person p1 = JSON.parseObject(str1, Person.class, Feature.AutoCloseSource);
        System.out.println(p1.equals(person));
        System.out.println(JSON.toJSONString(person, SerializerFeature.WriteMapNullValue));

        // test list
        String str2 = JSON.toJSONString(persons, SerializerFeature.WriteMapNullValue);
        System.out.println(str2);
        List<Person> persons2 = JSON.parseObject(str2, new ParameterizedTypeImpl(new Class[]{Person.class}, null, List.class));
        System.out.println(JSON.toJSONString(persons2, SerializerFeature.WriteMapNullValue));
        // test map
        String str3 = JSON.toJSONString(idToPersonMap);
        System.out.println(str3);
        Map<Integer, Person> personMap = JSON.parseObject(str3, new ParameterizedTypeImpl(new Class[]{Integer.class, Person.class}, null, Map.class));
        System.out.println(JSON.toJSONString(personMap, SerializerFeature.WriteMapNullValue));
        System.out.println("=====================FastJson test end =============================");
    }


}
