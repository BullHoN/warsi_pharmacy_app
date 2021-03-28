package com.avit.warsipharmacy.ui.category;

import android.app.Application;
import android.graphics.Paint;
import android.util.Log;
import android.util.Pair;
import android.widget.LinearLayout;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avit.warsipharmacy.db.CartRepository;
import com.avit.warsipharmacy.network.NetworkAPI;
import com.avit.warsipharmacy.network.RetrofitClient;
import com.avit.warsipharmacy.ui.cart.CartItem;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryViewModel extends AndroidViewModel {

    private MutableLiveData<List<CategoryItem>> categoryItems;
    private MutableLiveData<List<CategoryItem>> searchItems;
    private CartRepository cartRepository;
    private LiveData<List<CartItem>> cartItemLiveData;
    private String TAG = "CategoryViewModel";

    public CategoryViewModel(Application application){
        super(application);

        cartRepository = new CartRepository(application);
        cartItemLiveData = cartRepository.getAllItems();

        categoryItems = new MutableLiveData<>();
        searchItems = new MutableLiveData<>();

    }

    public void getTheCategoryItemsFromServer(String categoryName,int limit,int page){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Call<List<CategoryItem>> call = networkAPI.getCategoryItems(categoryName,limit,page);
        call.enqueue(new Callback<List<CategoryItem>>() {
            @Override
            public void onResponse(Call<List<CategoryItem>> call, Response<List<CategoryItem>> response) {
                List<CategoryItem> items = response.body();
                categoryItems.setValue(items);
            }

            @Override
            public void onFailure(Call<List<CategoryItem>> call, Throwable t) {
                Toasty.error(getApplication(),t.getMessage(),Toasty.LENGTH_SHORT)
                        .show();
            }
        });

    }

    public void getTheSearchItemsFromServer(String categoryName,String query,int limit,int page){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Call<List<CategoryItem>> call = networkAPI.searchCategoryItem(page,limit,categoryName,query);
        call.enqueue(new Callback<List<CategoryItem>>() {
            @Override
            public void onResponse(Call<List<CategoryItem>> call, Response<List<CategoryItem>> response) {
                searchItems.setValue(response.body());
                Log.i(TAG, "onResponse: " + response.body().size());
            }

            @Override
            public void onFailure(Call<List<CategoryItem>> call, Throwable t) {
                Toasty.error(getApplication(),t.getMessage(),Toasty.LENGTH_SHORT)
                        .show();
            }
        });
    }

    public MutableLiveData<List<CategoryItem>> getSearchItems() {
        return searchItems;
    }

    public Pair<Integer,Integer> getDetails(List<CartItem> cartItems){
        int total_amount = 0;
        int total_save = 0;
        for(CartItem cartItem : cartItems){
            int curr_price = cartItem.getPriceItems().get(cartItem.getSelectedPriceIndex()).getPrice();
            int curr_amount = curr_price - (curr_price*cartItem.getDiscount()/100);
            total_amount += curr_amount;
            total_save += curr_price - curr_amount;
        }

        return new Pair<>(total_amount,total_save);
    }

    boolean isPresentInCart(CategoryItem categoryItem){
        return cartRepository.findCartItem(categoryItem.getItemName());
    }

    public void deleteCartItemByName(String name){
        cartRepository.deleteCartItemByName(name);
    }

    public LiveData<List<CartItem>> getCartItemLiveData() {
        return cartItemLiveData;
    }

    public void addToCart(CartItem cartItem){
        cartRepository.insert(cartItem);
    }

    public MutableLiveData<List<CategoryItem>> getCategoryItems() {
        return categoryItems;
    }
}