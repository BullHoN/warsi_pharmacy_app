package com.avit.warsipharmacy.ui.checkout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.ui.cart.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CheckoutOrderItemsAdapter extends RecyclerView.Adapter<CheckoutOrderItemsAdapter.CheckoutOrderItemsViewHolder>{

    private List<CartItem> cartItems;
    private Context context;

    public CheckoutOrderItemsAdapter(Context context) {
        this.context = context;
        cartItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public CheckoutOrderItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.checkout_order_item,parent,false);
        return new CheckoutOrderItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutOrderItemsViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        int originalPrice = cartItem.getPriceItems().get(cartItem.getSelectedPriceIndex()).getPrice();
        int price = originalPrice - (originalPrice*cartItem.getDiscount()/100);
        int savedPrice = originalPrice - price;

        holder.priceView.setText("₹ " + price);
        holder.nameView.setText(cartItem.getItemName());
        holder.totalPriceView.setText(cartItem.getPriceItems().get(cartItem.getSelectedPriceIndex()).getPriceTag());
        holder.savedPriceView.setText("SAVED " + "₹" + savedPrice);

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    public class CheckoutOrderItemsViewHolder extends RecyclerView.ViewHolder{

        TextView priceView,savedPriceView,totalPriceView, nameView;

        public CheckoutOrderItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            priceView = itemView.findViewById(R.id.item_price);
            nameView = itemView.findViewById(R.id.item_name);
            savedPriceView = itemView.findViewById(R.id.saved_price);
            totalPriceView = itemView.findViewById(R.id.item_total_price);

        }
    }
}
