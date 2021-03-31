package com.avit.warsipharmacy.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.avit.warsipharmacy.HomeActivity;
import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.db.SharedPrefNames;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class NotificationService extends FirebaseMessagingService {

    public static int NOTIFICATION_ID = 1;
    private SharedPreferences sharedPreferences;
    private String TAG = "NotificationService";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        sharedPreferences = getSharedPreferences(SharedPrefNames.SHARED_PREFRENCE_DATABASE_NAME,MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPrefNames.FCM_ID,s);

        editor.apply();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String,String> data = remoteMessage.getData();

        String title = data.get("title");
        String body = data.get("body");

        generateNotification(title,body);
    }

    private void generateNotification(String title,String body){

        Bundle bundle = new Bundle();
        bundle.putString("order_id","asfasf_safasfsaf");

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("notification",bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = "WarsiPharmacy_channel_02";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            CharSequence name = "WarsiPharmacy_channel";
            String Description = "This is my WarsiPharmacy Channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;


            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.setImportance(importance);
            mChannel.setShowBadge(false);
            mChannel.enableVibration(true);

            notificationManager.createNotificationChannel(mChannel);

        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent
                , PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);


        if(NOTIFICATION_ID > 1073741824){
            NOTIFICATION_ID = 0;
        }

        notificationManager.notify(NOTIFICATION_ID,notificationBuilder.build());
    }


}
