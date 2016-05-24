package com.example.cristianmmuresan.challengeme.ui.record;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristianmmuresan.challengeme.Globals;
import com.example.cristianmmuresan.challengeme.R;
import com.example.cristianmmuresan.challengeme.data.Activity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.ParseObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecordActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final LatLng WP1 = new LatLng(46.764122, 23.549852);
    private static final LatLng WP2 = new LatLng(46.765061, 23.553379);
    private static final LatLng WP3 = new LatLng(46.766588, 23.556591);
    private static final LatLng WP4 = new LatLng(46.767343, 23.568060);
    private static final LatLng WP5 = new LatLng(46.770049, 23.574389);
    private static final LatLng WP6 = new LatLng(46.767503, 23.575273);
    private static final LatLng WP7 = new LatLng(46.768770, 23.580127);
    private static final LatLng WP8 = new LatLng(46.769685, 23.583204);
    private static final LatLng WP9 = new LatLng(46.769499, 23.585242);
    private static final LatLng LOWER_MARASTI = new LatLng(46.7772063, 23.6158486);
    private static final int FINE_LOCATION_REQUEST = 200;
    private static final int COARSE_LOCATION_REQUEST = 201;

    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLng mOrigin = null;
    private LatLng mDest = null;
    private ArrayList<LatLng> mWaypoints;
    private ArrayList<LatLng> mAllWaypoints;
    private TextView stopBtn;
    private TextView saveBtn;
    private TextView timerTv;
    private long startTime = 0L;
    private long timeInMillis = 0L;
    private long stopTime = 0L;
    private Handler customHandler = new Handler();
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        timerTv = (TextView) findViewById(R.id.timer);
        stopBtn = (TextView) findViewById(R.id.stop_activity);
        saveBtn = (TextView) findViewById(R.id.save_activity);
        stopBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        mWaypoints = new ArrayList<>();
        mAllWaypoints = new ArrayList<>();

        setupGoogleApiClientIfNeeded();
        connectGoogleApiClient();

        setUpMapIfNeeded();

        checkLocationPermission();

        setupTimer();
    }

    private void setupGoogleApiClientIfNeeded() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void connectGoogleApiClient() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    private void setUpMapIfNeeded() {
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
        }
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_REQUEST);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, COARSE_LOCATION_REQUEST);
        }
    }

    private void setupTimer() {
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMillis = SystemClock.uptimeMillis() - startTime;

            int secs = (int) (timeInMillis / 1000);
            int mins = secs / 60;
            secs = secs % 60;

            if (secs % 10 == 0 && secs != 0) {
                LatLng waypoint = getLocation();
                mAllWaypoints.add(waypoint);

                Log.d("ADD WAYPOINT", waypoint.latitude
                        + "; " + waypoint.longitude);
            }

            String timeText = mins + ":" + String.format("%02d", secs);
            timerTv.setText(timeText);
            customHandler.postDelayed(this, 1000);
        }
    };

    private String getMapsApiDirectionsUrl() {
        getFinalWaypoints();

        String waypoints = getWaypointsString();
        String OriDest = "&origin=" + mOrigin.latitude + "," + mOrigin.longitude +
                "&destination=" + mDest.latitude + "," + mDest.longitude;
        String travelMode = "&mode=walking";
        String avoid = "&avoid=highways";
        String params = travelMode + avoid + OriDest + waypoints;
        String output = "json";

        return "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
    }

    private void getFinalWaypoints() {
        if(mAllWaypoints.size()>6) {
            int nth = mAllWaypoints.size() / 6;
            for (int i = 0; i < mAllWaypoints.size(); i++) {
                if (i % nth == 0 && i > 0)
                    mWaypoints.add(mAllWaypoints.get(i));
            }
        }
        else
            mWaypoints.addAll(mAllWaypoints);
        mAllWaypoints.clear();
    }

    private String getWaypointsString() {
        String waypoints = "&waypoints=optimize:true";
        for (LatLng latLng : mWaypoints) {
            waypoints += "|" + latLng.latitude + "," + latLng.longitude;
        }
        return waypoints;
    }

    private void addMarkers() {
        if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions().position(mDest)
                    .title("DESTINATION").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_finish)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case FINE_LOCATION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mOrigin == null) {
                        mOrigin = getLocation();
                    }
                }
            }
            case COARSE_LOCATION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mOrigin == null) {
                        mOrigin = getLocation();
                    }
                }
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mOrigin == null) {
            mOrigin = getLocation();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigin, 13));
            if (googleMap != null)
                googleMap.addMarker(new MarkerOptions().position(mOrigin)
                        .title("ORIGIN").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start)));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stop_activity:
                stopTime = timeInMillis;
                customHandler.removeCallbacks(updateTimerThread);
                mDest = getLocation();
//                mDest = new LatLng(46.770250, 23.588309);
                stopBtn.setVisibility(View.GONE);
                saveBtn.setVisibility(View.VISIBLE);
                computePath();
                break;
            case R.id.save_activity:
                getActivityTitle();
                break;
        }
    }

    private void saveActivity() {
        ParseObject activity  = new ParseObject("Activity");
        activity.put("user", Globals.iUser.getEmail());
        if(mActivityTitle != null)
            activity.put("title",mActivityTitle);
        activity.put("time", Activity.getActivityTime(stopTime));
        activity.saveInBackground();

        Toast.makeText(RecordActivity.this, "Activity successfully saved!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void getActivityTitle() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Enter title");

        final EditText editText = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        alertDialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mActivityTitle = editText.getText().toString();
                saveActivity();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mActivityTitle = null;
            }
        });
        alertDialog.show();
    }

    private void computePath() {
        String url = getMapsApiDirectionsUrl();
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDest, 15));
        addMarkers();
    }

    @SuppressWarnings("ResourceType")
    public LatLng getLocation() {

        if (mGoogleApiClient.isConnected()) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            return latLng;
        } else return new LatLng(0, 0);
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(4);
                polyLineOptions.color(getResources().getColor(R.color.colorAccent));
            }

            googleMap.addPolyline(polyLineOptions);
        }
    }

}
