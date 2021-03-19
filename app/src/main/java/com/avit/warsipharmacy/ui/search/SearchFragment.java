package com.avit.warsipharmacy.ui.search;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.utility.Utility;

public class SearchFragment extends Fragment {

    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = root.findViewById(R.id.search_bar);

        searchView.setSubmitButtonEnabled(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        // To hide the keyboard when not required
        Utility.setupUI(root,getContext());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO: Make Some query to the database
                Toast.makeText(getContext(),query,Toast.LENGTH_SHORT)
                        .show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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

        return root;
    }
}