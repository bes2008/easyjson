package com.jn.easyjson.gson.typeadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.jn.easyjson.core.JSONBuilderAware;
import com.jn.easyjson.gson.GsonJSONBuilder;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.codec.base64.Base64;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.PrimitiveArrays;
import com.jn.langx.util.io.Charsets;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class BytesTypeAdapter extends TypeAdapter implements JSONBuilderAware<GsonJSONBuilder>, FieldAware {
    private GsonJSONBuilder jsonBuilder;

    private Field field;

    /**
     * 参数只支持 Byte[], byte[]
     *
     * @param value
     * @return
     */
    private byte[] toBytes(@NonNull Object value) {
        if (value.getClass() == byte[].class) {
            return (byte[]) value;
        }
        if (value.getClass() == Byte[].class) {
            return PrimitiveArrays.unwrap((Byte[]) value, true);
        }
        throw new IllegalArgumentException("value type is not byte[] or Byte[]");
    }

    @Override
    public void write(JsonWriter out, Object value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        byte[] bytes = toBytes(value);
        bytes = Base64.encodeBase64(bytes);
        String bytesString = new String(bytes, Charsets.UTF_8);
        out.value(bytesString);
    }


    @Override
    public Object read(JsonReader in) throws IOException {
        JsonToken jsonToken = in.peek();
        if (jsonToken != JsonToken.STRING) {
            return null;
        }

        String str = in.nextString();
        byte[] bytes = str.getBytes(Charsets.UTF_8);
        bytes = Base64.decodeBase64(bytes);
        if (field != null && field.getType() == Byte[].class) {
            return PrimitiveArrays.wrap(bytes, true);
        }
        return bytes;
    }

    private static List<JsonToken> invalidValueTokens = Collects.newArrayList(
            JsonToken.BEGIN_ARRAY,
            JsonToken.END_ARRAY,
            JsonToken.BEGIN_OBJECT,
            JsonToken.END_OBJECT,
            JsonToken.END_DOCUMENT,
            JsonToken.NAME
    );

    @Override
    public void setJSONBuilder(GsonJSONBuilder jsonBuilder) {
        this.jsonBuilder = jsonBuilder;
    }

    @Override
    public GsonJSONBuilder getJSONBuilder() {
        return jsonBuilder;
    }

    @Override
    public void setField(Field field) {
        this.field = field;
    }
}
