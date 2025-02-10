package in.imast.impact.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import in.imast.impact.Connection.ApiClient;
import in.imast.impact.R;
import in.imast.impact.adapter.ViewPagerLoginAdapter;
import in.imast.impact.helper.CarouselEffectTransformer;
import in.imast.impact.helper.DialogClass;
import in.imast.impact.helper.OnKeyboardVisibilityListener;
import in.imast.impact.helper.Utilities;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class WelcomeScreen extends AppCompatActivity implements OnKeyboardVisibilityListener {

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
    Button loginMaterialButton;
    Button registerMaterialButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setKeyboardVisibilityListener(this);
        initViews();
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

        loginMaterialButton = findViewById(R.id.loginMaterialButton);
        registerMaterialButton = findViewById(R.id.registerMaterialButton);

        viewPager.setClipChildren(false);
        viewPager.setPageMargin(0);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(false, new CarouselEffectTransformer(this)); // Set transformer

        loginMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeScreen.this,ActivityLogin.class);
                startActivity(intent);
            }
        });
        registerMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeScreen.this, RegistrationActivity.class);
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



    }



    private int dotscount;
    private ImageView[] dotss;


    int pagePosition = 0;
    //Method for slider
    ViewPagerLoginAdapter viewPagerAdapter;

    private void setSlider(boolean isBottom) {
        viewPager.removeAllViews();
        viewPagerAdapter = new ViewPagerLoginAdapter(WelcomeScreen.this, isBottom);
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
                    dotss[i].setImageDrawable(ContextCompat.getDrawable(WelcomeScreen.this, R.drawable.non_active_dot_login));
                }
                dotss[position].setImageDrawable(ContextCompat.getDrawable(WelcomeScreen.this, R.drawable.active_dot_login));
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
