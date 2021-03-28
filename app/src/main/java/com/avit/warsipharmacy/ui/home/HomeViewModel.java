package com.avit.warsipharmacy.ui.home;

import android.app.Application;
import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.db.CartRepository;
import com.avit.warsipharmacy.network.NetworkAPI;
import com.avit.warsipharmacy.network.RetrofitClient;
import com.avit.warsipharmacy.ui.cart.CartItem;
import com.avit.warsipharmacy.ui.category.CategoryItem;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeViewModel extends AndroidViewModel {

    private List<Pair<String,Integer>> categoriesItems;
    private MutableLiveData<List<CategoryItem>> listMutableLiveData;
    private CartRepository cartRepository;
    private String TAG = "HomeViewModel";

    public HomeViewModel(Application application) {
        super(application);

        cartRepository = new CartRepository(application);

        categoriesItems = new ArrayList<>();

        categoriesItems.add(new Pair<>("Tablets", R.drawable.tablets_icon));
        categoriesItems.add(new Pair<>("Syrups",R.drawable.syrups_icon));
        categoriesItems.add(new Pair<>("Tablets",R.drawable.tablets_icon));
        categoriesItems.add(new Pair<>("Syrups",R.drawable.syrups_icon));


        listMutableLiveData = new MutableLiveData<>();
        getTopSellingItemsFromServer();
    }

    private void getTopSellingItemsFromServer(){
        Retrofit retrofitClient = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofitClient.create(NetworkAPI.class);

        Call<List<CategoryItem>> call = networkAPI.getTopSelling();
        call.enqueue(new Callback<List<CategoryItem>>() {
            @Override
            public void onResponse(Call<List<CategoryItem>> call, Response<List<CategoryItem>> response) {
                List<CategoryItem> categoryItems  = response.body();
                listMutableLiveData.setValue(categoryItems);
            }

            @Override
            public void onFailure(Call<List<CategoryItem>> call, Throwable t) {
                Toasty.error(getApplication(),t.getMessage(),Toasty.LENGTH_SHORT)
                        .show();
            }
        });

    }

    public MutableLiveData<List<CategoryItem>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    public void delete(CartItem cartItem){
        cartRepository.delete(cartItem);
    }

    public void addToCart(CartItem cartItem){
        cartRepository.insert(cartItem);
    }

    boolean isPresentInCart(CategoryItem categoryItem){
        return cartRepository.findCartItem(categoryItem.getItemName());
    }

    public void deleteCartItemByName(String name){
        cartRepository.deleteCartItemByName(name);
    }

    public List<Pair<String, Integer>> getCategoriesItems() {
        return categoriesItems;
    }

}