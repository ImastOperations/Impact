package in.imast.impact.helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import in.imast.impact.R;
import in.imast.impact.activity.NotificationActivity;
import in.imast.impact.model.NotificationModel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import androidx.core.app.NotificationCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String mes = remoteMessage.getData().get("message") ;
        String title = remoteMessage.getData().get("title") ;
        String time = remoteMessage.getData().get("time");
        String image = remoteMessage.getData().get("image");

        Gson gson = new Gson();
        String json2 = StaticSharedpreference.getInfo("notification", getApplicationContext());

        Type type = new TypeToken<ArrayList<NotificationModel>>() {
        }.getType();
       ArrayList<NotificationModel>  userInfo2 = gson.fromJson(json2, type);


        if (userInfo2 != null) {
            if (!userInfo2.equals("")) {
                boolean available = false;
                for (int i=0;i<userInfo2.size();i++) {
                    if (userInfo2.get(i).getMessage() == mes) {

                        available = true;
                    }
                }


                    if (userInfo2.size() == 10) {
                        userInfo2.remove(9);
                    }

                    NotificationModel notification = new NotificationModel();

                    notification.setTitle(title.toString());
                    notification.setMessage(mes.toString());
                    notification.setTime(time.toString());
                    notification.setBitmap(image.toString());

                    userInfo2.add(0, notification);

                    Gson gson1 =new Gson();
                    String specialization = gson1.toJson(userInfo2);

                    StaticSharedpreference.saveInfo("notification", specialization, getApplicationContext());


            }
        } else if (mes != "") {

            ArrayList<NotificationModel> notiArr =new ArrayList<NotificationModel>();

            NotificationModel notification =new NotificationModel();

            notification.setTitle(title.toString());
            notification.setMessage(mes.toString());
            notification.setTime(time.toString());
            notification.setBitmap(image.toString());

            notiArr.add(notification);

            Gson gson1 =new Gson();
            String specialization = gson1.toJson(notiArr);

            StaticSharedpreference.saveInfo("notification", specialization, getApplicationContext());
        }

        int count = StaticSharedpreference.getInt("notificationCount", getApplicationContext());

        if (count != 10)
            StaticSharedpreference.saveInt("notificationCount", count + 1, getApplicationContext());


        if (image.equals(""))
        notification(title, mes);
        else {

            Bitmap bitmap = convertBitmap(image);
            notificationImage(title, mes, bitmap);
        }
    }

    private Bitmap convertBitmap(String urlImage) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlImage);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            // Log exception
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    public void notificationImage(String title, String message, Bitmap bitmap) {
        Intent intent = new Intent(this, NotificationActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Uri uri = Uri.parse("android.resource://"
                + getApplicationContext().getPackageName() + "/" + R.raw.mix_positive);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.app_name))
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap))
                .setAutoCancel(true);


        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build();

            String name = "name";
            String descriptionText = "channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Ramaply", name, importance);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setSound(uri, audioAttributes);
            // Register the channel with the system

            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(106, builder.build());

    }

    public void notification(String title, String message) {
        Intent intent = new Intent(this, NotificationActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Uri uri = Uri.parse("android.resource://"
                + getApplicationContext().getPackageName() + "/" + R.raw.mix_positive);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.app_name))
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build();

            String name = "name";
            String descriptionText = "channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(getString(R.string.app_name), name, importance);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setSound(uri, audioAttributes);
            // Register the channel with the system

            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(106, builder.build());

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.v("akram","fcmToken "+s);
        StaticSharedpreference.saveInfoForgot("fcmToken",s,getApplicationContext());
    }
}
