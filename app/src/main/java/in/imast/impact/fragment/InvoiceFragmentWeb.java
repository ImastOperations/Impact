package in.imast.impact.fragment;

import static android.app.Activity.RESULT_OK;
import static in.imast.impact.helper.Utilities.INTENTCAMERA;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import id.zelory.compressor.Compressor;
import in.imast.impact.Connection.ApiClient;
import in.imast.impact.R;
import in.imast.impact.activity.CameraActivity;
import in.imast.impact.activity.MainActivity;
import in.imast.impact.helper.DialogClass;
import in.imast.impact.helper.StaticSharedpreference;

public class InvoiceFragmentWeb extends Fragment {

    //Declaration of variables
    private View parentView;
    private Context context;
    DialogClass dialogClass;
    Dialog dialog;
    Dialog dialogAddImage;
    View view;
    WebView webView;
    public static final int SPLASH_DELAY = 2; // in second

    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath = "";
    private String str_image_path1 = "";
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int SELECT_FILE = 2;

    private static final int camera = 1, gallery = 2;

    public static InvoiceFragmentWeb newInstance() {
        return new InvoiceFragmentWeb();
    }

    HashMap<String, String> headers;
    WebViewInitialize webViewInitialize;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        Bundle bundle = new Bundle();
        bundle.putString("userId", StaticSharedpreference.getInfo("UserId", getContext()));
        bundle.putString("mobile", StaticSharedpreference.getInfo("mobile", getContext()));
        mFirebaseAnalytics.logEvent("home_view", bundle);

        if (checkPermission()) {

        } else {
            requestPermission();
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        dialogClass = new DialogClass();
        webView = view.findViewById(R.id.webView);
        webView.clearCache(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        final String acToken = StaticSharedpreference.getInfo("AccessToken", getActivity());
        headers = new HashMap<>();
        String lan = StaticSharedpreference.getInfo("language", getContext());
        Log.e("language>", lan);
        headers.put("Authorization", acToken);
        headers.put("Accept-Language", StaticSharedpreference.getInfo("language", getContext()));
        webView.loadUrl(ApiClient.WEB_BASE_URL + "invoice-create", headers);
        webView.setWebViewClient(new CustomWebViewClient());

        webView.setWebChromeClient(new WebChromeClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                if (url.contains(".pdf")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url), "application/pdf");
                    try {
                        view.getContext().startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        //user does not have a pdf viewer installed
                    }
                } else {
                    webView.loadUrl(url);
                }
                return false; // then it is not handled by default action
            }


        });

        webView.setWebChromeClient(new ChromeClient());

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private class CustomWebViewClient extends WebViewClient {
        public CustomWebViewClient() {
            dialogClass = new DialogClass();
            dialog = dialogClass.progresesDialog(getActivity());
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            Log.v("akram", "url 1" + url);

            if (url.contains("reward-cart") || url.contains("invoice-transaction-detail")) {
                final String acToken = StaticSharedpreference.getInfo("AccessToken1", getContext());
                HashMap<String, String> headers = new HashMap<>();
                // String basicAuthHeader = android.util.Base64.encodeToString((username + ":" + password).getBytes(), android.util.Base64.NO_WRAP);
                headers.put("Authorization", acToken);
                view.loadUrl(url, headers);
            } else if (url.contains("https://waaree.close/")) {

            } else if (url.contains("login")) {
                webView.clearHistory();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("url", "" + url);
                intent.putExtra("from", "rewards");
                startActivity(intent);
                getActivity().finish();
            } else if (url.contains("invoicecreatedsuccessfully")) {
                startActivity(new Intent(getContext(), MainActivity.class)
                        .putExtra("status", "lead"));
                getActivity().finish();

            } else {
                final String acToken = StaticSharedpreference.getInfo("AccessToken1", getContext());
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", acToken);
                view.loadUrl(url, headers);
                webView.clearHistory();
            }

            return true;
        }

        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {
            Log.v("akram", "url2" + url);
            if (url.contains("customer-visit-page")) {
                //binding.linearCheckout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.v("akram", "url3" + url);
            dialog.dismiss();
            super.onPageFinished(view, url);
        }
    }

    private void gotoAddProfileImage(final int camera, final int gallery) {
        dialogAddImage = new Dialog(getContext());
        dialogAddImage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddImage.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialogAddImage.setContentView(R.layout.dialog_show_image_selection1);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogAddImage.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutCamera = (LinearLayout) dialogAddImage.findViewById(R.id.layoutCemera);
        LinearLayout layoutGallary = (LinearLayout) dialogAddImage.findViewById(R.id.layoutGallary);

        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddImage.dismiss();
                dialogAddImage.cancel();
                openCamera();
            }
        });

        layoutGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddImage.dismiss();
                dialogAddImage.cancel();
                getPhotoFromGallary();
            }
        });

        dialogAddImage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAddImage.show();

        dialogAddImage.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.v("OnCancel>", " # onCancel");
                //important to return new Uri[]{}, when nothing to do. This can slove input file wrok for once.
                //InputEventReceiver: Attempted to finish an input event but the input event receiver has already been disposed.

                dialogAddImage.dismiss();
            }
        });


    }

    private void getPhotoFromGallary() {
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(contentSelectionIntent, "Select Image"), SELECT_FILE);
    }

    private void openCamera() {
        Intent intent = new Intent(getContext(), CameraActivity.class);
        intent.putExtra("camera", "1");
        startActivityForResult(intent, INTENTCAMERA);
    }

    public class ChromeClient extends WebChromeClient {

        // For Android 5.0
        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams) {
            // Double check that we don't have any existing callbacks
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePath;

            Log.e("Clicked>", "Clicked!!!");

            gotoAddProfileImage(camera, gallery);




            return true;

        }


        // openFileChooser for Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {

            gotoAddProfileImage(camera, gallery);

        }

        // openFileChooser for Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        //openFileChooser for other Android versions
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType,
                                    String capture) {

            openFileChooser(uploadMsg, acceptType);
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_FILE) {

                Uri[] resultfile = null;

                String dataString = data.getDataString();
                //Log.e("dataStringCamera> ",dataString);
                if (dataString != null) {
                    resultfile = new Uri[]{Uri.parse(dataString)};
                    Log.e("resultCamera22>>", "" + resultfile);
                }

                mFilePathCallback.onReceiveValue(resultfile);
                mFilePathCallback = null;


            } else if (requestCode == INTENTCAMERA) {
                Uri[] results = null;
                str_image_path1 = data.getSerializableExtra("image").toString();
                Log.e("str_image_path1>", str_image_path1);

                File photoFile = new File(str_image_path1);
                File compressedFile = null;
                try {
                    compressedFile = new Compressor(getContext()).setQuality(50).compressToFile(photoFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                str_image_path1 = "file:" + compressedFile.getAbsolutePath();


                results = new Uri[]{Uri.parse(str_image_path1)};


                mFilePathCallback.onReceiveValue(results);
                mFilePathCallback = null;

            }

        }
    }


    public interface WebViewInitialize {
        public void onWebView(WebView webView);
    }

    public void setWebView(WebViewInitialize webView) {
        webViewInitialize = webView;
    }
}
