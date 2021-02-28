package com.example.test1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Graph {
    private ArrayList<MyPoint> points = new ArrayList<>();
    private final float pointRADIUS = 50;
    private int k = 0;
    private int index1 = -1;
    private int index2 = -1;
    private ConcurrentHashMap<Integer, ArrayList<Integer>> links = new ConcurrentHashMap<>();
    private int start = 0;
    public static final int DFS_ID = 1;
    public static final int BFS_ID = 2;

    public ArrayList<MyPoint> getPoints() {
        return points;
    }

    public ConcurrentHashMap<Integer, ArrayList<Integer>> getLinks() {
        return links;
    }

    public int getStart() {return start;}

    public void setStart(int start) {this.start = start;}

    public void setPoints(ArrayList<MyPoint> points) {
        this.points = points;
    }

    public void setLinks(ConcurrentHashMap<Integer, ArrayList<Integer>> links) {
        this.links = links;
    }

    void setPointCoordinates(int pointX, int pointY) {
        MyPoint p = new MyPoint(pointX, pointY);
        if (points.size() > 0) {
            if (checkDistance(p))
                points.add(p);
        }
        else
            points.add(p);
        if (k == 2) {
            addLink();
            k = 0;
            points.get(index1).setB(false);
            points.get(index2).setB(false);
            index1 = -1;
            index2 = -1;
        }
    }



    int findPoint(int x, int y) {
        for (int i = 0; i < points.size(); ++i) {
            if (Math.abs(x - points.get(i).getX()) < pointRADIUS && Math.abs(y - points.get(i).getY()) < pointRADIUS) {
                return i;
            }
        }
        return -1;
    }

    private boolean checkDistance(MyPoint p) {
        for (int i = 0; i < points.size(); ++i) {
            float x = Math.abs(p.getX() - points.get(i).getX());
            float y = Math.abs(p.getY() - points.get(i).getY());
            double d = Math.sqrt(x*x + y*y);
            if (d <= pointRADIUS) {
                if (!points.get(i).isB()) {
                    points.get(i).setB(true);
                    k++;
                    if (k == 1) index1 = i;
                    else if (k == 2) index2 = i;
                    return false;
                }
                else {
                    points.get(i).setB(false);
                    k--;
                    if (k == 1) index2 = -1;
                    else if (k == 0) index1 = -1;
                    return false;
                }
            }
            else if (d <= 2*pointRADIUS) return false;
        }
        return true;
    }

    private void addLink() {
        if (links.containsKey(index1)) {
            if (!Objects.requireNonNull(links.get(index1)).contains(index2))
                Objects.requireNonNull(links.get(index1)).add(index2);
        }
        else {
            links.put(index1, new ArrayList<Integer>());
            Objects.requireNonNull(links.get(index1)).add(index2);
        }
        if (links.containsKey(index2)) {
            if (!Objects.requireNonNull(links.get(index2)).contains(index1))
                Objects.requireNonNull(links.get(index2)).add(index1);
        }
        else {
            links.put(index2, new ArrayList<Integer>());
            Objects.requireNonNull(links.get(index2)).add(index1);
        }
    }


    //drawing
    void drawGraph(Canvas canvas) {
        drawLinks(canvas);
        drawPoints(canvas);
    }

    private synchronized void drawLinks(Canvas canvas) {
        if (links.size() != 0) {
            for (Integer fromLink : links.keySet()) {

                for (Integer toLink : Objects.requireNonNull(links.get(fromLink))) {
                    MyPoint.setBackgroundColor(R.color.colorStandartLine);
                    canvas.drawLine(points.get(fromLink).getX(), points.get(fromLink).getY(), points.get(toLink).getX(), points.get(toLink).getY(), MyPoint.getBackground());
                }
            }
        }
    }

    private void drawPoints(Canvas canvas) {
        for (int i = 0; i < points.size(); ++i) {
            MyPoint p = points.get(i);
            if (p.isCur()) {
                MyPoint.setBackgroundColor(R.color.colorCurrentPoint);
                canvas.drawCircle(p.getX(), p.getY(), pointRADIUS, MyPoint.getBackground());
            }
            else if (p.isVisited()) {
                MyPoint.setBackgroundColor(R.color.colorVisitedPoint);
                canvas.drawCircle(p.getX(), p.getY(), pointRADIUS, MyPoint.getBackground());
            }
            else if (!p.isB()) {
                MyPoint.setBackgroundColor(R.color.colorStandartPoint);
                canvas.drawCircle(p.getX(), p.getY(), pointRADIUS, MyPoint.getBackground());
            }
            else {
                MyPoint.setBackgroundColor(R.color.colorChosenPoint);
                canvas.drawCircle(p.getX(), p.getY(), pointRADIUS, MyPoint.getBackground());
            }
            int len = p.getNum().getWidth()/5;
            int ip = i%5 * len;
            int jp = i/5 * len;

            Rect sourceRect = new Rect(ip, jp, ip+len, jp+len);
            Rect destRect = new Rect((int)(p.getX()-pointRADIUS), (int)(p.getY()-pointRADIUS), (int)(p.getX()+pointRADIUS), (int)(p.getY()+pointRADIUS));
            canvas.drawBitmap(p.getNum(), sourceRect, destRect, new Paint());
        }
    }

    public void clean() {
        points.clear();
        links.clear();
    }



    //algorithms

    void DFS(int s) throws InterruptedException {
        if (points.get(s).isVisited()) return;
        points.get(s).setVisited(true);
        /*points.get(s).cur = true;
        Thread.sleep(1000);
        points.get(s).cur = false;*/
        for (int u : Objects.requireNonNull(links.get(s))) {
            DFS(u);
            points.get(s).setCur(true);
            Thread.sleep(1000);
            points.get(s).setCur(false);
        }
    }

    void BFS(int s) throws InterruptedException {
        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.add(s);
        points.get(s).setVisited(true);
        points.get(s).setCur(true);
        Thread.sleep(1000);
        points.get(s).setCur(false);
        while(!q.isEmpty()) {
            int v = q.remove();
            for (int u : Objects.requireNonNull(links.get(v))) {
                if (!points.get(u).isVisited()) {
                    points.get(u).setVisited(true);
                    q.add(u);
                    points.get(u).setCur(true);
                    Thread.sleep(1000);
                    points.get(u).setCur(false);
                }

            }
        }

    }

}
