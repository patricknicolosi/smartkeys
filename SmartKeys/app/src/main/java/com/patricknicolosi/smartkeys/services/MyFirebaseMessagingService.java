package com.patricknicolosi.smartkeys.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.patricknicolosi.smartkeys.receivers.MyBroadcastReceiver;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        SharedPreferences preferences = getApplication().getSharedPreferences("smartKeysFirebaseToken",Context.MODE_PRIVATE);
        preferences.edit().putString("token",token).apply();
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Intent intent = new Intent();
        intent.setAction("com.patricknicolosi.smartkeys.NOTIFICATION_RESPONSE");
        intent.putExtra("notificationBody", remoteMessage.getNotification().getBody());
        sendBroadcast(intent);
        super.onMessageReceived(remoteMessage);
    }

}
