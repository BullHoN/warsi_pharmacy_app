package com.avit.warsipharmacy.ui.checkout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.db.SharedPrefNames;
import com.avit.warsipharmacy.network.NetworkAPI;
import com.avit.warsipharmacy.network.RetrofitClient;
import com.avit.warsipharmacy.ui.cart.CartItem;
import com.avit.warsipharmacy.ui.cart.CartViewModel;
import com.avit.warsipharmacy.ui.orders.OrderItem;
import com.avit.warsipharmacy.ui.success.SuccessFragment;
import com.avit.warsipharmacy.utility.Utility;
import com.avit.warsipharmacy.utility.Validation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import static android.app.Activity.RESULT_OK;

public class CheckoutFragment extends Fragment {

    private RecyclerView cartOrderItemsRecyclerView;
    private CheckoutViewModel checkoutViewModel;
    private Button checkoutButton;
    private TextView deliveryChargeView,totalPriceView;
    private String TAG = "CheckoutFragment";
    private TextInputEditText userNameView,buildingNameView,mainAddressView,landmarkView,pinCodeView,phoneNoView;
    private SharedPreferences sharedPreferences;
    private List<CartItem> cartItems;
    private Gson gson;
    private ProgressBar progressBar;
    final int UPI_PAYMENT = 16;
    private int deliveryPrice;
    private UpiDetails upiDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_checkout, container, false);
        checkoutViewModel =
                ViewModelProviders.of(this).get(CheckoutViewModel.class);

        sharedPreferences = getActivity().getSharedPreferences(SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME,Context.MODE_PRIVATE);
        gson = new Gson();

        progressBar = root.findViewById(R.id.progressBar);
        deliveryChargeView = root.findViewById(R.id.delivery_charge);
        totalPriceView = root.findViewById(R.id.total_price);
        userNameView = root.findViewById(R.id.user_name);
        buildingNameView = root.findViewById(R.id.buildingName);
        mainAddressView = root.findViewById(R.id.main_address);
        landmarkView = root.findViewById(R.id.landmark);
        pinCodeView = root.findViewById(R.id.pincode);
        phoneNoView = root.findViewById(R.id.phoneNo);


        getFromSharedPref();
        Utility.setupUI(root,getContext());

        Bundle bundle = getArguments();
        String cartItemString = bundle.getString("cartItems");
        OrderItem.OrderItemsclass orderItemsclass = gson.fromJson(cartItemString, OrderItem.OrderItemsclass.class);
        cartItems = orderItemsclass.getOrderItems();

        // Upi Details
        checkoutViewModel.getUpiDetailsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<UpiDetails>() {
            @Override
            public void onChanged(UpiDetails upidet) {
                upiDetails = upidet;
            }
        });

        // delivery price
        checkoutViewModel.getDeliveryPrice().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                deliveryPrice = integer;
                deliveryChargeView.setText("₹" + deliveryPrice);
            }
        });

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

        //  CHECKOUT BUTTON
        checkoutButton = root.findViewById(R.id.checkout);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isValid()){
                    return;
                }

                if(deliveryPrice == -1){
                    Toasty.error(getContext(),"Please try again later",Toasty.LENGTH_SHORT)
                            .show();
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
                alertDialog.dismiss();
                upiPayment();
            }
        });

        payWithCOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                sendDataToServer(false);
            }
        });

        alertDialog.show();

    }

    private void upiPayment(){
        int total = calclulateTotal(cartItems);

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiDetails.getUpiId())
                .appendQueryParameter("pn", upiDetails.getUpiName())
                .appendQueryParameter("tr", "25584584")
                .appendQueryParameter("am", String.valueOf(total))
                .appendQueryParameter("cu", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);

        upiPayIntent.setData(uri);

        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        // check if intent resolves
        if(null !=  chooser.resolveActivity(getContext().getPackageManager()))  {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(getContext(),"No UPI app found, please install one to continue",Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private void sendDataToServer(boolean isPaid){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        // TODO: SET CORRECT FCM ID
        OrderItem.CreateOrderData orderData = new OrderItem.CreateOrderData(cartItems,deliveryPrice,isPaid,"skdgnskdgnksdgn"
                ,sharedPreferences.getString(SharedPrefNames.USER_ID,""),userNameView.getText().toString(),buildingNameView.getText().toString()
                ,mainAddressView.getText().toString(),landmarkView.getText().toString(),pinCodeView.getText().toString());

        Call<Boolean> call = networkAPI.createOrder(orderData);

        checkoutButton.setClickable(false);
        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Boolean res = response.body();
                if(!res){
                    Toasty.error(getContext(),"Order failed please try again later",Toasty.LENGTH_LONG)
                            .show();
                    checkoutButton.setClickable(true);
                }else{
                    progressBar.setVisibility(View.GONE);
                    onPaymentSucessfull();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toasty.error(getContext(),t.getMessage(),Toasty.LENGTH_SHORT)
                        .show();
                checkoutButton.setClickable(true);
            }
        });

    }

    private void getFromSharedPref(){
        userNameView.setText(sharedPreferences.getString(SharedPrefNames.USER_NAME,""));
        buildingNameView.setText(sharedPreferences.getString(SharedPrefNames.BUILDING_NAME,""));
        mainAddressView.setText(sharedPreferences.getString(SharedPrefNames.MAIN_ADDRESS,""));
        landmarkView.setText(sharedPreferences.getString(SharedPrefNames.LANDMARK,""));
        pinCodeView.setText(sharedPreferences.getString(SharedPrefNames.PINCODE,""));
        phoneNoView.setText(sharedPreferences.getString(SharedPrefNames.PHONE_NUMBER,""));
    }

    private void saveToSharedPref(){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SharedPrefNames.USER_NAME,userNameView.getText().toString());
        editor.putString(SharedPrefNames.BUILDING_NAME,buildingNameView.getText().toString());
        editor.putString(SharedPrefNames.MAIN_ADDRESS,mainAddressView.getText().toString());
        editor.putString(SharedPrefNames.LANDMARK,landmarkView.getText().toString());
        editor.putString(SharedPrefNames.PINCODE,pinCodeView.getText().toString());
        editor.putString(SharedPrefNames.PHONE_NUMBER,phoneNoView.getText().toString());

        editor.apply();
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

        saveToSharedPref();
        return true;
    }

    public void onPaymentSucessfull(){

        checkoutViewModel.clearCart();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == UPI_PAYMENT){
            if((resultCode == RESULT_OK || resultCode == 11)){
                if(data != null && data.getStringExtra("response") != null) {
                    String upiResponse = data.getStringExtra("response").toLowerCase();
                    if (upiResponse.contains("success")) {
                        sendDataToServer(true);
                    } else {
                        Toasty.error(getContext(), "Payment failed", Toasty.LENGTH_SHORT)
                                .show();
                    }
                }else{
                    Toasty.error(getContext(), "Payment failed", Toasty.LENGTH_SHORT)
                            .show();
                }
            }else{
                Toasty.error(getContext(), "Payment failed", Toasty.LENGTH_SHORT)
                        .show();
            }
        }

    }
}