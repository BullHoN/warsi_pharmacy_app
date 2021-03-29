package com.avit.warsipharmacy.ui.orders;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avit.warsipharmacy.network.NetworkAPI;
import com.avit.warsipharmacy.network.RetrofitClient;
import com.avit.warsipharmacy.ui.cart.CartItem;
import com.avit.warsipharmacy.ui.category.CategoryItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrdersViewModel extends AndroidViewModel {

    private MutableLiveData<List<OrderItem>> orderItemMutableLiveData;
    private String TAG = "OrdersViewModel";

    public OrdersViewModel(Application application) {
        super(application);

        orderItemMutableLiveData = new MutableLiveData<>();
        getOrderItemsFromServer();
//
//        List<CartItem> cartItems = new ArrayList<>();
//        List<CategoryItem.PriceItem> priceItems = new ArrayList<>();
//
//        priceItems.add(new CategoryItem.PriceItem("100ml",120));
//        priceItems.add(new CategoryItem.PriceItem("200ml",170));
//        priceItems.add(new CategoryItem.PriceItem("500ml",220));
//
//        cartItems.add(new CartItem("ssdgsdgsdg","sgsdg",0,12,priceItems,"sdfsfsfd"));
//        cartItems.add(new CartItem("zeher 2","sgsdg",1,22,priceItems,"svdsvsv"));
//        cartItems.add(new CartItem("zeher 3","sgsdg",2,27,priceItems,"virjojjeovj"));
//
//
//        // TODO: GET THE DATA FROM THE SERVER
//        List<OrderItem> orderItems = new ArrayList<>();
//        orderItems.add(new OrderItem("sdfsgsgddsg",new Date(),cartItems,0,25));
//        orderItems.add(new OrderItem("asfaiwrjwie",new Date(),cartItems,1,0));
//        orderItems.add(new OrderItem("vdgsgsgddsg",new Date(),cartItems,-1,12));
//        orderItems.add(new OrderItem("jbsgsgddsg",new Date(),cartItems,2,23));
//
//        orderItemMutableLiveData.setValue(orderItems);
    }

    private void getOrderItemsFromServer(){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        // TODO: PUT CORRECT CUSTOMER ID
        Call<List<OrderItem>> call = networkAPI.getOrderItems("6061692f9de9068198c78d06");
        call.enqueue(new Callback<List<OrderItem>>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                List<OrderItem> orderItems = response.body();
                orderItemMutableLiveData.setValue(orderItems);
            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {
                Toasty.error(getApplication(),t.getMessage(),Toasty.LENGTH_SHORT)
                        .show();
            }
        });

    }

    public MutableLiveData<List<OrderItem>> getOrderItemMutableLiveData() {
        return orderItemMutableLiveData;
    }
}