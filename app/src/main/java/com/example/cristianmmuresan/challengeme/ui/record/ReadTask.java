package com.example.cristianmmuresan.challengeme.ui.record;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.cristianmmuresan.challengeme.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Cristian M. Muresan on 5/24/2016.
 */
public class ReadTask extends AsyncTask<String, Void, String> {

    private final Context mContext;
    private final GoogleMap mGoogleMap;

    public ReadTask(Context context, GoogleMap googleMap) {
        mContext = context;
        mGoogleMap = googleMap;
    }

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
        new ParserTask(mContext,mGoogleMap).execute(result);
    }
}

class ParserTask extends
        AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

    private final Context mContext;
    private final GoogleMap mGoogleMap;

    public ParserTask(Context context, GoogleMap googleMap) {
        mContext = context;
        mGoogleMap = googleMap;
    }

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
            polyLineOptions.color(mContext.getResources().getColor(R.color.colorAccent));
        }

        mGoogleMap.addPolyline(polyLineOptions);
    }
}
