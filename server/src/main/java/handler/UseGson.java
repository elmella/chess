package handler;

import com.google.gson.Gson;
import request.Request;

import java.lang.reflect.Type;

public class UseGson {

    public static Request fromJson(String json, Type t) {
        Gson serializer = new Gson();
        return serializer.fromJson(json, t);
    }

    public static String toJson(Object o) {
        Gson serializer = new Gson();
        return serializer.toJson(o);
    }

}
