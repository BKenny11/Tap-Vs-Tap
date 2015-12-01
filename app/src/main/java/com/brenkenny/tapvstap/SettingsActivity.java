package com.brenkenny.tapvstap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    public int lives = 3;
    public int gameSpeed = 1;
    public boolean backgroundMusic = true;
    public boolean soundEffects = true;
    public boolean powerups = true;


    public final static String EXTRA_GAME_LIVES = "com.BrenandEric.MESSAGE";
    public final static String EXTRA_GAME_SPEED = "com.BrenandEric.SPEED";
    public final static String EXTRA_GAME_SOUNDS = "com.BrenandEric.SOUNDS";
    public final static String EXTRA_GAME_MUSIC = "com.BrenandEric.MUSIC";
    public final static String EXTRA_GAME_POWERUPS = "com.BrenandEric.POWERUPS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }

    public void close(View view){
        Intent i = new Intent(this, GameActivity.class);
        // Start our GameActivity class via the Intent
        i.putExtra(EXTRA_GAME_LIVES, "" + lives);
        i.putExtra(EXTRA_GAME_MUSIC, backgroundMusic);
        i.putExtra(EXTRA_GAME_POWERUPS,powerups);
        i.putExtra(EXTRA_GAME_SOUNDS,soundEffects);
        i.putExtra(EXTRA_GAME_SPEED, "" + gameSpeed);
        startActivity(i);
        //finish();
    }

    public void addLife(View view){

        lives++;
        TextView t = (TextView)findViewById(R.id.textView3);
        t.setText(String.valueOf(lives));


    }
    public void minusLife(View view){
        if(lives > 1) {
            lives--;
            TextView t = (TextView) findViewById(R.id.textView3);
            t.setText(String.valueOf(lives));
        }
    }

    public void changePowerups(View view){
        if (powerups == true){
            powerups = false;
        }else if (powerups == false){
            powerups = true;
        }
    }

    public void changeBackgroundSound(View view){
        if (backgroundMusic == true){
            backgroundMusic = false;
        }else if (backgroundMusic == false){
            backgroundMusic = true;
        }
    }

    public void changeSoundEffects(View view){
        if (soundEffects == true){
            soundEffects = false;
        }else if (soundEffects == false){
            soundEffects = true;
        }
    }

}
