package in.imast.impact.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import in.imast.impact.BuildConfig;
import in.imast.impact.Connection.APIResultLitener;
import in.imast.impact.Connection.ApiClient;
import in.imast.impact.R;
import in.imast.impact.fragment.HomeFragment;
import in.imast.impact.fragment.InvoiceFragmentWeb;
import in.imast.impact.fragment.RewardFragment;
import in.imast.impact.fragment.SideMenuFragment;
import in.imast.impact.fragment.WalletFragment;
import in.imast.impact.helper.DialogClass;
import in.imast.impact.helper.LanguageHelper;
import in.imast.impact.helper.StaticSharedpreference;
import in.imast.impact.helper.Utilities;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.JsonObject;

public class MainActivity extends AppCompatActivity implements SideMenuFragment.OnClickWallet {
    LinearLayout linearHome, linearAttendance, linearLead, linearActivity, linearMore;
    ImageView imgHome, imgAttendance, imgLead, imgActivity, imgMore;
    TextView tvHome, tvAttendance, tvLead, tvActivity, tvMore;
    Utilities utilities;
    DialogClass dialogClass;
    RelativeLayout relativeNotification;
    AppCompatTextView tvCheckout;
    Dialog dialogg;
    AppCompatTextView tvContent;
    public static int tabPosition = 0;
    Dialog dialog;
    public static ProgressBar progress;
    public static WebView webView;
    public static TextView tvSave;
    private String status;
    ImageView callButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        utilities = new Utilities(this);
        dialogClass = new DialogClass();


        initView();


        String ul = StaticSharedpreference.getInfo("app_language", this);
        Log.e("appLanguage>MyAPP", ul);
        // if null the language doesn't need to be changed as the user has not chosen one.
        if (ul != null) {
            LanguageHelper.updateLanguage(MainActivity.this, ul);
        }

        String select_staus = StaticSharedpreference.getInfo("language_status", this);
        if (!select_staus.equals("selected")) {
            Intent intent = new Intent(MainActivity.this, ChooseLanguageActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            /*chekversion*/
            CheckVersion();
        }



        Intent intent = getIntent();
        String languageActivity = intent.getStringExtra("fromLanguage");
        String status = intent.getStringExtra("status");

        Log.e("fromLanguage", "" + languageActivity);
        if (Objects.equals(languageActivity, "yes")) {
            tabPosition = 0;
            unselected();
            imgHome.setBackgroundResource(R.drawable.ic_home_active);
            tvHome.setTextColor(Color.parseColor("#393185"));
            HomeFragment homeFragment = new HomeFragment();
            goToFragment(homeFragment);
        }


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                status = null;
            } else {
                status = extras.getString("status");
            }
        } else {
            status = (String) savedInstanceState.getSerializable("status");
        }


        if (status.equals("invoice")) {

            tabPosition = 1;
            unselected();
            imgAttendance.setBackgroundResource(R.drawable.ic_transaction_active);
            tvAttendance.setTextColor(Color.parseColor("#ffcb0d"));
            goToFragment(new WalletFragment());
        }

        if (status.equals("lead")) {

            tabPosition = 0;
            linearHome.setClickable(false);
            linearAttendance.setClickable(true);
            linearLead.setClickable(true);
            linearActivity.setClickable(true);
            linearMore.setClickable(true);

            unselected();
            relativeNotification.setVisibility(View.VISIBLE);
            tvSave.setVisibility(View.GONE);
            imgHome.setBackgroundResource(R.drawable.ic_home_active);
            tvHome.setTextColor(Color.parseColor("#f27135"));

            HomeFragment homeFragment = new HomeFragment();
            goToFragment(homeFragment);
        }





        final String acToken = StaticSharedpreference.getInfo("AccessToken", MainActivity.this);
        Log.e("acToken> ", acToken);





    }


    /*chekversion*/
    private void CheckVersion() {
        if (!utilities.isOnline())
            return;
        //dialog = dialogClass.progresesDialog(this);
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("app_type", "customer");
        queryParams.put("customer_type_id", "2");

        ApiClient.checkVersion(queryParams, new APIResultLitener<JsonObject>() {
            private ImageView closed;

            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                if (response.body().get("status").getAsString().equals("success")) {
                    Log.e("my_version_rsponse>>>", "" + response.body());

                    String onlineVersion = response.body().get("version").getAsString();
                    String force_update = response.body().get("force_update").getAsString();

                    try {
                        int currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
                        Log.e("currentVersion>>>>", "" + currentVersion);

                        if (currentVersion < Integer.parseInt(onlineVersion)) {

                            if (force_update.equals("yes")) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
// ...
                                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                                View dialogView = inflater.inflate(R.layout.play_store_update, null);
                                dialogBuilder.setView(dialogView);

                                CardView cardLogin = (CardView) dialogView.findViewById(R.id.updateCardView);
                                closed = (ImageView) dialogView.findViewById(R.id.closed);
                                AlertDialog alertDialog = dialogBuilder.create();

                                cardLogin.setOnClickListener(v -> {
                                   // final String appPackageName = BuildConfig.APPLICATION_ID; // getPackageName() from Context or Activity object
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
                                    finishAffinity();
                                    alertDialog.dismiss();
                                });

                                closed.setOnClickListener(v -> {
                                    alertDialog.dismiss();
                                });

                                alertDialog.show();
                            } else {
                                closed.setVisibility(View.GONE);
                            }
                        } else {
                        }

                    } catch (PackageManager.NameNotFoundException e) {

                    }
                    //dialog.dismiss();
                } else {
                    //dialog.dismiss();
                    Toast.makeText(MainActivity.this, "" + response.body().get("message").getAsString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, this);

    }


    private void initView() {


        relativeNotification = findViewById(R.id.relativeNotification);
        callButton = findViewById(R.id.call_icon);
        progress = findViewById(R.id.progress);
        linearHome = findViewById(R.id.linearHome);
        linearAttendance = findViewById(R.id.linearAttendance);
        linearLead = findViewById(R.id.linearLead);
        linearActivity = findViewById(R.id.linearActivity);
        linearMore = findViewById(R.id.linearMore);
        imgHome = findViewById(R.id.imgHome);
        imgAttendance = findViewById(R.id.imgAttendance);
        imgLead = findViewById(R.id.imgLead);
        imgActivity = findViewById(R.id.imgActivity);
        imgMore = findViewById(R.id.imgMore);
        tvHome = findViewById(R.id.tvHome);
        tvAttendance = findViewById(R.id.tvAttendance);
        tvLead = findViewById(R.id.tvLead);
        tvActivity = findViewById(R.id.tvActivity);
        tvMore = findViewById(R.id.tvMore);
        tvSave = findViewById(R.id.tvSave);

        String customerType = StaticSharedpreference.getInfo("customerType", this);
        Log.e("CustomerType>>", customerType);

        String acToken = StaticSharedpreference.getInfo("customerType", this);
        Log.e("acToken>>", acToken);

        /*if (!StaticSharedpreference.getInfo("customerType", this).equals("1")) {
            tvLead.setText("Scan");
        } else {
            tvLead.setText("Invoice");
        }*/

      /*  HomeFragment homeFragment = new HomeFragment();
        goToFragment(homeFragment);*/


        linearHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabPosition = 0;
                linearHome.setClickable(false);
                linearAttendance.setClickable(true);
                linearLead.setClickable(true);
                linearActivity.setClickable(true);
                linearMore.setClickable(true);

                unselected();
                relativeNotification.setVisibility(View.VISIBLE);
                tvSave.setVisibility(View.GONE);
                imgHome.setBackgroundResource(R.drawable.ic_home_active);
                tvHome.setTextColor(Color.parseColor("#f27135"));

                HomeFragment homeFragment = new HomeFragment();
                goToFragment(homeFragment);

            }
        });

        linearAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unselected();
                relativeNotification.setVisibility(View.VISIBLE);
                tvSave.setVisibility(View.GONE);
                linearAttendance.setClickable(false);
                linearHome.setClickable(true);
                linearLead.setClickable(true);
                linearActivity.setClickable(true);
                linearMore.setClickable(true);
                onWalletClick();
            }
        });

        relativeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "09875900609"));

                if (Build.VERSION.SDK_INT > 23) {
                    startActivity(intent);
                    Log.e("BuildV>","true---");
                } else {

                    if (ActivityCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(MainActivity.this, "Permission Not Granted ", Toast.LENGTH_SHORT).show();
                    } else {
                        final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, 9);
                        startActivity(intent);
                    }
                }
            }
        });

        /*linearLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},

                        1);

            }
        });*/

        linearLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabPosition = 2;
                unselected();
                linearAttendance.setClickable(true);
                linearHome.setClickable(true);
                linearLead.setClickable(false);
                linearActivity.setClickable(true);
                linearMore.setClickable(true);

                //InvoicesFragment.productId.clear();

                Log.e("InvoiceClicked>>", "Clicked..");
                //relativeNotification.setVisibility(View.GONE);
                //tvSave.setVisibility(View.VISIBLE);
                imgLead.setBackgroundResource(R.drawable.invoice_active);
                tvLead.setTextColor(Color.parseColor("#f27135"));
                goToFragment(new InvoiceFragmentWeb());

                /*if (!StaticSharedpreference.getInfo("customerType", MainActivity.this).equals("1")) {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    Log.e("ScanClicked>>", "Clicked..");
                } else {

                    Log.e("InvoiceClicked>>", "Clicked..");
                    relativeNotification.setVisibility(View.GONE);
                    tvSave.setVisibility(View.VISIBLE);
                    imgLead.setImageDrawable(getResources().getDrawable(R.drawable.ic_prime_scan));
                    tvLead.setTextColor(Color.parseColor("#ffcb0d"));
                    goToFragment(new InvoicesFragment());


                }*/

            }
        });

        linearActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabPosition = 3;
                linearActivity.setClickable(false);
                linearAttendance.setClickable(true);
                linearHome.setClickable(true);
                linearLead.setClickable(true);
                linearMore.setClickable(true);
                relativeNotification.setVisibility(View.VISIBLE);
                tvSave.setVisibility(View.GONE);
                unselected();
                imgActivity.setBackground(getResources().getDrawable(R.drawable.ic_rewards_active));
                //imgActivity.setBackgroundResource(R.drawable.ic_rewards_active);
                tvActivity.setTextColor(Color.parseColor("#f27135"));
                goToFragment(new RewardFragment());
            }
        });

        linearMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearMore.setClickable(false);
                linearActivity.setClickable(true);
                linearAttendance.setClickable(true);
                linearHome.setClickable(true);
                linearLead.setClickable(true);
                relativeNotification.setVisibility(View.VISIBLE);
                tvSave.setVisibility(View.GONE);
                tabPosition = 4;
                unselected();
                imgMore.setBackgroundResource(R.drawable.ic_menu_active);
                tvMore.setTextColor(Color.parseColor("#f27135"));
                SideMenuFragment sideMenuFragment = new SideMenuFragment();
                sideMenuFragment.setWalletListener(MainActivity.this);
                goToFragment(sideMenuFragment);
            }
        });


    }

    private void onWalletClick() {
        tabPosition = 1;
        unselected();
        imgAttendance.setBackgroundResource(R.drawable.ic_transaction_active);
        tvAttendance.setTextColor(Color.parseColor("#f27135"));
        goToFragment(new WalletFragment());
    }

    private void unselected() {
        imgHome.setBackgroundResource(R.drawable.ic_home_inactive);
        imgAttendance.setBackgroundResource(R.drawable.ic_transaction_inactive);
        imgLead.setBackgroundResource(R.drawable.invoice_inactive);
        imgActivity.setBackgroundResource(R.drawable.ic_rewards_inactive_newc);
        imgMore.setBackgroundResource(R.drawable.ic_menu_inactive_newc);

        tvHome.setTextColor(Color.parseColor("#999999"));
        tvAttendance.setTextColor(Color.parseColor("#999999"));
        tvLead.setTextColor(Color.parseColor("#999999"));
        tvActivity.setTextColor(Color.parseColor("#999999"));
        tvMore.setTextColor(Color.parseColor("#999999"));

        /*if (!StaticSharedpreference.getInfo("customerType", this).equals("1")) {
            tvLead.setText("Scan");
        } else {
            tvLead.setText("Invoice");
        }*/

    }

    public void goToFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //  fragmentTransaction.setCustomAnimations(R.anim.slide_to_left, R.anim.slide_from_right);
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

   /* public void goToFragmentTag(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //  fragmentTransaction.setCustomAnimations(R.anim.slide_to_left, R.anim.slide_from_right);
        fragmentTransaction.replace(R.id.container, fragment, tag);
        fragmentTransaction.commit();
    }*/

    @Override
    public void onResume() {
        super.onResume();
        unselected();
        if (tabPosition == 0) {
            imgHome.setBackgroundResource(R.drawable.ic_home_active);
            tvHome.setTextColor(Color.parseColor("#f27135"));
            goToFragment(new HomeFragment());
        } else if (tabPosition == 1) {
            imgAttendance.setBackgroundResource(R.drawable.ic_transaction_active);
            tvAttendance.setTextColor(Color.parseColor("#f27135"));
        } else if (tabPosition == 2) {
            imgLead.setBackgroundResource(R.drawable.invoice_active);
            tvLead.setTextColor(Color.parseColor("#f27135"));
        } else if (tabPosition == 3) {
            imgActivity.setBackgroundResource(R.drawable.ic_rewards_active);
            tvActivity.setTextColor(Color.parseColor("#f27135"));
        } else if (tabPosition == 4) {
            imgMore.setBackgroundResource(R.drawable.ic_menu_active);
            tvMore.setTextColor(Color.parseColor("#f27135"));
        }
    }

    boolean isBackOne = false;

    @Override
    public void onBackPressed() {
        if (tabPosition == 0) {
            if (webView != null && webView.canGoBack()) {
                webView.goBack();
            } else if (isBackOne) {
                super.onBackPressed();
            } else {
                isBackOne = true;
                linearHome.setClickable(true);
                linearActivity.setClickable(true);
                linearMore.setClickable(true);
                linearAttendance.setClickable(true);
                linearLead.setClickable(true);
                Toast.makeText(MainActivity.this, "Press again to exit", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isBackOne = false;
                    }
                }, 2000);
            }
        } else {

           /* tabPosition = 0;
            unselected();

            imgHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_active));
            tvHome.setTextColor(Color.parseColor("#ffcb0d"));
            goToFragment(new HomeFragment());*/
            if (webView != null && webView.canGoBack() && tabPosition != 4) {
                webView.goBack();
            } else {
                linearLead.setClickable(true);
                linearHome.setClickable(false);
                linearActivity.setClickable(true);
                linearAttendance.setClickable(true);
                linearMore.setClickable(true);

                tabPosition = 0;
                unselected();
                imgHome.setBackgroundResource(R.drawable.ic_home_active);
                tvHome.setTextColor(Color.parseColor("#f27135"));
                goToFragment(new HomeFragment());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                ) {
                    startActivity(new Intent(MainActivity.this, ActivityNewEntry.class));                 // All Permissions Granted

                } else {

                }

            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onWallet() {
        onWalletClick();
    }
}