package com.avit.warsipharmacy.ui.category;

import android.app.Application;
import android.graphics.Paint;
import android.util.Pair;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avit.warsipharmacy.db.CartRepository;
import com.avit.warsipharmacy.ui.cart.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private MutableLiveData<List<CategoryItem>> categoryItems;
    private CartRepository cartRepository;
    private LiveData<List<CartItem>> cartItemLiveData;

    public CategoryViewModel(Application application){
        super(application);

        //TODO: GET THE CATEGORY ITEMS FROM DATABASE
        cartRepository = new CartRepository(application);
        cartItemLiveData = cartRepository.getAllItems();

        categoryItems = new MutableLiveData<>();
        List<CategoryItem> list = new ArrayList<>();
        List<CategoryItem.PriceItem> priceItems = new ArrayList<>();

        priceItems.add(new CategoryItem.PriceItem("100ml",120));
        priceItems.add(new CategoryItem.PriceItem("200ml",170));
        priceItems.add(new CategoryItem.PriceItem("500ml",220));

        // TODO: REMOVE THE PRICE FROM HERE
        list.add(new CategoryItem("AMREE PLUS CAPSULE 20","Tablets",5,priceItems));
        list.add(new CategoryItem("AMREE PLUS CAPSULE 21","Syrups",10,priceItems));
        list.add(new CategoryItem("AMREE PLUS CAPSULE 22","Tablets",14,priceItems));
        list.add(new CategoryItem("AMREE PLUS CAPSULE 23","Syrups",19,priceItems));
        list.add(new CategoryItem("AMREE PLUS CAPSULE 24","Tablets",80,priceItems));

        categoryItems.setValue(list);

    }

    public Pair<Integer,Integer> getDetails(List<CartItem> cartItems){
        int total_amount = 0;
        int total_save = 0;
        for(CartItem cartItem : cartItems){
            int curr_price = cartItem.getPriceItems().get(cartItem.getSelectedPriceIndex()).getPrice();
            int curr_amount = curr_price - (curr_price*cartItem.getDiscount()/100);
            total_amount += curr_amount;
            total_save += curr_price - curr_amount;
        }

        return new Pair<>(total_amount,total_save);
    }

    boolean isPresentInCart(CategoryItem categoryItem){
        return cartRepository.findCartItem(categoryItem.getItemName());
    }

    public void deleteCartItemByName(String name){
        cartRepository.deleteCartItemByName(name);
    }

    public LiveData<List<CartItem>> getCartItemLiveData() {
        return cartItemLiveData;
    }

    public void addToCart(CartItem cartItem){
        cartRepository.insert(cartItem);
    }

    public MutableLiveData<List<CategoryItem>> getCategoryItems() {
        return categoryItems;
    }
}