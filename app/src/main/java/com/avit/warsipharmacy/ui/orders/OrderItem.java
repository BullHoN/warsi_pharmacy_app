package com.avit.warsipharmacy.ui.orders;

import com.avit.warsipharmacy.ui.cart.CartItem;

import java.util.ArrayList;
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

    public static class OrderItemsclass{
        private List<CartItem> orderItems;

        public OrderItemsclass(List<CartItem> orderItems) {
            this.orderItems = orderItems;
        }

        public List<CartItem> getOrderItems() {
            return orderItems;
        }
    }

    public static class CreateOrderData{
        private List<CartItem> orderItems;
        private int deliveryPrice;
        private boolean isPaid;
        private String customerFCMID;
        private String userId;

        // user details
        private String name,buildingName,mainAddress,landMark,pinCode;

//        public CreateOrderData(List<CartItem> orderItems, int deliveryPrice, boolean isPaid
//                , String customerFCMID, String userId) {
//            this.orderItems = orderItems;
//            this.deliveryPrice = deliveryPrice;
//            this.isPaid = isPaid;
//            this.customerFCMID = customerFCMID;
//            this.userId = userId;
//        }

        public CreateOrderData(List<CartItem> orderItems, int deliveryPrice, boolean isPaid, String customerFCMID, String userId, String name, String buildingName, String mainAddress, String landMark, String pinCode) {
            this.orderItems = orderItems;
            this.deliveryPrice = deliveryPrice;
            this.isPaid = isPaid;
            this.customerFCMID = customerFCMID;
            this.userId = userId;
            this.name = name;
            this.buildingName = buildingName;
            this.mainAddress = mainAddress;
            this.landMark = landMark;
            this.pinCode = pinCode;
        }

        public String getName() {
            return name;
        }

        public String getBuildingName() {
            return buildingName;
        }

        public String getMainAddress() {
            return mainAddress;
        }

        public String getLandMark() {
            return landMark;
        }

        public String getPinCode() {
            return pinCode;
        }

        public String getUserId() {
            return userId;
        }

        public List<CartItem> getOrderItems() {
            return orderItems;
        }

        public int getDeliveryPrice() {
            return deliveryPrice;
        }

        public boolean isPaid() {
            return isPaid;
        }

        public String getCustomerFCMID() {
            return customerFCMID;
        }
    }

}
