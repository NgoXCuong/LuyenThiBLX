package com.example.luyenthiblxmay.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class OptionsConverter {
    @TypeConverter
    public static String fromMap(Map<String, String> map) {
        return new Gson().toJson(map);
    }

    @TypeConverter
    public static Map<String, String> toMap(String json) {
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        return new Gson().fromJson(json, type);
    }
}
