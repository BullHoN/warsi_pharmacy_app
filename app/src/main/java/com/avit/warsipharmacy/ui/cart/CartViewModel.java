package com.avit.warsipharmacy.ui.cart;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avit.warsipharmacy.db.CartRepository;
import com.avit.warsipharmacy.ui.category.CategoryItem;

import java.util.ArrayList;
import java.util.List;

public class CartViewModel extends AndroidViewModel {

    LiveData<List<CartItem>> categoryItems;
    List<CategoryItem> recommendationList;
    private CartRepository cartRepository;

    public CartViewModel(Application application) {
        super(application);

        // TODO: GET THE RECOMMENDATION ITEMS FROM SERVER

        cartRepository = new CartRepository(application);
        categoryItems = cartRepository.getAllItems();

        recommendationList = new ArrayList<>();
        List<CategoryItem.PriceItem> priceItems = new ArrayList<>();

        priceItems.add(new CategoryItem.PriceItem("100ml",120));
        priceItems.add(new CategoryItem.PriceItem("200ml",170));
        priceItems.add(new CategoryItem.PriceItem("500ml",220));

        recommendationList.add(new CategoryItem("AMREE PLUS CAPSULE 30","Tablets",5,priceItems));
        recommendationList.add(new CategoryItem("AMREE PLUS CAPSULE 31","Syrups",10,priceItems));
        recommendationList.add(new CategoryItem("AMREE PLUS CAPSULE 32","Tablets",14,priceItems));

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

    public void update(CartItem cartItem){
        cartRepository.update(cartItem);
    }

    public List<CategoryItem> getRecommendationList() {
        return recommendationList;
    }

    public LiveData<List<CartItem>> getCategoryItems() {
        return categoryItems;
    }
}