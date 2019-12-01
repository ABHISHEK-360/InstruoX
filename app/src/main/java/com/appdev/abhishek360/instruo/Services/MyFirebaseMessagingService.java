package com.appdev.abhishek360.instruo.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.appdev.abhishek360.instruo.EventDetailsActivity;
import com.appdev.abhishek360.instruo.HomeActivity;
import com.appdev.abhishek360.instruo.R;
import com.appdev.abhishek360.instruo.SplashActivity;
import com.appdev.abhishek360.instruo.UserProfileActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static int count = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//      Log.d("NOTIFICATION_SERVICE", "Notification Message TITLE: " + remoteMessage.getNotification().getTitle());
 //     Log.d("NOTIFICATION_SERVICE", "Notification Message BODY: " + remoteMessage.getNotification().getBody());
 //     Log.d("NOTIFICATION_SERVICE", "Notification Message DATA: " + remoteMessage.getData().toString());

        sendNotification(remoteMessage.getData().get("title"),
                remoteMessage.getData().get("body"), remoteMessage.getData());
    }

    private void sendNotification(String messageTitle, String messageBody, Map<String, String> row) {

        Intent intent;
        if(row.containsKey("eventCat")&&row.containsKey("eventKey")){
            String eventKey = row.get("eventKey");
            String eventCat = row.get("eventCat");
            intent = new Intent(this, EventDetailsActivity.class);
            intent.putExtra("tabCode",0);
            intent.putExtra(EventDetailsActivity.KEY_EVENT_ID, eventKey);
            intent.putExtra(EventDetailsActivity.KEY_EVENT_CAT, eventCat);
            intent.putExtra(EventDetailsActivity.KEY_POSTER_REF,"/EVENTS_INSTRUO/"+eventCat+"/"+eventKey+".png");
        }
        else {
            intent = new Intent(this, SplashActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "default_notification_channel_id";

        Notification.Builder notificationBuilder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationBuilder = new Notification.Builder(this, channelId)
                .setSmallIcon(R.drawable.logo_black_notification)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                    .setStyle(new Notification.BigTextStyle().bigText(messageBody))
                .setContentIntent(contentIntent);
        }
        else {
            notificationBuilder = new Notification.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_black_notification))
                .setSmallIcon(R.drawable.logo_black_notification)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                    .setStyle(new Notification.BigTextStyle().bigText(messageBody))
                .setContentIntent(contentIntent);
        }

        NotificationManager notificationManager =
                //NotificationManagerCompat.from(getApplicationContext());
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "GENERAL_NOTIFY_CHANNEL", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(count, notificationBuilder.build());
        count++;
    }
}
