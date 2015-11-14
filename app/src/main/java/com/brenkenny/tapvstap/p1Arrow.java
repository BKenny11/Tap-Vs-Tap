package com.brenkenny.tapvstap;

/**
 * Created by bkishere11 on 11/9/15.
 */
    import android.content.Context;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;

    public class p1Arrow {
        private Bitmap bitmap2;
        private Bitmap bitmap;
        private int x, y;
        private int speed;
        private int dotSize;
        // Constructor
        public p1Arrow(Context context, int screenX, int screenY, String color, int size) {
            x = screenX;
            y = screenY;
            speed = 20;

            dotSize = size*2;

            if(color == "blue") {
                bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.bluearrow);
            }else if(color == "red") {
                bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.redarrow);
            }
            else if(color == "green") {
                bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.greenarrow);
            }
            else if(color == "yellow") {
                bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.yellowarrow);
            }
            else if(color == "orange") {
                bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.orangearrow);
            }
            bitmap = Bitmap.createScaledBitmap(bitmap2, dotSize, dotSize, true);
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


