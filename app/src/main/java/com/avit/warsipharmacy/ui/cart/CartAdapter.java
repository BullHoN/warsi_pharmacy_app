package com.avit.warsipharmacy.ui.cart;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.ui.category.CategoryItem;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    public interface ChangeCartItem{
        void delete(CartItem cartItem);
        void update(CartItem cartItem);
    }

    private String TAG = "CartAdapter";
    private Context context;
    private List<CartItem> cartItems;
    private ChangeCartItem changeCartItemInterface;

    public CartAdapter(Context context,ChangeCartItem changeCartItemInterface){
        this.context = context;
        cartItems = new ArrayList<>();
        this.changeCartItemInterface = changeCartItemInterface;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        final CartItem currItem = cartItems.get(position);

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCartItemInterface.delete(currItem);
            }
        });

        String[] possibleValues = currItem.getPossibleValues();
        final Spinner spinner =  holder.allowedValues;
        ArrayAdapter arrayAdapter = new ArrayAdapter(context,android.R.layout.simple_spinner_item,possibleValues);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);

        spinner.setSelection(currItem.getSelectedPriceIndex());

        final int[] check = {0};
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // It will execute first time if the value is one
                if(++check[0] > 1){
                    currItem.setSelectedPriceIndex(position);
                    changeCartItemInterface.update(currItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        int originalPrice = currItem.getPriceItems().get(currItem.getSelectedPriceIndex()).getPrice();
        int discount = currItem.getDiscount();
        int discountedPrice = originalPrice - originalPrice*currItem.getDiscount()/100;
        int savedAmount = originalPrice - discountedPrice;

        //TODO: DECIDE THE ITEM IMAGE BASED ON CATEOGRY

        holder.itemNameView.setText(currItem.getItemName());
        holder.itemPriceView.setText("₹" + discountedPrice);
        holder.itemOriginalPriceView.setText("₹" + originalPrice);
        holder.itemOriginalPriceView.setPaintFlags(holder.itemOriginalPriceView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.itemTotalSaveView.setText("SAVE " + "₹" + savedAmount);

    }

//    private String[] getThePossibleValues(String str){
//         String[] list = str.split(",");
//         return list;
//    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    public class  CartViewHolder extends RecyclerView.ViewHolder{

        TextView itemNameView,itemPriceView,itemOriginalPriceView,itemTotalSaveView;
        ImageView itemImage;
        ImageButton deleteItem;
        Spinner allowedValues;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            itemNameView = itemView.findViewById(R.id.item_name);
            itemPriceView = itemView.findViewById(R.id.item_price);
            itemOriginalPriceView = itemView.findViewById(R.id.item_original_price);
            itemTotalSaveView = itemView.findViewById(R.id.saved_price);

            itemImage = itemView.findViewById(R.id.item_image);

            deleteItem = itemView.findViewById(R.id.deleteItem);

            allowedValues = itemView.findViewById(R.id.unit_dropdown);

        }
    }

}
