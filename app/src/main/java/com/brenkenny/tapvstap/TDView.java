package com.brenkenny.tapvstap;

/**
 * Created by bkishere11 on 10/19/15.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class TDView extends SurfaceView implements Runnable {

    private int circlex;
    private int circley;


    private boolean p1BluePressed;
    private boolean p1RedPressed;
    private boolean p1GreenPressed;
    private boolean p1YellowPressed;
    private boolean p1OrangePressed;

    private boolean p2BluePressed;
    private boolean p2RedPressed;
    private boolean p2GreenPressed;
    private boolean p2YellowPressed;
    private boolean p2OrangePressed;

    private Arrow arrow;

    ArrayList<Arrow> arrowList = new ArrayList<Arrow>();

    private Context context;

    private int screenX;
    private int screenY;



    volatile boolean playing;
    Thread gameThread = null;

    // For drawing
    private Paint paint;
    private Paint paintRed;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    // For saving and loading the high score
    private SharedPreferences prefs;


    TDView(Context context, int x, int y) {
        super(context);
        this.context  = context;

        circlex = 10000;
        circley = 200;

        screenX = x;
        screenY = y;

        // Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();
        paintRed = new Paint();

        arrow = new Arrow(context, screenX, screenY);

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

        arrow.update();

        circlex = circlex+35;


    }

    private void draw() {

        if (ourHolder.getSurface().isValid()) {
            //First we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();

            // Rub out the last frame
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            // For debugging
            // Switch to white pixels
            paint.setColor(Color.argb(255, 255, 255, 255));

            arrow = new Arrow(context, circlex, circley);

            paintRed.setColor(Color.argb(255, 0, 0, 255));
            canvas.drawCircle(circlex, circley, 100, paintRed);



            paint.setColor(Color.argb(255, 0, 0, 255));
            canvas.drawCircle(200, 200, 100, paint);
            paint.setColor(Color.argb(255, 255, 0, 0));
            canvas.drawCircle(200, 450, 100, paint);
            paint.setColor(Color.argb(255, 0, 255, 0));
            canvas.drawCircle(200, 700, 100, paint);
            paint.setColor(Color.argb(255, 255, 255, 0));
            canvas.drawCircle(200, 950, 100, paint);
            paint.setColor(Color.argb(255, 255, 140, 0));
            canvas.drawCircle(200,1200,100,paint);



            paint.setColor(Color.argb(255, 0, 0, 255));
            canvas.drawCircle(2300, 200, 100, paint);
            paint.setColor(Color.argb(255, 255, 0, 0));
            canvas.drawCircle(2300, 450, 100, paint);
            paint.setColor(Color.argb(255, 0, 255, 0));
            canvas.drawCircle(2300, 700, 100, paint);
            paint.setColor(Color.argb(255, 255, 255, 0));
            canvas.drawCircle(2300, 950, 100, paint);
            paint.setColor(Color.argb(255, 255, 140, 0));
            canvas.drawCircle(2300,1200,100,paint);

            //Player 1 Pressed
            if (p1BluePressed == true){
                //paint.setColor(Color.argb(255, 0, 0, 255));
                //canvas.drawCircle(700, 500, 400, paint);
                circlex = 200;
                //arrow = new Arrow(context, screenX, screenY);

            }

            if ( p1RedPressed == true){
                paint.setColor(Color.argb(255, 255, 0, 0));
                canvas.drawCircle(700,500,400,paint);
            }
            if ( p1GreenPressed == true){
                paint.setColor(Color.argb(255, 0, 255, 0));
                canvas.drawCircle(700,500,400,paint);
            }
            if ( p1YellowPressed == true){
                paint.setColor(Color.argb(255, 255, 255, 0));
                canvas.drawCircle(700,500,400,paint);
            }
            if ( p1OrangePressed == true){
                paint.setColor(Color.argb(255, 255, 140, 0));
                canvas.drawCircle(700,500,400,paint);
            }


            //Player 2 Pressed
            if (p2BluePressed == true){
                paint.setColor(Color.argb(255, 0, 0, 255));
                canvas.drawCircle(1000,500,400,paint);
            }

            if ( p2RedPressed == true){
                paint.setColor(Color.argb(255, 255, 0, 0));
                canvas.drawCircle(1000,500,400,paint);
            }
            if ( p2GreenPressed == true){
                paint.setColor(Color.argb(255, 0, 255, 0));
                canvas.drawCircle(1000,500,400,paint);
            }
            if ( p2YellowPressed == true){
                paint.setColor(Color.argb(255, 255, 255, 0));
                canvas.drawCircle(1000,500,400,paint);
            }
            if ( p2OrangePressed == true){
                paint.setColor(Color.argb(255, 255, 140, 0));
                canvas.drawCircle(1000,500,400,paint);
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
                p1BluePressed = false;
                p1RedPressed = false;
                p1GreenPressed = false;
                p1YellowPressed = false;
                p1OrangePressed = false;

                p2BluePressed = false;
                p2RedPressed = false;
                p2GreenPressed = false;
                p2YellowPressed = false;
                p2OrangePressed = false;

                break;

            // Has the player touched the screen?
            case MotionEvent.ACTION_DOWN:


                //Player 1 Buttons
                if ( 100 < motionEvent.getX() && motionEvent.getX() < 300  &&  100 < motionEvent.getY() && motionEvent.getY() < 300) {

                    p1BluePressed = true;
                    //arrow = new Arrow(context);
                    //canvas.drawBitmap(arrow.getBitmap(),screenX,screenY, paint);
                }

                if ( 100 < motionEvent.getX() && motionEvent.getX() < 300  &&  350 < motionEvent.getY() && motionEvent.getY() < 550) {

                    p1RedPressed = true;
                }
                if ( 100 < motionEvent.getX() && motionEvent.getX() < 300  &&  600 < motionEvent.getY() && motionEvent.getY() < 800) {

                    p1GreenPressed = true;
                }
                if ( 100 < motionEvent.getX() && motionEvent.getX() < 300  &&  850 < motionEvent.getY() && motionEvent.getY() < 1050) {

                    p1YellowPressed = true;
                }
                if ( 100 < motionEvent.getX() && motionEvent.getX() < 300  &&  1100 < motionEvent.getY() && motionEvent.getY() < 1300) {

                    p1OrangePressed = true;
                }


                //Player 2 Buttons
                if ( 2200 < motionEvent.getX() && motionEvent.getX() < 2400  &&  100 < motionEvent.getY() && motionEvent.getY() < 300) {

                    p2BluePressed = true;
                }

                if ( 2200 < motionEvent.getX() && motionEvent.getX() < 2400  &&  350 < motionEvent.getY() && motionEvent.getY() < 550) {

                    p2RedPressed = true;
                }
                if ( 2200 < motionEvent.getX() && motionEvent.getX() < 2400  &&  600 < motionEvent.getY() && motionEvent.getY() < 800) {

                    p2GreenPressed = true;
                }
                if ( 2200 < motionEvent.getX() && motionEvent.getX() < 2400  &&  850 < motionEvent.getY() && motionEvent.getY() < 1050) {

                    p2YellowPressed = true;
                }
                if ( 2200 < motionEvent.getX() && motionEvent.getX() < 2400  &&  1100 < motionEvent.getY() && motionEvent.getY() < 1300) {

                    p2OrangePressed = true;
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


