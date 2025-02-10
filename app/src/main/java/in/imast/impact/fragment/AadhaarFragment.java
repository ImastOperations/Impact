package in.imast.impact.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.imast.impact.Connection.APIResultLitener;
import in.imast.impact.Connection.ApiClient;
import in.imast.impact.R;
import in.imast.impact.activity.KycVerifyActivity;
import in.imast.impact.activity.MainActivity;
import in.imast.impact.helper.DialogClass;
import in.imast.impact.helper.StaticSharedpreference;
import in.imast.impact.helper.Utilities;
import retrofit2.Response;

public class AadhaarFragment extends Fragment {

    private Utilities utilities;
    private DialogClass dialogClass;

    private TextView otp_tv, send_tv, resend_tv;
    private EditText aadhar_edt, otp_edt;
    private Button verify_btn;

    private String client_id = "", aadhar_number = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_aadhaar, container, false);
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getActivity(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    }
                }
        );

        init(view);

        return view;
    }

    private void init(View view)
    {
        utilities = new Utilities(getActivity());
        dialogClass = new DialogClass();

        aadhar_edt = view.findViewById(R.id.aadhar_edt);
        otp_edt = view.findViewById(R.id.otp_edt);
        otp_tv = view.findViewById(R.id.otp_tv);
        send_tv = view.findViewById(R.id.send_tv);
        resend_tv = view.findViewById(R.id.resend_tv);
        verify_btn = view.findViewById(R.id.verify_btn);

        send_tv.setOnClickListener(v -> {
            checkValidationCallApi(view);
        });

        resend_tv.setOnClickListener(v -> {
            checkValidationCallApi(view);
        });

        aadhar_edt.setOnEditorActionListener((v, actionId, event) -> {

            if(actionId == EditorInfo.IME_ACTION_DONE)
            {
                checkValidationCallApi(view);
                return true;
            }
            return false;
        });

        verify_btn.setOnClickListener(v -> {
            checkAndVerifyApi(view);
        });

        otp_edt.setOnEditorActionListener((v, actionId, event) -> {

            if(actionId == EditorInfo.IME_ACTION_DONE)
            {
                checkAndVerifyApi(view);
                return true;
            }
            return false;
        });

    }

    private void checkValidationCallApi(View view)
    {
        aadhar_number = aadhar_edt.getText().toString().trim();

        if(utilities.isOnline()) {
            if (aadhar_number.length() >= 10) {
                generateOTPApi(view,aadhar_number);
            } else {
                createSnackBar(view, "Please enter valid Aadhar Number");
            }
        }
        else {
            createSnackBar(view, "Please check you internet");
        }
    }

    private void checkAndVerifyApi(View view)
    {
        String aadhar_number = aadhar_edt.getText().toString().trim();
        String otp_number = otp_edt.getText().toString().trim();
        if(utilities.isOnline()) {
            if (otp_number.length() == 6) {
                verifyOTPApi(view, aadhar_number, otp_number);
            } else {
                createSnackBar(view, "Please enter valid OTP");
            }
        }
        else {
            createSnackBar(view, "Please check you internet");
        }
    }


    private void generateOTPApi(View view,String aadhaar_number)
    {
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("customer_id", StaticSharedpreference.getInfo("UserId", getContext()));
        queryParams.put("aadhar_number", aadhaar_number);

        ApiClient.sendAadharOTP(StaticSharedpreference.getInfo("AccessToken", getContext()), queryParams, new APIResultLitener<JsonObject>() {

            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                try {
                    if (response.isSuccessful() && (response.code() >= 200 && response.code() < 300)) {

                        String status = String.valueOf(response.body().get("status").getAsString());
                        if(status.equalsIgnoreCase("success"))
                        {
                            send_tv.setVisibility(View.GONE);
                            aadhar_edt.setEnabled(false);
                            otp_tv.setVisibility(View.VISIBLE);
                            otp_edt.setVisibility(View.VISIBLE);
                            resend_tv.setVisibility(View.VISIBLE);
                            verify_btn.setEnabled(true);

                            JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                            JSONObject data = jsonObject.getJSONObject("data");

                            client_id = data.optString("client_id");

                        }
                    } else if (response.code() == 401) {
                        dialogClass.alertDialogAuthentication(getActivity());
                    } else {
                        createSnackBar(view, String.valueOf(response.body().get("message")));
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void verifyOTPApi(View view, String aadhaar_number, String otp_number)
    {
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("customer_id", StaticSharedpreference.getInfo("UserId", getContext()));
        queryParams.put("aadhar_number", aadhaar_number);
        queryParams.put("client_id", client_id);
        queryParams.put("otp", otp_number);

        ApiClient.verifyAadharOtp(StaticSharedpreference.getInfo("AccessToken", getContext()), queryParams, new APIResultLitener<JsonObject>() {

            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                try {
                    if (response.isSuccessful() && (response.code() >= 200 && response.code() < 300)) {

                        String status = String.valueOf(response.body().get("status").getAsString());
                        if(status.equalsIgnoreCase("success"))
                        {
                            if (getActivity() instanceof KycVerifyActivity) {
                                ((KycVerifyActivity) getActivity()).checkKyc(
                                        StaticSharedpreference.getInfo("UserId", getContext())
                                );
                            }
                            createSnackBar(view , "Congratulation aadhar kyc is approved!.");
                        }
                    } else if (response.code() == 401) {
                        dialogClass.alertDialogAuthentication(getActivity());
                    } else {
                        createSnackBar(view , String.valueOf(response.body().get("message")));
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createSnackBar(View view,String msg)
    {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }




}