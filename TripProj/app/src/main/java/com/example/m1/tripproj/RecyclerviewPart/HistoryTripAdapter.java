package com.example.m1.tripproj.RecyclerviewPart;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.m1.tripproj.Activities.AddTripScreen;
import com.example.m1.tripproj.Activities.DetailScreen;
import com.example.m1.tripproj.Activities.Home;
import com.example.m1.tripproj.DataBase.DatabaseAdapter;
import com.example.m1.tripproj.Models.Trip;
import com.example.m1.tripproj.R;

import java.util.ArrayList;

public class HistoryTripAdapter extends RecyclerView.Adapter <ViewHolder> {

    AppCompatActivity activity;
    private ArrayList<Trip> trips;
    private DatabaseAdapter databaseAdapter;
    public static final String NEW_START_POINT = "newStartPoint";
    public static final String NEW_END_POINT = "newEndPoint";
    public static final String NEW_START_LATITUDE = "newStartLatitude";
    public static final String NEW_START_LONGITUDE = "newStartLongitude";
    public static final String NEW_END_LATITUDE = "newEndLatitude";
    public static final String NEW_END_LONGITUDE = "newEndLatitude";

    public HistoryTripAdapter(AppCompatActivity activity, ArrayList<Trip> trips) {
        this.activity = activity;
        this.trips = trips;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Trip trip = trips.get(position);

        holder.startBtn.setText("Show Map");
        holder.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open Map...
                Log.i("startLatitude: ", String.valueOf(trip.getStartLatitude()));
                Log.i("endLatitude: ", String.valueOf(trip.getEndLatitude()));
                Log.i("startLongitude: ", String.valueOf(trip.getStartLongitude()));
                Log.i("endLongitude: ", String.valueOf(trip.getEndLongitude()));
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + String.valueOf(trip.getStartLatitude()) +
                                ", " + String.valueOf(trip.getStartLongitude()) + "&daddr=" + String.valueOf(trip.getEndLatitude()) +
                                ", " + String.valueOf(trip.getEndLongitude())));
                activity.startActivity(intent);
            }
        });

        String trip_direction = trip.getDirection();
        Log.i("dir: ", trip_direction);
        if(trip_direction.equalsIgnoreCase("one way")) {
            holder.reverseDirection.setVisibility(View.GONE);
        }
        else if(trip_direction.equalsIgnoreCase("round trip")) {
            holder.reverseDirection.setVisibility(View.VISIBLE);
            holder.reverseDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, AddTripScreen.class);
                    intent.putExtra(NEW_START_POINT, trip.getEndPoint());
                    intent.putExtra(NEW_END_POINT, trip.getStartPoint());
                    intent.putExtra(NEW_START_LATITUDE, trip.getEndLatitude());
                    intent.putExtra(NEW_START_LONGITUDE, trip.getEndLongitude());
                    intent.putExtra(NEW_END_LATITUDE, trip.getStartLatitude());
                    intent.putExtra(NEW_END_LONGITUDE, trip.getStartLongitude());

                    Log.i("newStartPoint: ", trip.getEndPoint());
                    Log.i("newEndPoint: ", trip.getStartPoint());
                    Log.i("newStartLatitude: ", trip.getEndLatitude() + "");
                    Log.i("newStartLongitude: ", trip.getEndLongitude() + "");
                    Log.i("newEndLatitude: ", trip.getStartLatitude() + "");
                    Log.i("newEndLongitude: ", trip.getStartLongitude() + "");
                    activity.startActivity(intent);
                }
            });
        }

        holder.timeTxtView.setText(trip.getTime());
        holder.tripIcon.setImageResource(R.drawable.done_trips);
        holder.tripNameTxtView.setText(trip.getTripName().trim());
        holder.tripNameTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailScreen.class);
                intent.putExtra("tripObj", trips.get(position));
                activity.startActivity(intent);
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Delete Trip ?")
                        .setCancelable(false)
                        .setPositiveButton("CONFIRM",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Log.i("pos: ", position + "");
                                        Log.i("item: ", trips.get(position).getTripName());
                                        databaseAdapter = new DatabaseAdapter(activity);
                                        databaseAdapter.deleteTrip(trips.get(position));
                                        Log.i("test","ttttttttt" + trips.get(position).getId());
                                        trips.remove(position);
                                        notifyDataSetChanged();

                                        if(getItemCount() == 0) {
                                            Home home = (Home)activity;
                                            home.setVisibilityForNoItems(true);
                                        }
                                    }
                                })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {}
                        });

                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }
}
