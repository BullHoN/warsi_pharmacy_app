package com.avit.warsipharmacy.ui.home;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avit.warsipharmacy.R;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private List<Pair<String,Integer>> categoriesItems;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

        // TODO: MAKE THE NEWTOWRK CALL TO GET ALL CATEGORIES
        categoriesItems = new ArrayList<>();

        categoriesItems.add(new Pair<>("Tablets", R.drawable.tablets_icon));
        categoriesItems.add(new Pair<>("Syrups",R.drawable.syrups_icon));
        categoriesItems.add(new Pair<>("Tablets",R.drawable.tablets_icon));
        categoriesItems.add(new Pair<>("Syrups",R.drawable.syrups_icon));

    }

    public List<Pair<String, Integer>> getCategoriesItems() {
        return categoriesItems;
    }

    public LiveData<String> getText() {
        return mText;
    }
}