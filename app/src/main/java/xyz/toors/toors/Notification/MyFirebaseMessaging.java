package xyz.toors.toors.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import xyz.toors.toors.ChatActivity;
import xyz.toors.toors.Members;
import xyz.toors.toors.R;

import static android.content.ContentValues.TAG;
import static xyz.toors.toors.App.CHANNEL_ID;

public class MyFirebaseMessaging extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        Log.d(TAG, "From: " + remoteMessage.getFrom());


        super.onMessageReceived(remoteMessage);
        String msg = remoteMessage.getData().get("Message");
        String title = remoteMessage.getData().get("Title");
        String user = remoteMessage.getData().get("user");
        Log.d(TAG,"User  "+user);
        SharedPreferences preferences = getSharedPreferences("PREFS",MODE_PRIVATE);
        String currentUser = preferences.getString("currentUser","none");
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if(!currentUser.equals(user) ){
            Intent activityIntent = new Intent(this, Members.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(contentIntent)
                    .setSound(alarmSound)
                    .build();
            notificationManager.notify(0, notification);
       }


    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String user=remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title=remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent =new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int i=0;
        if(j>0)
        {
            i=j;
        }
        noti.notify(i,builder.build());
    }

}
