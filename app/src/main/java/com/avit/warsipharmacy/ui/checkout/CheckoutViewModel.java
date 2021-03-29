package com.avit.warsipharmacy.ui.checkout;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avit.warsipharmacy.db.CartRepository;
import com.avit.warsipharmacy.network.NetworkAPI;
import com.avit.warsipharmacy.network.RetrofitClient;
import com.avit.warsipharmacy.ui.cart.CartItem;
import com.avit.warsipharmacy.ui.category.CategoryItem;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CheckoutViewModel extends AndroidViewModel {
    private LiveData<List<CartItem>> cartItems;
    private MutableLiveData<Integer> deliveryPrice;
    private MutableLiveData<UpiDetails> upiDetailsMutableLiveData;
    private CartRepository cartRepository;

    public CheckoutViewModel(Application application){
        super(application);

        cartRepository = new CartRepository(application);
        cartItems = cartRepository.getAllItems();

        deliveryPrice = new MutableLiveData<>();
        deliveryPrice.setValue(-1);
        getDeliveryPriceFromServer();

        upiDetailsMutableLiveData = new MutableLiveData<>();
        getUpiDetailsFromServer();

    }

    private void getUpiDetailsFromServer(){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Call<UpiDetails> call = networkAPI.getUpiDetails();

        call.enqueue(new Callback<UpiDetails>() {
            @Override
            public void onResponse(Call<UpiDetails> call, Response<UpiDetails> response) {
                upiDetailsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UpiDetails> call, Throwable t) {
                Toasty.error(getApplication(),"Some error occurred please try again later",Toasty.LENGTH_LONG)
                        .show();
            }
        });

    }

    private void getDeliveryPriceFromServer(){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Call<Integer> call = networkAPI.getDeliveryPrice();
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                deliveryPrice.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toasty.error(getApplication(),"Some error occurred please try again later",Toasty.LENGTH_LONG)
                        .show();
            }
        });


    }

    public void clearCart(){
        cartRepository.deleteAll();
    }

    public MutableLiveData<UpiDetails> getUpiDetailsMutableLiveData() {
        return upiDetailsMutableLiveData;
    }

    public MutableLiveData<Integer> getDeliveryPrice() {
        return deliveryPrice;
    }

    public LiveData<List<CartItem>> getCartItems() {
        return cartItems;
    }
}
