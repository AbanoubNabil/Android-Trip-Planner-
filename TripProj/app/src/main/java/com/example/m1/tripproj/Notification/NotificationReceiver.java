package com.example.m1.tripproj.Notification;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.m1.tripproj.Alarm.MyAlertDialog;
import com.example.m1.tripproj.DataBase.DatabaseAdapter;
import com.example.m1.tripproj.Models.Trip;

public class NotificationReceiver extends BroadcastReceiver {

    DatabaseAdapter databaseAdapter;
    int notificationClickedFlag, notificationCancelledFlag;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("test","here in receiver ");

        databaseAdapter = new DatabaseAdapter(context);
        Trip trip = (Trip) intent.getSerializableExtra("tripObj");

        notificationClickedFlag = intent.getIntExtra("NotifClickedFlag", 0);
        notificationCancelledFlag = intent.getIntExtra("NotifCancelledFlag", 0);
        Log.i("test", " clicked" + notificationClickedFlag + "");
        Log.i("test", " cancelled" + notificationCancelledFlag + "");

        if(notificationClickedFlag == 1) {
            Intent mapIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + String.valueOf(trip.getStartLatitude()) +
                                ", " + String.valueOf(trip.getStartLongitude()) + "&daddr=" + String.valueOf(trip.getEndLatitude()) +
                                ", " + String.valueOf(trip.getEndLongitude())));
            mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mapIntent);
            databaseAdapter.setTripStatus(trip, "done");
            Log.i("test", "clicked notif");
        }

        else if(notificationCancelledFlag == 2){
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(trip.getId());
            databaseAdapter.setTripStatus(trip, "cancelled");
            Log.i("test", "delete notif");
        }

        else {
            Log.i("test", "nothing");
        }

    }
}
