package com.avit.warsipharmacy.ui.checkout;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avit.warsipharmacy.db.CartRepository;
import com.avit.warsipharmacy.ui.cart.CartItem;
import com.avit.warsipharmacy.ui.category.CategoryItem;

import java.util.ArrayList;
import java.util.List;

public class CheckoutViewModel extends AndroidViewModel {
    private LiveData<List<CartItem>> cartItems;

    public CheckoutViewModel(Application application){
        super(application);

        CartRepository cartRepository = new CartRepository(application);
        cartItems = cartRepository.getAllItems();

        // TODO: GET DELIVERY CHARGE FROM THE SERVER

    }

    public LiveData<List<CartItem>> getCartItems() {
        return cartItems;
    }
}
