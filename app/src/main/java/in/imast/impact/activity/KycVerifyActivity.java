package in.imast.impact.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.imast.impact.Connection.APIResultLitener;
import in.imast.impact.Connection.ApiClient;
import in.imast.impact.R;
import in.imast.impact.fragment.AadhaarFragment;
import in.imast.impact.fragment.GSTINFragment;
import in.imast.impact.helper.DialogClass;
import in.imast.impact.helper.StaticSharedpreference;
import in.imast.impact.helper.Utilities;
import retrofit2.Response;

public class KycVerifyActivity extends AppCompatActivity
{
    private Utilities utilities;
    private DialogClass dialogClass;
    ProgressBar progress_indicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc_verify);


        init();
    }

    private void init()
    {
        utilities = new Utilities(this);
        dialogClass = new DialogClass();

        progress_indicator = findViewById(R.id.progress_indicator);

        if(utilities.isOnline())
        {
           checkKyc(StaticSharedpreference.getInfo("UserId", KycVerifyActivity.this));
        }
        else {

        }

    }

    public void checkKyc(String customer_id)
    {
        if (progress_indicator == null) {
            Log.e("KycVerifyActivity", "ProgressBar initialization failed!");
        }
        else {
            progress_indicator.setVisibility(View.VISIBLE);
        }

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("customer_id", customer_id);

        ApiClient.checkKyc(StaticSharedpreference.getInfo("AccessToken", this), queryParams, new APIResultLitener<JsonObject>() {

            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage)
            {
                try {

                    if (progress_indicator == null) {
                        Log.e("KycVerifyActivity", "ProgressBar initialization failed!");
                    }
                    else {
                        progress_indicator.setVisibility(View.GONE);
                    }

                    if (response.isSuccessful() && (response.code() >= 200 && response.code() < 300)) {

                        String status = String.valueOf(response.body().get("status").getAsString());
                        if(status.equalsIgnoreCase("success"))
                        {
                            try {
                                JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                                JSONObject data = jsonObject.getJSONObject("data");

                                Boolean gst_kyc_model = data.optBoolean("gst_kyc_model");
                                Boolean aadhar_kyc_model = data.optBoolean("aadhar_kyc_model");

                                if(!gst_kyc_model)
                                {
                                    replaceFragment(new GSTINFragment());
                                }
                                else if(!aadhar_kyc_model)
                                {
                                    replaceFragment(new AadhaarFragment());
                                }
                                else {
                                    Intent intent = new Intent(KycVerifyActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                if(aadhar_kyc_model && gst_kyc_model)
                                {
                                    StaticSharedpreference.saveInfo("kyc_complete", "",KycVerifyActivity.this);
                                }
                                else {
                                    StaticSharedpreference.saveInfo("kyc_complete", "",KycVerifyActivity.this);
                                }

                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    } else if (response.code() == 401) {
                        dialogClass.alertDialogAuthentication(KycVerifyActivity.this);
                    } else {

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment); // R.id.fragment_container is the FrameLayout in your layout where fragments are loaded.
        transaction.addToBackStack(null); // Optional: Adds the transaction to the back stack.
        transaction.commitAllowingStateLoss();
    }
}
