package com.jn.easyjson.gson.typeadapter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.codec.base64.Base64;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.PrimitiveArrays;
import com.jn.langx.util.io.Charsets;

import java.io.IOException;
import java.util.List;

public class BytesTypeAdapter extends EasyjsonAbstractTypeAdapter{

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
        if (getDataClass() == Byte[].class) {
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


}
