package com.example.cristianmmuresan.challengeme.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.cristianmmuresan.challengeme.Globals;
import com.example.cristianmmuresan.challengeme.data.User;
import com.google.gson.Gson;

/**
 * Created by Cristian M. Muresan on 5/8/2016.
 */
public class PreferenceUtils {
    private static final String PREFERENCE_FILE = "preferences";
    private static final String PREF_USER = "user";

    public static void saveUser(User user) {
        SharedPreferences p = Globals.iApplication.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);

        if (user == null) {
            p.edit().remove(PREF_USER).apply();
        } else {
            if (Globals.iUser != null && Globals.iUser.getToken() != null && user.getToken() == null)
                user.setToken(Globals.iUser.getToken());
            p.edit().putString(PREF_USER, new Gson().toJson(user)).apply();
        }

        Globals.iUser = user;
    }

    public static User getUser() {
        SharedPreferences p = Globals.iApplication.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        String val = p.getString(PREF_USER, null);

        if (val == null)
            return null;

        User u = new Gson().fromJson(val, User.class);

        Globals.iUser = u;

        return u;
    }

    public static void logout() {
        saveUser(null);
        Globals.iUser = null;
    }
}
