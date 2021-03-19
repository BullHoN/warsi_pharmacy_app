package com.avit.warsipharmacy.ui.success;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.avit.warsipharmacy.R;

public class SuccessFragment extends Fragment {

    private Button callToActionButton;
    private int NOTIFICATION_ID = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_success, container, false);

        // TODO: SHOW NOTIFICATION
        showNotification("We Got Your Prescription!!","Our team will contact you soon");

        // TODO: CHECK FOR THE TYPE OF MESSAGE TO SET THE CALL TO ACTION
        // TODO: IF ORDER THAN SHOW ORDER OTHERWISE CONTINUE SHOPPING

        callToActionButton = root.findViewById(R.id.call_to_action_button);
        callToActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                    .popBackStackImmediate();
            }
        });

        return root;
    }

    private void showNotification(String title,String body){
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "WarsiPharmacy_updates_channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "WarsiPharmacyUpdate_channel";
            String Description = "This is my WarsiPharmacy Updates Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.setShowBadge(true);
            mChannel.enableVibration(true);

            notificationManager.createNotificationChannel(mChannel);
        }

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext(),CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(soundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true);

        if(NOTIFICATION_ID > 1073741824){
            NOTIFICATION_ID = 0;
        }

        notificationManager.notify(NOTIFICATION_ID,notificationBuilder.build());

    }

}