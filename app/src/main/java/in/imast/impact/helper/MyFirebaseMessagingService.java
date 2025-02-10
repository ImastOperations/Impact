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
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "Impact Loyalty";
    private static final String CHANNEL_NAME = "Impact Loyalty";
    private static final String CHANNEL_DESCRIPTION = "Channel for Impact Loyalty notifications";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (!remoteMessage.getData().isEmpty()) {
            handleDataPayload(remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            handleNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody(),
                    remoteMessage.getNotification().getImageUrl() != null ? remoteMessage.getNotification().getImageUrl().toString() : null
            );
        }
    }

    private void handleDataPayload(Map<String, String> data) {
        String title = data.get("title");
        String body = data.get("body");
        String imageUrl = data.get("image_url");

        if (imageUrl != null && !imageUrl.isEmpty()) {
            createNotificationWithImage(title, body, imageUrl);
        } else {
            createNotificationWithoutImage(title, body);
        }
    }

    private void handleNotification(String title, String body, String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            createNotificationWithImage(title, body, imageUrl);
        } else {
            createNotificationWithoutImage(title, body);
        }
    }

    private void createNotificationWithoutImage(String title, String message) {
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setContentIntent(createPendingIntent())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        Log.v("NotificationIntent", "Testing intent: " + title);
        Log.v("NotificationIntent", "Testing intent: " + message);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(106, builder.build());
        }
    }

    private void createNotificationWithImage(String title, String message, String imageUrl) {
        createNotificationChannel();

        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            Log.e("NotificationLogs", "Error downloading image for notification: " + e.getMessage());
        }

        if (bitmap != null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.logo)
                    .setAutoCancel(true)
                    .setContentIntent(createPendingIntent())
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(106, builder.build());
            }
        } else {
            createNotificationWithoutImage(title, message);
        }
    }

    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("isFromNotification", true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESCRIPTION);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        StaticSharedpreference.saveInfoForgot("fcmToken", s, getApplicationContext());
        Log.v("fcmToken", s);
    }
}