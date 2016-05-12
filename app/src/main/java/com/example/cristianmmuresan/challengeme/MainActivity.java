package com.example.cristianmmuresan.challengeme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.cristianmmuresan.challengeme.ui.home.HomeActivity;
import com.example.cristianmmuresan.challengeme.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Globals.iUser == null || Globals.iUser.getToken() == null)
                    MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
                else
                    MainActivity.this.startActivity(new Intent(MainActivity.this, HomeActivity.class));
                MainActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
