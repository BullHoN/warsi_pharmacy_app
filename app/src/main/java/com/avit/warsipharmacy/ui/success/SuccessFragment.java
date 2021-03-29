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
import android.widget.TextView;

import com.avit.warsipharmacy.R;
import com.avit.warsipharmacy.ui.orders.OrdersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SuccessFragment extends Fragment {

    private Button callToActionButton;
    private int NOTIFICATION_ID = 1;
    private String title,message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_success, container, false);
        TextView titleView = root.findViewById(R.id.title);

        callToActionButton = root.findViewById(R.id.call_to_action_button);
        Bundle bundle = getArguments();
        if(bundle != null){ // Order
            title = "We Got Your Order";
            message = "Our team will contact you soon";
            titleView.setText(title);
            callToActionButton.setText("View Order");

            callToActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager()
                            .popBackStack();

                    Fragment fragment = new OrdersFragment();

                    BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_notifications).setChecked(true);

                    openFragment(fragment,android.R.anim.fade_in,android.R.anim.fade_out);
                }
            });

        }else{ // Prescription
            title = "We Got Your Prescription";
            message = "Our team will contact you soon";
            titleView.setText(title);
            callToActionButton.setText("Continue shopping");

            callToActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager()
                            .popBackStackImmediate();
                }
            });
        }

        showNotification(title,message);

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

    private void openFragment(Fragment fragment,int anim1,int anim2){
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(anim1,anim2)
                .replace(R.id.nav_host_fragment,fragment)
                .addToBackStack(null)
                .commit();
    }

}