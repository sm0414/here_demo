package com.vincent.gps_product;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    public void onCreate(){
        super.onCreate();
    }
    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null){
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    public void sendNotification(String message){

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("lcc","lcc01",NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);

        Intent it = new Intent(this,StartView.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //FLAG_ACTIVITY_CLEAR_TASK 是畫面全部清掉
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,it,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"lcc")
                .setSmallIcon(R.mipmap.ic_launcher).setTicker("FCM").setContentTitle(message).setContentIntent(pendingIntent);

        manager.notify(0,builder.build());
    }

    public  void onMessageSent(String s){
        super.onMessageSent(s);
    }
}