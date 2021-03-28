package com.avit.warsipharmacy.ui.category;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.avit.warsipharmacy.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    public interface CategoryInterface{
        void addToCart(CategoryItem categoryItem);
        boolean alreadyAddedToCart(CategoryItem categoryItem);
        void deleteFromCart(String name);
    }

    private List<CategoryItem> categoryItemList;
    private Context context;
    private CategoryInterface categoryInterface;
    private boolean isRecommendation;
    private String TAG = "CategoryAdapter";

    public CategoryAdapter(Context context,CategoryInterface categoryInterface){
        this.categoryItemList = new ArrayList<>();
        this.context = context;
        this.categoryInterface = categoryInterface;
        isRecommendation = false;
    }

    public CategoryAdapter(Context context,CategoryInterface categoryInterface,boolean isRecommendation){
        this.categoryItemList = new ArrayList<>();
        this.context = context;
        this.categoryInterface = categoryInterface;
        this.isRecommendation = isRecommendation;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position) {

        final CategoryItem currItem = categoryItemList.get(position);
        boolean isalreadyPresent = categoryInterface.alreadyAddedToCart(currItem);


        if(isalreadyPresent){
            holder.addToCartButton.setVisibility(View.GONE);
            holder.deleteItemButton.setVisibility(View.VISIBLE);
        }else{
            holder.addToCartButton.setVisibility(View.VISIBLE);
            holder.deleteItemButton.setVisibility(View.GONE);
        }

//         Add to Cart
        if(!isalreadyPresent) {
            holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.addToCartButton.setVisibility(View.GONE);
                    holder.deleteItemButton.setVisibility(View.VISIBLE);
                    categoryInterface.addToCart(currItem);

                    if(isRecommendation){
                        categoryItemList.remove(position);
                        notifyDataSetChanged();
                        return;
                    }

                    holder.deleteItemButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.addToCartButton.setVisibility(View.VISIBLE);
                            holder.deleteItemButton.setVisibility(View.GONE);
                            categoryInterface.deleteFromCart(currItem.getItemName());
                        }
                    });
                }
            });
        }else{

            holder.deleteItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.addToCartButton.setVisibility(View.VISIBLE);
                    holder.deleteItemButton.setVisibility(View.GONE);
                    categoryInterface.deleteFromCart(currItem.getItemName());

                    holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.addToCartButton.setVisibility(View.GONE);
                            holder.deleteItemButton.setVisibility(View.VISIBLE);
                            categoryInterface.addToCart(currItem);
                        }
                    });
                }
            });
        }

        // DECIDE THE PRICE BASED ON CURRENT SELECTION OF PRICE ITEM

        int originalPrice = currItem.getPriceItems().get(0).getPrice();
        int discount = currItem.getDiscount();
        int discountedPrice = originalPrice - originalPrice*currItem.getDiscount()/100;
        int savedAmount = originalPrice - discountedPrice;

        //TODO: DECIDE THE ITEM IMAGE BASED ON CATEOGRY

        holder.itemNameView.setText(currItem.getItemName());
        holder.itemPriceView.setText("₹" + discountedPrice);
        holder.itemOriginalPriceView.setText("₹" + originalPrice);
        holder.itemOriginalPriceView.setPaintFlags(holder.itemOriginalPriceView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.itemTotalSaveView.setText("SAVE " + "₹" + savedAmount);
        holder.itemDiscountView.setText(discount + "% OFF");

    }

    public void setCategoryItemList(List<CategoryItem> categoryItemList) {
        this.categoryItemList = categoryItemList;
        notifyDataSetChanged();
    }

    public void addCategoryItems(List<CategoryItem> categoryItems){
        this.categoryItemList.addAll(categoryItems);
        notifyDataSetChanged();
    }

    public void clearAllItems(){
        this.categoryItemList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{

        TextView itemNameView,itemPriceView,itemDiscountView,itemOriginalPriceView,itemTotalSaveView;
        ImageView itemImage;
        RelativeLayout addToCartButton;
        ImageButton deleteItemButton;
        LinearLayout categoryItemLayout;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            itemDiscountView = itemView.findViewById(R.id.discount);
            itemNameView = itemView.findViewById(R.id.item_name);
            itemPriceView = itemView.findViewById(R.id.item_price);
            itemOriginalPriceView = itemView.findViewById(R.id.item_original_price);
            itemTotalSaveView = itemView.findViewById(R.id.saved_price);

            itemImage = itemView.findViewById(R.id.item_image);

            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
            deleteItemButton = itemView.findViewById(R.id.deleteItem);

            categoryItemLayout = itemView.findViewById(R.id.category_item_layout);

        }
    }



}
