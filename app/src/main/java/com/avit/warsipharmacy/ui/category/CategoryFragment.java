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

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.ui.cart.CartFragment;
import com.avit.warsipharmacy.ui.cart.CartItem;
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

                List<CategoryItem.PriceItem> priceItems = new ArrayList<>();
                priceItems.add(new CategoryItem.PriceItem("100ml",300));
                priceItems.add(new CategoryItem.PriceItem("300ml",500));
                priceItems.add(new CategoryItem.PriceItem("500ml",800));

                CartItem cartItem = new CartItem(categoryItem.getItemName(),categoryItem.getCategoryName(),0,categoryItem.getDiscount()
                        ,priceItems);
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

        categoryViewModel.getCategoryItems().observe(getViewLifecycleOwner(), new Observer<List<CategoryItem>>() {
            @Override
            public void onChanged(List<CategoryItem> categoryItems) {
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