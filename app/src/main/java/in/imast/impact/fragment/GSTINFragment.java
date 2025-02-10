package in.imast.impact.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.imast.impact.Connection.APIResultLitener;
import in.imast.impact.Connection.ApiClient;
import in.imast.impact.R;
import in.imast.impact.activity.KycVerifyActivity;
import in.imast.impact.helper.DialogClass;
import in.imast.impact.helper.StaticSharedpreference;
import in.imast.impact.helper.Utilities;
import retrofit2.Response;


public class GSTINFragment extends Fragment {

    private Utilities utilities;
    private DialogClass dialogClass;

    private RelativeLayout main_lout;
    private EditText gstin_edt;
    private TextView edit_tv;
    private Button verify_btn;
    private ProgressBar progress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_g_s_t_i_n, container, false);
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

    private void init(View view) {
        utilities = new Utilities(getActivity());
        dialogClass = new DialogClass();

        main_lout = view.findViewById(R.id.main_lout);
        gstin_edt = view.findViewById(R.id.gstin_edt);
        edit_tv = view.findViewById(R.id.edit_tv);
        verify_btn = view.findViewById(R.id.verify_btn);
        progress = view.findViewById(R.id.progress);

        getGstinApi();

        verify_btn.setOnClickListener(v -> {
            checkAndCallApi(view);
        });

        gstin_edt.setOnEditorActionListener((TextView.OnEditorActionListener) (v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkAndCallApi(view);
                return true;
            }
            return false;
        });

        edit_tv.setOnClickListener(v -> {
            gstin_edt.setText("");
        });

        gstin_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String gstin = s.toString().trim();
                String gstinPattern = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$";
                if (gstin.matches(gstinPattern)) {
                    gstin_edt.setError(null); // Clear error if valid
                } else {
                    gstin_edt.setError("Invalid GSTIN format");
                }
            }
        });

    }

    private void checkAndCallApi(View view) {
        String gstin_number = gstin_edt.getText().toString().trim();

        if (utilities.isOnline()) {
            if (gstin_number.length() == 15) {
                gstinVerifyApi(view, gstin_number);
            } else {
                createSnackBar(view, "Please enter GSTIN number proper");
            }
        } else {
            createSnackBar(view, "Please check your internet");
        }
    }

    private void getGstinApi() {
        progress.setVisibility(View.VISIBLE);
        main_lout.setVisibility(View.GONE);
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("customer_id", StaticSharedpreference.getInfo("UserId", getContext()));

        ApiClient.gstin_kyc(StaticSharedpreference.getInfo("AccessToken", getContext()), queryParams, new APIResultLitener<JsonObject>() {
            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                try {
                    if (response.code() >= 200 && response.code() < 300) {
                        String status = String.valueOf(response.body().get("status").getAsString());
                        progress.setVisibility(View.GONE);

                        main_lout.setVisibility(View.VISIBLE);
                        if (status.equalsIgnoreCase("success")) {

                            JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                            JSONObject data = jsonObject.optJSONObject("data");
                            JSONObject gstin_info = data.optJSONObject("gstin_info");

                            String gstin_no = gstin_info.optString("gstin_no");

                            if (!gstin_no.equalsIgnoreCase("")) {
                                gstin_edt.setText("" + gstin_no);
                            } else {
                                gstin_edt.setText("");
                            }
                        }

                    }
                    else if(response.code() == 401) {
                        dialogClass.alertDialogAuthentication(getActivity());
                    }
                    else {

                    }

                } catch (Exception e) {
                    progress.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        });
    }


    private void gstinVerifyApi(View view, String gstin_number) {
        progress.setVisibility(View.VISIBLE);
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("customer_id", StaticSharedpreference.getInfo("UserId", getContext()));
        queryParams.put("gstin_no", gstin_number);

        ApiClient.submitGstin(StaticSharedpreference.getInfo("AccessToken", getContext()), queryParams, new APIResultLitener<JsonObject>() {
            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {

                try {
                    String status = String.valueOf(response.body().get("status").getAsString());
                    progress.setVisibility(View.GONE);
                    if (status.equals("success")) {
                        createSnackBar(view, String.valueOf(response.body().get("message").getAsString()));
                        if (getActivity() instanceof KycVerifyActivity) {
                            ((KycVerifyActivity) getActivity()).checkKyc(
                                    StaticSharedpreference.getInfo("UserId", getContext())
                            );
                        }
                    } else if (response.code() == 401) {
                        dialogClass.alertDialogAuthentication(getActivity());
                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });

    }

    private void createSnackBar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

}
