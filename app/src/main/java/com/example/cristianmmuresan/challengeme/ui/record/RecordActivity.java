package com.example.cristianmmuresan.challengeme.ui.record;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.cristianmmuresan.challengeme.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class RecordActivity extends AppCompatActivity {

    private static final LatLng LOWER_MANHATTAN = new LatLng(40.722543,
            -73.998585);
    private static final LatLng TIMES_SQUARE = new LatLng(40.7577, -73.9857);
    private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            if (googleMap != null) {
                addLines();
            }
        }
    }

    private void addLines() {
        googleMap.addPolyline((new PolylineOptions()).add(TIMES_SQUARE, BROOKLYN_BRIDGE,
                LOWER_MANHATTAN, TIMES_SQUARE).width(4).color(getResources().getColor(R.color.colorAccent)).geodesic(true));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOWER_MANHATTAN, 13));
    }

}
