package com.avit.warsipharmacy.ui.order;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.ui.checkout.CheckoutOrderItemsAdapter;
import com.avit.warsipharmacy.ui.orders.OrderItem;
import com.google.gson.Gson;


public class OrderFragment extends Fragment {

    private OrderItem currOrderItem;
    private RecyclerView orderItemsView;
    private TextView deliveryPriceView,totalPriceView,cancelledView;
    private View firstCircle,secondCircle,thirdCircle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order, container, false);

        orderItemsView = root.findViewById(R.id.orderItemsList);
        deliveryPriceView = root.findViewById(R.id.delivery_charge);
        totalPriceView = root.findViewById(R.id.total_price);
        firstCircle = root.findViewById(R.id.first);
        secondCircle = root.findViewById(R.id.second);
        thirdCircle = root.findViewById(R.id.third);
        cancelledView = root.findViewById(R.id.cancelled);

        // Back Button
        root.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .popBackStackImmediate();
            }
        });

        Bundle bundle = getArguments();
        Gson gson = new Gson();

        String orderItemString = bundle.getString("orderItem");
        currOrderItem = gson.fromJson(orderItemString,OrderItem.class);

        orderItemsView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        CheckoutOrderItemsAdapter adapter = new CheckoutOrderItemsAdapter(getContext());
        adapter.setCartItems(currOrderItem.getCartItems());
        orderItemsView.setAdapter(adapter);

        int total_amount = OrderItem.getTotal(currOrderItem.getCartItems());
        deliveryPriceView.setText("₹" + currOrderItem.getDeliveryPrice());
        totalPriceView.setText("₹" + total_amount);


        int status = currOrderItem.getStatus();

        if(status >= 0 || status == -1){
            firstCircle.setBackgroundResource(R.drawable.success_circle);
        }

        if(status >= 1){
            secondCircle.setBackgroundResource(R.drawable.success_circle);
        }

        if(status >= 2){
            thirdCircle.setBackgroundResource(R.drawable.success_circle);
        }

        if(status == -1){
            secondCircle.setBackgroundResource(R.drawable.error_circle);
            cancelledView.setText("Cancelled");
        }

        return root;
    }
}