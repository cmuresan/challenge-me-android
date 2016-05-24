package com.example.cristianmmuresan.challengeme.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.parse.ParseObject;

/**
 * Created by Cristian M. Muresan on 5/21/2016.
 */
public class Activity {
    private String name;
    private String time;
    private String url;
    private LatLng dest;
    private LatLng origin;

    public Activity(ParseObject parseObject) {
        setName(parseObject.get("title").toString());
        setTime(parseObject.get("time").toString());
        setUrl(parseObject.get("url").toString());
        setDest(new Gson().fromJson(parseObject.getString("dest"), LatLng.class));
        setOrigin(new Gson().fromJson(parseObject.getString("origin"), LatLng.class));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LatLng getDest() {
        return dest;
    }

    public void setDest(LatLng dest) {
        this.dest = dest;
    }

    public LatLng getOrigin() {
        return origin;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
    }

    public static String getActivityTime(long time) {
       int secs = (int) (time / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        return mins + ":" + String.format("%02d", secs);
    }
}
