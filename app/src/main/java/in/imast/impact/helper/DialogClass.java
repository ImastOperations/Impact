package in.imast.impact.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import in.imast.impact.R;
import in.imast.impact.activity.ActivityLogin;

import androidx.cardview.widget.CardView;


public class DialogClass {

    Dialog custom_pd;

    public Dialog progresesDialog(final Activity activity) {

        custom_pd = new Dialog(activity, android.R.style.Theme_Translucent);
        custom_pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom_pd.setContentView(R.layout.dialog_progress);
        custom_pd.setCancelable(true);
        custom_pd.setCanceledOnTouchOutside(false);
        custom_pd.show();
        return custom_pd;

    }

    public void alertDialogMsg(Context context, String message, String title) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
        alert.setTitle(title);
        alert.show();

    }

    public  void alertDialogAuthentication(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Please login again")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        StaticSharedpreference.deleteSharedPreference(activity);
                        activity.startActivity(new Intent(activity, ActivityLogin.class));
                        activity.finishAffinity();
                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Authentication failed");
        alert.show();
    }

    public static void alertDialog(String title, String description, final Activity activity, final boolean isFinishActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(description)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isFinishActivity)
                            activity.finish();
                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(title);
        alert.show();
    }

    public Dialog scanPointsDialog(final Activity activity, int totalPoints, int totalEntry, final String url, String message) {

        custom_pd = new Dialog(activity, android.R.style.Theme_Translucent);
        custom_pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom_pd.setContentView(R.layout.dialog_scan_points);

        TextView tvPoints = custom_pd.findViewById(R.id.tvPoints);
        TextView tvMessage = custom_pd.findViewById(R.id.tvMessage);
        TextView tvViewDetails = custom_pd.findViewById(R.id.tvViewDetails);
        TextView tvBtn = custom_pd.findViewById(R.id.tvBtn);
        CardView cardSubmit = custom_pd.findViewById(R.id.cardSubmit);
        final ImageView imgGif = custom_pd.findViewById(R.id.imgGif);

        Glide.with(activity).load(R.drawable.confetti_new).into(imgGif);

        cardSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(url.equals(""))
                    custom_pd.dismiss();
                else{
                  /*  Intent intent = new Intent(activity, WebPageActivity.class);
                    //   intent.putExtra("url", "http://www.togetherstronger.in/Exciting_detail");
                    intent.putExtra("url", ApiClient.BASE_WEB_URL + url);
                    intent.putExtra("from", "");
                    activity.startActivity(intent);*/
                }
            }
        });

        tvViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent intent = new Intent(activity, WebPageTransactionActivity.class);
                intent.putExtra("url", ApiClient.BASE_WEB_URL+"v1/trans-history");
                intent.putExtra("from", "");
                activity.startActivity(intent);*/
            }
        });

        if(url.equals("")) {
            if (totalPoints != 0) {
                tvMessage.setText("YOU HAVE SUCCESSFULLY UNLOCKED");
                tvPoints.setText(totalPoints + " Points");
            } else {
                tvMessage.setText("YOU HAVE SUCCESSFULLY REGISTERED");
                if (totalEntry == 1)
                    tvPoints.setText(totalEntry + " Code");
                else
                    tvPoints.setText(totalEntry + " Codes");
            }
        }else{
            imgGif.setVisibility(View.VISIBLE);
            tvViewDetails.setVisibility(View.INVISIBLE);
            tvPoints.setVisibility(View.GONE);
            tvMessage.setText(message);
            tvBtn.setText("View Details");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imgGif.setVisibility(View.GONE);
            }
        },3000);


        custom_pd.setCancelable(true);
        custom_pd.setCanceledOnTouchOutside(true);
        custom_pd.show();

        return custom_pd;

    }
 }
