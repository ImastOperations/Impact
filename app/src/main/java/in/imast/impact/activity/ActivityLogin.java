package in.imast.impact.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;


import in.imast.impact.Connection.APIResultLitener;
import in.imast.impact.Connection.ApiClient;
import in.imast.impact.R;
import in.imast.impact.adapter.ViewPagerLoginAdapter;
import in.imast.impact.helper.CarouselEffectTransformer;
import in.imast.impact.helper.DialogClass;
import in.imast.impact.helper.OnKeyboardVisibilityListener;
import in.imast.impact.helper.StaticSharedpreference;
import in.imast.impact.helper.Utilities;
import in.imast.impact.model.LoginResponse;

import com.bumptech.glide.BuildConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;


import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity implements OnKeyboardVisibilityListener {

    CardView cardGetOtp;
    EditText edtMobile;
    Utilities utilities;
    DialogClass dialogClass;
    Dialog dialog;
    Button submitmobile;
    TextView tvWaareeGroup;
    ViewPager viewPager;
    LinearLayout linearDots;
   /* private int[] listItems = {R.mipmap.img1, R.mipmap.img2, R.mipmap.img3, R.mipmap.img4,
            R.mipmap.img5, R.mipmap.img6, R.mipmap.img7, R.mipmap.img8, R.mipmap.img9, R.mipmap.img10};
*/

    TextView tvGetOTP;
    CardView cardSubmit;
    TextView tvRegister;
    LinearLayout linearRegister;
    LinearLayout linearResend;
    TextView tvCountdown;
    TextView tvResend,tvWelcome,tvFieldTitle;
    ArrayList<String> string = new ArrayList<>();
    boolean isSendOTP = false;
    private String deviceToken;
    String modal, brand, android_version;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setKeyboardVisibilityListener(this);
        initViews();
        getFcmToken();
        deviceinformation();
    }

    public void getFcmToken() {

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                deviceToken = token;
                Log.d("FCMToken>>", "retrieve token successful : " + token);
            } else {
                Log.w("FCMToken>>", "token should not be null...");
            }
        }).addOnFailureListener(e -> {
            deviceToken = null;
            //handle e
        }).addOnCanceledListener(() -> {
            deviceToken = null;
        });
    }

    private void initViews() {
        tvFieldTitle = findViewById(R.id.tvFieldTitle);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvCountdown = findViewById(R.id.tvCountdown);
        tvResend = findViewById(R.id.tvResend);
        linearRegister = findViewById(R.id.linearRegister);
        linearResend = findViewById(R.id.linearResend);
        tvRegister = findViewById(R.id.tvRegister);
        tvGetOTP = findViewById(R.id.tvGetOTP);
        cardSubmit = findViewById(R.id.cardSubmit);
        linearDots = findViewById(R.id.linearDots);
        viewPager = findViewById(R.id.viewPager);
        edtMobile = findViewById(R.id.edtMobile);

        viewPager.setClipChildren(false);
        viewPager.setPageMargin(0);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(false, new CarouselEffectTransformer(this)); // Set transformer

        cardSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSendOTP) {
                    if (edtMobile.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(ActivityLogin.this, "Please enter Mobile Number", Toast.LENGTH_SHORT).show();
                    } else if (edtMobile.getText().toString().length() <= 9) {
                        Toast.makeText(ActivityLogin.this, "Please enter valid Mobile Number", Toast.LENGTH_SHORT).show();
                    } else
                        loginvalidation(edtMobile.getText().toString(),false);
                } else {
                    if (edtMobile.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(ActivityLogin.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                    } else
                        retailerLogin();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLogin.this, WebPageActivity.class);
                intent.putExtra("from", "register");
                intent.putExtra("url", ApiClient.WEB_BASE_URL + "customer-create");
                intent.putExtra("title", "Registration");
                startActivity(intent);
            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 5000, 5000);


        utilities = new Utilities(this);
        dialogClass = new DialogClass();
        setSlider(false);

        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClick){
                    loginvalidation(mobileNumber,true);
                    linearResend.setVisibility(View.INVISIBLE);
                }
                else
                    Toast.makeText(ActivityLogin.this, "Please wait for some time", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deviceinformation() {
        Log.i("TAG", "SERIAL: " + Build.SERIAL);
        Log.i("TAG", "MODEL: " + Build.MODEL);
        Log.i("TAG", "ID: " + Build.ID);
        Log.i("TAG", "Manufacture: " + Build.MANUFACTURER);
        Log.i("TAG", "brand: " + Build.BRAND);
        Log.i("TAG", "type: " + Build.TYPE);
        Log.i("TAG", "user: " + Build.USER);
        Log.i("TAG", "BASE: " + Build.VERSION_CODES.BASE);
        Log.i("TAG", "INCREMENTAL " + Build.VERSION.INCREMENTAL);
        Log.i("TAG", "SDK  " + Build.VERSION.SDK);
        Log.i("TAG", "BOARD: " + Build.BOARD);
        Log.i("TAG", "BRAND " + Build.BRAND);
        Log.i("TAG", "HOST " + Build.HOST);
        Log.i("TAG", "FINGERPRINT: " + Build.FINGERPRINT);
        Log.i("TAG", "Version Code: " + Build.VERSION.RELEASE);
        Log.i("TAG", "App Version: " + BuildConfig.VERSION_CODE);

        modal = Build.MODEL;
        brand = Build.BRAND;
        android_version = Build.VERSION.RELEASE;

    }

    String mobileNumber = "";

    private void loginvalidation(String mobile,boolean isResendOTP) {
        if (!utilities.isOnline())
            return;
        dialog = dialogClass.progresesDialog(this);
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("mobile", "" + mobile);
        queryParams.put("customer_type", "1,3,4");
        queryParams.put("logintype", "customer");
        ApiClient.loginvalidation(queryParams, new APIResultLitener<JsonObject>() {
            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                if (response != null && errorMessage == null) {
                    if (response.code() == 200) {
                        dialog.dismiss();
                        if (isResendOTP) {
                            Toast.makeText(ActivityLogin.this, "Resend OTP send successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            mobileNumber = edtMobile.getText().toString();
                            Toast.makeText(ActivityLogin.this, "OTP sent on your mobile number", Toast.LENGTH_SHORT).show();
                        }

                        linearRegister.setVisibility(View.GONE);

                        timer();
                        isSendOTP = true;

                        edtMobile.setHint("Enter OTP");
                        tvGetOTP.setText("Login");



                        //tvWelcome.setText("LOGIN");
                       // tvFieldTitle.setText("One Time Password (OTP)");
                        //tvGetOTP.setText("LOGIN");

                        edtMobile.setText("");

                        // dialogClass.alertDialogMsg(getString(R.string.success), getString(R.string.enter_email), SignInActivity.this, false);

                    } else {
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.errorBody().string());
                            dialogClass.alertDialog(jsonObject.getString("status"), jsonObject.getString("message")
                                    , ActivityLogin.this, false);

                     /*   JSONArray userMessage = jsonObject.getJSONArray("errors");
                        if (userMessage.length() >= 1) {

                            dialogClass.alertDialogMsg(getString(R.string.error), userMessage.get(0) + "", SignInActivity.this, false);
                            // Toast.makeText(SignInActivity.this, "" + userMessage.get(0), Toast.LENGTH_SHORT).show();
                        }
                    */
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    dialog.dismiss();
                }
            }
        }, this);
    }

    LoginResponse.UserInfo UserInfo;

    private void retailerLogin() {

        if (!utilities.isOnline())
            return;

        dialog = dialogClass.progresesDialog(this);
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("username", mobileNumber);
        //  queryParams.put("provider", "customer");
        queryParams.put("otp", edtMobile.getText().toString());
        queryParams.put("fcm_token", deviceToken);
        queryParams.put("device_os", "Android");
        queryParams.put("brand", Build.BRAND);
        queryParams.put("model", Build.MODEL);
        queryParams.put("android_version", android_version);
        queryParams.put("app_version", Build.VERSION.RELEASE);

        Log.e("queryParams>>",""+queryParams);
        ApiClient.retailerLogin(queryParams, new APIResultLitener<LoginResponse>() {
            @Override
            public void onAPIResult(Response<LoginResponse> response, String errorMessage) {

                if (response != null && errorMessage == null) {

                    if (response.code() == 200) {
                        dialog.dismiss();

                        UserInfo = response.body().getUserinfo();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<LoginResponse.UserInfo>() {
                        }.getType();

                        String specialization = gson.toJson(UserInfo);
                        StaticSharedpreference.saveInfo("customerType", UserInfo.getCustomerType() + "", ActivityLogin.this);
                        StaticSharedpreference.saveInfo("UserId", UserInfo.getCustomer_id() + "", ActivityLogin.this);
                        StaticSharedpreference.saveInfo("id", UserInfo.getId() + "", ActivityLogin.this);
                        StaticSharedpreference.saveInfo("name", UserInfo.getName() + "", ActivityLogin.this);
                        StaticSharedpreference.saveInfo("email", UserInfo.getEmail() + "", ActivityLogin.this);
                        StaticSharedpreference.saveInfo("AccessToken", "Bearer " + response.body().getAccess_token() + "", ActivityLogin.this);
                        StaticSharedpreference.saveInfo("mobile", UserInfo.getMobile() + "", ActivityLogin.this);


                        StaticSharedpreference.saveInfo("mobile", UserInfo.getMobile() + "", ActivityLogin.this);

                        if (UserInfo.getEmail() != null)
                            StaticSharedpreference.saveInfo("email", UserInfo.getEmail() + "", ActivityLogin.this);


                        startActivity(new Intent(ActivityLogin.this, MainActivity.class)
                                .putExtra("status",""));
                        finishAffinity();
                        //getCustomer();

                        // dialogClass.alertDialogMsg(getString(R.string.success), getString(R.string.enter_email), SignInActivity.this, false);

                    } else {
                        dialog.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.errorBody().string());

                            dialogClass.alertDialog(jsonObject.getString("status"), jsonObject.getString("message")
                                    , ActivityLogin.this, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    dialog.dismiss();
                }
            }
        }, this);
    }

    /*private void getCustomer() {
        if (!utilities.isOnline())
            return;
        dialog = dialogClass.progresesDialog(this);

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("user_id", "" + StaticSharedpreference.getInfo("UserId", this));

        ApiClient.getCustomer("" + StaticSharedpreference.getInfo("AccessToken", this),
                "" + StaticSharedpreference.getInfo("UserId", this), new APIResultLitener<JsonObject>() {
                    @Override
                    public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                        if (response != null && errorMessage == null) {
                            if (response.code() == 200) {
                                JsonObject json = response.body().get("data").getAsJsonObject();


                                if (json.get("media").getAsJsonArray().size() > 0) {

                                    StaticSharedpreference.saveInfo("profileImage", ApiClient.BASE_IMAGE_URL + "/"
                                            + json.get("media").getAsJsonArray().get(0).getAsJsonObject().
                                            get("id").getAsString() + "/"
                                            + json.get("media").getAsJsonArray().get(0).getAsJsonObject().
                                            get("file_name").getAsString(), ActivityLogin.this);
                                }

                                try {
                                   String fName = "";
                                   String lName = "";
                                    try {
                                        fName = json.get("owner_first_name").getAsString();
                                    } catch (Exception e) {
                                        fName = "";
                                        e.printStackTrace();
                                    }

                                    try {
                                        lName = json.get("owner_last_name").getAsString();
                                    } catch (Exception e) {
                                        lName = "";
                                        e.printStackTrace();
                                    }

                                    StaticSharedpreference.saveInfo("name", fName
                                            + " " + lName, ActivityLogin.this);

                                    String fNameChar = "";
                                    String lNameChar = "";

                                    if(!TextUtils.isEmpty(fName))
                                        fNameChar = fName.toUpperCase().charAt(0)+"";

                                    if(!TextUtils.isEmpty(lName))
                                        lNameChar = lName.toUpperCase().charAt(0)+"";

                                    StaticSharedpreference.saveInfo("heading", fNameChar + "" +
                                            lNameChar, ActivityLogin.this);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                StaticSharedpreference.saveInfo("cityId",
                                        json.get("customer_city").getAsJsonObject().get("id").getAsInt() + "", ActivityLogin.this);

                                startActivity(new Intent(ActivityLogin.this, MainActivity.class));
                                finishAffinity();
                            } else {
                                dialog.dismiss();

                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.errorBody().string());

                                    dialogClass.alertDialog(jsonObject.getString("status"), jsonObject.getString("message")
                                            , ActivityLogin.this, false);
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                            }
                        } else {
                            dialog.dismiss();

                        }
                    }
                });
    }*/


    private int dotscount;
    private ImageView[] dotss;


    int pagePosition = 0;
    //Method for slider
    ViewPagerLoginAdapter viewPagerAdapter;

    private void setSlider(boolean isBottom) {
        viewPager.removeAllViews();
        viewPagerAdapter = new ViewPagerLoginAdapter(ActivityLogin.this, isBottom);
        viewPager.setAdapter(viewPagerAdapter);
        dotscount = viewPagerAdapter.getCount();
        dotss = new ImageView[dotscount];

        linearDots.removeAllViews();

        for (int i = 0; i < dotscount; i++) {
            dotss[i] = new ImageView(this);
            dotss[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.non_active_dot_login));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 5, 0);
            linearDots.addView(dotss[i], params);
        }
        dotss[0].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dot_login));
        viewPager.setCurrentItem(pagePosition);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                pagePosition = position;
                for (int i = 0; i < dotscount; i++) {
                    dotss[i].setImageDrawable(ContextCompat.getDrawable(ActivityLogin.this, R.drawable.non_active_dot_login));
                }
                dotss[position].setImageDrawable(ContextCompat.getDrawable(ActivityLogin.this, R.drawable.active_dot_login));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private class SliderTimer extends TimerTask {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < dotscount - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }


    private void setKeyboardVisibilityListener(
            final OnKeyboardVisibilityListener onKeyboardVisibilityListener) {
        final View parentView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private boolean alreadyOpen;
            private final int defaultKeyboardHeightDP = 100;
            private final int EstimatedKeyboardDP = defaultKeyboardHeightDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
            private final Rect rect = new Rect();

            @Override
            public void onGlobalLayout() {
                int estimatedKeyboardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, parentView.getResources().getDisplayMetrics());
                parentView.getWindowVisibleDisplayFrame(rect);
                int heightDiff = parentView.getRootView().getHeight() - (rect.bottom - rect.top);
                boolean isShown = heightDiff >= estimatedKeyboardHeight;

                if (isShown == alreadyOpen) {
                    Log.i("Keyboard state", "Ignoring global layout change...");
                    return;
                }
                alreadyOpen = isShown;
                onKeyboardVisibilityListener.onVisibilityChanged(isShown);
            }
        });
    }

    @Override
    public void onVisibilityChanged(boolean visible) {

        if (visible) {
            setSlider(true);
        } else
            setSlider(false);
    }

    boolean isClick = false;

    private void timer() {
        isClick = false;
        tvCountdown.setVisibility(View.VISIBLE);
        CountDownTimer timer = new CountDownTimer(1000 * 30, 1000) {

            public void onTick(long millisUntilFinished) {
                tvCountdown.setText("Resend OTP in " + timeString(millisUntilFinished));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                isClick = true;
                tvCountdown.setVisibility(View.INVISIBLE);
                linearResend.setVisibility(View.VISIBLE);
            }

        }.start();


    }


    private String timeString(Long millisUntilFinished1) {
        Long millisUntilFinished = millisUntilFinished1;
        Long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
        millisUntilFinished -= TimeUnit.DAYS.toMillis(days);

        Long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
        millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);

        Long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
        millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);

        Long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

        // Format the string
        return String.format(
                Locale.getDefault(),
                "%02d : %02d",
                minutes, seconds
        );
    }


}
