package in.imast.impact.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import in.imast.impact.Connection.ApiClient;
import in.imast.impact.R;
import in.imast.impact.helper.DialogClass;
import in.imast.impact.helper.StaticSharedpreference;
import in.imast.impact.helper.Utilities;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static in.imast.impact.activity.MainActivity.progress;

public class RewardFragment extends Fragment {

    //Declaration of variables
    private View parentView;
    private Context context;
    DialogClass dialogClass;
    Dialog dialog;
    View view;

    WebView webView;
    private Button btnTryAgain;
    private RelativeLayout relativeServerError,relativeError;

    private int statusCode = 0;
    private Utilities utilities;
    public static final int SPLASH_DELAY = 2; // in second

    public static RewardFragment newInstance() {
        return new RewardFragment();
    }

    HashMap<String, String> headers;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        Bundle bundle = new Bundle();
        bundle.putString("userId", StaticSharedpreference.getInfo("UserId", getContext()));
        bundle.putString("mobile", StaticSharedpreference.getInfo("mobile", getContext()));
        mFirebaseAnalytics.logEvent("reward_view", bundle);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //  setHasOptionsMenu(true);

        context = getActivity();
        dialogClass = new DialogClass();
        utilities = new Utilities(getActivity());

        webView = view.findViewById(R.id.webView);
        btnTryAgain = view.findViewById(R.id.btnTryAgain);
        relativeError = view.findViewById(R.id.relativeError);
        relativeServerError = view.findViewById(R.id.relativeServerError);

        if(utilities.isOnline())
        {
            setWebView();
        }else {
            relativeError.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        }

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (utilities.isOnline()) {
                    progress.setVisibility(View.GONE);
                    if(statusCode >= 200 && statusCode < 300)
                    {
                        getActivity().recreate();
                        relativeError.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);
                    }
                    else {
                        getActivity().recreate();
                        relativeServerError.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


    }

    private void setWebView()
    {
        webView.clearCache(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        final String acToken = StaticSharedpreference.getInfo("AccessToken", getActivity());
        headers = new HashMap<>();
        String lan = StaticSharedpreference.getInfo("language",getContext());
        Log.e("language>",lan);
        // String basicAuthHeader = android.util.Base64.encodeToString((username + ":" + password).getBytes(), android.util.Base64.NO_WRAP);
        headers.put("Authorization", acToken);
        headers.put("Accept-Language", StaticSharedpreference.getInfo("language",getContext()));

        webView.loadUrl(ApiClient.WEB_BASE_URL + "reward-page", headers);
        webView.setWebViewClient(new CustomWebViewClient());

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress==100){
                    progress.setVisibility(View.GONE);
                    if(statusCode >= 400  && statusCode <= 500)
                    {
                        webView.setVisibility(View.GONE);
                    }
                    else {
                        webView.setVisibility(View.VISIBLE);
                    }
                }else{
                    progress.setProgress(newProgress);
                    progress.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private class CustomWebViewClient extends WebViewClient {
        public CustomWebViewClient() {

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("/login")) {
                dialogClass.alertDialogAuthentication(getActivity());
            } else
                view.loadUrl(url, headers);
            return true;
        }

        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {
            Log.v("akram", "url2" + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.v("akram", "url3" + url);

            super.onPageFinished(view, url);
        }
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);

            if(request.getUrl().toString().contains(ApiClient.WEB_BASE_URL))
            {
                statusCode = errorResponse.getStatusCode();
                Log.v("test ", " sdf + :" + statusCode);
                webView.setVisibility(View.GONE);

                if(statusCode == 401)
                {
                    dialogClass.alertDialogAuthentication(getActivity());
                }
                else {
                    relativeServerError.setVisibility(View.VISIBLE);
                }
            }
            else {
                webView.setVisibility(View.GONE);
            }
        }
    }
}
