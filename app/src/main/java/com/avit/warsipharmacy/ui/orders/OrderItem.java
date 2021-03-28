package com.avit.warsipharmacy.ui.orders;

import com.avit.warsipharmacy.ui.cart.CartItem;

import java.util.Date;
import java.util.List;

public class OrderItem {
    private String order_id;
    private Date order_date;
    private List<CartItem> orderItems;
    private int status;
    private int deliveryPrice;

    public OrderItem(String order_id, Date order_date, List<CartItem> orderItems, int status, int deliveryPrice) {
        this.order_id = order_id;
        this.order_date = order_date;
        this.orderItems = orderItems;
        this.status = status;
        this.deliveryPrice = deliveryPrice;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public static String getOrderStatus(int s){
        switch (s){
            case 0: return "Got Your Order";
            case 1: return "Out For Delivery";
            case 2: return "Delivered";
            case -1: return "Cancelled";
        }
        return "";
    }

    public static int getTotal(List<CartItem> cartItemList){
        int total_amount = 0;
        for(CartItem cartItem : cartItemList){
            int curr_price = cartItem.getPriceItems().get(cartItem.getSelectedPriceIndex()).getPrice();
            int curr_amount = curr_price - (curr_price*cartItem.getDiscount()/100);
            total_amount += curr_amount;
        }

        return total_amount;
    }

    public String getOrder_id() {
        return order_id;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public List<CartItem> getOrderItems() {
        return orderItems;
    }

    public int getStatus() {
        return status;
    }

}
