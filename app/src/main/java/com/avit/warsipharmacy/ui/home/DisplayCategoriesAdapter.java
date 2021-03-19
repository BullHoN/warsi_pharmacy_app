package com.avit.warsipharmacy.ui.home;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avit.warsipharmacy.R;

import java.util.ArrayList;
import java.util.List;

public class DisplayCategoriesAdapter extends RecyclerView.Adapter<DisplayCategoriesAdapter.DisplayCategoriesViewHolder>{

    public interface DisplayCategoryOnClickListener{
        void onItemClick(int position);
    }

    // Pair<Name,ImageResource>
    private List<Pair<String,Integer>> items;
    private Context context;
    private DisplayCategoryOnClickListener displayCategoryOnClickListener;

    public DisplayCategoriesAdapter(List<Pair<String,Integer>> items,Context context
            ,DisplayCategoryOnClickListener displayCategoryOnClickListener){
        this.items = items;
        this.context = context;
        this.displayCategoryOnClickListener = displayCategoryOnClickListener;
    }

    @NonNull
    @Override
    public DisplayCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.categories_item,parent,false);
        return new DisplayCategoriesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayCategoriesViewHolder holder, final int position) {
        // TODO: Assign the respective images and text
        Pair<String,Integer> currPair = items.get(position);

        holder.nameView.setText(currPair.first);
        holder.imageView.setImageResource(currPair.second);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCategoryOnClickListener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class DisplayCategoriesViewHolder extends RecyclerView.ViewHolder{

        TextView nameView;
        ImageView imageView;
        LinearLayout cardView;

        public DisplayCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.category_text);
            imageView = itemView.findViewById(R.id.category_image);
            cardView = itemView.findViewById(R.id.categories_list_item);
        }
    }

}

