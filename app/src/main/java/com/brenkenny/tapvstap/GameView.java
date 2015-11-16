package com.brenkenny.tapvstap;

/**
 * Created by bkishere11 on 10/19/15.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {

    private p1Arrow arrow;
    private p2Arrow p2arrow;

    int p1SpawnPointX;
    int p2SpawnPointX;

    ArrayList<p1Arrow> p1ArrowList = new ArrayList<p1Arrow>();
    ArrayList<p2Arrow> p2ArrowList = new ArrayList<p2Arrow>();

    private Context context;

    private int roundCount;

    private boolean gameEnd;

    private int p1Lives;
    private int p2Lives;

    private int screenX;
    private int screenY;
    private int distBetween;
    private int dotSize;
    private int screenMargin;

    volatile boolean playing;
    Thread gameThread = null;

    // For drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    // For saving and loading the high score
    private SharedPreferences prefs;

    GameView(Context context, int x, int y) {
        super(context);
        this.context  = context;

        screenX = x;
        screenY = y;

        distBetween = screenY / 6;
        dotSize = screenY / 15;

        screenMargin = (int)(dotSize * 1.5);

        p1SpawnPointX = screenMargin - dotSize;
        p2SpawnPointX = screenX - screenMargin - dotSize;

        p1Lives = 3;
        p2Lives = 3;

        // Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();

        prefs = context.getSharedPreferences("HiScores", context.MODE_PRIVATE);

        startGame();
    }

    private void startGame(){

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
        if(p1Lives == 0 || p2Lives == 0){
            p2ArrowList.clear();
            p1ArrowList.clear();
            gameEnd = true;
        }
    }

    private void draw() {
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 0, 0, 0));

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

            paint.setTextSize(24f);
            canvas.drawText("Player 1 lives: "+p1Lives, screenX/2,24, paint);
            canvas.drawText("Player 2 lives: "+p2Lives, screenX/2,screenY-10, paint);

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
                paint.setTextSize(100f);
                paint.setColor(Color.argb(255, 255, 255, 255)); //green
                if(p1Lives == 0) {
                    canvas.drawText("Game Over : Player 2 Wins!!", screenX / 2 - 500, screenY / 2, paint);
                }else if(p2Lives == 0){
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
            case MotionEvent.ACTION_DOWN:

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
                break;
        }
        return true;
    }

    public void checkColorTapped(int playerNum, String color, MotionEvent motionEvent){
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

        if(playerNum == 1){
            if(screenMargin - dotSize < motionEvent.getX() && motionEvent.getX() < screenMargin + dotSize  &&  distBetween * colorNum - dotSize < motionEvent.getY() && motionEvent.getY() < distBetween * colorNum + dotSize){
                for(int i = 0; i < p2ArrowList.size(); i++){
                    if(screenMargin - dotSize < p2ArrowList.get(i).getX() + dotSize * 2/*?*/ && p2ArrowList.get(i).getX() < screenMargin + dotSize && distBetween * colorNum - dotSize < p2ArrowList.get(i).getY() + dotSize/2 && p2ArrowList.get(i).getY() + dotSize/2 < distBetween * colorNum + dotSize){
                        p2ArrowList.remove(i);
                        hitDetected = true;
                        break; //prevent hitting multiple arrows at once
                    }
                }
                if(!hitDetected){ //temporary until we get turns working
                    arrow = new p1Arrow(context, p1SpawnPointX, distBetween * colorNum - dotSize, color, dotSize);
                    p1ArrowList.add(arrow);
                }
            }
        }
        else{
            if(screenX - (screenMargin + dotSize) < motionEvent.getX() && motionEvent.getX() < screenX - (screenMargin - dotSize)  &&  distBetween * colorNum - dotSize < motionEvent.getY() && motionEvent.getY() < distBetween * colorNum + dotSize){
                for(int i = 0; i < p1ArrowList.size(); i++){
                    if(screenX - (screenMargin + dotSize) < p1ArrowList.get(i).getX() + dotSize * 2/*?*/ && p1ArrowList.get(i).getX() < screenX - (screenMargin - dotSize) && distBetween * colorNum - dotSize < p1ArrowList.get(i).getY() + dotSize/2 && p1ArrowList.get(i).getY() + dotSize/2 < distBetween * colorNum + dotSize){
                        p1ArrowList.remove(i);
                        hitDetected = true;
                        break; //prevent hitting multiple arrows at once
                    }
                }
                if(!hitDetected){
                    p2arrow = new p2Arrow(context, p2SpawnPointX, distBetween * colorNum - dotSize, color, dotSize);
                    p2ArrowList.add(p2arrow);
                }
            }
        }
    }

    // Clean up our thread if the game is interrupted or the player quits
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {

        }
    }

    // Make a new thread and start it
    // Execution moves to our R
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

}


