package com.avit.warsipharmacy.ui.category;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avit.warsipharmacy.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    public interface CategoryInterface{
        void addToCart(CategoryItem categoryItem);
    }

    private List<CategoryItem> categoryItemList;
    private Context context;
    private CategoryInterface categoryInterface;

    public CategoryAdapter(Context context,CategoryInterface categoryInterface){
        this.categoryItemList = new ArrayList<>();
        this.context = context;
        this.categoryInterface = categoryInterface;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {
        final CategoryItem currItem = categoryItemList.get(position);

//         Add to Cart
        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryInterface.addToCart(currItem);
            }
        });

        int originalPrice = currItem.getPrice();
        int discount = currItem.getDiscount();
        int discountedPrice = currItem.getPrice() - currItem.getPrice()*currItem.getDiscount()/100;
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

    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{

        TextView itemNameView,itemPriceView,itemDiscountView,itemOriginalPriceView,itemTotalSaveView;
        ImageView itemImage;
        ImageButton addToCartButton;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            itemDiscountView = itemView.findViewById(R.id.discount);
            itemNameView = itemView.findViewById(R.id.item_name);
            itemPriceView = itemView.findViewById(R.id.item_price);
            itemOriginalPriceView = itemView.findViewById(R.id.item_original_price);
            itemTotalSaveView = itemView.findViewById(R.id.saved_price);

            itemImage = itemView.findViewById(R.id.item_image);

            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);

        }
    }

}
