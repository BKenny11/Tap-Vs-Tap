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
    private int speed;

    // Constructor
    public p2Arrow(Context context, int screenX, int screenY, String color) {
        x = screenX;
        y = screenY;
        speed = 20;

        if(color == "blue") {
            bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.p2bluearrow);
        }else if(color == "red") {
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
        bitmap = Bitmap.createScaledBitmap(bitmap2, 200, 200, true);
    }

    public void update() {
        x = x - speed;
    }

    //Getters
    public Bitmap getBitmap() {

        return bitmap;
    }

    public int getSpeed() {

        return speed;
    }

    public int getX() {

        return x;
    }

    public int getY() {

        return y;
    }
}


