package com.example.m1.tripproj.Alarm;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.m1.tripproj.DataBase.DatabaseAdapter;
import com.example.m1.tripproj.Models.Trip;
import com.example.m1.tripproj.Notification.NotificationReceiver;
import com.example.m1.tripproj.R;


public class MyAlertDialog extends AppCompatActivity implements View.OnClickListener{

    public Button yesBtn, noBtn, latterBtn;
    Ringtone ringTone;
    Dialog dialog;
    TextView tripName, tripNotes ;
    DatabaseAdapter databaseAdapter;
    Trip trip;
    int tripId;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        databaseAdapter = new DatabaseAdapter(this);
        tripId = getIntent().getIntExtra("id_trip", 0);
        trip = databaseAdapter.getTrip(tripId);
        Log.i("test","bbbbbbbb " + trip.getId());

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            ringTone = RingtoneManager.getRingtone(getApplicationContext(), notification);
            ringTone.play();
            vibrator = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_alarm);

        initViews();



        yesBtn.setOnClickListener(this);
        noBtn.setOnClickListener(this);
        latterBtn.setOnClickListener(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_start:
                dialog.dismiss();
                ringTone.stop();
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + String.valueOf(trip.getStartLatitude()) +
                                ", " + String.valueOf(trip.getStartLongitude()) + "&daddr=" + String.valueOf(trip.getEndLatitude()) +
                                ", " + String.valueOf(trip.getEndLongitude())));
                startActivity(intent);
                vibrator.cancel();
                databaseAdapter.setTripStatus(trip, "done");
                MyAlertDialog.this.finish();
                break;

            case R.id.btn_cancel:
                ringTone.stop();
                dialog.dismiss();
                vibrator.cancel();
                databaseAdapter.setTripStatus(trip, "cancelled");
                MyAlertDialog.this.finish();
                break;

            case R.id.btn_latter:
                ringTone.stop();
                dialog.dismiss();
                NotificationCompat.Builder builder =
                        (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.notification_icon)
                                .setContentTitle("Remember :" + trip.getTripName())
                                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.notification_icon))
                                .setContentText("Your notes :" + trip.getNotes())
                                .setAutoCancel(true);

                Intent clickedNotifIntent = new Intent(this, NotificationReceiver.class);
                clickedNotifIntent.putExtra("NotifClickedFlag", 1);
                clickedNotifIntent.putExtra("tripObj", trip);
                PendingIntent clickedNotifPendingIntent = PendingIntent.getBroadcast(
                        this, 1, clickedNotifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(clickedNotifPendingIntent);

                Log.i("test","gggggggggggggggggggggggggg " + trip.getId());

                Intent cancelledNotifIntent = new Intent(this, NotificationReceiver.class);
                cancelledNotifIntent.putExtra("NotifCancelledFlag", 2);
                cancelledNotifIntent.putExtra("tripObj", trip);
                PendingIntent cancelledNotifPendingIntent = PendingIntent.getBroadcast(
                        this, 2, cancelledNotifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setDeleteIntent(cancelledNotifPendingIntent);

                int notificationId = tripId;
                // Gets an instance of the NotificationManager service
                NotificationManager notificationMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                notificationMgr.notify(notificationId, builder.build());
                vibrator.cancel();
                MyAlertDialog.this.finish();
                break;

            default:
                break;
        }
    }

    public void initViews() {
        tripName = (TextView) dialog.findViewById(R.id.remember);
        tripNotes = (TextView) dialog.findViewById(R.id.remember_notes);
        yesBtn = (Button) dialog.findViewById(R.id.btn_start);
        noBtn = (Button) dialog.findViewById(R.id.btn_cancel);
        latterBtn = (Button) dialog.findViewById(R.id.btn_latter);
        tripName.setText("Remember that today is your trip \"" + trip.getTripName() + "\"");
        tripNotes.setText("Your Notes: " + trip.getNotes());
    }
}
