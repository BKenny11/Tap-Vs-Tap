package com.brenkenny.tapvstap;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class GameActivity extends Activity {

    // Our object to handle the View
    private GameView gameView;
    public int lives;
    public int speed;
    public Boolean powerups;
    public Boolean music;
    public Boolean sounds;

    // This is where the "Play" button from HomeActivity sends us
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();
        // Load the resolution into a Point object
        Point size = new Point();
        display.getSize(size);

        Intent intent = getIntent();
        if (intent.getStringExtra(SettingsActivity.EXTRA_GAME_LIVES) != null) {
            lives = Integer.valueOf(intent.getStringExtra(SettingsActivity.EXTRA_GAME_LIVES));
            speed = Integer.valueOf(intent.getStringExtra(SettingsActivity.EXTRA_GAME_SPEED));
            powerups = intent.getBooleanExtra(SettingsActivity.EXTRA_GAME_POWERUPS, true);
            music = intent.getBooleanExtra(SettingsActivity.EXTRA_GAME_MUSIC,true);
            sounds = intent.getBooleanExtra(SettingsActivity.EXTRA_GAME_SOUNDS,true);

        }else {
            lives = 3;
            speed = 1;
            powerups = true;
            music = true;
            sounds = true;


        }

        // Create an instance of our Tappy Defender View
        // Also passing in this.
        // Also passing in the screen resolution to the constructor
        gameView = new GameView(this, size.x, size.y, lives, powerups, music,sounds,speed);

        // Make our gameView the view for the Activity
        setContentView(gameView);

    }

    // If the Activity is paused make sure to pause our thread
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    // If the Activity is resumed make sure to resume our thread
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }



}
