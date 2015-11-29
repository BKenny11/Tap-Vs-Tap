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
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.media.SoundPool;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import java.util.ArrayList;
import java.util.Set;

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

    private Bitmap life;

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

    private int Lives;

    private int distBetween;
    private int dotSize;
    private int screenMargin;

    volatile boolean playing;
    Thread gameThread = null;

    // For drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    private RectF p1Time, p2Time;

    // For saving and loading the high score
    private SharedPreferences prefs;

    GameView(Context context, int x, int y, int numLives) {
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

        mPlayer = MediaPlayer.create(context, R.raw.background); // in 2nd param u have to pass your desire ringtone
        //mPlayer.prepare();

        Lives = numLives;

        screenX = x;
        screenY = y;

        xPixel = screenX/100;
        yPixel = screenY/100;

        distBetween = screenY / 6;
        dotSize = screenY / 15;

        screenMargin = (int)(dotSize * 3);

        life  = BitmapFactory.decodeResource(context.getResources(), R.drawable.life);
        life = Bitmap.createScaledBitmap(life, dotSize / 2, dotSize / 2, true);

        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.gameground);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, true);

        p1SpawnPointX = screenMargin - dotSize;
        p2SpawnPointX = screenX - screenMargin - dotSize;

        p1Time = new RectF(0, 0, 30, screenY);
        p2Time = new RectF(screenX - 30, 0, screenX, screenY);

        p1Lives = 3;
        p2Lives = 3;

        // Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();

        prefs = context.getSharedPreferences("HiScores", context.MODE_PRIVATE);

        startGame();
    }

    private void startGame(){
        gameEnd = false;
        p1Lives = Lives;
        p2Lives = Lives;

        roundCount = 1;
        timeRemaining = 210;
        p1ArrowsLeft = roundCount;
        p2ArrowsLeft = roundCount;

        p1Turn = true;
        p2Turn = false;

        mPlayer.start();
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

            if (mp1Arrow.getX() > 3000) {
                p1ArrowList.remove(i);
                p2Lives--;
            }
        }
        for(int i = 0; i < p2ArrowList.size(); i++){
            p2Arrow mp2Arrow = p2ArrowList.get(i);
            mp2Arrow.update();

            if (mp2Arrow.getX() < -400) {
                p2ArrowList.remove(i);
                p1Lives--;
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

            if(p1Turn){
                p1Time.set(0, 0, 30, timeRemaining / (200 + roundCount * 10) * screenY); //decrease 4th param from screenY to 0
                canvas.drawRect(p1Time, paint);
            }
            else{
                p2Time.set(screenX - 30, (1 - timeRemaining / (200 + roundCount * 10)) * screenY, screenX, screenY); //increase 2nd param from 0 to screenY
                canvas.drawRect(p2Time, paint);
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

            //P1 UI
            canvas.save();
                canvas.rotate(90);
                canvas.drawText("Lives:", 10, -6*yPixel, paint);
                canvas.drawText("Round: "+roundCount,screenY-20*yPixel,-yPixel,paint);
            for (int i= 0; i<p1Lives; i++) {
                canvas.drawBitmap(life, 5*xPixel*i + 10, -5*yPixel, paint);
            }
            if(p1Turn) {
                canvas.drawText("FIRE!! ("+ p1ArrowsLeft+")", screenY / 2 - 5*xPixel, -2*yPixel, paint);
            }

            canvas.restore();

            //P2 UI
            canvas.save();
                canvas.rotate(-90);
                canvas.drawText("Lives: ", -screenY, screenX-70, paint);
                canvas.drawText("Round: "+roundCount,-screenY+80*yPixel,screenX-xPixel,paint);
            for (int i= 0; i<p2Lives; i++) {
                canvas.drawBitmap(life, -screenY + i*5*xPixel, screenX-5*yPixel, paint);
            }
            if(p2Turn) {
                canvas.drawText("FIRE!! ("+ p2ArrowsLeft+")", -screenY / 2 - 5*xPixel, screenX -2*yPixel, paint);
            }
            canvas.restore();

            //Player1 Pressed
            for(int i = 0; i < p1ArrowList.size(); i++){
                p1Arrow mp1Arrow = p1ArrowList.get(i);
                canvas.drawBitmap(mp1Arrow.getBitmap(), mp1Arrow.getX(), mp1Arrow.getY(), paint);

            }

            //Player2 Pressed
            for(int i = 0; i < p2ArrowList.size(); i++){
                p2Arrow mp2Arrow = p2ArrowList.get(i);
                canvas.drawBitmap(mp2Arrow.getBitmap(), mp2Arrow.getX(), mp2Arrow.getY(), paint);
            }

            if(gameEnd == true){
                paint.setTextSize(75f);
                paint.setColor(Color.argb(255, 255, 255, 255));
                if(p1Lives == 0) {
                    canvas.drawText("Game Over : Player 2 Wins!!", screenX / 2 - 500, screenY / 2, paint);
                }
                else if(p2Lives == 0){
                    canvas.drawText("Game Over : Player 1 Wins!!", screenX / 2-500, screenY / 2, paint);
                }
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

                            if (color == "blue") {
                                mSoundPool.play(soundId1, 1, 1, 0, 0, 1);
                            }
                            else if (color == "red"){
                                mSoundPool.play(soundId2, 1, 1, 0, 0, 1);
                            }
                            else if (color == "green"){
                                mSoundPool.play(soundId3, 1, 1, 0, 0, 1);
                            }
                            else if (color == "yellow"){
                                mSoundPool.play(soundId4, 1, 1, 0, 0, 1);
                            }
                            else if (color == "orange"){
                                mSoundPool.play(soundId5, 1, 1, 0, 0, 1);
                            }

                            break; //prevent hitting multiple arrows at once
                        }
                    }
                    if(hitDetected == false){
                        p1Lives--;
                    }
                }

                else if(p1ArrowsLeft > 0){
                    arrow = new p1Arrow(context, p1SpawnPointX, distBetween * colorNum - dotSize, color, dotSize, roundCount);
                    p1ArrowList.add(arrow);
                    p1ArrowsLeft--;

                    if (color == "blue") {
                        mSoundPool.play(soundId1, 1, 1, 0, 0, 1);
                    }
                    else if (color == "red"){
                        mSoundPool.play(soundId2, 1, 1, 0, 0, 1);
                    }
                    else if (color == "green"){
                        mSoundPool.play(soundId3, 1, 1, 0, 0, 1);
                    }
                    else if (color == "yellow"){
                        mSoundPool.play(soundId4, 1, 1, 0, 0, 1);
                    }
                    else if (color == "orange"){
                        mSoundPool.play(soundId5, 1, 1, 0, 0, 1);
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

                            if (color == "blue") {
                                mSoundPool.play(soundId6, 1, 1, 0, 0, 1);
                            }
                            else if (color == "red"){
                                mSoundPool.play(soundId7, 1, 1, 0, 0, 1);
                            }
                            else if (color == "green"){
                                mSoundPool.play(soundId8, 1, 1, 0, 0, 1);
                            }
                            else if (color == "yellow"){
                                mSoundPool.play(soundId9, 1, 1, 0, 0, 1);
                            }
                            else if (color == "orange"){
                                mSoundPool.play(soundId10, 1, 1, 0, 0, 1);
                            }

                            break; //prevent hitting multiple arrows at once
                        }
                    }
                    if(hitDetected == false){
                        p2Lives--;
                    }
                }
                else if (p2ArrowsLeft > 0){
                    p2arrow = new p2Arrow(context, p2SpawnPointX, distBetween * colorNum - dotSize, color, dotSize, roundCount);
                    p2ArrowList.add(p2arrow);
                    p2ArrowsLeft--;

                    if (color == "blue") {
                        mSoundPool.play(soundId6, 1, 1, 0, 0, 1);
                    }
                    else if (color == "red"){
                        mSoundPool.play(soundId7, 1, 1, 0, 0, 1);
                    }
                    else if (color == "green"){
                        mSoundPool.play(soundId8, 1, 1, 0, 0, 1);
                    }
                    else if (color == "yellow"){
                        mSoundPool.play(soundId9, 1, 1, 0, 0, 1);
                    }
                    else if (color == "orange"){
                        mSoundPool.play(soundId10, 1, 1, 0, 0, 1);
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
        mPlayer.start();
        gameThread = new Thread(this);
        gameThread.start();
    }

}


