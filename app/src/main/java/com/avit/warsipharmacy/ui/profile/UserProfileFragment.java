package com.avit.warsipharmacy.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.ui.address.AddressFragment;

public class UserProfileFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);

        // Back Button
        root.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .popBackStackImmediate();
            }
        });

        // TODO: LOGOUT

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