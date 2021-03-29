package com.avit.warsipharmacy.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.network.NetworkAPI;
import com.avit.warsipharmacy.network.RetrofitClient;
import com.avit.warsipharmacy.ui.cart.CartItem;
import com.avit.warsipharmacy.ui.categories.CategoriesFragment;
import com.avit.warsipharmacy.ui.category.CategoryAdapter;
import com.avit.warsipharmacy.ui.category.CategoryFragment;
import com.avit.warsipharmacy.ui.category.CategoryItem;
import com.avit.warsipharmacy.ui.prescription.PrescriptionFragment;
import com.avit.warsipharmacy.ui.profile.UserProfileFragment;
import com.avit.warsipharmacy.ui.search.SearchFragment;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private List<Pair<String,Integer>> list;
    private String TAG = "HomeFragment";
    private ProgressBar progressBar;

    // TODO: TAKE IMAGES FROM SERVER
    int[] sampleImages = {R.drawable.banner1, R.drawable.banner2, R.drawable.banner1, R.drawable.banner4, R.drawable.banner2};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar = root.findViewById(R.id.progressBar);

        // TODO: DO SOMETHING ON USER PROFILE BUTTON
        root.findViewById(R.id.user_profile_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment profileFragment = new UserProfileFragment();
                openFragment(profileFragment,android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        // TODO: DO SOMETHING ON SEARCH BUTTON
        root.findViewById(R.id.search_bar_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment searchFragment = new SearchFragment();
                openFragment(searchFragment,android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });

        // TODO: DO SOMETHING ON UPLOAD PRESCRIPTION BUTTON
        root.findViewById(R.id.upload_prescription_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment prescriptionFragment = new PrescriptionFragment();
                openFragment(prescriptionFragment,android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        // TODO: UPLOAD PRESCRIPTION WITH IMAGE
        root.findViewById(R.id.upload_prescription_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment prescriptionFragment = new PrescriptionFragment();
                openFragment(prescriptionFragment,android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        // initilize carousel
        CarouselView carouselView = root.findViewById(R.id.horizontal_list_banner_images);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        });

        // open the all categories fragment
        root.findViewById(R.id.view_all_categories_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment allCategoriesFragment = new CategoriesFragment();
                openFragment(allCategoriesFragment,android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        // horizontal display categories RecyclerView
        RecyclerView displayCategoriesRecyclerView = root.findViewById(R.id.categories_list_items);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        displayCategoriesRecyclerView.setLayoutManager(layoutManager);

        list = homeViewModel.getCategoriesItems();

        DisplayCategoriesAdapter displayCategoriesAdapter = new DisplayCategoriesAdapter(list, getContext(), new DisplayCategoriesAdapter.DisplayCategoryOnClickListener() {
            @Override
            public void onItemClick(int position) {
                Fragment categoryFragment = new CategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("categoryName",list.get(position).first);
                categoryFragment.setArguments(bundle);

                openFragment(categoryFragment,android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });

        displayCategoriesRecyclerView.setAdapter(displayCategoriesAdapter);


        // top selling items
        RecyclerView topSellingRecyclerView = root.findViewById(R.id.topSellingRecyclerView);
        topSellingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        CategoryAdapter topSellingAdapter = new CategoryAdapter(getContext(), new CategoryAdapter.CategoryInterface() {
            @Override
            public void addToCart(CategoryItem categoryItem) {
                CartItem cartItem = new CartItem(categoryItem.getItemName(),categoryItem.getCategoryName(),0,categoryItem.getDiscount()
                        ,categoryItem.getPriceItems(),categoryItem.get_id());
                homeViewModel.addToCart(cartItem);
            }

            @Override
            public boolean alreadyAddedToCart(CategoryItem categoryItem) {
                return homeViewModel.isPresentInCart(categoryItem);
            }

            @Override
            public void deleteFromCart(String name) {
                homeViewModel.deleteCartItemByName(name);
            }
        });
        topSellingRecyclerView.setAdapter(topSellingAdapter);

        homeViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<CategoryItem>>() {
            @Override
            public void onChanged(List<CategoryItem> topSellingItems) {
                if(topSellingItems.size() > 0){
                    progressBar.setVisibility(View.GONE);
                }
                topSellingAdapter.setCategoryItemList(topSellingItems);
            }
        });

        return root;
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