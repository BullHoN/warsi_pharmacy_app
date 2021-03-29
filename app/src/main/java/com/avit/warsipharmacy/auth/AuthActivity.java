package com.avit.warsipharmacy.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.avit.warsipharmacy.HomeActivity;
import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.db.SharedPrefNames;
import com.avit.warsipharmacy.network.NetworkAPI;
import com.avit.warsipharmacy.network.RetrofitClient;
import com.avit.warsipharmacy.utility.Utility;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.nio.file.ClosedFileSystemException;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthActivity extends AppCompatActivity {

    private EditText phoneNoView;
    private CircularProgressIndicator progressIndicator;
    private LinearLayout sendOtpButton;
    private SharedPreferences sharedPreferences;
    private String TAG = "AuthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        phoneNoView = findViewById(R.id.phoneNo);
        sendOtpButton = findViewById(R.id.sendOtp);
        progressIndicator = findViewById(R.id.progressBar);

        sharedPreferences = getSharedPreferences(SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME,MODE_PRIVATE);

        // remove keyboard when not required
        Utility.setupUI(findViewById(R.id.mainView),getApplicationContext());

        // Send Otp Button
        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = phoneNoView.getText().toString();
                if(phoneNo.length() != 10){
                    Toasty.warning(getApplicationContext(),"Enter valid phone number",Toasty.LENGTH_SHORT)
                            .show();
                    return;
                }

                // request the otp from server
                progressIndicator.setVisibility(View.VISIBLE);
                sendOtpButton.setClickable(false);
                requestOTP(phoneNo);

            }
        });

    }

    private void requestOTP(String phoneNo){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Call<String> call = networkAPI.requestOTP(phoneNo);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String userId = response.body();
                if(userId != null){
                    openOtpActivity(userId,phoneNo);
                }else{
                    Toasty.error(getApplicationContext(),"try again later",Toasty.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toasty.error(getApplicationContext(),t.getMessage(),Toasty.LENGTH_SHORT)
                        .show();
            }
        });

    }

    private void openOtpActivity(String userId,String phoneNo){
        Intent otpActivityIntent = new Intent(getApplicationContext(), OtpActivity.class);
        otpActivityIntent.putExtra("userId",userId);
        otpActivityIntent.putExtra("phoneNo",phoneNo);

        startActivity(otpActivityIntent);
        finish();
    }

}