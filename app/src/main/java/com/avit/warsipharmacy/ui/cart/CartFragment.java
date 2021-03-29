package com.avit.warsipharmacy.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.FtsOptions;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.ui.category.CategoryAdapter;
import com.avit.warsipharmacy.ui.category.CategoryItem;
import com.avit.warsipharmacy.ui.checkout.CheckoutFragment;
import com.avit.warsipharmacy.ui.orders.OrderItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private CartViewModel cartViewModel;
    private RecyclerView cartItemsList, recommendationRecyclerView;
    private CartAdapter cartAdapter;
    private String TAG = "CartFragment";
    private LinearLayout emptyCartView, recommendationView;
    private Button proceedButton;
    private Gson gson;
    private CategoryAdapter recommendationAdapter;
    private ProgressBar recommendationProgressBar;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cartViewModel =
                ViewModelProviders.of(this).get(CartViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        gson = new Gson();
        cartItemsList = root.findViewById(R.id.cart_items_list);
        recommendationRecyclerView = root.findViewById(R.id.recommendation_items);
        proceedButton = root.findViewById(R.id.proceed_button);
        emptyCartView = root.findViewById(R.id.emptyCart);
        recommendationView = root.findViewById(R.id.recommendationView);
        recommendationProgressBar = root.findViewById(R.id.recommendationProgressBar);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        cartItemsList.setLayoutManager(layoutManager);

        cartAdapter = new CartAdapter(getContext(), new CartAdapter.ChangeCartItem() {
            @Override
            public void delete(CartItem cartItem) {
                cartViewModel.delete(cartItem);
            }

            @Override
            public void update(CartItem cartItem) {
                cartViewModel.update(cartItem);
            }
        });
        cartItemsList.setAdapter(cartAdapter);

        cartViewModel.getCategoryItems().observe(getViewLifecycleOwner(), new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItemList) {
                cartAdapter.setCartItems(cartItemList);
                if(cartItemList.size() > 0) {
                    emptyCartView.setVisibility(View.GONE);
                    recommendationView.setVisibility(View.VISIBLE);
                    recommendationProgressBar.setVisibility(View.VISIBLE);
                    setUpRecommendationAdapter();
                }else{
                    emptyCartView.setVisibility(View.VISIBLE);
                    recommendationView.setVisibility(View.GONE);
                }
            }
        });

        // proceed button
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CheckoutFragment();

                OrderItem.OrderItemsclass orderItemsclass = new OrderItem.OrderItemsclass(cartViewModel.getCategoryItems().getValue());

                Bundle bundle = new Bundle();
                String cartItemString = gson.toJson(orderItemsclass, OrderItem.OrderItemsclass.class);
                bundle.putString("cartItems",cartItemString);
                fragment.setArguments(bundle);

                openFragment(fragment,android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });



        return root;
    }

    private void setUpRecommendationAdapter(){
        // recommendation recyclerview
        recommendationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false){
            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
                lp.width = getWidth() - 60;
                return true;
            }
        });

        CategoryAdapter recommendationAdapter = new CategoryAdapter(getContext(), new CategoryAdapter.CategoryInterface() {
            @Override
            public void addToCart(CategoryItem categoryItem) {

                CartItem cartItem = new CartItem(categoryItem.getItemName(),categoryItem.getCategoryName(),0
                        ,categoryItem.getDiscount(),categoryItem.getPriceItems(),categoryItem.get_id());
                cartViewModel.addToCart(cartItem);
            }

            @Override
            public boolean alreadyAddedToCart(CategoryItem categoryItem) {
                return cartViewModel.isPresentInCart(categoryItem);
            }

            @Override
            public void deleteFromCart(String name) {
                cartViewModel.deleteCartItemByName(name);
            }
        },true);

        recommendationRecyclerView.setAdapter(recommendationAdapter);
        cartViewModel.getRecommendationItemFromServer();


        cartViewModel.getRecommendationItems().observe(getViewLifecycleOwner(), new Observer<List<CategoryItem>>() {
            @Override
            public void onChanged(List<CategoryItem> categoryItems) {
                recommendationProgressBar.setVisibility(View.GONE);
                recommendationAdapter.setCategoryItemList(categoryItems);
            }
        });

    }

    private void openFragment(Fragment fragment,int anim1,int anim2){
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(anim1,anim2)
                .replace(R.id.nav_host_fragment,fragment)
                .addToBackStack(null)
                .commit();
    }



}