package com.example.test1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.Button;

import static com.example.test1.MainAction.graph;


class MyButton {
    private boolean pressed = false;
    private float x, y;
    private Bitmap image;

    private byte type;//1 - start, 2 - clean, 3 - addToMemory

    public MyButton(float x, float y, Bitmap image, byte type) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.type = type;
    }

    public float getX() {
        return x;
    }

    public Bitmap getImage() {
        return image;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public void drawButton(Canvas canvas) {
        canvas.drawBitmap(this.image, this.x, this.y, new Paint());
    }

    public boolean onClick(float px, float py) throws InterruptedException {
        float centerX = this.x + (float)this.image.getWidth()/2;
        float centerY = this.y + (float)this.image.getHeight()/2;
        float radius = (float)this.image.getHeight();
        float d = (float) Math.sqrt((Math.abs(px - centerX) * Math.abs(px - centerX)) + (Math.abs(py - centerY) * Math.abs(py - centerY)));
        if (d <= radius) {
            this.setImage(BitmapFactory.decodeResource(Res.getInstance().getResources(), R.drawable.launch2));
            Thread.sleep(500);
            //do something

            this.setImage(BitmapFactory.decodeResource(Res.getInstance().getResources(), R.drawable.launch1));
            return true;
        }
        return false;
    }

}
