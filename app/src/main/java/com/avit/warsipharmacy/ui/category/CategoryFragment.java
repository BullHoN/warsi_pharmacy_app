package com.avit.warsipharmacy.ui.category;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private CategoryViewModel categoryViewModel;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private TextView itemsCountView, categoryNameView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.category_fragment, container, false);
        categoryViewModel =
                ViewModelProviders.of(this).get(CategoryViewModel.class);

        searchView = root.findViewById(R.id.search_bar);
        searchView.setSubmitButtonEnabled(true);

        // To hide the keyboard when not required
        Utility.setupUI(root,getContext());

        // back Button
        root.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .popBackStackImmediate();
            }
        });

        Bundle bundle = getArguments();
        String categoryName = bundle.getString("categoryName");
        categoryNameView = root.findViewById(R.id.categories_heading);
        categoryNameView.setText(categoryName);

        itemsCountView = root.findViewById(R.id.items_count);
        itemsCountView.setText("0 Items");

        recyclerView = root.findViewById(R.id.categories_list_items);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(getContext(), new CategoryAdapter.CategoryInterface() {
            @Override
            public void addToCart(CategoryItem categoryItem) {
                // TODO: ADD THE ADD TO CART FUNCIONALITY
                Toast.makeText(getContext(),categoryItem.getItemName(),Toast.LENGTH_SHORT)
                .show();
            }
        });

        recyclerView.setAdapter(categoryAdapter);

        categoryViewModel.getCategoryItems().observe(getViewLifecycleOwner(), new Observer<List<CategoryItem>>() {
            @Override
            public void onChanged(List<CategoryItem> categoryItems) {
                Toast.makeText(getContext(),categoryItems.size()+"",Toast.LENGTH_SHORT)
                        .show();
                categoryAdapter.setCategoryItemList(categoryItems);
                itemsCountView.setText(categoryItems.size() + " Items");
            }
        });


        // TODO: IMPLEMENT SEARCH QUERY LOGIC
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getContext(),query,Toast.LENGTH_SHORT)
                        .show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return root;
    }

}