package in.imast.impact.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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



import in.imast.impact.Connection.APIResultLitener;
import in.imast.impact.Connection.ApiClient;
import in.imast.impact.R;
import in.imast.impact.fragment.HomeFragment;
import in.imast.impact.fragment.InvoicesFragmentNative;
import in.imast.impact.fragment.RewardFragment;
import in.imast.impact.fragment.SideMenuFragment;
import in.imast.impact.fragment.WalletFragment;
import in.imast.impact.helper.DialogClass;
import in.imast.impact.helper.LanguageHelper;
import in.imast.impact.helper.PackageAppName;
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

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.InstallStatus;
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

    private PackageAppName packageAppName;

    private AppUpdateManager appUpdateManager;
    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private InstallStateUpdatedListener installStateUpdatedListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        utilities = new Utilities(this);
        dialogClass = new DialogClass();

        packageAppName = new PackageAppName(this);

        appUpdateManager = AppUpdateManagerFactory.create(this);
        checkForAppUpdate();

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

        Intent intent = getIntent();
        String languageActivity = intent.getStringExtra("fromLanguage");
        String status = intent.getStringExtra("status");

        // Check if 'status' is null or empty
        if (status != null && status.equalsIgnoreCase("invoice")) {
            tabPosition = 1;
            unselected();
            imgAttendance.setBackgroundResource(R.drawable.ic_transaction_active);
            tvAttendance.setTextColor(Color.parseColor("#ffcb0d"));
            goToFragment(new WalletFragment());
        } else if (status != null && status.equalsIgnoreCase("lead")) {
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
        } else {
            // Default behavior or handling if 'status' is null
            tabPosition = 0;
            unselected();
            imgHome.setBackgroundResource(R.drawable.ic_home_active);
            tvHome.setTextColor(Color.parseColor("#393185"));
            goToFragment(new HomeFragment());
        }
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

        if (!StaticSharedpreference.getInfo("customerType", this).equals("1")) {
            tvLead.setText("Scan");
        } else {
            tvLead.setText("Invoice");
        }


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
                intent.setData(Uri.parse("tel:" + "8069081275"));

                if (Build.VERSION.SDK_INT > 23) {
                    startActivity(intent);
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

                imgLead.setBackgroundResource(R.drawable.invoice_active);
                tvLead.setTextColor(Color.parseColor("#f27135"));
                goToFragment(new InvoicesFragmentNative());

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
                //sideMenuFragment.setWalletListener(MainActivity.this);
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
                    //startActivity(new Intent(MainActivity.this, ActivityNewEntry.class));                 // All Permissions Granted

                } else {

                }
            }
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void checkPermission()
    {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

    }

    private void checkForAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);


        // Create a listener to track request state updates.
        installStateUpdatedListener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(InstallState installState) {
                // Show module progress, log state, or install the update.
                if (installState.installStatus() == InstallStatus.DOWNLOADED)
                    // After the update is downloaded, show a notification
                    // and request user confirmation to restart the app.
                    popupSnackbarForCompleteUpdateAndUnregister();
            }
        };

    }

    private void unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
    }

    private void popupSnackbarForCompleteUpdateAndUnregister() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "An update has just been downloaded.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Restart the App", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.white));
        snackbar.show();

        unregisterInstallStateUpdListener();
    }


    @Override
    protected void onStart() {
        super.onStart();
        checkPermission();

        // Retrieve the app language from SharedPreferences
        String ul = StaticSharedpreference.getInfo("app_language", this);
        Log.e("appLanguage>MyAPP", ul);

        // Update language if available
        if (ul != null) {
            LanguageHelper.updateLanguage(MainActivity.this, ul);
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