package com.avit.warsipharmacy.ui.prescription;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.db.SharedPrefNames;
import com.avit.warsipharmacy.network.NetworkAPI;
import com.avit.warsipharmacy.network.RetrofitClient;
import com.avit.warsipharmacy.ui.success.SuccessFragment;
import com.avit.warsipharmacy.utility.FileUtils;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;


public class PrescriptionFragment extends Fragment {

    private static final int PICK_IMAGE_FROM_GALARY = 10;
    private Uri prescriptionImageUri;
    private ImageView prescriptionImageView;
    private Button sendPrescriptionButton;
    private SharedPreferences sharedPreferences;
    private String TAG = "PrescriptionFragment";
    private CircularProgressIndicator progressIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_prescription, container, false);
        prescriptionImageUri = null;

        sharedPreferences = getActivity().getSharedPreferences(SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME, Context.MODE_PRIVATE);
        prescriptionImageView = root.findViewById(R.id.prescription_image);
        sendPrescriptionButton = root.findViewById(R.id.send_prescription_button);
        progressIndicator = root.findViewById(R.id.progressBar);

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

                        if(prescriptionImageUri == null){
                            Toasty.warning(getContext(),"Select Image",Toasty.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                        // SEND PRESCRIPTION TO SERVER
                        progressIndicator.setVisibility(View.VISIBLE);
                        sendPrescriptionButton.setClickable(false);
                        sendPrescriptionToServer();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.show();

    }

    private void sendPrescriptionToServer(){

        String userId = sharedPreferences.getString(SharedPrefNames.USER_ID,"");

        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        FileUtils fileUtils = new FileUtils(getContext());
        String fullPath = fileUtils.getPath(prescriptionImageUri);
        String imageExtension = fullPath;
        imageExtension = imageExtension.substring(imageExtension.indexOf('.'));

        String uniuePrefix = userId + getrandomSixDigits();
        String fileName = uniuePrefix + imageExtension;

        File file = new File(fullPath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),file);
        MultipartBody.Part body;
        body = MultipartBody.Part.createFormData("upload",fileName,requestBody);

        PrescriptionUploadData data = new PrescriptionUploadData(userId,fileName);

        Call<Boolean> call = networkAPI.sendPrescription(body,data);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                progressIndicator.setVisibility(View.GONE);
                sendPrescriptionButton.setClickable(true);
                if(response.body()){
                    Toasty.success(getContext(),"Prescription Uploaded",Toasty.LENGTH_SHORT)
                            .show();

                    getFragmentManager()
                            .popBackStack();

                    Fragment fragment = new SuccessFragment();
                    openFragment(fragment,android.R.anim.fade_in,android.R.anim.fade_out);

                }else{
                    Toasty.error(getContext(),"Some error occurred",Toasty.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.i(TAG, "onFailure: ",t);
                Toasty.error(getContext(),t.getMessage(),Toasty.LENGTH_SHORT)
                        .show();
            }
        });

    }


    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public String getrandomSixDigits(){
        int numbers[] = {0,1,2,3,4,5,6,7,8,9};
        StringBuilder random6digits = new StringBuilder();
        for(int i=0;i<6;i++){
            int j = getRandomNumber(0,10);
            random6digits.append(String.valueOf(numbers[j]));
        }

        return random6digits.toString();
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

    public static class PrescriptionUploadData{
        private String userId;
        private String imageName;

        public PrescriptionUploadData(String userId,String imageName) {
            this.userId = userId;
            this.imageName = imageName;
        }

        public String getImageURL() {
            return imageName;
        }

        public String getUserId() {
            return userId;
        }
    }

}