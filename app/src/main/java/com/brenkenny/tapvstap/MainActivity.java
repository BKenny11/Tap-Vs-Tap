package com.brenkenny.tapvstap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // This is the entry point to our game
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Here we set our UI layout as the view
        setContentView(R.layout.activity_main);
    }

    //start game activity
    public void onClick(View v) {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
        finish();
    }

    //start settings
    public void openSettings(View v) {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
        finish();
    }

    //start about page
    public void openAbout(View v) {
        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);
        finish();
    }
}