package com.example.cristianmmuresan.challengeme;

import android.app.Application;

import com.example.cristianmmuresan.challengeme.data.User;
import com.example.cristianmmuresan.challengeme.util.PreferenceUtils;
import com.firebase.client.Firebase;

/**
 * Created by Cristian M. Muresan on 5/8/2016.
 */
public class ChallengeMeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        setGlobals();
        setupFirebase();
    }

    private void setupFirebase() {
        Firebase.setAndroidContext(this);

        Globals.sFirebase = new Firebase("https://challengemeapp.firebaseio.com");
    }

    private void setGlobals() {
        Globals.iApplication = this;

        Globals.iUser = PreferenceUtils.getUser();
        if (Globals.iUser == null)
            Globals.iUser = new User();
    }
}
