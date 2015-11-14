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

    ArrayList<p1Arrow> p1ArrowList = new ArrayList<p1Arrow>();
    ArrayList<p2Arrow> p2ArrowList = new ArrayList<p2Arrow>();

    private Context context;

    private int roundCount;

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

        // Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();

        // Load fastest time
        prefs = context.getSharedPreferences("HiScores", context.MODE_PRIVATE);
        // Initialize the editor ready

        //downPressed = false;
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
            }

        }
        for(int i = 0; i < p2ArrowList.size(); i++){
            p2Arrow mp2Arrow = p2ArrowList.get(i);
            mp2Arrow.update();

            if (mp2Arrow.getX() < -400) {
                p2ArrowList.remove(i);
            }
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
            canvas.drawCircle(screenMargin, distBetween*2, dotSize, paint);
            canvas.drawCircle(screenX - screenMargin, distBetween * 2, dotSize, paint);

            paint.setColor(Color.argb(255, 0, 255, 0)); //green
            canvas.drawCircle(screenMargin, distBetween*3, dotSize, paint);
            canvas.drawCircle(screenX - screenMargin, distBetween*3, dotSize, paint);

            paint.setColor(Color.argb(255, 255, 255, 0)); //yellow
            canvas.drawCircle(screenMargin, distBetween*4, dotSize, paint);
            canvas.drawCircle(screenX - screenMargin, distBetween*4, dotSize, paint);

            paint.setColor(Color.argb(255, 255, 140, 0)); //orange
            canvas.drawCircle(screenMargin, distBetween*5, dotSize, paint);
            canvas.drawCircle(screenX - screenMargin, distBetween*5, dotSize, paint);

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


                //Player 1 Buttons
                if ( screenMargin-dotSize < motionEvent.getX() && motionEvent.getX() < screenMargin+dotSize  &&  distBetween-dotSize < motionEvent.getY() && motionEvent.getY() < distBetween+dotSize) {
                    double spawnPointY = (distBetween) - dotSize;
                    double spawnPointX = screenMargin - (dotSize);
                    arrow = new p1Arrow(context, (int)spawnPointX, (int)spawnPointY, "blue",dotSize);
                    p1ArrowList.add(arrow);
                }

                if (screenMargin-dotSize < motionEvent.getX() && motionEvent.getX() < screenMargin+dotSize  &&  distBetween*2-dotSize < motionEvent.getY() && motionEvent.getY() < distBetween*2+dotSize) {
                    double spawnPointY = (distBetween * 2) - dotSize;
                    double spawnPointX = screenMargin - (dotSize);
                    arrow = new p1Arrow(context, (int)spawnPointX, (int)spawnPointY, "red",dotSize);
                    p1ArrowList.add(arrow);
                }
                if (screenMargin-dotSize < motionEvent.getX() && motionEvent.getX() < screenMargin+dotSize  &&  distBetween*3-dotSize < motionEvent.getY() && motionEvent.getY() < distBetween*3+dotSize) {
                    double spawnPointY = (distBetween * 3) - dotSize;
                    double spawnPointX = screenMargin - (dotSize);
                    arrow = new p1Arrow(context, (int)spawnPointX, (int)spawnPointY, "green",dotSize);
                    p1ArrowList.add(arrow);
                }
                if (screenMargin-dotSize < motionEvent.getX() && motionEvent.getX() < screenMargin+dotSize  &&  distBetween*4-dotSize < motionEvent.getY() && motionEvent.getY() < distBetween*4+dotSize) {
                    double spawnPointY = (distBetween * 4) - dotSize;
                    double spawnPointX = screenMargin - (dotSize);
                    arrow = new p1Arrow(context, (int)spawnPointX, (int)spawnPointY , "yellow",dotSize);
                    p1ArrowList.add(arrow);
                }
                if (screenMargin-dotSize < motionEvent.getX() && motionEvent.getX() < screenMargin+dotSize  &&  distBetween*5-dotSize < motionEvent.getY() && motionEvent.getY() < distBetween*5+dotSize) {
                    double spawnPointY = (distBetween * 5) - dotSize;
                    double spawnPointX = screenMargin - (dotSize);
                    arrow = new p1Arrow(context, (int)spawnPointX, (int)spawnPointY, "orange",dotSize);
                    p1ArrowList.add(arrow);
                }


                //Player 2 Buttons
                if ( screenX - (screenMargin+dotSize) < motionEvent.getX() && motionEvent.getX() < screenX - (screenMargin-dotSize)  &&  distBetween-dotSize < motionEvent.getY() && motionEvent.getY() < distBetween+dotSize) {
                    double spawnPointY = (distBetween) - dotSize;
                    double spawnPointX = screenX - (screenMargin + (dotSize));
                    p2arrow = new p2Arrow(context, (int)spawnPointX, (int)spawnPointY, "blue", dotSize);
                    p2ArrowList.add(p2arrow);
                }

                if ( screenX - (screenMargin+dotSize) < motionEvent.getX() && motionEvent.getX() < screenX - (screenMargin-dotSize)  &&  distBetween*2-dotSize < motionEvent.getY() && motionEvent.getY() < distBetween*2+dotSize) {
                    double spawnPointY = (distBetween*2) - dotSize;
                    double spawnPointX = screenX - (screenMargin + dotSize);
                    p2arrow = new p2Arrow(context, (int)spawnPointX, (int)spawnPointY, "red", dotSize);
                    p2ArrowList.add(p2arrow);
                }
                if ( screenX - (screenMargin+dotSize) < motionEvent.getX() && motionEvent.getX() < screenX - (screenMargin-dotSize)  &&  distBetween*3-dotSize < motionEvent.getY() && motionEvent.getY() < distBetween*3+dotSize) {
                    double spawnPointY = (distBetween*3) - dotSize;
                    double spawnPointX = screenX - (screenMargin + dotSize);
                    p2arrow = new p2Arrow(context, (int)spawnPointX, (int)spawnPointY, "green", dotSize);
                    p2ArrowList.add(p2arrow);
                }
                if ( screenX - (screenMargin+dotSize) < motionEvent.getX() && motionEvent.getX() < screenX - (screenMargin-dotSize)  &&  distBetween*4-dotSize < motionEvent.getY() && motionEvent.getY() < distBetween*4+dotSize) {
                    double spawnPointY = (distBetween*4) - dotSize;
                    double spawnPointX = screenX - (screenMargin + dotSize);
                    p2arrow = new p2Arrow(context, (int)spawnPointX, (int)spawnPointY, "yellow", dotSize);
                    p2ArrowList.add(p2arrow);
                }
                if ( screenX - (screenMargin+dotSize) < motionEvent.getX() && motionEvent.getX() < screenX - (screenMargin-dotSize)  &&  distBetween*5-dotSize < motionEvent.getY() && motionEvent.getY() < distBetween*5+dotSize) {
                    double spawnPointY = (distBetween*5) - dotSize;
                    double spawnPointX = screenX - (screenMargin + dotSize);
                    p2arrow = new p2Arrow(context, (int)spawnPointX, (int)spawnPointY, "orange", dotSize);
                    p2ArrowList.add(p2arrow);
                }


                break;
        }
        return true;
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


