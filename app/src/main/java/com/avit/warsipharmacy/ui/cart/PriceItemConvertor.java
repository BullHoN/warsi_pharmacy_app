package com.avit.warsipharmacy.ui.cart;

import androidx.room.TypeConverter;

import com.avit.warsipharmacy.ui.category.CategoryItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class PriceItemConvertor {
    private static Gson gson = new Gson();

    @TypeConverter
    public static List<CategoryItem.PriceItem> stringToList(String data){
        if(data == null){
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<CategoryItem.PriceItem>>(){}.getType();
        return gson.fromJson(data,listType);
    }

    @TypeConverter
    public static String ListToString(List<CategoryItem.PriceItem> priceItems){
        return gson.toJson(priceItems);
    }

}
