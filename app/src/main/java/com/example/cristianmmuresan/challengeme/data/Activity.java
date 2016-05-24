package com.example.cristianmmuresan.challengeme.data;

import com.parse.ParseObject;

/**
 * Created by Cristian M. Muresan on 5/21/2016.
 */
public class Activity {
    private String name;
    private String time;

    public Activity(ParseObject parseObject) {
        setName(parseObject.get("title").toString());
        setTime(parseObject.get("time").toString());
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

    public static String getActivityTime(long time) {
       int secs = (int) (time / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        return mins + ":" + String.format("%02d", secs);
    }
}
