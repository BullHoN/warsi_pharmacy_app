package com.avit.warsipharmacy.ui.categories;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.ui.category.CategoryFragment;
import com.avit.warsipharmacy.ui.home.DisplayCategoriesAdapter;

import java.util.List;


public class CategoriesFragment extends Fragment {

    private RecyclerView categoriesRecyclerView;
    private DisplayCategoriesAdapter displayCategoriesAdapter;
    private CategoriesViewModel categoriesViewModel;
    private List<Pair<String,Integer>> categoriesItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        categoriesViewModel =
                ViewModelProviders.of(this).get(CategoriesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_categories, container, false);

        // Back Button
        root.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .popBackStackImmediate();
            }
        });

        categoriesItems = categoriesViewModel.getCategoriesItems();
        categoriesRecyclerView = root.findViewById(R.id.categories_list_items);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        categoriesRecyclerView.setLayoutManager(layoutManager);

        displayCategoriesAdapter = new DisplayCategoriesAdapter(categoriesItems, getContext(), new DisplayCategoriesAdapter.DisplayCategoryOnClickListener() {
            @Override
            public void onItemClick(int position) {
                Fragment categoryFragment = new CategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("categoryName",categoriesItems.get(position).first);
                categoryFragment.setArguments(bundle);

                openFragment(categoryFragment,android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        categoriesRecyclerView.setAdapter(displayCategoriesAdapter);

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