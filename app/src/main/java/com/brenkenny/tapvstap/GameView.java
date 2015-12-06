package com.brenkenny.tapvstap;

/**
 * Created by bkishere11 on 10/19/15.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.media.SoundPool;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {

    private SoundPool mSoundPool;
    MediaPlayer mPlayer;
    int soundId1;
    int soundId2;
    int soundId3;
    int soundId4;
    int soundId5;

    int soundId6;
    int soundId7;
    int soundId8;
    int soundId9;
    int soundId10;

    private p1Arrow arrow;
    private p2Arrow p2arrow;

    int p1SpawnPointX;
    int p2SpawnPointX;

    ArrayList<p1Arrow> p1ArrowList = new ArrayList<p1Arrow>();
    ArrayList<p2Arrow> p2ArrowList = new ArrayList<p2Arrow>();

    private Context context;

    private Bitmap background;

    private Bitmap powerup1;
    private Bitmap powerup2;
    private Bitmap powerup3;

    private int roundCount;

    private boolean p1Turn;
    private boolean p2Turn;

    private float timeRemaining;

    private int p1ArrowsLeft;
    private int p2ArrowsLeft;

    private boolean gameEnd;

    private int p1Lives;
    private int p2Lives;

    private int screenX;
    private int screenY;

    private int xPixel;
    private int yPixel;

    private int lives;
    private Boolean Powerups;
    private int Speed;
    private Boolean BackgroundMusic;
    private Boolean SoundEffects;

    private int distBetween;
    private int dotSize;
    private int screenMargin;

    volatile boolean playing;
    Thread gameThread = null;

    // For drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    private RectF p1Life, p2Life;
    private RectF timer;

    // For saving and loading the high score
    private SharedPreferences prefs;

    GameView(Context context, int x, int y, int numLives, boolean powerups, boolean music, boolean sounds, int speed) {
        super(context);
        this.context  = context;

        mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        soundId1 = mSoundPool.load(context, R.raw.cnote, 1);
        soundId2 = mSoundPool.load(context, R.raw.dnote, 1);
        soundId3 = mSoundPool.load(context, R.raw.enote, 1);
        soundId4 = mSoundPool.load(context, R.raw.fnote, 1);
        soundId5 = mSoundPool.load(context, R.raw.gnote, 1);

        soundId6 = mSoundPool.load(context, R.raw.cnote2, 1);
        soundId7 = mSoundPool.load(context, R.raw.dnote2, 1);
        soundId8 = mSoundPool.load(context, R.raw.enote2, 1);
        soundId9 = mSoundPool.load(context, R.raw.fnote2, 1);
        soundId10 = mSoundPool.load(context, R.raw.gnote2, 1);

        mPlayer = MediaPlayer.create(context, R.raw.background);

        lives = numLives;
        Powerups = powerups;
        Speed = speed;
        BackgroundMusic = music;
        SoundEffects = sounds;

        screenX = x;
        screenY = y;

        xPixel = screenX/100;
        yPixel = screenY/100;

        distBetween = screenY / 6;
        dotSize = screenY / 15;

        screenMargin = (int)(dotSize * 3);


        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.gameground);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, true);

        powerup1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.curtain);
        powerup1 = Bitmap.createScaledBitmap(powerup1, dotSize, dotSize, true);
        powerup2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.faster);
        powerup2 = Bitmap.createScaledBitmap(powerup2, dotSize, dotSize, true);
        powerup3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.doublecount);
        powerup3 = Bitmap.createScaledBitmap(powerup3, dotSize, dotSize, true);

        p1SpawnPointX = screenMargin - dotSize;
        p2SpawnPointX = screenX - screenMargin - dotSize;

        p1Life = new RectF(0, 0, 30, screenY);
        p2Life = new RectF(screenX - 30, 0, screenX, screenY);

        timer = new RectF(screenX/2 - 200, screenY/2 - 200, screenX/2 + 200, screenY/2 + 200);

        p1Lives = 10;
        p2Lives = 10;

        // Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();

        prefs = context.getSharedPreferences("HiScores", context.MODE_PRIVATE);

        startGame();
    }

    private void startGame(){
        gameEnd = false;
        p1Lives = lives;
        p2Lives = lives;

        roundCount = 1;
        timeRemaining = 210;
        p1ArrowsLeft = roundCount;
        p2ArrowsLeft = roundCount;

        p1Life.set(0, 0, 30, screenY);
        p2Life.set(screenX - 30, 0, screenX, screenY);

        p1Turn = true;
        p2Turn = false;

        if(BackgroundMusic == true) {
            mPlayer.start();
        }
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        timeRemaining--;

        for(int i = 0; i < p1ArrowList.size(); i++){
            p1Arrow mp1Arrow = p1ArrowList.get(i);
            mp1Arrow.update();

            if (mp1Arrow.getX() > screenX + 100) {
                p1ArrowList.remove(i);
                p2Lives--;
                p2Life.set(screenX - 30, (1 - (float)p2Lives/lives) * screenY, screenX, screenY);
            }
        }
        for(int i = 0; i < p2ArrowList.size(); i++){
            p2Arrow mp2Arrow = p2ArrowList.get(i);
            mp2Arrow.update();

            if (mp2Arrow.getX() < -100) {
                p2ArrowList.remove(i);
                p1Lives--;
                p1Life.set(0, 0, 30, (float)p1Lives/lives * screenY);
            }
        }
        if(p1Lives < 1 || p2Lives < 1){
            p2ArrowList.clear();
            p1ArrowList.clear();
            gameEnd = true;
        }
        
        if (p1Turn && p1ArrowsLeft < 1 && p1ArrowList.size() < 1){
            p2Turn = true;
            p1Turn = false;
            roundCount++;
            p2ArrowsLeft = roundCount;
            timeRemaining = 200 + roundCount * 10;
        }
        else if (p2Turn && p2ArrowsLeft < 1 && p2ArrowList.size() < 1) {
            p1Turn = true;
            p2Turn = false;
            roundCount++;
            p1ArrowsLeft = roundCount;
            timeRemaining = 200 + roundCount * 10;
        }
        if(timeRemaining < 1){
            if(p1Turn){
                p1ArrowsLeft = 0;
            }
            else{
                p2ArrowsLeft = 0;
            }
        }
    }

    private void draw() {
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();

            canvas.drawBitmap(background, 0, 0, paint);

            if (gameEnd != true) {
                paint.setColor(Color.argb(255, 255, 0, 0));
                canvas.drawRect(p1Life, paint);
                canvas.drawRect(p2Life, paint);

                paint.setColor(Color.argb(200, 255, 255, 255));
                if (p1Turn && p1ArrowsLeft != 0) {
                    canvas.drawArc(timer, 0, timeRemaining / (200 + roundCount * 10) * 360, true, paint);
                }
                else if(p2Turn && p2ArrowsLeft != 0) {
                    canvas.drawArc(timer, 180, timeRemaining / (200 + roundCount * 10) * 360, true, paint);
                }

                paint.setColor(Color.argb(255, 255, 255, 255));
                //P1 UI
                canvas.save();
                canvas.rotate(90);
                //canvas.drawText("Round: "+roundCount,screenY-20*yPixel,-yPixel,paint);
                if (p1Turn && p1ArrowsLeft != 0) {
                    canvas.drawText("FIRE!! (" + p1ArrowsLeft + ")", 60, -6 * yPixel, paint);
                }
                canvas.restore();

                //P2 UI
                canvas.save();
                canvas.rotate(-90);
                // canvas.drawText("Round: "+roundCount,-screenY+80*yPixel,screenX-xPixel,paint);
                if (p2Turn && p2ArrowsLeft != 0) {
                    canvas.drawText("FIRE!! (" + p2ArrowsLeft + ")", -screenY + 60, screenX - 6 * yPixel, paint);
                }
                canvas.restore();
            }

            if (Powerups == true) {
                paint.setColor(Color.argb(255, 255, 255, 255));
//                canvas.drawCircle(dotSize * 1.2f, screenY - distBetween * 0.5f, dotSize / 2, paint);
//                canvas.drawCircle(dotSize * 1.2f, screenY - distBetween * 1.25f, dotSize / 2, paint);
//                canvas.drawCircle(dotSize * 1.2f, screenY - distBetween * 2.0f, dotSize / 2, paint);
//
//                canvas.drawCircle(screenX - dotSize * 1.2f, distBetween * 0.5f, dotSize / 2, paint);
//                canvas.drawCircle(screenX - dotSize * 1.2f, distBetween * 1.25f, dotSize / 2, paint);
//                canvas.drawCircle(screenX - dotSize * 1.2f, distBetween * 2.0f, dotSize / 2, paint);


                canvas.drawBitmap(powerup1, dotSize * .8f, screenY - distBetween * 0.5f, paint);
                canvas.drawBitmap(powerup2,dotSize * .8f, screenY - distBetween * 1.25f, paint);
                canvas.drawBitmap(powerup3,dotSize * .8f, screenY - distBetween * 2.0f, paint);

                canvas.drawBitmap(powerup1,screenX - dotSize * 1.6f, distBetween * 0.5f, paint);
                canvas.drawBitmap(powerup2, screenX - dotSize * 1.6f, distBetween * 1.25f, paint);
                canvas.drawBitmap(powerup3,screenX - dotSize * 1.6f, distBetween * 2.0f, paint);

            }

            paint.setColor(Color.argb(255, 0, 0, 255)); //blue
            canvas.drawCircle(screenMargin, distBetween, dotSize, paint);
            canvas.drawCircle(screenX - screenMargin, distBetween, dotSize, paint);

            paint.setColor(Color.argb(255, 255, 0, 0)); //red
            canvas.drawCircle(screenMargin, distBetween * 2, dotSize, paint);
            canvas.drawCircle(screenX - screenMargin, distBetween * 2, dotSize, paint);

            paint.setColor(Color.argb(255, 0, 255, 0)); //green
            canvas.drawCircle(screenMargin, distBetween * 3, dotSize, paint);
            canvas.drawCircle(screenX - screenMargin, distBetween * 3, dotSize, paint);

            paint.setColor(Color.argb(255, 255, 255, 0)); //yellow
            canvas.drawCircle(screenMargin, distBetween * 4, dotSize, paint);
            canvas.drawCircle(screenX - screenMargin, distBetween * 4, dotSize, paint);

            paint.setColor(Color.argb(255, 255, 140, 0)); //orange
            canvas.drawCircle(screenMargin, distBetween * 5, dotSize, paint);
            canvas.drawCircle(screenX - screenMargin, distBetween * 5, dotSize, paint);

            paint.setColor(Color.argb(255, 255, 255, 255));

            paint.setTextSize(50f);

            //Player1 Pressed
            for (int i = 0; i < p1ArrowList.size(); i++) {
                p1Arrow mp1Arrow = p1ArrowList.get(i);
                canvas.drawBitmap(mp1Arrow.getBitmap(), mp1Arrow.getX(), mp1Arrow.getY(), paint);
            }

            //Player2 Pressed
            for (int i = 0; i < p2ArrowList.size(); i++) {
                p2Arrow mp2Arrow = p2ArrowList.get(i);
                canvas.drawBitmap(mp2Arrow.getBitmap(), mp2Arrow.getX(), mp2Arrow.getY(), paint);
            }

            if (gameEnd == true) {
                paint.setTextSize(300f);

                if (p1Lives == 0) {
                    canvas.save();
                    canvas.rotate(90);
                    paint.setColor(Color.argb(255, 255, 0, 0));
                    canvas.drawText("LOSE", screenY / 2 - 360, -screenX / 4, paint);

                    canvas.restore();

                    canvas.save();
                    canvas.rotate(-90);
                    paint.setColor(Color.argb(255, 0, 255, 0));
                    canvas.drawText("WIN", -screenY / 2 - 280, screenX * 0.75f, paint);
                    canvas.restore();
                }
                else if (p2Lives == 0) {
                    canvas.save();
                    canvas.rotate(90);
                    paint.setColor(Color.argb(255, 0, 255, 0));
                    canvas.drawText("WIN", screenY / 2 - 280, -screenX / 4, paint);

                    canvas.restore();

                    canvas.save();
                    canvas.rotate(-90);
                    paint.setColor(Color.argb(255, 255, 0, 0));
                    canvas.drawText("LOSE", -screenY / 2 - 360, screenX * 0.75f, paint);
                    canvas.restore();
                }
                paint.setTextSize(50f);
            }

            // Unlock and draw the scene
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {

        }
    }

    // SurfaceView allows us to handle the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        // There are many different events in MotionEvent
        // We care about just 2 - for now.
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // Has the player lifted there finger up?
            case MotionEvent.ACTION_UP:
                //Nothing Here Yet
                break;

            // Has the player touched the screen?
            case MotionEvent.ACTION_DOWN: //fall through
            case MotionEvent.ACTION_POINTER_DOWN:
                if(gameEnd!=true) {
                    //Player 1 Buttons
                    checkColorTapped(1, "blue", motionEvent);
                    checkColorTapped(1, "red", motionEvent);
                    checkColorTapped(1, "green", motionEvent);
                    checkColorTapped(1, "yellow", motionEvent);
                    checkColorTapped(1, "orange", motionEvent);

                    //Player 2 Buttons
                    checkColorTapped(2, "blue", motionEvent);
                    checkColorTapped(2, "red", motionEvent);
                    checkColorTapped(2, "green", motionEvent);
                    checkColorTapped(2, "yellow", motionEvent);
                    checkColorTapped(2, "orange", motionEvent);



                    for (int i= 1; i < 4;i++) {
                        //Detect player1 powerups touched
                        if (dotSize * .8f - dotSize < motionEvent.getX() && motionEvent.getX() < dotSize * .8f + dotSize && screenY -  distBetween * i * 0.5f - dotSize < motionEvent.getY() && motionEvent.getY() < screenY - distBetween * i * 0.5f + dotSize) {

                            if (p1Turn && p1ArrowsLeft != 0){

                            }


                            Log.e("Player1 powerup", i + " was pressed");
                        }

                        else if (screenX - dotSize * 1.6f - dotSize < motionEvent.getX() && motionEvent.getX() < screenX - dotSize * 1.6f + dotSize && distBetween * i * 0.75f - dotSize < motionEvent.getY() && motionEvent.getY() < distBetween * i * 0.75f + dotSize) {

                            Log.e("Player2 powerup", i+" was pressed");



                        }

                    }



                }
                if(gameEnd){
                    startGame();
                }


                break;
        }
        return true;
    }

    public void checkColorTapped(int playerNum, String color, MotionEvent motionEvent){

        int pointerIndex = motionEvent.getActionIndex();
        int colorNum;

        switch(color){
            case "blue":
                colorNum = 1;
                break;
            case "red":
                colorNum = 2;
                break;
            case "green":
                colorNum = 3;
                break;
            case "yellow":
                colorNum = 4;
                break;
            default:
                colorNum = 5;
        }
        boolean hitDetected = false;

        //Player1
        if(playerNum == 1){
            if(screenMargin - dotSize < motionEvent.getX(pointerIndex) && motionEvent.getX(pointerIndex) < screenMargin + dotSize  &&  distBetween * colorNum - dotSize < motionEvent.getY(pointerIndex) && motionEvent.getY(pointerIndex) < distBetween * colorNum + dotSize){
                if(p2Turn) {
                    for (int i = 0; i < p2ArrowList.size(); i++) {
                        if (screenMargin - dotSize < p2ArrowList.get(i).getX() + dotSize * 2/*?*/ && p2ArrowList.get(i).getX() < screenMargin + dotSize && distBetween * colorNum - dotSize < p2ArrowList.get(i).getY() + dotSize / 2 && p2ArrowList.get(i).getY() + dotSize / 2 < distBetween * colorNum + dotSize) {
                            p2ArrowList.remove(i);
                            hitDetected = true;

                            if(SoundEffects == true) {
                                if (color == "blue") {
                                    mSoundPool.play(soundId1, 1, 1, 0, 0, 1);
                                } else if (color == "red") {
                                    mSoundPool.play(soundId2, 1, 1, 0, 0, 1);
                                } else if (color == "green") {
                                    mSoundPool.play(soundId3, 1, 1, 0, 0, 1);
                                } else if (color == "yellow") {
                                    mSoundPool.play(soundId4, 1, 1, 0, 0, 1);
                                } else if (color == "orange") {
                                    mSoundPool.play(soundId5, 1, 1, 0, 0, 1);
                                }
                            }
                            break; //prevent hitting multiple arrows at once
                        }
                    }
                    if(hitDetected == false){
                        p1Lives--;
                        p1Life.set(0, 0, 30, (float)p1Lives/lives * screenY);
                    }
                }

                else if(p1ArrowsLeft > 0){
                    arrow = new p1Arrow(context, p1SpawnPointX, distBetween * colorNum - dotSize, color, dotSize, roundCount);
                    p1ArrowList.add(arrow);
                    p1ArrowsLeft--;
                    if (SoundEffects == true) {
                        if (color == "blue") {
                            mSoundPool.play(soundId1, 1, 1, 0, 0, 1);
                        } else if (color == "red") {
                            mSoundPool.play(soundId2, 1, 1, 0, 0, 1);
                        } else if (color == "green") {
                            mSoundPool.play(soundId3, 1, 1, 0, 0, 1);
                        } else if (color == "yellow") {
                            mSoundPool.play(soundId4, 1, 1, 0, 0, 1);
                        } else if (color == "orange") {
                            mSoundPool.play(soundId5, 1, 1, 0, 0, 1);
                        }
                    }

                }
            }

        }

        //Player 2
        else{
            if(screenX - (screenMargin + dotSize) < motionEvent.getX(pointerIndex) && motionEvent.getX(pointerIndex) < screenX - (screenMargin - dotSize)  &&  distBetween * colorNum - dotSize < motionEvent.getY(pointerIndex) && motionEvent.getY(pointerIndex) < distBetween * colorNum + dotSize) {
                if(p1Turn) {
                    for (int i = 0; i < p1ArrowList.size(); i++) {
                        if (screenX - (screenMargin + dotSize) < p1ArrowList.get(i).getX() + dotSize * 2/*?*/ && p1ArrowList.get(i).getX() < screenX - (screenMargin - dotSize) && distBetween * colorNum - dotSize < p1ArrowList.get(i).getY() + dotSize / 2 && p1ArrowList.get(i).getY() + dotSize / 2 < distBetween * colorNum + dotSize) {
                            p1ArrowList.remove(i);
                            hitDetected = true;

                            if (SoundEffects == true) {
                                if (color == "blue") {
                                    mSoundPool.play(soundId6, 1, 1, 0, 0, 1);
                                } else if (color == "red") {
                                    mSoundPool.play(soundId7, 1, 1, 0, 0, 1);
                                } else if (color == "green") {
                                    mSoundPool.play(soundId8, 1, 1, 0, 0, 1);
                                } else if (color == "yellow") {
                                    mSoundPool.play(soundId9, 1, 1, 0, 0, 1);
                                } else if (color == "orange") {
                                    mSoundPool.play(soundId10, 1, 1, 0, 0, 1);
                                }
                            }
                            break; //prevent hitting multiple arrows at once
                        }
                    }
                    if(hitDetected == false){
                        p2Lives--;
                        p2Life.set(screenX - 30, (1 - (float)p2Lives/lives) * screenY, screenX, screenY);
                    }
                }
                else if (p2ArrowsLeft > 0){
                    p2arrow = new p2Arrow(context, p2SpawnPointX, distBetween * colorNum - dotSize, color, dotSize, roundCount);
                    p2ArrowList.add(p2arrow);
                    p2ArrowsLeft--;

                    if(SoundEffects == true) {
                        if (color == "blue") {
                            mSoundPool.play(soundId6, 1, 1, 0, 0, 1);
                        } else if (color == "red") {
                            mSoundPool.play(soundId7, 1, 1, 0, 0, 1);
                        } else if (color == "green") {
                            mSoundPool.play(soundId8, 1, 1, 0, 0, 1);
                        } else if (color == "yellow") {
                            mSoundPool.play(soundId9, 1, 1, 0, 0, 1);
                        } else if (color == "orange") {
                            mSoundPool.play(soundId10, 1, 1, 0, 0, 1);
                        }
                    }
                }
            }



        }
    }

    // Clean up our thread if the game is interrupted or the player quits
    public void pause() {
        playing = false;
        mPlayer.pause();
        try {
            gameThread.join();
        } catch (InterruptedException e) {

        }
    }


    // Make a new thread and start it
    // Execution moves to our R
    public void resume() {
        playing = true;
        if(BackgroundMusic == true) {
            mPlayer.start();
        }
        gameThread = new Thread(this);
        gameThread.start();
    }

}


