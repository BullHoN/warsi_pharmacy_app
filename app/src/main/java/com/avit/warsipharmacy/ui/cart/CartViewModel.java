package com.avit.warsipharmacy.ui.cart;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.avit.warsipharmacy.db.CartRepository;
import com.avit.warsipharmacy.network.NetworkAPI;
import com.avit.warsipharmacy.network.RetrofitClient;
import com.avit.warsipharmacy.ui.category.CategoryItem;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartViewModel extends AndroidViewModel {

    LiveData<List<CartItem>> categoryItems;
    MutableLiveData<List<CategoryItem>> recommendationItems;
//    List<CategoryItem> recommendationList;
    private CartRepository cartRepository;

    public CartViewModel(Application application) {
        super(application);

        cartRepository = new CartRepository(application);
        categoryItems = cartRepository.getAllItems();

        recommendationItems = new MutableLiveData<>();

//        recommendationList = new ArrayList<>();
//        List<CategoryItem.PriceItem> priceItems = new ArrayList<>();
//
//        priceItems.add(new CategoryItem.PriceItem("100ml",120));
//        priceItems.add(new CategoryItem.PriceItem("200ml",170));
//        priceItems.add(new CategoryItem.PriceItem("500ml",220));
//
//        recommendationList.add(new CategoryItem("AMREE PLUS CAPSULE 30","Tablets",5,priceItems,"asff"));
//        recommendationList.add(new CategoryItem("AMREE PLUS CAPSULE 31","Syrups",10,priceItems,"Asffsa"));
//        recommendationList.add(new CategoryItem("AMREE PLUS CAPSULE 32","Tablets",14,priceItems,"vdxvcxvf"));

    }

    public MutableLiveData<List<CategoryItem>> getRecommendationItems() {
        return recommendationItems;
    }

    public void getRecommendationItemFromServer(){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Call<List<CategoryItem>> call = networkAPI.getTopSelling();
        call.enqueue(new Callback<List<CategoryItem>>() {
            @Override
            public void onResponse(Call<List<CategoryItem>> call, Response<List<CategoryItem>> response) {
                recommendationItems.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<CategoryItem>> call, Throwable t) {
                Toasty.error(getApplication(),t.getMessage(),Toasty.LENGTH_SHORT)
                        .show();
            }
        });
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

    public void update(CartItem cartItem){
        cartRepository.update(cartItem);
    }

//    public List<CategoryItem> getRecommendationList() {
//        return recommendationList;
//    }

    public LiveData<List<CartItem>> getCategoryItems() {
        return categoryItems;
    }
}