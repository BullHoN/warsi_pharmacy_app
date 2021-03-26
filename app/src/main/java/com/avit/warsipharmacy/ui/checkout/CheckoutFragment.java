package com.avit.warsipharmacy.ui.checkout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.ui.cart.CartItem;
import com.avit.warsipharmacy.ui.cart.CartViewModel;
import com.avit.warsipharmacy.ui.success.SuccessFragment;
import com.avit.warsipharmacy.utility.Utility;
import com.avit.warsipharmacy.utility.Validation;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class CheckoutFragment extends Fragment {

    private RecyclerView cartOrderItemsRecyclerView;
    private CheckoutViewModel checkoutViewModel;
    private Button checkoutButton;
    private TextView deliveryChargeView,totalPriceView;
    private String TAG = "CheckoutFragment";
    private TextInputEditText userNameView,buildingNameView,mainAddressView,landmarkView,pinCodeView,phoneNoView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_checkout, container, false);
        checkoutViewModel =
                ViewModelProviders.of(this).get(CheckoutViewModel.class);

        deliveryChargeView = root.findViewById(R.id.delivery_charge);
        totalPriceView = root.findViewById(R.id.total_price);
        userNameView = root.findViewById(R.id.user_name);
        buildingNameView = root.findViewById(R.id.buildingName);
        mainAddressView = root.findViewById(R.id.main_address);
        landmarkView = root.findViewById(R.id.landmark);
        pinCodeView = root.findViewById(R.id.pincode);
        phoneNoView = root.findViewById(R.id.phoneNo);


        Utility.setupUI(root,getContext());

        // Back Button
        root.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .popBackStackImmediate();
            }
        });

        cartOrderItemsRecyclerView = root.findViewById(R.id.cartOrderItemsList);
        cartOrderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        final CheckoutOrderItemsAdapter adapter = new CheckoutOrderItemsAdapter(getContext());
        cartOrderItemsRecyclerView.setAdapter(adapter);

        checkoutViewModel.getCartItems().observe(getViewLifecycleOwner(), new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                adapter.setCartItems(cartItems);
                int totalPrice = calclulateTotal(cartItems);
                totalPriceView.setText("₹" + totalPrice);
            }
        });

        // TODO: CHECKOUT BUTTON
        checkoutButton = root.findViewById(R.id.checkout);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: SAVE TO SHAREDPREF AFTER SUBMITTED
                if(!isValid()){
                    return;
                }

                showAlertBox();
            }
        });

        return root;
    }

    private void showAlertBox(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.checkout_payment_options_view,null,false);
        builder.setView(view);

        builder.setTitle("Select Payment Option")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toasty.warning(getContext(),"Payment Failed",Toasty.LENGTH_SHORT)
                                .show();
                    }
                });

        final AlertDialog alertDialog = builder.create();
        Button payWithUPIButton = view.findViewById(R.id.upi_button);
        Button payWithCOD = view.findViewById(R.id.cod_button);

        payWithUPIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: START UPI PAYMENT
                alertDialog.dismiss();
                // TODO: REMOVE THIS METHOD
                tempSuccess();
            }
        });

        payWithCOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: START COD PAYMENT
                alertDialog.dismiss();
                // TODO: REMOVE THIS METHOD
                tempSuccess();
            }
        });

        alertDialog.show();

    }

    private boolean isValid(){
        String nameText = userNameView.getText().toString();
        if(!Validation.isValidName(nameText)){
            userNameView.setError("Please Enter a Valid Name");
            userNameView.requestFocus();
            return  false;
        }

        String buildingText = buildingNameView.getText().toString();
        if(!Validation.addressValidation(buildingText)){
            buildingNameView.setError("Please Fill In The Details");
            buildingNameView.requestFocus();
            return false;
        }

        String addressText = mainAddressView.getText().toString();
        if(!Validation.addressValidation(addressText)){
            mainAddressView.setError("Please Fill The Address");
            mainAddressView.requestFocus();
            return false;
        }

        String pinCodeText = pinCodeView.getText().toString();
        if(!Validation.pinCodeValidation(pinCodeText)){
            pinCodeView.setError("Enter Valid Pincode");
            pinCodeView.requestFocus();
            return false;
        }

        String phoneNoText = phoneNoView.getText().toString();
        if(!Validation.phoneNoValidation(phoneNoText)){
            phoneNoView.setError("Enter Valid Phone Number");
            phoneNoView.requestFocus();
            return false;
        }

        return true;
    }

    public void tempSuccess(){
        Toasty.success(getContext(),"Success !!",Toasty.LENGTH_SHORT)
                .show();

        getFragmentManager()
                .popBackStack();

        Fragment fragment = new SuccessFragment();

        Bundle bundle = new Bundle();
        bundle.putBoolean("isOrder",true);
        fragment.setArguments(bundle);

        openFragment(fragment,android.R.anim.fade_in,android.R.anim.fade_out);
    }

    private void openFragment(Fragment fragment,int anim1,int anim2){
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(anim1,anim2)
                .replace(R.id.nav_host_fragment,fragment)
                .addToBackStack(null)
                .commit();
    }


    private int calclulateTotal(List<CartItem> cartItems){
        int total = 0;
        for(CartItem cartItem : cartItems){
            int originalPrice = cartItem.getPriceItems().get(cartItem.getSelectedPriceIndex()).getPrice();
            int discountedPrice = originalPrice - originalPrice*cartItem.getDiscount()/100;
            total += discountedPrice;
        }

        return total;
    }

}