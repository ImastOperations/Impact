package in.imast.impact.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import in.imast.impact.R;

import in.imast.impact.helper.StaticSharedpreference;
import com.google.firebase.analytics.FirebaseAnalytics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SplashActivity extends AppCompatActivity {

    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 124;
    String newFcmToken;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_spash);

        checkLoginAvailability();

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putInt("splash", 1);
        mFirebaseAnalytics.logEvent("splash_view", bundle);

    }

    private void checkLoginAvailability(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!StaticSharedpreference.getInfo("AccessToken",SplashActivity.this).equals("")
                && StaticSharedpreference.getInfo("user_verify", SplashActivity.this).equalsIgnoreCase("true")){

                    startActivity(new Intent(SplashActivity.this, KycVerifyActivity.class));
                    finishAffinity();

                    /*if(!StaticSharedpreference.getInfo("kyc_complete", SplashActivity.this).equalsIgnoreCase("Done"))
                    {
                        startActivity(new Intent(SplashActivity.this, KycVerifyActivity.class));
                        finishAffinity();
                    }
                    else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class)
                                .putExtra("status",""));
                        finishAffinity();
                    }*/
                }else
                {
                    startActivity(new Intent(SplashActivity.this,WelcomeScreen.class));
                    finish();
                }

            }
        },2000);

    }





}
