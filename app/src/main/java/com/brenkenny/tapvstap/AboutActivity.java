package com.brenkenny.tapvstap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void openSettings(View v) {
        // must be the Play button.
        // Create a new Intent object
        Intent i = new Intent(this, MainActivity.class);
        // Start our GameActivity class via the Intent
        startActivity(i);
        finish();
    }
}
