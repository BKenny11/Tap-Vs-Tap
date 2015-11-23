package com.brenkenny.tapvstap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    public int lives = 3;
    public int gameSpeed;
    public boolean backgroundMusic;
    public boolean soundEffects;
    public boolean powerups;

    public final static String EXTRA_EVENT_TITLE = "com.BrenandEric.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


    }

    public void close(View view){
        Intent i = new Intent(this, GameActivity.class);
        // Start our GameActivity class via the Intent
        i.putExtra(EXTRA_EVENT_TITLE, ""+lives);
        startActivity(i);
        //finish();
    }

    public void addLife(View view){
        lives++;
        TextView t = (TextView)findViewById(R.id.textView3);
        t.setText(String.valueOf(lives));


    }
    public void minusLife(View view){
        lives--;
        TextView t = (TextView)findViewById(R.id.textView3);
        t.setText(String.valueOf(lives));
    }


}
