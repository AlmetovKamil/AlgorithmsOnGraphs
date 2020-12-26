package com.example.test1;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import static com.example.test1.MainAction.graph;
//import static com.example.test1.MainAction.startAlgorithm;

public class DrawThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private volatile boolean running = true;//флаг для остановки потока

    DrawThread(Context context, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    void requestStop() {
        running = false;
    }

    @Override
    public void run() {

        while (running) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                try {
                    canvas.drawARGB(255, 255, 255, 255);
                    graph.drawGraph(canvas);
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
