package com.avit.warsipharmacy.ui.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewModel extends ViewModel {

    MutableLiveData<List<CategoryItem>> categoryItems;

    public CategoryViewModel(){
        //TODO: GET THE CATEGORY ITEMS FROM DATABASE
        categoryItems = new MutableLiveData<>();

        List<CategoryItem> list = new ArrayList<>();

        list.add(new CategoryItem("AMREE PLUS CAPSULE 20","Tablets",5,100));
        list.add(new CategoryItem("AMREE PLUS CAPSULE 21","Syrups",10,120));
        list.add(new CategoryItem("AMREE PLUS CAPSULE 22","Tablets",14,400));
        list.add(new CategoryItem("AMREE PLUS CAPSULE 23","Syrups",19,120));
        list.add(new CategoryItem("AMREE PLUS CAPSULE 24","Tablets",80,900));

        categoryItems.setValue(list);

    }

    public MutableLiveData<List<CategoryItem>> getCategoryItems() {
        return categoryItems;
    }
}