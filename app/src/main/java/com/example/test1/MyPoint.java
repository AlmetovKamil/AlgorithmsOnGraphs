package com.example.test1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;

public class MyPoint {
    private float x;
    private float y;
    private boolean b;
    private boolean visited;
    private boolean cur;
    private static Paint background = new Paint();
    private Bitmap num = BitmapFactory.decodeResource(Res.getInstance().getResources(), R.drawable.allciferkabuttons);
    {
        background.setStyle(Paint.Style.FILL);
        background.setStrokeWidth(10);

    }

    //getters
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isB() {
        return b;
    }

    public boolean isVisited() {
        return visited;
    }

    public boolean isCur() {
        return cur;
    }

    public static Paint getBackground() {
        return background;
    }

    public Bitmap getNum() { return num; }

    //setters
    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setB(boolean b) {
        this.b = b;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setCur(boolean cur) {
        this.cur = cur;
    }

    public void setNum(Bitmap num) { this.num = num; }

    public static void setBackground(Paint background) {
        MyPoint.background = background;
    }

    //constructors
    public MyPoint() {
        this.b = false;
        this.visited = false;
        this.cur = false;
    }

    public MyPoint(int x, int y) {
        this();
        this.x = x;
        this.y = y;
    }

    public MyPoint(float x, float y) {
        this();
        this.x = x;
        this.y = y;
    }

    public MyPoint(int x, int y, boolean b) {
        this();
        this.x = x;
        this.y = y;
        this.b = b;
    }

    public MyPoint(float x, float y, boolean b) {
        this();
        this.x = x;
        this.y = y;
        this.b = b;
    }

    //background
    public static void setBackgroundColor(int color) {
        background.setColor(Res.getInstance().getColor(color));
    }
}
