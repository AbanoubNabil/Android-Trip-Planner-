package com.example.m1.tripproj.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.m1.tripproj.DataBase.DatabaseAdapter;
import com.example.m1.tripproj.Models.Trip;
import com.example.m1.tripproj.R;

import static com.example.m1.tripproj.R.color.green;

public class DetailScreen extends AppCompatActivity {

    Toolbar toolbar;
    Button editBtn, startBtn, saveChangesBtn;
    EditText tripName, tripStartPoint, tripEndPoint, tripDate, tripTime, tripNotes, tripDirection;
    Trip trip;
    DatabaseAdapter databaseAdapter = new DatabaseAdapter(DetailScreen.this);
    Spinner tripStatusSpinner;
    String tripStatus;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);

        initUIComponents();
        toolbar.setTitle("Trip Details");
        toolbar.setBackgroundColor(Color.parseColor("#4da6ff"));

        saveChangesBtn.setEnabled(false);
        trip = (Trip) getIntent().getSerializableExtra("tripObj");
        tripStatus = trip.getStatus();
        setValues();
        disableEditTexts();

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEditTexts();
                saveChangesBtn.setEnabled(true);
                saveChangesBtn.setBackgroundColor(Color.parseColor("#4da6ff"));
            }
        });

        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableEditTexts();
                saveChangesBtn.setEnabled(false);
                saveChangesBtn.setBackgroundColor(Color.parseColor("#4da6ff"));

                setTripValues();
                setValues();
                databaseAdapter.editTrip(trip);
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + trip.getStartLongitude() +
                                ", " + trip.getEndLongitude() + "&daddr=" + trip.getStartLatitude() + ", " + trip.getEndLatitude()));
                startActivity(intent);
            }
        });

        tripStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tripStatus = (String) parent.getItemAtPosition(position);
                Log.i("tripStatus: ", tripStatus);
                databaseAdapter.setTripStatus(trip, tripStatus);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void initUIComponents() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editBtn = (Button) findViewById(R.id.tripEdit);
        startBtn = (Button) findViewById(R.id.tripStart);
        saveChangesBtn = (Button) findViewById(R.id.saveChanges);
        tripName = (EditText) findViewById(R.id.tripName);
        tripStartPoint = (EditText) findViewById(R.id.tripStartPoint);
        tripEndPoint = (EditText) findViewById(R.id.tripEndPoint);
        tripDate = (EditText) findViewById(R.id.tripDate);
        tripTime = (EditText) findViewById(R.id.tripTime);
        tripNotes = (EditText) findViewById(R.id.tripNotes);
        tripDirection = (EditText) findViewById(R.id.tripDirection);
        tripStatusSpinner = (Spinner) findViewById(R.id.tripStatus);
    }

    public void setValues() {
        tripName.setText(trip.getTripName());
        tripStartPoint.setText(trip.getStartPoint());
        tripEndPoint.setText(trip.getEndPoint());
        tripDate.setText(trip.getDate());
        tripTime.setText(trip.getTime());
        tripNotes.setText(trip.getNotes());
        tripDirection.setText(trip.getDirection());

        if(tripStatus.equalsIgnoreCase("upcoming"))
            tripStatusSpinner.setSelection(0);
        else if(tripStatus.equalsIgnoreCase("done"))
            tripStatusSpinner.setSelection(1);
        else
            tripStatusSpinner.setSelection(2);
    }

    public void disableEditTexts() {
        tripName.setEnabled(false);
        tripStartPoint.setEnabled(false);
        tripEndPoint.setEnabled(false);
        tripDate.setEnabled(false);
        tripTime.setEnabled(false);
        tripNotes.setEnabled(false);
        tripDirection.setEnabled(false);
        tripStatusSpinner.setEnabled(false);
    }

    public void enableEditTexts() {
        tripName.setEnabled(true);
        tripName.setFocusable(true);
        tripStartPoint.setEnabled(true);
        tripStartPoint.setFocusable(true);
        tripEndPoint.setEnabled(true);
        tripEndPoint.setFocusable(true);
        tripDate.setEnabled(true);
        tripDate.setFocusable(true);
        tripTime.setEnabled(true);
        tripTime.setFocusable(true);
        tripNotes.setEnabled(true);
        tripNotes.setFocusable(true);
        tripDirection.setEnabled(true);
        tripDirection.setFocusable(true);
        tripStatusSpinner.setEnabled(true);
        tripStatusSpinner.setFocusable(true);
    }

    public void setTripValues() {
        trip.setTripName(tripName.getText().toString());
        trip.setStartPoint(tripStartPoint.getText().toString());
        trip.setEndPoint(tripEndPoint.getText().toString());
        trip.setDate(tripDate.getText().toString());
        trip.setTime(tripTime.getText().toString());
        trip.setDirection(tripDirection.getText().toString());
        trip.setNotes(tripNotes.getText().toString());
        trip.setStatus(tripStatus);
    }
}
