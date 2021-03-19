package com.avit.warsipharmacy.ui.categories;

import android.util.Pair;

import androidx.lifecycle.ViewModel;

import com.avit.warsipharmacy.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriesViewModel extends ViewModel {
    private List<Pair<String,Integer>> categoriesItems;

    public CategoriesViewModel(){
        categoriesItems = new ArrayList<>();

        categoriesItems.add(new Pair<>("Tablets", R.drawable.tablets_icon));
        categoriesItems.add(new Pair<>("Syrups",R.drawable.syrups_icon));
        categoriesItems.add(new Pair<>("Tablets",R.drawable.tablets_icon));
        categoriesItems.add(new Pair<>("Syrups",R.drawable.syrups_icon));
        categoriesItems.add(new Pair<>("Tablets", R.drawable.tablets_icon));
        categoriesItems.add(new Pair<>("Syrups",R.drawable.syrups_icon));
        categoriesItems.add(new Pair<>("Tablets",R.drawable.tablets_icon));
        categoriesItems.add(new Pair<>("Syrups",R.drawable.syrups_icon));
    }

    public List<Pair<String, Integer>> getCategoriesItems() {
        return categoriesItems;
    }
}
