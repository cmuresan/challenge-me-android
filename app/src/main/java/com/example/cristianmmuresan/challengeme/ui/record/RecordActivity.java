package com.example.cristianmmuresan.challengeme.ui.record;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.cristianmmuresan.challengeme.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecordActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final LatLng LOWER_MARASTI = new LatLng(46.7772063,23.6158486);
    private static final LatLng TIMES_SQUARE = new LatLng(40.7577, -73.9857);
    private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);
    private static final LatLng ORIGIN = new LatLng(40.712143, -73.998585);
    private static final LatLng DEST = new LatLng(40.912143, -73.999585);
    private static final int FINE_LOCATION_REQUEST = 200;
    private static final int COARSE_LOCATION_REQUEST = 201;

    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLng mOrigin = null;
    private LatLng mDest = null;
    private ArrayList<LatLng> waypoints;
    private TextView stopBtn;
    private TextView timerTv;
    private long startTime = 0L;
    private long timeInMillis = 0L;
    private long stopTime = 0L;
    private Handler customHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        timerTv = (TextView)findViewById(R.id.timer);
        stopBtn = (TextView) findViewById(R.id.stop_activity);
        stopBtn.setOnClickListener(this);

        setupGoogleApiClientIfNeeded();
        connectGoogleApiClient();

        checkPermission();

        setupTimer();

        setUpMapIfNeeded();
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_LOCATION_REQUEST);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},COARSE_LOCATION_REQUEST);
        }
    }

    private void connectGoogleApiClient() {
        if(mGoogleApiClient!=null)
            mGoogleApiClient.connect();
    }

    private void setupTimer() {
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMillis = SystemClock.uptimeMillis() - startTime;

            if(timeInMillis%6000 == 0){
                waypoints.add(getLocation());
                Log.d("ADD WAYPOINT",waypoints.get(waypoints.size()-1).latitude
                        + "; " + waypoints.get(waypoints.size()-1).latitude);

            }

            int secs = (int)(timeInMillis/1000);
            int mins = secs / 60;
            secs = secs % 60;
            String timeText = mins+":"+String.format("%02d",secs);
            timerTv.setText(timeText);
            customHandler.postDelayed(this,0);
        }
    };

    private void setupGoogleApiClientIfNeeded() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void setUpMapIfNeeded() {
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
//            if (googleMap != null) {
//                MarkerOptions options = new MarkerOptions();
//                options.position(LOWER_MANHATTAN);
//                options.position(BROOKLYN_BRIDGE);
//                options.position(TIMES_SQUARE);
//                googleMap.addMarker(options);
//            }
        }
    }

    private String getMapsApiDirectionsUrl() {
//        String waypoints = "&waypoints=optimize:true|" + LOWER_MARASTI.latitude + "," + LOWER_MARASTI.longitude;
//                + LOWER_MANHATTAN.latitude + "," + LOWER_MANHATTAN.longitude
//                + "|" + "|" + BROOKLYN_BRIDGE.latitude + ","
//                + BROOKLYN_BRIDGE.longitude + "|" + TIMES_SQUARE.latitude + ","
//                + TIMES_SQUARE.longitude;

        String OriDest = "origin=" + mOrigin.latitude + "," + mOrigin.longitude + "&destination=" + mDest.latitude + "," + mDest.longitude;
//        String APIKey = "&key=AIzaSyDftl65Cbl6cjAzSmba6UjGA9tvPZaleRw";
//        String travelMode = "bicycling";
//        String avoid = "highways";
        String params = OriDest; //+ waypoints;
        String output = "json";
        return "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
    }

    private void addMarkers() {
        if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions().position(mOrigin)
                    .title("ORIGIN"));
            googleMap.addMarker(new MarkerOptions().position(mDest)
                    .title("DESTINATION"));
        }
    }

//    private void addLines() {
//        googleMap.addPolyline((new PolylineOptions()).add(TIMES_SQUARE, BROOKLYN_BRIDGE,
//                LOWER_MANHATTAN, TIMES_SQUARE).width(6).color(getResources().getColor(R.color.colorAccent)).geodesic(true));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOWER_MANHATTAN, 13));
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case FINE_LOCATION_REQUEST:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(mOrigin == null){
                        mOrigin = getLocation();
                    }
                }
            }
            case COARSE_LOCATION_REQUEST:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(mOrigin == null){
                        mOrigin = getLocation();
                    }
                }
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(mOrigin == null){
            mOrigin = getLocation();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigin,13));
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
        switch (v.getId()){
            case R.id.stop_activity:
                stopTime = timeInMillis;
                customHandler.removeCallbacks(updateTimerThread);
                mDest = getLocation();
//                mDest = new LatLng(mDest.latitude+0.20,mDest.longitude);
                computePath();
                break;
        }
    }

    private void computePath() {
        String url = getMapsApiDirectionsUrl();
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDest,13));
        addMarkers();
    }

    @SuppressWarnings("ResourceType")
    public LatLng getLocation(){

        if(mGoogleApiClient.isConnected()) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            return latLng;
        }
        else return new LatLng(0,0);
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
