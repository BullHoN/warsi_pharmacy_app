package com.avit.warsipharmacy.ui.search;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.ui.cart.CartItem;
import com.avit.warsipharmacy.ui.category.CategoryAdapter;
import com.avit.warsipharmacy.ui.category.CategoryItem;
import com.avit.warsipharmacy.ui.category.CategoryViewModel;
import com.avit.warsipharmacy.utility.Utility;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class SearchFragment extends Fragment {

    private SearchView searchView;
    private CategoryViewModel searchViewModel;
    private ScrollView scrollView;
    private int limit = 10,page_search = 1;
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private ProgressBar mainProgressBar,bottomProgressBar;
    private String search_query;
    private boolean requestMoreItems;
    private TextView itemsCountView;
    private String TAG = "SearchFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        searchViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);


        searchView = root.findViewById(R.id.search_bar);
        scrollView = root.findViewById(R.id.scrollView);
        recyclerView = root.findViewById(R.id.categories_list_items);
        mainProgressBar = root.findViewById(R.id.mainProgressBar);
        bottomProgressBar = root.findViewById(R.id.progressBar);
        itemsCountView = root.findViewById(R.id.items_count);
        requestMoreItems = false;

        searchView.setSubmitButtonEnabled(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        // To hide the keyboard when not required
        Utility.setupUI(root,getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        // add to cart events
        categoryAdapter = new CategoryAdapter(getContext(), new CategoryAdapter.CategoryInterface() {
            @Override
            public void addToCart(CategoryItem categoryItem) {

                CartItem cartItem = new CartItem(categoryItem.getItemName(),categoryItem.getCategoryName(),0,categoryItem.getDiscount()
                        ,categoryItem.getPriceItems(),categoryItem.get_id());
                searchViewModel.addToCart(cartItem);

            }

            @Override
            public boolean alreadyAddedToCart(CategoryItem categoryItem) {
                return searchViewModel.isPresentInCart(categoryItem);
            }

            @Override
            public void deleteFromCart(String name) {
                searchViewModel.deleteCartItemByName(name);
            }
        });

        recyclerView.setAdapter(categoryAdapter);

        // Search Items
        searchViewModel.getSearchItems().observe(getViewLifecycleOwner(), new Observer<List<CategoryItem>>() {
            @Override
            public void onChanged(List<CategoryItem> categoryItems) {
                if(mainProgressBar.getVisibility() == View.VISIBLE){
                    mainProgressBar.setVisibility(View.GONE);
                    if(categoryItems.size() == 0){
                        Toasty.warning(getContext(),"No Items Found",Toasty.LENGTH_SHORT)
                                .show();
                    }
                }

                if(bottomProgressBar.getVisibility() == View.VISIBLE && categoryItems.size() == 0) {
                    requestMoreItems = false;
                    bottomProgressBar.setVisibility(View.INVISIBLE);
                }

                if(categoryItems.size() > 0){
                    bottomProgressBar.setVisibility(View.INVISIBLE);
                }

                categoryAdapter.addCategoryItems(categoryItems);
                itemsCountView.setText(categoryAdapter.getItemCount() + " Items");

            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mainProgressBar.setVisibility(View.VISIBLE);
                page_search = 1;
                categoryAdapter.clearAllItems();
                search_query = query;
                requestMoreItems = true;

                searchViewModel.getTheSearchItemsFromServer(null,query,limit,page_search++);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Back Button
        root.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .popBackStackImmediate();
            }
        });

        // Scroll Event Listener for at bottom
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if(scrollView.getHeight() > 400 && mainProgressBar.getVisibility() != View.VISIBLE && requestMoreItems){

                    View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
                    int bottomDetector = view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY());
                    if (bottomDetector == 0) {
                        bottomProgressBar.setVisibility(View.VISIBLE);
                        searchViewModel.getTheSearchItemsFromServer(null,search_query,limit,page_search++);
                    }
                }
            }
        });



        return root;
    }
}