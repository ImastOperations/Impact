package in.imast.impact.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.HashMap;

import in.imast.impact.Connection.ApiClient;
import in.imast.impact.R;
import in.imast.impact.helper.DialogClass;
import in.imast.impact.helper.StaticSharedpreference;
import in.imast.impact.helper.Utilities;

public class RegistrationActivity extends AppCompatActivity {

    HashMap<String, String> headers;
    DialogClass dialogClass;
    Dialog dialog;
    Utilities utilities;
    WebView webView;
    ProgressBar progress;
    private RelativeLayout relativeError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();
    }

    private void init()
    {
        utilities = new Utilities(this);
        dialogClass = new DialogClass();

        webView = findViewById(R.id.webView);
        relativeError = findViewById(R.id.relativeError);
        progress = findViewById(R.id.progress);

        if (!utilities.isOnline()) {
            relativeError.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        }
        else {
            setWebView();
        }
    }

    private void setWebView()
    {
        webView.clearCache(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().getDomStorageEnabled();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        final String acToken = StaticSharedpreference.getInfo("AccessToken", RegistrationActivity.this);
        headers = new HashMap<>();
        String lan = StaticSharedpreference.getInfo("language", RegistrationActivity.this);
        Log.e("language>", lan);
        // String basicAuthHeader = android.util.Base64.encodeToString((username + ":" + password).getBytes(), android.util.Base64.NO_WRAP);
        headers.put("Authorization", acToken);
        headers.put("Accept-Language", StaticSharedpreference.getInfo("language", RegistrationActivity.this));
        // view.loadUrl(url, headers);
        webView.loadUrl(ApiClient.WEB_BASE_URL + "customer-create", headers);
        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progress.setVisibility(View.GONE);

                } else {
                    progress.setProgress(newProgress);
                    progress.setVisibility(View.VISIBLE);
                }
                Log.v("akram", "progress " + newProgress);
            }
        });
        MainActivity.webView = webView;

    }
    private class CustomWebViewClient extends WebViewClient {
        public CustomWebViewClient() {

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("/login")) {
                dialogClass.alertDialogAuthentication(RegistrationActivity.this);
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
    }


}