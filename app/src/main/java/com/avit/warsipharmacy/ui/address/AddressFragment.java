package com.avit.warsipharmacy.ui.address;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.utility.Utility;

import es.dmoral.toasty.Toasty;

public class AddressFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_address, container, false);

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

        return root;
    }
}