package com.patricknicolosi.smartkeys.receivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.patricknicolosi.smartkeys.R;
import com.patricknicolosi.smartkeys.activities.MainActivity;


public class MyBroadcastReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationChannel notificationChannel = new NotificationChannel("smartKeysResponseChannel", "SmartKeys Response", NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableVibration(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);

        Intent onTapNotificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent =  PendingIntent.getActivity(context, 1, onTapNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(context, "smartKeysResponseChannel").setContentTitle("SmartKey Response").setContentText(intent.getStringExtra("notificationBody")).setSmallIcon(R.drawable.icon_logo).setContentIntent(pendingIntent).build();
        notificationManager.notify(1, notification);

    }
}
