package com.avit.warsipharmacy.ui.prescription;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.ui.success.SuccessFragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;


public class PrescriptionFragment extends Fragment {

    private static final int PICK_IMAGE_FROM_GALARY = 10;
    private Uri prescriptionImageUri;
    private ImageView prescriptionImageView;
    private Button sendPrescriptionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_prescription, container, false);
        prescriptionImageUri = null;

        prescriptionImageView = root.findViewById(R.id.prescription_image);
        sendPrescriptionButton = root.findViewById(R.id.send_prescription_button);

        // back Button
        root.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .popBackStackImmediate();
            }
        });

        // Select Image
        root.findViewById(R.id.select_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromGallery();
            }
        });

        // TODO: ADD A PROGRESSBAR
        sendPrescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogBox();
            }
        });

        return root;
    }

    private void showAlertDialogBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Send Your Prescription To Warsi Pharmacy ??")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: ADD SOME VALIDATION FOR IMAGE
                        if(prescriptionImageUri == null){
                            Toasty.warning(getContext(),"Select Image",Toasty.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                        // TODO: SEND PRESCRIPTION TO SERVER
                        Toasty.success(getContext(),"Success !!",Toasty.LENGTH_SHORT,true)
                                .show();

                        getFragmentManager()
                                .popBackStack();

                        // TODO: ADD SOME DATA

                        Fragment fragment = new SuccessFragment();
                        openFragment(fragment,android.R.anim.fade_in,android.R.anim.fade_out);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.show();

    }

    private void selectImageFromGallery(){
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);

                        startActivityForResult(Intent.createChooser(intent,"select image"),PICK_IMAGE_FROM_GALARY);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(getContext(),"Unable the permission to use this feature",Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void openFragment(Fragment fragment,int anim1,int anim2){
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(anim1,anim2)
                .replace(R.id.nav_host_fragment,fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE_FROM_GALARY
                && data != null && data.getData() != null){
            prescriptionImageUri = data.getData();

            prescriptionImageView.setImageURI(prescriptionImageUri);
        }

    }
}