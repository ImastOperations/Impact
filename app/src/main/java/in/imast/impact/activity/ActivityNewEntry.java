package in.imast.impact.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonObject;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;

import in.imast.impact.Connection.APIResultLitener;
import in.imast.impact.Connection.ApiClient;
import in.imast.impact.R;
import in.imast.impact.helper.DialogClass;
import in.imast.impact.helper.StaticSharedpreference;
import in.imast.impact.helper.Utilities;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

public class ActivityNewEntry extends Activity {

    RecyclerView recyclerView;
    EditText Edtname;
    ImageView checkbox;
    Button submit;
    ImageView imgBack;
    RelativeLayout relativeOrcan;
    Dialog dialog;
    DialogClass dialogClass;
    CardView cardSubmit;
    Utilities utilities;
    private TextView resultTextView, tvAdd;
    String barcodeValue = "";
    private BarcodeView mBarcodeView;
    public static ArrayList<String> codeArray = new ArrayList<>();
    public static ArrayList<String> arrResponse = new ArrayList<>();
    public static ArrayList<String> arrEntryId = new ArrayList<>();
    ArrayList<String> string = new ArrayList<>();
    TextView tvSubmit, tvScan, tvEnter, tvOrScan;
    RecyclerView recyclerViewCoupon;
    private String AccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        arrResponse.clear();

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString("userId", StaticSharedpreference.getInfo("UserId", this));
        bundle.putString("mobile", StaticSharedpreference.getInfo("mobile", this));
        mFirebaseAnalytics.logEvent("scan_view", bundle);


        codeArray.clear();
        recyclerViewCoupon = findViewById(R.id.recyclerViewCoupon);
        tvScan = findViewById(R.id.tvScan);
        tvScan = findViewById(R.id.tvScan);
        tvEnter = findViewById(R.id.tvEnter);
        tvOrScan = findViewById(R.id.tvOrScan);
        tvSubmit = findViewById(R.id.tvSubmit);

        tvSubmit = findViewById(R.id.tvSubmit);
        relativeOrcan = findViewById(R.id.relativeOrcan);
        Edtname = findViewById(R.id.Edt_name);
        checkbox = findViewById(R.id.checkbox);
        submit = findViewById(R.id.btn_submit);
        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        imgBack = findViewById(R.id.imgBack);
        mBarcodeView = findViewById(R.id.scanner);
        tvAdd = findViewById(R.id.tvAdd);
        cardSubmit = findViewById(R.id.cardSubmit);

        mBarcodeView.decodeContinuous(callback);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cardSubmit
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (Edtname.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(ActivityNewEntry.this, "Please enter Coupon Code", Toast.LENGTH_SHORT).show();
                        } else if (Edtname.getText().toString().length() < 8) {
                            Toast.makeText(ActivityNewEntry.this, "Coupon Code length should be 8 to 12 character", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog = dialogClass.progresesDialog(ActivityNewEntry.this);
                            schemeredeemed(Edtname.getText().toString());
                        }


                    }
                });

        Edtname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // submit.setVisibility(View.VISIBLE);
                isScan = false;
                mBarcodeView.setVisibility(View.GONE);
                relativeOrcan.setVisibility(View.GONE);
                recyclerViewCoupon.setVisibility(View.VISIBLE);

                return false;
            }
        });

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Edtname.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(ActivityNewEntry.this, "Please enter UID code", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (codeArray.size() == 10) {
                } else {
                    codeArray.add(Edtname.getText().toString());
                    Edtname.setText("");
                }

                // codeAdapter.notifyDataSetChanged();

            }
        });

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Edtname.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(ActivityNewEntry.this, "Please enter Coupon Code", Toast.LENGTH_SHORT).show();
                } else if (Edtname.getText().toString().length() < 8) {
                    Toast.makeText(ActivityNewEntry.this, "Coupon Code length should be 8 to 12 character", Toast.LENGTH_SHORT).show();
                } else {
                    if (codeArray.contains(Edtname.getText().toString())) {
                        Toast.makeText(ActivityNewEntry.this, "Already Added", Toast.LENGTH_SHORT).show();
                    } else {
                        barcodeValue = Edtname.getText().toString();
                        codeArray.add(Edtname.getText().toString());
                        Toast.makeText(ActivityNewEntry.this, "Added", Toast.LENGTH_SHORT).show();

                        recyclerViewCoupon.setVisibility(View.VISIBLE);

                        Edtname.setText("");
                        // schemeredeemed(false);
                        //  mScannerView = new ZXingScannerView(this);
                    }
                }
            }
        });

        utilities = new Utilities(this);
        dialogClass = new DialogClass();

        // mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        dialogClass = new DialogClass();
        // setContentView(mScannerView);

    }

    boolean isScan = true;


    @Override
    public void onBackPressed() {
        if (mBarcodeView.getVisibility() == View.GONE) {
            mBarcodeView.setVisibility(View.VISIBLE);
            relativeOrcan.setVisibility(View.VISIBLE);
            isScan = true;
            Edtname.setText("");
            recyclerViewCoupon.setVisibility(View.GONE);

            // submit.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }


    int arrCount = 0;
    int totalPoints = 0;

    private void schemeredeemed(String code) {
        if (!utilities.isOnline())
            return;

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("coupon_code_id", code);
        queryParams.put("customer_id", StaticSharedpreference.getInfo("UserId", this));
        queryParams.put("entry_date", formattedDate);
        queryParams.put("entry_source_type", "app");
        queryParams.put("customer_type_id", StaticSharedpreference.getInfo("customerType", this));
        Log.e("Query>>",""+queryParams);
        AccessToken =   StaticSharedpreference.getInfo("AccessToken", this);

        Log.e("AccessToken>>",""+AccessToken);

        ApiClient.schemeredeemed1(StaticSharedpreference.getInfo("AccessToken", this), queryParams, new APIResultLitener<JsonObject>() {
            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                Log.e("response>>>",""+response);
                if (response != null && errorMessage == null) {

                    if (response.code() == 200) {

                        dialog.dismiss();
                        if (response.body().get("message").getAsString().contains("Invalid")) {
                            alertDialogScan("Error", "Invalid Coupon Code", ActivityNewEntry.this, false);
                        } else if (response.body().get("message").getAsString().contains("success")) {

                            Log.e("response>>>", "" + response.body().get("request"));


                            totalPoints = response.body().get("request").getAsJsonObject().get("coupon_points").getAsInt();
                            String url = "";
                            String message = "";
                             /* String  url =  response.body().get("request").getAsJsonObject().get("url").getAsString();
                              String  message =  response.body().get("request").getAsJsonObject().get("message").getAsString();
*/

                            Toast.makeText(ActivityNewEntry.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();

                            dialogClass.scanPointsDialog(ActivityNewEntry.this, totalPoints, arrEntryId.size(), url, message);


                        } else if (response.body().get("message").getAsString().contains("already")) {


                            alertDialogScan("Error", "Coupon Code already Redeemed", ActivityNewEntry.this, false);


                        } else {
                            dialog.dismiss();

                            JSONObject jsonObject = null;
                            try {
                                //jsonObject = new JSONObject(response.errorBody().string());
                                alertDialogScan(response.body().get("status").getAsString(), response.body().get("message").getAsString()
                                        , ActivityNewEntry.this, false);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    } else {

                    }
                } else {

                    dialog.dismiss();
                }
            }
        }, this);
    }

    public void alertDialogScan(String title, String description, final Activity activity, final boolean isFinishActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(description)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (isFinishActivity) {

                            startActivity(new Intent(ActivityNewEntry.this, MainActivity.class)
                                    .putExtra("status",""));
                            finishAffinity();
                        }
                    }
                });

        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(title);
        alert.show();
    }


    @Override
    protected void onResume() {
        mBarcodeView.resume();
        mBarcodeView.setVisibility(View.VISIBLE);

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mBarcodeView.pause();
        mBarcodeView.setActivated(false);
    }

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result)
        {
            if (isScan) {
                isScan = false;

                barcodeValue = result.getText();
                // If you would like to resume scanning, call this method below:
                //mScannerView.resumeCameraPreview(this);

                if (barcodeValue.length() < 8) {
                    Toast.makeText(ActivityNewEntry.this, "Invalid Coupon code", Toast.LENGTH_SHORT).show();
                } else {
                    dialog = dialogClass.progresesDialog(ActivityNewEntry.this);
                    schemeredeemed(barcodeValue);
                }
                mBarcodeView.pause();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isScan = true;
                        mBarcodeView.resume();
                        mBarcodeView.setVisibility(View.VISIBLE);
                    }
                }, 1500);


            }

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
            BarcodeCallback.super.possibleResultPoints(resultPoints);
        }
    };


}

