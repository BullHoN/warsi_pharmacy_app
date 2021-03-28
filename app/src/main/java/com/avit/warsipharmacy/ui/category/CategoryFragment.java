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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.ui.cart.CartFragment;
import com.avit.warsipharmacy.ui.cart.CartItem;
import com.avit.warsipharmacy.utility.Utility;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class CategoryFragment extends Fragment {

    private CategoryViewModel categoryViewModel;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private TextView itemsCountView, categoryNameView;
    private int page = 1,limit = 10;
    private int page_search = 1;
    private ScrollView scrollView;
    private ProgressBar bottomProgressBar, mainProgressBar;
    private boolean requestMoreItems, isSearching;
    private String search_query;
    private String TAG = "CategoryFragment";
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.category_fragment, container, false);
        categoryViewModel =
                ViewModelProviders.of(this).get(CategoryViewModel.class);

        scrollView = root.findViewById(R.id.scrollView);
        searchView = root.findViewById(R.id.search_bar);
        bottomProgressBar = root.findViewById(R.id.progressBar);
        mainProgressBar = root.findViewById(R.id.mainProgressBar);
        swipeRefreshLayout = root.findViewById(R.id.swipable_layout);

        searchView.setSubmitButtonEnabled(true);
        requestMoreItems = true;
        isSearching = false;

        // To hide the keyboard when not required
        Utility.setupUI(root,getContext());

        // back Button
        root.findViewById(R.id.backButton).setOnClickListener(v -> getFragmentManager()
                .popBackStackImmediate());

        Bundle bundle = getArguments();
        String categoryName = bundle.getString("categoryName");
        categoryNameView = root.findViewById(R.id.categories_heading);
        categoryNameView.setText(categoryName);

        categoryViewModel.getTheCategoryItemsFromServer(categoryName.toLowerCase(),limit,page++);

        itemsCountView = root.findViewById(R.id.items_count);
        itemsCountView.setText("0 Items");

        recyclerView = root.findViewById(R.id.categories_list_items);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // LOADING MORE ELEMENTS IN SCROLL END
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if(scrollView.getHeight() > 300 && requestMoreItems) {
                View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
                int bottomDetector = view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY());
                if (bottomDetector == 0 && mainProgressBar.getVisibility() != View.VISIBLE) {
                    bottomProgressBar.setVisibility(View.VISIBLE);
                    if(!isSearching) {
                        categoryViewModel.getTheCategoryItemsFromServer(categoryName.toLowerCase(), limit, page++);
                    }else{
                        categoryViewModel.getTheSearchItemsFromServer(categoryName.toLowerCase(),search_query,limit,page_search++);
                    }
                }
            }
        });


        categoryAdapter = new CategoryAdapter(getContext(), new CategoryAdapter.CategoryInterface() {
            @Override
            public void addToCart(CategoryItem categoryItem) {

                CartItem cartItem = new CartItem(categoryItem.getItemName(),categoryItem.getCategoryName(),0,categoryItem.getDiscount()
                        ,categoryItem.getPriceItems());
                categoryViewModel.addToCart(cartItem);

            }

            @Override
            public boolean alreadyAddedToCart(CategoryItem categoryItem) {
                return categoryViewModel.isPresentInCart(categoryItem);
            }

            @Override
            public void deleteFromCart(String name) {
                categoryViewModel.deleteCartItemByName(name);
            }
        });

        recyclerView.setAdapter(categoryAdapter);

        // OPEN CART VIEW LOGIC
        categoryViewModel.getCartItemLiveData().observe(getViewLifecycleOwner(), new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItemsList) {

                if(cartItemsList.size() > 0){
                    Pair<Integer,Integer> details = categoryViewModel.getDetails(cartItemsList);
                    showGoToCart(true,details);
                }else{
                    showGoToCart(false,null);
                }
            }
        });

        // show category items
        categoryViewModel.getCategoryItems().observe(getViewLifecycleOwner(), new Observer<List<CategoryItem>>() {
            @Override
            public void onChanged(List<CategoryItem> categoryItems) {

                if(mainProgressBar.getVisibility() == View.VISIBLE){
                    mainProgressBar.setVisibility(View.GONE);
                    if(categoryItems.size() == 0){
                        Toasty.warning(getContext(),"No Items Found",Toasty.LENGTH_SHORT)
                                .show();
                    }
                }

                if(bottomProgressBar.getVisibility() == View.VISIBLE && categoryItems.size() == 0){
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


        // show search items
        categoryViewModel.getSearchItems().observe(getViewLifecycleOwner(), new Observer<List<CategoryItem>>() {
            @Override
            public void onChanged(List<CategoryItem> categoryItems) {
                mainProgressBar.setVisibility(View.INVISIBLE);
                if(!isSearching){
                    // Main ProgressBar
                    isSearching = true;
                    if(categoryItems.size() == 0){
                        Toasty.warning(getContext(),"No Items Found",Toasty.LENGTH_SHORT)
                                .show();
                    }else{
                        categoryAdapter.setCategoryItemList(categoryItems);
                        itemsCountView.setText(categoryAdapter.getItemCount() + " Items");
                    }
                }
                else{
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

            }
        });

        // search item listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search_query = query;
                isSearching = false;
                page_search = 1;
                requestMoreItems = true;
                mainProgressBar.setVisibility(View.VISIBLE);
                categoryAdapter.clearAllItems();

                categoryViewModel.getTheSearchItemsFromServer(categoryName.toLowerCase(),query,limit,page_search++);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // on refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                categoryAdapter.clearAllItems();
                page = 1;
                isSearching = false;
                requestMoreItems = true;
                mainProgressBar.setVisibility(View.VISIBLE);
                categoryViewModel.getTheCategoryItemsFromServer(categoryName.toLowerCase(),limit,page++);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return root;
    }

    private void showGoToCart(Boolean show,Pair<Integer,Integer> details){
        LinearLayout cartDetailsView = getActivity().findViewById(R.id.cartDetails);
        if(!show){
            cartDetailsView.setVisibility(View.INVISIBLE);
            return;
        }else{
            cartDetailsView.setVisibility(View.VISIBLE);
            TextView totalPriceView = cartDetailsView.findViewById(R.id.total_price);
            totalPriceView.setText("₹ " + ((float)details.first));

            TextView saveView = cartDetailsView.findViewById(R.id.saved_price);
            saveView.setText("SAVED ₹" + details.second);

            cartDetailsView.findViewById(R.id.go_to_cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new CartFragment();

                    BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);

                    openFragment(fragment,android.R.anim.fade_in,android.R.anim.fade_out);
                }
            });

        }
    }

    private void openFragment(Fragment fragment,int anim1,int anim2){
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(anim1,anim2)
                .replace(R.id.nav_host_fragment,fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        showGoToCart(false,null);
    }

    @Override
    public void onPause() {
        super.onPause();
        showGoToCart(false,null);
    }

}