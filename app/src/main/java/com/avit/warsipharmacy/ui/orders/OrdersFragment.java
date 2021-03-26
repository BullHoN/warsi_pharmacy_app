package com.avit.warsipharmacy.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.ui.home.HomeFragment;
import com.avit.warsipharmacy.ui.order.OrderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    private OrdersViewModel ordersViewModel;
    private RecyclerView ordersListRecyclerView;
    private ChipGroup chipGroup;
    private List<OrderItem> orderItemList;
    private Gson gson;
    private LinearLayout emptyOrdersView;
    private Button exploreButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ordersViewModel =
                ViewModelProviders.of(this).get(OrdersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_orders, container, false);

        emptyOrdersView = root.findViewById(R.id.emptyOrders);
        exploreButton = root.findViewById(R.id.explore);

        exploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new HomeFragment();
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
                bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);

                openFragment(fragment,android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        orderItemList = new ArrayList<>();
        gson = new Gson();
        chipGroup = root.findViewById(R.id.chip_group);
        ordersListRecyclerView = root.findViewById(R.id.ordersList);
        ordersListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        final OrdersAdapter ordersAdapter = new OrdersAdapter(getContext(), new OrdersAdapter.OrdersInterface() {
            @Override
            public void openDetailsTab(OrderItem orderItem) {
                String orderItemString = gson.toJson(orderItem,OrderItem.class);

                Fragment fragment = new OrderFragment();
                Bundle bundle = new Bundle();
                bundle.putString("orderItem",orderItemString);
                fragment.setArguments(bundle);

                openFragment(fragment,android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
        ordersListRecyclerView.setAdapter(ordersAdapter);

        ordersViewModel.getOrderItemMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<OrderItem>>() {
            @Override
            public void onChanged(List<OrderItem> orderItems) {

                if(orderItems.size() > 0){
                    emptyOrdersView.setVisibility(View.INVISIBLE);
                }else{
                    emptyOrdersView.setVisibility(View.VISIBLE);
                }

                orderItemList = orderItems;
                ordersAdapter.setOrderItemList(orderItems);
            }
        });

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                if(orderItemList.size() < 1){
                    return;
                }

                if(checkedId == R.id.chip1){
                    ordersAdapter.setOrderItemList(orderItemList);
                }
                else if(checkedId == R.id.chip2){ // Active
                    ordersAdapter.setOrderItemList(filterOrderItemsOnStatus(0,1));
                }
                else if(checkedId == R.id.chip3){ // Completed
                    ordersAdapter.setOrderItemList(filterOrderItemsOnStatus(2,100));
                }
                else if(checkedId == R.id.chip4){ // Cancelled
                    ordersAdapter.setOrderItemList(filterOrderItemsOnStatus(-1,100));
                }

            }
        });

        return root;
    }

    private List<OrderItem> filterOrderItemsOnStatus(int status1,int status2){
        List<OrderItem> filteredOrderItems = new ArrayList<>();
        for(OrderItem orderItem : orderItemList){
            if(orderItem.getStatus() == status1 || orderItem.getStatus() == status2){
                filteredOrderItems.add(orderItem);
            }
        }

        return filteredOrderItems;
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