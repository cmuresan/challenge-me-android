package com.example.cristianmmuresan.challengeme.ui.map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.cristianmmuresan.challengeme.R;
import com.example.cristianmmuresan.challengeme.ui.home.ActivitiesAdapter;
import com.example.cristianmmuresan.challengeme.ui.record.ReadTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

public class MapActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private String mUrl;
    private LatLng mDest;
    private LatLng mOrigin;
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getFromBundle();

        setupGoogleApiClientIfNeeded();
        connectGoogleApiClient();

        setUpMapIfNeeded();
    }

    private void openMap() {
        ReadTask downloadTask = new ReadTask(this, googleMap);
        downloadTask.execute(mUrl);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDest, 15));
        if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions().position(mDest)
                    .title("DESTINATION").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_finish)));
        }
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

    private void getFromBundle() {
        mUrl = getIntent().getStringExtra(ActivitiesAdapter.URL);
        mDest = new Gson().fromJson(getIntent().getStringExtra(ActivitiesAdapter.DEST), LatLng.class);
        mOrigin = new Gson().fromJson(getIntent().getStringExtra(ActivitiesAdapter.ORIGIN), LatLng.class);
    }

    @Override
    public void onConnected(Bundle bundle) {
        openMap();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
