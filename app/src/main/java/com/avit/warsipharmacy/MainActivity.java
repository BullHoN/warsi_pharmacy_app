package com.avit.warsipharmacy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new CountDownTimer(1500,750){
            @Override
            public void onFinish() {
                // TODO: Some Logic to check whether user is authenticated or not
                // TODO: if authenticated forward to the home activity
                // TODO: else forward to the login activity

                Intent homeActivityIntent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(homeActivityIntent);
                finish();
            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();

    }
}