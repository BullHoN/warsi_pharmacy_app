package com.avit.warsipharmacy.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PossibleValuesConvertor {

    @TypeConverter
    public static List<String> stringToStringList(String data){
        Gson gson = new Gson();

        if(data == null){
            return Collections.emptyList();
        }

//        List<String> list = gson.fromJson(data,);
        return new ArrayList<>();
    }

    @TypeConverter
    public static String listToString(List<String> list){
        Gson gson = new Gson();
        return gson.toJson(list);
    }

}
