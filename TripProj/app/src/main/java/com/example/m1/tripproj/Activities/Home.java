package com.example.m1.tripproj.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m1.tripproj.DataBase.DatabaseAdapter;
import com.example.m1.tripproj.Models.Trip;
import com.example.m1.tripproj.Models.User;
import com.example.m1.tripproj.R;
import com.example.m1.tripproj.RecyclerviewPart.CancelledTripAdapter;
import com.example.m1.tripproj.RecyclerviewPart.HistoryTripAdapter;
import com.example.m1.tripproj.RecyclerviewPart.UpcomingTripAdapter;
import com.example.m1.tripproj.HelperClasses.SessionManager;

import java.util.ArrayList;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Trip> upcomingTrips = new ArrayList<Trip>();
    private ArrayList<Trip> historyTrips = new ArrayList<Trip>();
    private ArrayList<Trip> cancelledTrips = new ArrayList<Trip>();
    private UpcomingTripAdapter upcomingTripAdapter;
    private HistoryTripAdapter historyTripAdapter;
    private CancelledTripAdapter cancelledTripAdapter;
    private DatabaseAdapter databaseAdapter =  new DatabaseAdapter(Home.this);
    private SessionManager session;
    boolean doubleBackToExitPressedOnce = false;
    private TextView noItemsTxtView;
//    private int userId;
    private User user ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        noItemsTxtView = (TextView) findViewById(R.id.no_items);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        toolbar.setTitle("Home");
        noItemsTxtView.setVisibility(View.GONE);

//        userId = getIntent().getIntExtra(LoginActivity.USER_ID , 0);
        user= (User) getIntent().getSerializableExtra("user");
        upcomingTrips = databaseAdapter.getUpcomingTrips(user.getId());
        if(upcomingTrips.size() == 0) {
            setVisibilityForNoItems(true);
        }
        else {
            setVisibilityForNoItems(false);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            upcomingTripAdapter = new UpcomingTripAdapter(this, upcomingTrips);
            recyclerView.setAdapter(upcomingTripAdapter);
            upcomingTripAdapter.notifyDataSetChanged();
        }

        /*recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.CustOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(HomeScreen.this, DetailScreen.class);
                startActivity(intent);
            }
        }));*/

        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#4da6ff"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, AddTripScreen.class);
                intent.putExtra(LoginActivity.USER_ID, user.getId());
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setVisibilityForNoItems(Boolean visibilityStatus) {
        if(visibilityStatus) {
            noItemsTxtView.setVisibility(View.VISIBLE);
            noItemsTxtView.setText("No Upcoming Trips Found");
        }
        else {
            noItemsTxtView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        toolbar.setTitle("Home");
        upcomingTrips = databaseAdapter.getUpcomingTrips(user.getId());

        if(upcomingTrips.size() == 0) {
            setVisibilityForNoItems(true);
        }
        else {
            setVisibilityForNoItems(false);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            upcomingTripAdapter = new UpcomingTripAdapter(this, upcomingTrips);
            recyclerView.setAdapter(upcomingTripAdapter);
            upcomingTripAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        toolbar.setTitle("Home");
        upcomingTrips = databaseAdapter.getUpcomingTrips(user.getId());

        if(upcomingTrips.size() == 0) {
            setVisibilityForNoItems(true);
        }
        else {
            Log.i("test", upcomingTrips.size() + " list size");
            setVisibilityForNoItems(false);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            upcomingTripAdapter = new UpcomingTripAdapter(this, upcomingTrips);
            recyclerView.setAdapter(upcomingTripAdapter);
            upcomingTripAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        toolbar.setTitle("Home");
        if(id == R.id.nav_upcoming) {
            upcomingTrips = databaseAdapter.getUpcomingTrips(user.getId());

            if(upcomingTrips.size() == 0) {
                setVisibilityForNoItems(true);
            }
            else {
                setVisibilityForNoItems(false);
                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                upcomingTripAdapter = new UpcomingTripAdapter(this, upcomingTrips);
                recyclerView.setAdapter(upcomingTripAdapter);
                upcomingTripAdapter.notifyDataSetChanged();
            }
        }

        else if (id == R.id.nav_history)
        {
            toolbar.setTitle("History Trips");
            historyTrips = databaseAdapter.getHistoryTrips(user.getId());

            if(historyTrips.size() == 0) {
                setVisibilityForNoItems(true);
            }
            else {
                setVisibilityForNoItems(false);
                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                historyTripAdapter = new HistoryTripAdapter(this, historyTrips);
                recyclerView.setAdapter(historyTripAdapter);
                historyTripAdapter.notifyDataSetChanged();
            }
        }

        else if (id == R.id.nav_cancelled)
        {
            toolbar.setTitle("Cancelled Trips");
            cancelledTrips = databaseAdapter.getCancelledTrips(user.getId());

            if(cancelledTrips.size() == 0) {
                setVisibilityForNoItems(true);
            }
            else {
                setVisibilityForNoItems(false);
                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                cancelledTripAdapter = new CancelledTripAdapter(this, cancelledTrips);
                recyclerView.setAdapter(cancelledTripAdapter);
                cancelledTripAdapter.notifyDataSetChanged();
            }
        }

        else if (id == R.id.nav_profile)
        {
            Intent intent = new Intent(Home.this, Profile.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_share)
        {
            try {
                Intent intent = new Intent(Intent.ACTION_MEDIA_SHARED);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=Orion.Soft \n\n";
                intent.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(intent, "choose an app"));
            } catch(Exception e) {
                //e.toString();
            }
        }

        else if (id == R.id.nav_log_out)
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("are you want to Exit ?");
            dialog.setCancelable(false);

            dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(Home.this , LoginActivity.class);
                    startActivity(intent);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
