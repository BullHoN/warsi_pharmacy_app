package com.avit.warsipharmacy.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avit.warsipharmacy.MainActivity;
import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.auth.AuthActivity;
import com.avit.warsipharmacy.db.SharedPrefNames;
import com.avit.warsipharmacy.ui.address.AddressFragment;

import java.sql.BatchUpdateException;

public class UserProfileFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);

        sharedPreferences = getActivity().getSharedPreferences(SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME, Context.MODE_PRIVATE);

        // Back Button
        root.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .popBackStackImmediate();
            }
        });

        // TODO: LOGOUT
        root.findViewById(R.id.signOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(SharedPrefNames.USER_ID);
                editor.remove(SharedPrefNames.USER_NAME);
                editor.remove(SharedPrefNames.BUILDING_NAME);
                editor.remove(SharedPrefNames.MAIN_ADDRESS);
                editor.remove(SharedPrefNames.LANDMARK);
                editor.remove(SharedPrefNames.PINCODE);
                editor.remove(SharedPrefNames.PHONE_NUMBER);

                editor.apply();

                Intent intent = new Intent(getContext(), AuthActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });

        // address
        root.findViewById(R.id.address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddressFragment();
                openFragment(fragment,android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });

        // TODO: TERMS AND PRIVACY
        // TODO: AVIT PHONE NO

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