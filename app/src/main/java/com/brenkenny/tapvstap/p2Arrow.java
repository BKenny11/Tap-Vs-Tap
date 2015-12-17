package com.brenkenny.tapvstap;

/**
 * Created by bkishere11 on 11/13/15.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class p2Arrow {
    private Bitmap bitmap2;
    private Bitmap bitmap;
    private int x, y;
    private float speed;
    private int dotSize;
    private Boolean spedUp;

    // Constructor
    public p2Arrow(Context context, int screenX, int screenY, String color, int size, int round) {
        x = screenX;
        y = screenY;
        speed = size/10 + size/70*round;
        dotSize = size*2;
        spedUp = false;

        if(color == "blue") {
            bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.p2bluearrow);
        }
        else if(color == "red") {
            bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.p2redarrow);
        }
        else if(color == "green") {
            bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.p2greenarrow);
        }
        else if(color == "yellow") {
            bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.p2yellowarrow);
        }
        else if(color == "orange") {
            bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.p2orangearrow);
        }
        bitmap = Bitmap.createScaledBitmap(bitmap2, dotSize, dotSize, true);
    }

    public void update() {
        x = x - (int)speed;
    }

    //Getters
    public Bitmap getBitmap() {
        return bitmap;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float newSpeed) {
        speed = newSpeed;
    }

    public int getX() {
        return x;
    }

    public void setX(int newX) {
        x = newX;
    }

    public int getY() {
        return y;
    }

    public Boolean isSpedUp() { return spedUp; }

    public void setSpedUp(Boolean usingSpeedUp) { spedUp = usingSpeedUp; }
}


