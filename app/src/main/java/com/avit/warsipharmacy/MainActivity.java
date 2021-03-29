package com.avit.warsipharmacy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.avit.warsipharmacy.auth.AuthActivity;
import com.avit.warsipharmacy.db.SharedPrefNames;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME,MODE_PRIVATE);

        new CountDownTimer(1500,750){
            @Override
            public void onFinish() {

                if(sharedPreferences.contains(SharedPrefNames.USER_ID)){
                    Intent homeActivityIntent = new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(homeActivityIntent);
                    finish();
                }else{
                    Intent authActivity = new Intent(getApplicationContext(), AuthActivity.class);
                    startActivity(authActivity);
                    finish();
                }

            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();

    }
}