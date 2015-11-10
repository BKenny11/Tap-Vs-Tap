package com.brenkenny.tapvstap;

/**
 * Created by bkishere11 on 11/9/15.
 */
    import android.content.Context;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;

    public class Arrow {
        private Bitmap bitmap;
        private int x, y;
        private int speed;

        // Constructor
        public Arrow(Context context, int screenX, int screenY) {
            x = 50;
            y = 50;
            speed = 10;
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow);

        }

        public void update() {
            x = x + speed;
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


