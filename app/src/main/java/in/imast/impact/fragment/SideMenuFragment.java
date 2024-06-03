package in.imast.impact.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import in.imast.impact.BuildConfig;
import in.imast.impact.Connection.APIResultLitener;
import in.imast.impact.Connection.ApiClient;
import in.imast.impact.R;
import in.imast.impact.activity.ActivityLogin;
import in.imast.impact.activity.ChooseLanguageActivity;
import in.imast.impact.activity.MainActivity;
import in.imast.impact.activity.NotificationActivity;
import in.imast.impact.activity.WebPageActivity;
import in.imast.impact.databinding.FragmentSideMenuBinding;
import in.imast.impact.helper.DialogClass;
import in.imast.impact.helper.StaticSharedpreference;
import in.imast.impact.helper.Utilities;
;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;

public class SideMenuFragment extends Fragment {

    //Declaration of variables
    private View parentView;
    private Context context;
    private FragmentSideMenuBinding binding;
    DialogClass dialogClass;
    Dialog dialog;
    public static final int SPLASH_DELAY = 2; // in second
    OnClickWallet onClickWallet;

    public static SideMenuFragment newInstance() {
        return new SideMenuFragment();
    }

    ImageView iv_logo;
    String name;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_side_menu, container, false);
            parentView = binding.getRoot();
        } catch (InflateException e) {
            e.printStackTrace();
        }

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        Bundle bundle = new Bundle();
        bundle.putString("userId", StaticSharedpreference.getInfo("UserId", getContext()));
        bundle.putString("mobile", StaticSharedpreference.getInfo("mobile", getContext()));
        mFirebaseAnalytics.logEvent("menu_view", bundle);

        if (StaticSharedpreference.getInfo("customerType", getActivity()).equals("1")) {
            binding.ivLogo.setImageDrawable(getResources().getDrawable(R.drawable.banner_update));
        } else {
            binding.ivLogo.setImageDrawable(getResources().getDrawable(R.drawable.banner_update));
        }

        String appVersion = BuildConfig.VERSION_NAME;
        binding.tvVersion.setText("App Version " + appVersion);

        try {
            name = StaticSharedpreference.getInfo("name", getActivity());
            if (!TextUtils.isEmpty(name)) {
                String output = toTitleCase(name);
                binding.tvName.setText(output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            binding.tvMobile.setText(StaticSharedpreference.getInfo("mobile", getActivity()));
            char fnameChar = name.toUpperCase().charAt(0);
            binding.tvNameHeading.setText("" + fnameChar);
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.linearBadges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebPageActivity.class);
                intent.putExtra("from", "");
                intent.putExtra("url", ApiClient.WEB_BASE_URL + "badge-show");
                intent.putExtra("title", "Badges");
                //  intent.putExtra("url","http://jaisarathi.com/wallet-show?customer_id=1262");
                startActivity(intent);
            }
        });

        binding.linearSpinWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebPageActivity.class);
                intent.putExtra("from", "");
                intent.putExtra("url", ApiClient.WEB_BASE_URL + "spin-and-win");
                intent.putExtra("title", "Spin and Win");
                //  intent.putExtra("url","http://jaisarathi.com/wallet-show?customer_id=1262");
                startActivity(intent);
            }
        });

        binding.linearWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickWallet.onWallet();

              /*  Intent intent = new Intent(getActivity(), WebPageActivity.class);
                intent.putExtra("from", "");
                intent.putExtra("url", ApiClient.WEB_BASE_URL + "transaction");
                intent.putExtra("title", "Wallet");
                //  intent.putExtra("url","http://jaisarathi.com/wallet-show?customer_id=1262");
                startActivity(intent);*/
            }
        });

        binding.linearLeaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebPageActivity.class);
                intent.putExtra("from", "");
                intent.putExtra("url", ApiClient.WEB_BASE_URL + "leaderboard");
                intent.putExtra("title", "Leader Board");
                //  intent.putExtra("url","http://jaisarathi.com/wallet-show?customer_id=1262");
                startActivity(intent);
            }
        });

        binding.linearProgramBooklet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebPageActivity.class);
                intent.putExtra("from", "");
                intent.putExtra("url", ApiClient.WEB_BASE_URL + "product-catlogue");
                intent.putExtra("title", "Program Booklet");
                //  intent.putExtra("url","http://jaisarathi.com/wallet-show?customer_id=1262");
                startActivity(intent);
            }
        });

        binding.linearFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebPageActivity.class);
                intent.putExtra("from", "");
                intent.putExtra("url", ApiClient.WEB_BASE_URL + "faq");
                intent.putExtra("title", "Frequently Asked Question");
                //  intent.putExtra("url","http://jaisarathi.com/wallet-show?customer_id=1262");
                startActivity(intent);
            }
        });

        binding.linearTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebPageActivity.class);
                intent.putExtra("from", "");
                intent.putExtra("url", ApiClient.WEB_BASE_URL + "termsandconditions");
                intent.putExtra("title", "Terms and Conditions");
                //  intent.putExtra("url","http://jaisarathi.com/wallet-show?customer_id=1262");
                startActivity(intent);
            }
        });

        binding.linearContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebPageActivity.class);
                intent.putExtra("from", "");
                intent.putExtra("url", ApiClient.WEB_BASE_URL + "help-and-support");
                intent.putExtra("title", "Help and Support");
                //  intent.putExtra("url","http://jaisarathi.com/wallet-show?customer_id=1262");
                startActivity(intent);
            }
        });

        binding.tvRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    getActivity().finish();
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    getActivity().finish();
                }
            }
        });
        binding.tvWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebPageActivity.class);
                intent.putExtra("from", "");
                intent.putExtra("url", ApiClient.WEB_BASE_URL + "testimonials-create");
                //  intent.putExtra("url","http://jaisarathi.com/wallet-show?customer_id=1262");
                startActivity(intent);
            }
        });
        binding.tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogMsg(getContext());
            }
        });
        binding.linearNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);
            }
        });
        binding.linearLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChooseLanguageActivity.class);
                startActivity(intent);
            }
        });
        binding.linearRaiseTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebPageActivity.class);
                intent.putExtra("from", "");
                intent.putExtra("url", ApiClient.WEB_BASE_URL + "task-history");
                //  intent.putExtra("url","http://jaisarathi.com/wallet-show?customer_id=1262");
                startActivity(intent);
            }
        });
        binding.linearProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebPageActivity.class);
                intent.putExtra("from", "");
                intent.putExtra("url", ApiClient.WEB_BASE_URL + "customer-details");
                //  intent.putExtra("url","http://jaisarathi.com/wallet-show?customer_id=1262");
                startActivity(intent);
            }
        });


        return parentView;
    }

    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    public void alertDialogMsg(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("Do you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logout();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Logout");
        alert.show();

    }

    Utilities utilities;

    private void logout() {
        utilities = new Utilities(getActivity());
        if (!utilities.isOnline())
            return;

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

        Map<String, String> queryParams = new HashMap<String, String>();

        ApiClient.logout(StaticSharedpreference.getInfo("AccessToken", getActivity()), queryParams, new APIResultLitener<JsonObject>() {
            @Override
            public void onAPIResult(Response<JsonObject> response, String errorMessage) {
                if (response != null && errorMessage == null) {
                    if (response.code() == 200) {

                        startActivity(new Intent(getContext(), ActivityLogin.class));
                        MainActivity.tabPosition = 0;
                        getActivity().finishAffinity();
                        StaticSharedpreference.deleteSharedPreference(getActivity());
                    } else {
                        startActivity(new Intent(getContext(), ActivityLogin.class));
                        MainActivity.tabPosition = 0;
                        getActivity().finishAffinity();
                        StaticSharedpreference.deleteSharedPreference(getActivity());
                        dialog.dismiss();

                        JSONObject jsonObject = null;
                        try {
                            //jsonObject = new JSONObject(response.errorBody().string());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                } else {

                }
            }
        }, getActivity());
    }


    private void handleThread() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, SPLASH_DELAY * 900);
    }

    private class CustomWebViewClient extends WebViewClient {
        public CustomWebViewClient() {
            dialogClass = new DialogClass();
            dialog = dialogClass.progresesDialog(getActivity());
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {
            Log.v("akram", "url2" + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.v("akram", "url3" + url);
            //dialog.dismiss();
            //checkUrl = url;
            super.onPageFinished(view, url);
        }
    }

    public void setWalletListener(OnClickWallet onClickWallet) {
        this.onClickWallet = onClickWallet;
    }

    public interface OnClickWallet {
        public void onWallet();
    }
}
