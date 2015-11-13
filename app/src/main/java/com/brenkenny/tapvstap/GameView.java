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

    private int p1BlueCircleX;
    private int p1RedCircleX;
    private int p1GreenCircleX;
    private int p1YellowCircleX;
    private int p1OrangeCircleX;

    private int p2BlueCircleX;
    private int p2RedCircleX;
    private int p2GreenCircleX;
    private int p2YellowCircleX;
    private int p2OrangeCircleX;

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

    private p1Arrow arrow;

    private p2Arrow p2arrow;

    ArrayList<p1Arrow> p1ArrowList = new ArrayList<p1Arrow>();
    ArrayList<p2Arrow> p2ArrowList = new ArrayList<p2Arrow>();

    private Context context;

    private int screenX;
    private int screenY;

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

        p1BlueCircleX = 10000;
        p1RedCircleX = 10000;
        p1GreenCircleX = 10000;
        p1YellowCircleX = 10000;
        p1OrangeCircleX = 10000;

        p2BlueCircleX = -10000;
        p2RedCircleX = -10000;
        p2GreenCircleX = -10000;
        p2YellowCircleX = -10000;
        p2OrangeCircleX = -10000;

        screenX = x;
        screenY = y;

        // Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();

        //arrow = new Arrow(context, screenX, screenY);

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
        //arrow.update();

        p1BlueCircleX += 35;
        p1RedCircleX += 35;
        p1GreenCircleX += 35;
        p1YellowCircleX += 35;
        p1OrangeCircleX += 35;

        p2BlueCircleX -= 35;
        p2RedCircleX -= 35;
        p2GreenCircleX -= 35;
        p2YellowCircleX -= 35;
        p2OrangeCircleX -= 35;

        for(int i = 0; i < p1ArrowList.size(); i++){
           p1Arrow mp1Arrow = p1ArrowList.get(i);
            mp1Arrow.update();
        }
        for(int i = 0; i < p2ArrowList.size(); i++){
            p2Arrow mp2Arrow = p2ArrowList.get(i);
            mp2Arrow.update();
        }
    }

    private void draw() {

        if (ourHolder.getSurface().isValid()) {

            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            paint.setColor(Color.argb(255, 0, 0, 255)); //blue
            canvas.drawCircle(200, 200, 100, paint);
            canvas.drawCircle(2300, 200, 100, paint);
            canvas.drawCircle(p1BlueCircleX, 200, 100, paint);
            canvas.drawCircle(p2BlueCircleX, 200, 100, paint);

            paint.setColor(Color.argb(255, 255, 0, 0)); //red
            canvas.drawCircle(200, 450, 100, paint);
            canvas.drawCircle(2300, 450, 100, paint);
            canvas.drawCircle(p1RedCircleX, 450, 100, paint);
            canvas.drawCircle(p2RedCircleX, 450, 100, paint);

            paint.setColor(Color.argb(255, 0, 255, 0)); //green
            canvas.drawCircle(200, 700, 100, paint);
            canvas.drawCircle(2300, 700, 100, paint);
            canvas.drawCircle(p1GreenCircleX, 700, 100, paint);
            canvas.drawCircle(p2GreenCircleX, 700, 100, paint);

            paint.setColor(Color.argb(255, 255, 255, 0)); //yellow
            canvas.drawCircle(200, 950, 100, paint);
            canvas.drawCircle(2300, 950, 100, paint);
            canvas.drawCircle(p1YellowCircleX, 950, 100, paint);
            canvas.drawCircle(p2YellowCircleX, 950, 100, paint);

            paint.setColor(Color.argb(255, 255, 140, 0)); //orange
            canvas.drawCircle(200, 1200, 100, paint);
            canvas.drawCircle(2300, 1200, 100, paint);
            canvas.drawCircle(p1OrangeCircleX, 1200, 100, paint);
            canvas.drawCircle(p2OrangeCircleX, 1200, 100, paint);

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

                    arrow = new p1Arrow(context, 50, 100, "blue");
                    p1ArrowList.add(arrow);
                }

                if ( 100 < motionEvent.getX() && motionEvent.getX() < 300  &&  350 < motionEvent.getY() && motionEvent.getY() < 550) {

                    arrow = new p1Arrow(context, 50, 350, "red");
                    p1ArrowList.add(arrow);
                }
                if ( 100 < motionEvent.getX() && motionEvent.getX() < 300  &&  600 < motionEvent.getY() && motionEvent.getY() < 800) {

                    arrow = new p1Arrow(context, 50, 600, "green");
                    p1ArrowList.add(arrow);
                }
                if ( 100 < motionEvent.getX() && motionEvent.getX() < 300  &&  850 < motionEvent.getY() && motionEvent.getY() < 1050) {
                    arrow = new p1Arrow(context, 50, 850, "yellow");
                    p1ArrowList.add(arrow);
                }
                if ( 100 < motionEvent.getX() && motionEvent.getX() < 300  &&  1100 < motionEvent.getY() && motionEvent.getY() < 1300) {
                    arrow = new p1Arrow(context, 50, 1100, "orange");
                    p1ArrowList.add(arrow);
                }


                //Player 2 Buttons
                if ( 2200 < motionEvent.getX() && motionEvent.getX() < 2400  &&  100 < motionEvent.getY() && motionEvent.getY() < 300) {
                    p2arrow = new p2Arrow(context, 2300, 100, "blue");
                    p2ArrowList.add(p2arrow);
                }

                if ( 2200 < motionEvent.getX() && motionEvent.getX() < 2400  &&  350 < motionEvent.getY() && motionEvent.getY() < 550) {
                    p2arrow = new p2Arrow(context, 2300, 350, "red");
                    p2ArrowList.add(p2arrow);
                }
                if ( 2200 < motionEvent.getX() && motionEvent.getX() < 2400  &&  600 < motionEvent.getY() && motionEvent.getY() < 800) {

                    p2arrow = new p2Arrow(context, 2300, 600, "green");
                    p2ArrowList.add(p2arrow);
                }
                if ( 2200 < motionEvent.getX() && motionEvent.getX() < 2400  &&  850 < motionEvent.getY() && motionEvent.getY() < 1050) {

                    p2arrow = new p2Arrow(context, 2300, 850, "yellow");
                    p2ArrowList.add(p2arrow);
                }
                if ( 2200 < motionEvent.getX() && motionEvent.getX() < 2400  &&  1100 < motionEvent.getY() && motionEvent.getY() < 1300) {

                    p2arrow = new p2Arrow(context, 2300, 1100, "orange");
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


