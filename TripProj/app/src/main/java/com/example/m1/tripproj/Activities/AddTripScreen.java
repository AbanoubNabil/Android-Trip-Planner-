package com.example.m1.tripproj.Activities;


import android.app.DatePickerDialog;
import 	android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.m1.tripproj.Alarm.MyAlertDialog;
import com.example.m1.tripproj.DataBase.DatabaseAdapter;
import com.example.m1.tripproj.Models.Trip;
import com.example.m1.tripproj.R;
import com.example.m1.tripproj.RecyclerviewPart.HistoryTripAdapter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

public class AddTripScreen extends AppCompatActivity {

    Toolbar toolbar;
    EditText tripNameEditTxt, dateEditTxt, timeEditTxt, notesEditTxt;
    Button addBtn ;
    RadioGroup tripTypeRadioGr;
    private DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);
    String startPointName = "", endPointName = "";
    LatLng queriedLocation ;
    Double startLongitude, startLatitude, endLongitude, endLatitude, newStartLatitude, newEndLatitude, newStartLongitude, newEndLongitude;
    Trip trip ;
    final Calendar dateAndTimeCalendar = Calendar.getInstance();
    int day, month, yearVal, hours, minutes, userId;
    String newStartPoint = "", newEndPoint = "";
    PlaceAutocompleteFragment startPoint, endPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip_screen);

        initUIComponents();
        toolbar.setTitle("Add New Trip");
        toolbar.setBackgroundColor(Color.parseColor("#4da6ff"));

        startPoint =  (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.startPoint);
        endPoint = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.endPoint);

        // to prevent pressing enter in the edit text
        tripNameEditTxt.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    return true;
                }
                return false;
            }
        });

        newStartPoint = getIntent().getStringExtra(HistoryTripAdapter.NEW_START_POINT);
        newEndPoint = getIntent().getStringExtra(HistoryTripAdapter.NEW_END_POINT);
        newStartLatitude = getIntent().getDoubleExtra(HistoryTripAdapter.NEW_START_LATITUDE, 0.0);
        newEndLatitude = getIntent().getDoubleExtra(HistoryTripAdapter.NEW_END_LATITUDE, 0.0);
        newStartLongitude = getIntent().getDoubleExtra(HistoryTripAdapter.NEW_START_LONGITUDE, 0.0);
        newEndLongitude = getIntent().getDoubleExtra(HistoryTripAdapter.NEW_END_LONGITUDE, 0.0);

        userId = getIntent().getIntExtra(LoginActivity.USER_ID , 0);

        if(newStartLatitude == 0.0) {

            //  Auto complete fragment for start point
            startPoint.setHint("start point");
            startPoint.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    Log.i("place", "Place: " + place.getName());
                    startPointName = (String) place.getAddress();
                    queriedLocation = place.getLatLng();
                    startLongitude = queriedLocation.longitude ;
                    startLatitude  = queriedLocation.latitude ;
                }
                @Override
                public void onError(Status status) {
                    Toast.makeText(AddTripScreen.this, "There's no internet Connection", Toast.LENGTH_LONG).show();
                }
            });

            // Auto complete fragment for end point
            endPoint.setHint("end point");
            endPoint.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    Log.i("place", "Place: " + place.getName());
                    endPointName = (String) place.getAddress();
                    queriedLocation = place.getLatLng();
                    endLongitude = queriedLocation.longitude ;
                    endLatitude = queriedLocation.latitude ;
                }

                @Override
                public void onError(Status status) {
                    Log.i("error", "An error occurred: " + status);
                }
            });
        }

        else {
            startPointName = newStartPoint;
            endPointName = newEndPoint;
            startLatitude  = newStartLatitude ;
            endLatitude = newEndLatitude;
            startLongitude = newStartLongitude ;
            endLongitude = newEndLongitude;

            startPoint.setText(startPointName);
            endPoint.setText(endPointName);
        }

       final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateAndTimeCalendar.set(Calendar.YEAR, year);
                dateAndTimeCalendar.set(Calendar.MONTH, monthOfYear);
                dateAndTimeCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                day = dayOfMonth;
                month = monthOfYear;
                yearVal = year;
                dateEditTxt.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
            }
        };

        dateEditTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddTripScreen.this, date,
                        dateAndTimeCalendar.get(Calendar.YEAR),
                        dateAndTimeCalendar.get(Calendar.MONTH),
                        dateAndTimeCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dateAndTimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                dateAndTimeCalendar.set(Calendar.MINUTE, minute);
                hours = hourOfDay;
                minutes = minute;
                timeEditTxt.setText(hourOfDay + ":" + minute);
            }
        };
        timeEditTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddTripScreen.this, time,
                        dateAndTimeCalendar.get(Calendar.HOUR_OF_DAY),
                        dateAndTimeCalendar.get(Calendar.MINUTE), true)
                        .show();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((tripNameEditTxt.getText().toString().equals("")) || (startPointName.equals("")) || (endPointName.equals("")) ||
                        (dateEditTxt.getText().toString().equals("")) || (timeEditTxt.getText().toString().equals("")) ||
                        (tripTypeRadioGr.getCheckedRadioButtonId() == -1) || (notesEditTxt.getText().toString().equals(""))) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Please Fill The Fields!")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {}
                            });
                    builder.show();
                }

                else {
                    trip = new Trip();
                    trip.setTripName(tripNameEditTxt.getText().toString());
                    trip.setStartPoint(startPointName);
                    trip.setEndPoint(endPointName);
                    trip.setNotes(notesEditTxt.getText().toString());
                    trip.setDate(dateEditTxt.getText().toString());
                    trip.setTime(timeEditTxt.getText().toString());
                    trip.setStartLatitude(startLatitude);
                    trip.setEndLatitude(endLatitude);
                    trip.setStartLongitude(startLongitude);
                    trip.setEndLongitude(endLongitude);
                    trip.setUserId(userId);

                    int selectedRadioBtnId = tripTypeRadioGr.getCheckedRadioButtonId();
                    RadioButton selectedRadioBtn = (RadioButton) findViewById(selectedRadioBtnId);
                    trip.setDirection(selectedRadioBtn.getText().toString());
                    Log.i("selected radio: ", selectedRadioBtn.getText().toString());

                    tripNameEditTxt.setText("");
                    dateEditTxt.setText("");
                    timeEditTxt.setText("");
                    notesEditTxt.setText("");
                    tripTypeRadioGr.setSelected(false);
                    startPoint.setText("");
                    endPoint.setText("");
                    tripTypeRadioGr.clearCheck();

                    databaseAdapter.addTrip(trip);
                    Toast.makeText(AddTripScreen.this, "trip added", Toast.LENGTH_SHORT).show();
                    dateAndTimeCalendar.set(yearVal, month, day, hours, minutes, 0);
                    int tripId = databaseAdapter.getTripId(trip);
                    startAlert(tripId);
                }
            }
        });

    }

    public void initUIComponents() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tripNameEditTxt = (EditText) findViewById(R.id.tripNameEditTxt);
        notesEditTxt = (EditText) findViewById(R.id.notesEditTxt);
        dateEditTxt = (EditText) findViewById(R.id.dateEditTxt);
        timeEditTxt = (EditText) findViewById(R.id.timeEditTxt);
        addBtn = (Button) findViewById(R.id.addBtn);
        tripTypeRadioGr = (RadioGroup) findViewById(R.id.tripTypeRadioGr);
    }

    public void startAlert(int tripId) {
        Intent intent = new Intent(AddTripScreen.this, MyAlertDialog.class);
        intent.putExtra("id_trip",tripId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,tripId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,dateAndTimeCalendar.getTimeInMillis(), pendingIntent);
        Log.i("test","/////////" + dateAndTimeCalendar.getTimeInMillis() + "hhhhhhhhhhhhhhhh " + tripId);
    }

}
