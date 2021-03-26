package com.avit.warsipharmacy.ui.home;

import android.app.Application;
import android.util.Pair;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.db.CartRepository;
import com.avit.warsipharmacy.ui.cart.CartItem;
import com.avit.warsipharmacy.ui.category.CategoryItem;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private List<Pair<String,Integer>> categoriesItems;
    private List<CategoryItem> topSellingItems;
    private CartRepository cartRepository;

    public HomeViewModel(Application application) {
        super(application);

        cartRepository = new CartRepository(application);

        // TODO: MAKE THE NEWTOWRK CALL TO GET ALL CATEGORIES
        categoriesItems = new ArrayList<>();

        categoriesItems.add(new Pair<>("Tablets", R.drawable.tablets_icon));
        categoriesItems.add(new Pair<>("Syrups",R.drawable.syrups_icon));
        categoriesItems.add(new Pair<>("Tablets",R.drawable.tablets_icon));
        categoriesItems.add(new Pair<>("Syrups",R.drawable.syrups_icon));


        // TODO: MAKE NETWORK CALL TO GET THE TOP SELLING
        topSellingItems = new ArrayList<>();
        List<CategoryItem.PriceItem> priceItems = new ArrayList<>();

        priceItems.add(new CategoryItem.PriceItem("1 unit",120));
        priceItems.add(new CategoryItem.PriceItem("2 unit",170));
        priceItems.add(new CategoryItem.PriceItem("3 unit",220));

        topSellingItems.add(new CategoryItem("topselling1","zeher",2,priceItems));
        topSellingItems.add(new CategoryItem("topselling2","zeher",13,priceItems));
        topSellingItems.add(new CategoryItem("topselling3","zeher",26,priceItems));
        topSellingItems.add(new CategoryItem("topselling4","zeher",87,priceItems));


    }

    public void delete(CartItem cartItem){
        cartRepository.delete(cartItem);
    }

    public void addToCart(CartItem cartItem){
        cartRepository.insert(cartItem);
    }

    boolean isPresentInCart(CategoryItem categoryItem){
        return cartRepository.findCartItem(categoryItem.getItemName());
    }

    public void deleteCartItemByName(String name){
        cartRepository.deleteCartItemByName(name);
    }

    public List<CategoryItem> getTopSellingItems() {
        return topSellingItems;
    }

    public List<Pair<String, Integer>> getCategoriesItems() {
        return categoriesItems;
    }

}