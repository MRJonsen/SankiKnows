package com.zc.pickuplearn.http;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.lang.reflect.Type;

/**
 * Description : Json转换工具类
 */
public class JsonUtils {

    private static Gson mGson = new Gson();

    /**
     * 将对象准换为json字符串
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String object2Json(T object) {
        return mGson.toJson(object);
    }

    /**
     * 将json字符串转换为对象
     *
     * @param json
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T json2object(String json, Class<T> clz) throws JsonSyntaxException {
        return mGson.fromJson(json, clz);
    }

    /**
     * 将json对象转换为实体对象
     *
     * @param json
     * @param clz
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */
    public static <T> T jsonobject2Object(JsonObject json, Class<T> clz) throws JsonSyntaxException {
        return mGson.fromJson(json, clz);
    }

    /**
     * 将json字符串转换为对象
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T deserialize(String json, Type type) throws JsonSyntaxException {
        return mGson.fromJson(json, type);
    }

    /**
     * 获取json字符串某个几点的JsonObject对象
     *
     * @param json
     * @param flag
     * @return
     */
    public static JsonElement decoElementJSONObject(String json, String flag) throws Exception {
        JsonElement parse = new JsonParser().parse(json);
        JsonObject root = parse.getAsJsonObject();
        return root.get(flag);
    }

    /**
     * 获取json字符串某个几点的String
     *
     * @param json
     * @param flag
     * @return
     */
    public static String decoElementASString(String json, String flag) throws Exception {
        JsonElement parse = new JsonParser().parse(json);
        JsonObject root = parse.getAsJsonObject();
        return root.get(flag).getAsString();
    }

    /**
     * JSON转成指定对象
     *
     * @param json
     */
    public static <T> T parseJson2Object(String json,TypeToken<T> typeToken) throws Exception {
        JsonReader reader = new JsonReader(new StringReader(json));
        return mGson.fromJson(reader, typeToken.getType());
    }
}
