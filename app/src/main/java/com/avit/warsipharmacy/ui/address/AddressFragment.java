package com.avit.warsipharmacy.ui.address;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.db.SharedPrefNames;
import com.avit.warsipharmacy.utility.Utility;
import com.avit.warsipharmacy.utility.Validation;
import com.google.android.material.textfield.TextInputEditText;

import es.dmoral.toasty.Toasty;

public class AddressFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private TextInputEditText userNameView,buildingNameView,mainAddressView,landmarkView,pinCodeView,phoneNoView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_address, container, false);

        sharedPreferences = getActivity().getSharedPreferences(SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME, Context.MODE_PRIVATE);

        userNameView = root.findViewById(R.id.user_name);
        buildingNameView = root.findViewById(R.id.buildingName);
        mainAddressView = root.findViewById(R.id.main_address);
        landmarkView = root.findViewById(R.id.landmark);
        pinCodeView = root.findViewById(R.id.pincode);
        phoneNoView = root.findViewById(R.id.phoneNo);

        getFromSharedPref();

        // to hide keyboard when not necessary
        Utility.setupUI(root,getContext());

        // back Button
        root.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .popBackStackImmediate();
            }
        });

        root.findViewById(R.id.saveDetails).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    Toasty.success(getContext(),"Changes Saved",Toasty.LENGTH_SHORT)
                            .show();
                }
            }
        });

        return root;
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

}