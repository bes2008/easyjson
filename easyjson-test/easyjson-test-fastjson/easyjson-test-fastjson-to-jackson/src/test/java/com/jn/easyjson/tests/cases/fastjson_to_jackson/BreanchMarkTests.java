package com.jn.easyjson.tests.cases.fastjson_to_jackson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jn.easyjson.core.factory.JsonFactorys;
import org.openjdk.jmh.annotations.*;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5, time = 5)
@Measurement(iterations = 5, time = 5)
@Threads(8)
@Fork(3)
//@State(value = Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BreanchMarkTests {


    // @Benchmark
    public void testComplexSerialize() {
        JSON.toJSONString(complexJsonObject);
    }


    // @Benchmark
    public void testComplexDeSerialize() {
        JSON.parseObject(complexJson);
    }


    @Test
    @Benchmark
    public void testSimpleSerialize() {
        JSON.toJSONString(simpleJsonObject);
    }

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static com.jn.easyjson.core.JSON easyjson = JsonFactorys.getJSONFactory().get();

    @Benchmark
    public void testEasyjsonToJacksonSimpleSerialize() {
        easyjson.toJson(simpleJsonObject);
    }

    @Benchmark
    public void testJacksonSimpleSerialize() {
        try {
            objectMapper.writeValueAsString(simpleJsonObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    private static String complexJson = "{\n" +
            "    \"code\": 0,\n" +
            "    \"message\": \"OK\",\n" +
            "    \"data\": [\n" +
            "        {\n" +
            "            \"provinceName\": \"北京市\",\n" +
            "            \"provinceId\": \"1\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"北京市\",\n" +
            "                    \"cityId\": \"1\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"天津市\",\n" +
            "            \"provinceId\": \"2\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"天津市\",\n" +
            "                    \"cityId\": \"2\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"上海市\",\n" +
            "            \"provinceId\": \"3\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"上海市\",\n" +
            "                    \"cityId\": \"3\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"重庆市\",\n" +
            "            \"provinceId\": \"4\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"重庆市\",\n" +
            "                    \"cityId\": \"4\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"黑龙江\",\n" +
            "            \"provinceId\": \"5\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"哈尔滨\",\n" +
            "                    \"cityId\": \"46\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"齐齐哈尔\",\n" +
            "                    \"cityId\": \"47\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"鸡西市\",\n" +
            "                    \"cityId\": \"2737\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"鹤岗市\",\n" +
            "                    \"cityId\": \"457\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"双鸭山市\",\n" +
            "                    \"cityId\": \"2929\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"大庆市\",\n" +
            "                    \"cityId\": \"54\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"伊春市\",\n" +
            "                    \"cityId\": \"53\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"佳木斯\",\n" +
            "                    \"cityId\": \"49\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"七台河市\",\n" +
            "                    \"cityId\": \"2930\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"牡丹江\",\n" +
            "                    \"cityId\": \"48\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"黑河市\",\n" +
            "                    \"cityId\": \"51\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"绥化市\",\n" +
            "                    \"cityId\": \"50\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"大兴安岭\",\n" +
            "                    \"cityId\": \"2931\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"吉林省\",\n" +
            "            \"provinceId\": \"6\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"长春市\",\n" +
            "                    \"cityId\": \"55\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"吉林市\",\n" +
            "                    \"cityId\": \"56\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"四平市\",\n" +
            "                    \"cityId\": \"58\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"辽源市\",\n" +
            "                    \"cityId\": \"61\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"通化市\",\n" +
            "                    \"cityId\": \"59\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"白山市\",\n" +
            "                    \"cityId\": \"2756\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"松原市\",\n" +
            "                    \"cityId\": \"62\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"白城市\",\n" +
            "                    \"cityId\": \"60\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"延边州\",\n" +
            "                    \"cityId\": \"2951\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"辽宁省\",\n" +
            "            \"provinceId\": \"7\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"沈阳市\",\n" +
            "                    \"cityId\": \"65\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"大连市\",\n" +
            "                    \"cityId\": \"67\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"鞍山市\",\n" +
            "                    \"cityId\": \"68\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"抚顺市\",\n" +
            "                    \"cityId\": \"69\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"本溪市\",\n" +
            "                    \"cityId\": \"70\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"丹东市\",\n" +
            "                    \"cityId\": \"71\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"锦州市\",\n" +
            "                    \"cityId\": \"72\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"营口市\",\n" +
            "                    \"cityId\": \"73\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"阜新市\",\n" +
            "                    \"cityId\": \"74\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"辽阳市\",\n" +
            "                    \"cityId\": \"75\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"盘锦市\",\n" +
            "                    \"cityId\": \"77\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"铁岭市\",\n" +
            "                    \"cityId\": \"66\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"朝阳市\",\n" +
            "                    \"cityId\": \"76\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"葫芦岛\",\n" +
            "                    \"cityId\": \"78\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"青海省\",\n" +
            "            \"provinceId\": \"8\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"西宁市\",\n" +
            "                    \"cityId\": \"79\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"海东市\",\n" +
            "                    \"cityId\": \"80\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"海北藏族自治州\",\n" +
            "                    \"cityId\": \"3182\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"黄南藏族自治州\",\n" +
            "                    \"cityId\": \"3184\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"海南藏族自治州\",\n" +
            "                    \"cityId\": \"3185\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"果洛藏族自治州\",\n" +
            "                    \"cityId\": \"3183\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"玉树藏族自治州\",\n" +
            "                    \"cityId\": \"84\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"海西蒙古族藏族自治州\",\n" +
            "                    \"cityId\": \"3181\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"甘肃省\",\n" +
            "            \"provinceId\": \"9\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"兰州市\",\n" +
            "                    \"cityId\": \"86\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"嘉峪关市\",\n" +
            "                    \"cityId\": \"478\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"金昌市\",\n" +
            "                    \"cityId\": \"3177\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"白银市\",\n" +
            "                    \"cityId\": \"96\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"天水市\",\n" +
            "                    \"cityId\": \"94\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"武威市\",\n" +
            "                    \"cityId\": \"91\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"张掖市\",\n" +
            "                    \"cityId\": \"92\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"平凉市\",\n" +
            "                    \"cityId\": \"89\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"酒泉市\",\n" +
            "                    \"cityId\": \"93\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"庆阳市\",\n" +
            "                    \"cityId\": \"270\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"定西市\",\n" +
            "                    \"cityId\": \"88\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"陇南市\",\n" +
            "                    \"cityId\": \"3142\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"临夏州\",\n" +
            "                    \"cityId\": \"87\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"甘南州\",\n" +
            "                    \"cityId\": \"95\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"陕西省\",\n" +
            "            \"provinceId\": \"10\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"西安市\",\n" +
            "                    \"cityId\": \"97\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"铜川市\",\n" +
            "                    \"cityId\": \"106\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"宝鸡市\",\n" +
            "                    \"cityId\": \"105\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"咸阳市\",\n" +
            "                    \"cityId\": \"98\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"渭南市\",\n" +
            "                    \"cityId\": \"101\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"延安市\",\n" +
            "                    \"cityId\": \"99\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"汉中市\",\n" +
            "                    \"cityId\": \"104\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"榆林市\",\n" +
            "                    \"cityId\": \"100\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"安康市\",\n" +
            "                    \"cityId\": \"103\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"商洛市\",\n" +
            "                    \"cityId\": \"102\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"西藏\",\n" +
            "            \"provinceId\": \"11\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"拉萨市\",\n" +
            "                    \"cityId\": \"107\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"日喀则\",\n" +
            "                    \"cityId\": \"108\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"昌都市\",\n" +
            "                    \"cityId\": \"3187\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"林芝市\",\n" +
            "                    \"cityId\": \"3186\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"山南市\",\n" +
            "                    \"cityId\": \"109\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"那曲市\",\n" +
            "                    \"cityId\": \"3188\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"阿里地区\",\n" +
            "                    \"cityId\": \"3189\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"内蒙古\",\n" +
            "            \"provinceId\": \"12\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"呼和浩特\",\n" +
            "                    \"cityId\": \"110\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"包头市\",\n" +
            "                    \"cityId\": \"111\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"乌海市\",\n" +
            "                    \"cityId\": \"112\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"赤峰市\",\n" +
            "                    \"cityId\": \"115\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"通辽市\",\n" +
            "                    \"cityId\": \"114\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"鄂尔多斯\",\n" +
            "                    \"cityId\": \"2679\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"呼伦贝尔\",\n" +
            "                    \"cityId\": \"2653\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"巴彦淖尔\",\n" +
            "                    \"cityId\": \"2689\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"乌兰察布\",\n" +
            "                    \"cityId\": \"2667\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"兴安盟\",\n" +
            "                    \"cityId\": \"471\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"锡盟\",\n" +
            "                    \"cityId\": \"484\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"阿拉善盟\",\n" +
            "                    \"cityId\": \"3179\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"新疆\",\n" +
            "            \"provinceId\": \"13\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"乌鲁木齐\",\n" +
            "                    \"cityId\": \"225\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"克拉玛依\",\n" +
            "                    \"cityId\": \"232\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"吐鲁番\",\n" +
            "                    \"cityId\": \"227\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"哈密\",\n" +
            "                    \"cityId\": \"229\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"昌吉州\",\n" +
            "                    \"cityId\": \"3064\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"博尔塔拉州\",\n" +
            "                    \"cityId\": \"3065\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"巴音郭楞州\",\n" +
            "                    \"cityId\": \"3066\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"库尔勒\",\n" +
            "                    \"cityId\": \"2725\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"阿克苏\",\n" +
            "                    \"cityId\": \"2724\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"克孜勒苏州\",\n" +
            "                    \"cityId\": \"3067\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"喀什\",\n" +
            "                    \"cityId\": \"226\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"和田\",\n" +
            "                    \"cityId\": \"233\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"伊犁州\",\n" +
            "                    \"cityId\": \"231\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"塔城\",\n" +
            "                    \"cityId\": \"230\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"阿勒泰\",\n" +
            "                    \"cityId\": \"2991\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"石河子市\",\n" +
            "                    \"cityId\": \"3014\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"阿拉尔市\",\n" +
            "                    \"cityId\": \"3068\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"图木舒克\",\n" +
            "                    \"cityId\": \"228\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"五家渠市\",\n" +
            "                    \"cityId\": \"3069\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"铁门关市\",\n" +
            "                    \"cityId\": \"3087\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"宁夏\",\n" +
            "            \"provinceId\": \"14\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"银川市\",\n" +
            "                    \"cityId\": \"118\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"石嘴山\",\n" +
            "                    \"cityId\": \"119\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"吴忠市\",\n" +
            "                    \"cityId\": \"120\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"固原市\",\n" +
            "                    \"cityId\": \"121\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"中卫市\",\n" +
            "                    \"cityId\": \"3180\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"广西\",\n" +
            "            \"provinceId\": \"15\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"南宁市\",\n" +
            "                    \"cityId\": \"122\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"柳州市\",\n" +
            "                    \"cityId\": \"123\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"桂林市\",\n" +
            "                    \"cityId\": \"124\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"梧州市\",\n" +
            "                    \"cityId\": \"125\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"北海市\",\n" +
            "                    \"cityId\": \"130\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"防城港市\",\n" +
            "                    \"cityId\": \"2865\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"钦州市\",\n" +
            "                    \"cityId\": \"128\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"贵港市\",\n" +
            "                    \"cityId\": \"2703\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"玉林市\",\n" +
            "                    \"cityId\": \"126\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"百色市\",\n" +
            "                    \"cityId\": \"127\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"贺州市\",\n" +
            "                    \"cityId\": \"474\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"河池市\",\n" +
            "                    \"cityId\": \"129\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"来宾市\",\n" +
            "                    \"cityId\": \"2866\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"崇左市\",\n" +
            "                    \"cityId\": \"2867\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"四川省\",\n" +
            "            \"provinceId\": \"16\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"成都市\",\n" +
            "                    \"cityId\": \"131\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"自贡市\",\n" +
            "                    \"cityId\": \"134\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"攀枝花\",\n" +
            "                    \"cityId\": \"133\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"泸州市\",\n" +
            "                    \"cityId\": \"430\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"德阳市\",\n" +
            "                    \"cityId\": \"428\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"绵阳市\",\n" +
            "                    \"cityId\": \"274\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"广元市\",\n" +
            "                    \"cityId\": \"429\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"遂宁市\",\n" +
            "                    \"cityId\": \"2747\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"内江市\",\n" +
            "                    \"cityId\": \"280\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"乐山市\",\n" +
            "                    \"cityId\": \"281\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"南充市\",\n" +
            "                    \"cityId\": \"277\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"眉山市\",\n" +
            "                    \"cityId\": \"2749\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"宜宾市\",\n" +
            "                    \"cityId\": \"279\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"广安市\",\n" +
            "                    \"cityId\": \"431\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"达州市\",\n" +
            "                    \"cityId\": \"432\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"雅安市\",\n" +
            "                    \"cityId\": \"434\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"巴中市\",\n" +
            "                    \"cityId\": \"2970\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"资阳市\",\n" +
            "                    \"cityId\": \"435\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"阿坝州\",\n" +
            "                    \"cityId\": \"482\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"甘孜州\",\n" +
            "                    \"cityId\": \"2710\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"凉山州\",\n" +
            "                    \"cityId\": \"2969\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"贵州省\",\n" +
            "            \"provinceId\": \"17\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"贵阳市\",\n" +
            "                    \"cityId\": \"136\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"六盘水市\",\n" +
            "                    \"cityId\": \"463\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"遵义市\",\n" +
            "                    \"cityId\": \"137\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"安顺市\",\n" +
            "                    \"cityId\": \"138\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"毕节市\",\n" +
            "                    \"cityId\": \"2644\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"铜仁市\",\n" +
            "                    \"cityId\": \"285\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"黔西南州\",\n" +
            "                    \"cityId\": \"2820\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"黔东南州\",\n" +
            "                    \"cityId\": \"2818\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"黔南州\",\n" +
            "                    \"cityId\": \"2819\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"云南省\",\n" +
            "            \"provinceId\": \"18\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"昆明市\",\n" +
            "                    \"cityId\": \"139\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"曲靖市\",\n" +
            "                    \"cityId\": \"142\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"玉溪市\",\n" +
            "                    \"cityId\": \"461\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"保山市\",\n" +
            "                    \"cityId\": \"286\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"昭通市\",\n" +
            "                    \"cityId\": \"466\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"丽江市\",\n" +
            "                    \"cityId\": \"141\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"普洱市\",\n" +
            "                    \"cityId\": \"288\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"临沧市\",\n" +
            "                    \"cityId\": \"2698\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"楚雄州\",\n" +
            "                    \"cityId\": \"287\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"红河州\",\n" +
            "                    \"cityId\": \"465\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"文山州\",\n" +
            "                    \"cityId\": \"2809\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"西双版纳州\",\n" +
            "                    \"cityId\": \"2891\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"大理州\",\n" +
            "                    \"cityId\": \"140\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"德宏州\",\n" +
            "                    \"cityId\": \"2888\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"怒江州\",\n" +
            "                    \"cityId\": \"2889\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"迪庆州\",\n" +
            "                    \"cityId\": \"2890\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"广东省\",\n" +
            "            \"provinceId\": \"19\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"广州市\",\n" +
            "                    \"cityId\": \"143\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"韶关市\",\n" +
            "                    \"cityId\": \"144\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"深圳市\",\n" +
            "                    \"cityId\": \"146\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"珠海市\",\n" +
            "                    \"cityId\": \"148\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"汕头市\",\n" +
            "                    \"cityId\": \"290\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"佛山市\",\n" +
            "                    \"cityId\": \"291\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"南海市\",\n" +
            "                    \"cityId\": \"295\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"三水市\",\n" +
            "                    \"cityId\": \"296\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"江门市\",\n" +
            "                    \"cityId\": \"301\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"新会市\",\n" +
            "                    \"cityId\": \"298\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"湛江市\",\n" +
            "                    \"cityId\": \"293\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"茂名市\",\n" +
            "                    \"cityId\": \"475\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"肇庆市\",\n" +
            "                    \"cityId\": \"292\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"惠州市\",\n" +
            "                    \"cityId\": \"145\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"梅州市\",\n" +
            "                    \"cityId\": \"289\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"汕尾市\",\n" +
            "                    \"cityId\": \"2784\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"河源市\",\n" +
            "                    \"cityId\": \"472\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"阳江市\",\n" +
            "                    \"cityId\": \"3097\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"清远市\",\n" +
            "                    \"cityId\": \"2712\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"东莞市\",\n" +
            "                    \"cityId\": \"149\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"中山市\",\n" +
            "                    \"cityId\": \"147\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"潮州市\",\n" +
            "                    \"cityId\": \"300\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"揭阳市\",\n" +
            "                    \"cityId\": \"294\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"云浮市\",\n" +
            "                    \"cityId\": \"469\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"福建省\",\n" +
            "            \"provinceId\": \"20\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"福州市\",\n" +
            "                    \"cityId\": \"150\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"厦门市\",\n" +
            "                    \"cityId\": \"151\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"莆田市\",\n" +
            "                    \"cityId\": \"153\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"三明市\",\n" +
            "                    \"cityId\": \"158\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"泉州市\",\n" +
            "                    \"cityId\": \"154\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"漳州市\",\n" +
            "                    \"cityId\": \"156\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"南平市\",\n" +
            "                    \"cityId\": \"159\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"龙岩市\",\n" +
            "                    \"cityId\": \"157\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"宁德市\",\n" +
            "                    \"cityId\": \"152\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"湖南省\",\n" +
            "            \"provinceId\": \"21\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"长沙市\",\n" +
            "                    \"cityId\": \"160\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"株洲市\",\n" +
            "                    \"cityId\": \"304\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"湘潭市\",\n" +
            "                    \"cityId\": \"162\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"衡阳市\",\n" +
            "                    \"cityId\": \"164\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"邵阳市\",\n" +
            "                    \"cityId\": \"309\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"岳阳市\",\n" +
            "                    \"cityId\": \"161\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"常德市\",\n" +
            "                    \"cityId\": \"306\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"张家界\",\n" +
            "                    \"cityId\": \"166\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"益阳市\",\n" +
            "                    \"cityId\": \"165\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"郴州市\",\n" +
            "                    \"cityId\": \"305\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"永州市\",\n" +
            "                    \"cityId\": \"470\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"怀化市\",\n" +
            "                    \"cityId\": \"163\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"娄底市\",\n" +
            "                    \"cityId\": \"308\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"湘西州\",\n" +
            "                    \"cityId\": \"2790\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"江西省\",\n" +
            "            \"provinceId\": \"22\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"南昌市\",\n" +
            "                    \"cityId\": \"167\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"景德镇\",\n" +
            "                    \"cityId\": \"173\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"萍乡市\",\n" +
            "                    \"cityId\": \"175\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"九江市\",\n" +
            "                    \"cityId\": \"168\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"新余市\",\n" +
            "                    \"cityId\": \"170\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"鹰潭市\",\n" +
            "                    \"cityId\": \"174\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"赣州市\",\n" +
            "                    \"cityId\": \"169\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"吉安市\",\n" +
            "                    \"cityId\": \"311\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"宜春市\",\n" +
            "                    \"cityId\": \"171\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"抚州市\",\n" +
            "                    \"cityId\": \"451\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"上饶市\",\n" +
            "                    \"cityId\": \"172\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"湖北省\",\n" +
            "            \"provinceId\": \"23\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"武汉市\",\n" +
            "                    \"cityId\": \"176\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"黄石市\",\n" +
            "                    \"cityId\": \"313\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"十堰市\",\n" +
            "                    \"cityId\": \"445\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"宜昌市\",\n" +
            "                    \"cityId\": \"180\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"襄阳市\",\n" +
            "                    \"cityId\": \"177\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"鄂州市\",\n" +
            "                    \"cityId\": \"178\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"荆门市\",\n" +
            "                    \"cityId\": \"317\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"孝感市\",\n" +
            "                    \"cityId\": \"179\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"荆州市\",\n" +
            "                    \"cityId\": \"477\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"江陵市\",\n" +
            "                    \"cityId\": \"315\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"黄冈市\",\n" +
            "                    \"cityId\": \"437\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"咸宁市\",\n" +
            "                    \"cityId\": \"314\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"随州市\",\n" +
            "                    \"cityId\": \"479\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"恩施市\",\n" +
            "                    \"cityId\": \"316\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"仙桃市\",\n" +
            "                    \"cityId\": \"2759\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"潜江市\",\n" +
            "                    \"cityId\": \"2760\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"天门市\",\n" +
            "                    \"cityId\": \"481\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"神农架林区\",\n" +
            "                    \"cityId\": \"3191\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"浙江省\",\n" +
            "            \"provinceId\": \"24\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"杭州市\",\n" +
            "                    \"cityId\": \"34\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"宁波市\",\n" +
            "                    \"cityId\": \"35\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"温州市\",\n" +
            "                    \"cityId\": \"40\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"嘉兴市\",\n" +
            "                    \"cityId\": \"37\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"湖州市\",\n" +
            "                    \"cityId\": \"36\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"绍兴市\",\n" +
            "                    \"cityId\": \"38\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"金华市\",\n" +
            "                    \"cityId\": \"42\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"衢州市\",\n" +
            "                    \"cityId\": \"43\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"舟山市\",\n" +
            "                    \"cityId\": \"44\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"台州市\",\n" +
            "                    \"cityId\": \"39\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"丽水市\",\n" +
            "                    \"cityId\": \"41\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"江苏省\",\n" +
            "            \"provinceId\": \"25\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"南京市\",\n" +
            "                    \"cityId\": \"181\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"无锡市\",\n" +
            "                    \"cityId\": \"182\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"徐州市\",\n" +
            "                    \"cityId\": \"188\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"常州市\",\n" +
            "                    \"cityId\": \"192\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"苏州市\",\n" +
            "                    \"cityId\": \"184\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"南通市\",\n" +
            "                    \"cityId\": \"185\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"连云港市\",\n" +
            "                    \"cityId\": \"191\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"淮安市\",\n" +
            "                    \"cityId\": \"190\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"盐城市\",\n" +
            "                    \"cityId\": \"187\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"扬州市\",\n" +
            "                    \"cityId\": \"186\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"镇江市\",\n" +
            "                    \"cityId\": \"183\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"泰州市\",\n" +
            "                    \"cityId\": \"193\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"宿迁市\",\n" +
            "                    \"cityId\": \"483\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"河南省\",\n" +
            "            \"provinceId\": \"26\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"郑州市\",\n" +
            "                    \"cityId\": \"194\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"开封市\",\n" +
            "                    \"cityId\": \"195\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"洛阳市\",\n" +
            "                    \"cityId\": \"196\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"平顶山市\",\n" +
            "                    \"cityId\": \"323\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"安阳市\",\n" +
            "                    \"cityId\": \"321\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"鹤壁市\",\n" +
            "                    \"cityId\": \"326\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"新乡市\",\n" +
            "                    \"cityId\": \"322\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"焦作市\",\n" +
            "                    \"cityId\": \"325\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"濮阳市\",\n" +
            "                    \"cityId\": \"476\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"许昌市\",\n" +
            "                    \"cityId\": \"197\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"长葛市\",\n" +
            "                    \"cityId\": \"456\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"漯河市\",\n" +
            "                    \"cityId\": \"440\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"三门峡\",\n" +
            "                    \"cityId\": \"442\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"南阳市\",\n" +
            "                    \"cityId\": \"198\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"商丘市\",\n" +
            "                    \"cityId\": \"324\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"信阳市\",\n" +
            "                    \"cityId\": \"458\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"周口市\",\n" +
            "                    \"cityId\": \"439\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"驻马店\",\n" +
            "                    \"cityId\": \"441\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"济源市\",\n" +
            "                    \"cityId\": \"1722\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"河北省\",\n" +
            "            \"provinceId\": \"27\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"石家庄市\",\n" +
            "                    \"cityId\": \"199\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"唐山市\",\n" +
            "                    \"cityId\": \"266\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"秦皇岛\",\n" +
            "                    \"cityId\": \"202\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"邯郸市\",\n" +
            "                    \"cityId\": \"200\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"邢台市\",\n" +
            "                    \"cityId\": \"269\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"保定市\",\n" +
            "                    \"cityId\": \"265\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"张家口\",\n" +
            "                    \"cityId\": \"459\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"承德市\",\n" +
            "                    \"cityId\": \"201\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"沧州市\",\n" +
            "                    \"cityId\": \"268\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"廊坊市\",\n" +
            "                    \"cityId\": \"267\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"衡水市\",\n" +
            "                    \"cityId\": \"203\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"山东省\",\n" +
            "            \"provinceId\": \"28\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"济南市\",\n" +
            "                    \"cityId\": \"204\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"青岛市\",\n" +
            "                    \"cityId\": \"205\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"淄博市\",\n" +
            "                    \"cityId\": \"208\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"枣庄市\",\n" +
            "                    \"cityId\": \"446\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"东营市\",\n" +
            "                    \"cityId\": \"443\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"烟台市\",\n" +
            "                    \"cityId\": \"206\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"潍坊市\",\n" +
            "                    \"cityId\": \"261\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"济宁市\",\n" +
            "                    \"cityId\": \"262\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"泰安市\",\n" +
            "                    \"cityId\": \"207\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"威海市\",\n" +
            "                    \"cityId\": \"448\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"日照市\",\n" +
            "                    \"cityId\": \"454\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"临沂市\",\n" +
            "                    \"cityId\": \"263\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"德州市\",\n" +
            "                    \"cityId\": \"209\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"聊城市\",\n" +
            "                    \"cityId\": \"436\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"滨州市\",\n" +
            "                    \"cityId\": \"467\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"菏泽市\",\n" +
            "                    \"cityId\": \"264\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"山西省\",\n" +
            "            \"provinceId\": \"29\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"太原市\",\n" +
            "                    \"cityId\": \"210\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"大同市\",\n" +
            "                    \"cityId\": \"211\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"阳泉市\",\n" +
            "                    \"cityId\": \"327\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"长治市\",\n" +
            "                    \"cityId\": \"328\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"晋城市\",\n" +
            "                    \"cityId\": \"213\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"朔州市\",\n" +
            "                    \"cityId\": \"3096\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"晋中市\",\n" +
            "                    \"cityId\": \"452\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"运城市\",\n" +
            "                    \"cityId\": \"215\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"忻州市\",\n" +
            "                    \"cityId\": \"212\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"临汾市\",\n" +
            "                    \"cityId\": \"214\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"吕梁市\",\n" +
            "                    \"cityId\": \"3112\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"海南省\",\n" +
            "            \"provinceId\": \"30\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"海口市\",\n" +
            "                    \"cityId\": \"216\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"三亚市\",\n" +
            "                    \"cityId\": \"217\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"三沙市\",\n" +
            "                    \"cityId\": \"3190\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"儋州市\",\n" +
            "                    \"cityId\": \"3025\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"五指山市\",\n" +
            "                    \"cityId\": \"3133\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"琼海市\",\n" +
            "                    \"cityId\": \"3135\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"文昌市\",\n" +
            "                    \"cityId\": \"3134\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"万宁市\",\n" +
            "                    \"cityId\": \"3136\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"东方市\",\n" +
            "                    \"cityId\": \"3137\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"定安县\",\n" +
            "                    \"cityId\": \"3138\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"屯昌县\",\n" +
            "                    \"cityId\": \"3139\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"澄迈县\",\n" +
            "                    \"cityId\": \"3140\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"临高县\",\n" +
            "                    \"cityId\": \"3141\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"陵水县\",\n" +
            "                    \"cityId\": \"3214\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"provinceName\": \"安徽省\",\n" +
            "            \"provinceId\": \"31\",\n" +
            "            \"cities\": [\n" +
            "                {\n" +
            "                    \"cityName\": \"合肥市\",\n" +
            "                    \"cityId\": \"234\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"芜湖市\",\n" +
            "                    \"cityId\": \"250\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"蚌埠市\",\n" +
            "                    \"cityId\": \"249\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"淮南市\",\n" +
            "                    \"cityId\": \"251\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"马鞍山\",\n" +
            "                    \"cityId\": \"252\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"淮北市\",\n" +
            "                    \"cityId\": \"460\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"铜陵市\",\n" +
            "                    \"cityId\": \"257\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"安庆市\",\n" +
            "                    \"cityId\": \"253\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"黄山市\",\n" +
            "                    \"cityId\": \"256\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"滁州市\",\n" +
            "                    \"cityId\": \"455\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"阜阳市\",\n" +
            "                    \"cityId\": \"255\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"宿州市\",\n" +
            "                    \"cityId\": \"254\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"六安市\",\n" +
            "                    \"cityId\": \"259\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"亳州市\",\n" +
            "                    \"cityId\": \"468\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"池州市\",\n" +
            "                    \"cityId\": \"462\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"宣城市\",\n" +
            "                    \"cityId\": \"3052\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"cityName\": \"宣州市\",\n" +
            "                    \"cityId\": \"258\"\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}";


    private static JSONObject complexJsonObject = JSON.parseObject(complexJson);

    private static String simpleJson = "{\"code\":0,\"msg\":\"OK\",\"data\":\"\"}";

    private static JSONObject simpleJsonObject = JSON.parseObject(simpleJson);

}