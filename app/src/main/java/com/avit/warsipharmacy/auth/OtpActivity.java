package com.avit.warsipharmacy.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avit.warsipharmacy.HomeActivity;
import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.db.SharedPrefNames;
import com.avit.warsipharmacy.network.NetworkAPI;
import com.avit.warsipharmacy.network.RetrofitClient;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OtpActivity extends AppCompatActivity {

    private OtpView otpView;
    private String TAG = "OtpActivity";
    private String phoneNo,userId;
    private TextView phoneNoView;
    private String otp;
    private Button verifyOtp;
    private CircularProgressIndicator progressIndicator;
    private TextView requestAgainView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        verifyOtp = findViewById(R.id.verifyOtp);
        progressIndicator = findViewById(R.id.progressBar);
        requestAgainView = findViewById(R.id.request_again);

        sharedPreferences = getSharedPreferences(SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME,MODE_PRIVATE);

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AuthActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // verify otp
        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp != null && otp.length() == 4){
                    progressIndicator.setVisibility(View.VISIBLE);
                    verifyOtp.setClickable(false);
                    verifyOtpFromServer();
                }
            }
        });


        phoneNoView = findViewById(R.id.phoneNo);

        phoneNo = getIntent().getStringExtra("phoneNo");
        userId = getIntent().getStringExtra("userId");

        phoneNoView.setText("OTP is sent to +91" + phoneNo);

        // TODO: REQUEST AGAIN
        requestAgainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressIndicator.setVisibility(View.VISIBLE);
                requestOTP(phoneNo);
            }
        });

        otpView = findViewById(R.id.otpView);
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otpString) {
                otp = otpString;
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
                progressIndicator.setVisibility(View.GONE);
                if(response.body() != null){
                    Toasty.success(getApplicationContext(),"Otp has been send",Toasty.LENGTH_SHORT)
                            .show();
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

    private void verifyOtpFromServer(){
        Retrofit retrofit = RetrofitClient.getInstance();
        NetworkAPI networkAPI = retrofit.create(NetworkAPI.class);

        Call<Boolean> call = networkAPI.verifyOTP(new OtpVerifyData(userId,otp));
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Boolean res = response.body();
                if(res){
                    progressIndicator.setVisibility(View.GONE);
                    saveToSharedPref();
                }else{
                    progressIndicator.setVisibility(View.GONE);
                    verifyOtp.setClickable(true);
                    Toasty.warning(getApplicationContext(),"Incorrect OTP",Toasty.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toasty.error(getApplicationContext(),t.getMessage(),Toasty.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void saveToSharedPref(){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SharedPrefNames.USER_ID,userId);
        editor.putString(SharedPrefNames.PHONE_NUMBER,phoneNo);

        editor.apply();

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();

    }

    public static class OtpVerifyData{
        private String userId;
        private String otp;

        public OtpVerifyData(String userId, String otp) {
            this.userId = userId;
            this.otp = otp;
        }

        public String getUserId() {
            return userId;
        }

        public String getOtp() {
            return otp;
        }
    }

}