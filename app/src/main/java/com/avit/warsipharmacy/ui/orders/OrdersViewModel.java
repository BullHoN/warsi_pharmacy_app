package com.avit.warsipharmacy.ui.orders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avit.warsipharmacy.ui.cart.CartItem;
import com.avit.warsipharmacy.ui.category.CategoryItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdersViewModel extends ViewModel {

    private MutableLiveData<List<OrderItem>> orderItemMutableLiveData;

    public OrdersViewModel() {

        orderItemMutableLiveData = new MutableLiveData<>();

        List<CartItem> cartItems = new ArrayList<>();
        List<CategoryItem.PriceItem> priceItems = new ArrayList<>();

        priceItems.add(new CategoryItem.PriceItem("100ml",120));
        priceItems.add(new CategoryItem.PriceItem("200ml",170));
        priceItems.add(new CategoryItem.PriceItem("500ml",220));

        cartItems.add(new CartItem("ssdgsdgsdg","sgsdg",0,12,priceItems));
        cartItems.add(new CartItem("zeher 2","sgsdg",1,22,priceItems));
        cartItems.add(new CartItem("zeher 3","sgsdg",2,27,priceItems));


        // TODO: GET THE DATA FROM THE SERVER
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem("sdfsgsgddsg",new Date(),cartItems,0,25));
        orderItems.add(new OrderItem("asfaiwrjwie",new Date(),cartItems,1,0));
        orderItems.add(new OrderItem("vdgsgsgddsg",new Date(),cartItems,-1,12));
        orderItems.add(new OrderItem("jbsgsgddsg",new Date(),cartItems,2,23));

        orderItemMutableLiveData.setValue(orderItems);
    }

    public MutableLiveData<List<OrderItem>> getOrderItemMutableLiveData() {
        return orderItemMutableLiveData;
    }
}