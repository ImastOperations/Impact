package in.imast.impact.activity;

import static in.imast.impact.activity.MainActivity.progress;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.imast.impact.Connection.ApiClient;
import in.imast.impact.R;
import in.imast.impact.helper.DialogClass;
import in.imast.impact.helper.StaticSharedpreference;
import in.imast.impact.helper.Utilities;

import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WebPageActivity extends AppCompatActivity {
    /*LinearLayout linearBack;*/
    RelativeLayout relativeCircle, relativeError, relativeServerError;
    WebView myWebView;
    Button btnTryAgain;
    String checkUrl = "";
    Utilities utilities;
    ProgressBar progress;
    TextView tvTitle;

    int statusCode = 0;

    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getStringExtra("from").equals("register")) {
            setContentView(R.layout.activity_webpage_new);
        } else
            setContentView(R.layout.activity_webpage);

        dialogClass = new DialogClass();

        tvTitle = findViewById(R.id.tvTitle);
        progress = findViewById(R.id.progress);
        /*linearBack = findViewById(R.id.linearBack);*/
        relativeError = findViewById(R.id.relativeError);
        relativeServerError = findViewById(R.id.relativeServerError);
        btnTryAgain = findViewById(R.id.btnTryAgain);
        relativeCircle = findViewById(R.id.relativeCircle);
        utilities = new Utilities(this);

        myWebView = findViewById(R.id.webview);

        /*if (getIntent().getStringExtra("from").equals("rewards")) {
            linearBack.setVisibility(View.GONE);
        }*/

        tvTitle.setText(getIntent().getStringExtra("title"));


        final String acToken = StaticSharedpreference.getInfo("AccessToken", WebPageActivity.this);

        HashMap<String, String> headers = new HashMap<>();
        // String basicAuthHeader = android.util.Base64.encodeToString((username + ":" + password).getBytes(), android.util.Base64.NO_WRAP);
        headers.put("Authorization", acToken);
        headers.put("Accept-Language", StaticSharedpreference.getInfo("language",this));

        myWebView.loadUrl(getIntent().getStringExtra("url"), headers);

        myWebView.getSettings().setJavaScriptEnabled(true);
        WebSettings ws = myWebView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.getDomStorageEnabled();


        //After Changed to SDK 33
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        ws.setLoadsImagesAutomatically(true);
        //ws.getMixedContentMode();
        ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // Set Download Listener
        myWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                String fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);

                if (fileName.endsWith(".bin") || mimetype.equalsIgnoreCase("application/octet-stream")) {
                    fileName = fileName.replace(".bin", ".pdf"); // Ensure correct extension
                }

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setMimeType(mimetype);
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading file...");
                request.setTitle(fileName);
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                if (downloadManager != null) {
                    downloadManager.enqueue(request);
                }
            }
        });


        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (utilities.isOnline()) {
                    progress.setVisibility(View.GONE);
                    if(statusCode >= 200 && statusCode < 300)
                    {
                        recreate();
                        relativeError.setVisibility(View.GONE);
                        myWebView.setVisibility(View.VISIBLE);
                    }
                    else {
                        recreate();
                        relativeServerError.setVisibility(View.GONE);
                        myWebView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        String from = getIntent().getStringExtra("from");
        if (from.equals("zoom")) {
            ws.setBuiltInZoomControls(true);
        }

        ws.setDomStorageEnabled(true);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        ws.setJavaScriptEnabled(true);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        myWebView.setWebViewClient(new CustomWebViewClient());
        myWebView.setWebChromeClient(new WebChromeClient() {
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

            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
            {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent intent = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    intent = fileChooserParams.createIntent();
                }
                try
                {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e)
                {
                    uploadMessage = null;
                    return false;
                }
                return true;
            }

        });

        /*linearBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (myWebView.canGoBack()) {
                    myWebView.goBack();
                } else {
                    finish();
                }

            }
        });*/

        if (!utilities.isOnline()) {
            relativeError.setVisibility(View.VISIBLE);
            myWebView.setVisibility(View.GONE);
        }
    }

    Dialog dialog;
    DialogClass dialogClass;

    private class CustomWebViewClient extends WebViewClient {

        public CustomWebViewClient() {
            //dialogClass = new DialogClass();
            //  dialog = dialogClass.progresesDialog(WebPageActivity.this);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("/login")) {
                dialogClass.alertDialogAuthentication(WebPageActivity.this);
            } else if (url.contains("go-back")) {
                if (myWebView.canGoBack()) {
                    myWebView.goBack();
                } else {
                    finish();
                }
            }else if(url.contains("tel:")){
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }else if(url.contains("mailto")){

                Uri uri = Uri.parse(url);

                String email = uri.getQueryParameter("email");
                String subject = uri.getQueryParameter("subject");
                String body = uri.getQueryParameter("body");

                Intent  intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:" ));
                String[] to={email};
                intent.putExtra(Intent.EXTRA_EMAIL, to);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, body);
                intent.setType("message/rfc822");
                Intent chooser = Intent.createChooser(intent, "Send Email");
                startActivity(chooser);

            } else {
                final String acToken = StaticSharedpreference.getInfo("AccessToken", WebPageActivity.this);
                HashMap<String, String> headers = new HashMap<>();
                // String basicAuthHeader = android.util.Base64.encodeToString((username + ":" + password).getBytes(), android.util.Base64.NO_WRAP);
                headers.put("Authorization", acToken);
                view.loadUrl(url, headers);
                myWebView.clearHistory();
            }

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
            checkUrl = url;
            super.onPageFinished(view, url);

        }
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);

            if(request.getUrl().toString().contains(ApiClient.WEB_BASE_URL))
            {
                statusCode = errorResponse.getStatusCode();
                Log.v("test ", " sdf + :" + statusCode);
                myWebView.setVisibility(View.GONE);
                if(statusCode == 401)
                {
                    dialogClass.alertDialogAuthentication(WebPageActivity.this);
                }
                else {
                    relativeServerError.setVisibility(View.VISIBLE);
                }
            }
            else {
                myWebView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {

            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK: {

                    if (myWebView.canGoBack()) {
                        myWebView.goBack();
                    } else {
                        finish();
                    }

                    return true;
                }
            }
        }

        return super.onKeyDown(keyCode, event);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
            if (requestCode == REQUEST_SELECT_FILE)
            {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


