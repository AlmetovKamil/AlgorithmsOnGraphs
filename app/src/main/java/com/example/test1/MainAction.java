package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MainAction extends AppCompatActivity {
    public static Graph graph = new Graph();
    //public static MyButton startAlgorithm = new MyButton(20, 20, BitmapFactory.decodeResource(Res.getInstance().getResources(), R.drawable.launch1), (byte)1);





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_main);



    }

    public void showDialog(View view) {
        AlgorithmsDialogFragment dialog = new AlgorithmsDialogFragment();
        dialog.show(getSupportFragmentManager(), "algorithms");
    }

    public void showFavoriteGraphs(View view) {
        GraphsDbDialogFragment dialog = new GraphsDbDialogFragment();
        //outGraph();
        dialog.show(getSupportFragmentManager(), "favorite_graphs");

    }



    /*public void setFavorites() {

        OpenHelper sqlHelper;
        SQLiteDatabase db;
        Cursor userCursor;
        SimpleCursorAdapter userAdapter;
        // открываем подключение
        sqlHelper = new OpenHelper(this);
        db = sqlHelper.getReadableDatabase();

        //получаем данные из бд в виде курсора
        userCursor =  db.rawQuery("select * from "+ OpenHelper.TABLE_GRAPH, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[] {OpenHelper.GRAPH_ID, OpenHelper.GRAPH_NAME};
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(this, R.layout.list_items,
                userCursor, headers, new int[]{R.id.id, R.id.name}, 0);
        userList.setAdapter(userAdapter);
    }


     */



    public void startDFS(View view) {
        // если переключатель отмечен
        boolean checked = ((RadioButton) view).isChecked();
        // Получаем нажатый переключатель
        switch(view.getId()) {
            case R.id.dfs:
                if (checked){
                    graph.setStart(true);
                }
                break;

        }
    }

    public void clearGraph(View view) {
        graph.clean();
    }

    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        OpenHelper sqlHelper;
        SQLiteDatabase db;
        sqlHelper = new OpenHelper(this);
        db = sqlHelper.getWritableDatabase();
        db.delete(OpenHelper.TABLE_GRAPH, null, null);
        db.delete(OpenHelper.TABLE_LINE, null, null);
        db.delete(OpenHelper.TABLE_POINT, null, null);
    }

    public void addToFavorites(View view) {
        OpenHelper sqlHelper;
        SQLiteDatabase db;
        sqlHelper = new OpenHelper(this);
        db = sqlHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(OpenHelper.GRAPH_NAME, "Graph1");
        db.delete(OpenHelper.TABLE_GRAPH, null, null);
        db.delete(OpenHelper.TABLE_POINT, null, null);
        db.delete(OpenHelper.TABLE_LINE, null, null);
        //db.delete(OpenHelper.TABLE_GRAPH, "_id = ?", new String[]{String.valueOf(1)});
        long graph_id = db.insert(OpenHelper.TABLE_GRAPH, null, cv);
        //Toast toast = Toast.makeText(this, Long.toString(graph_id), Toast.LENGTH_LONG);
        //toast.show();
        for (MyPoint point : graph.getPoints()) {
            cv = new ContentValues();
            cv.put(OpenHelper.POINT_X, point.getX());
            cv.put(OpenHelper.POINT_Y, point.getY());
            cv.put(OpenHelper.POINT_B, point.isB());
            cv.put(OpenHelper.POINT_CUR, point.isCur());
            cv.put(OpenHelper.POINT_FROMGRAPH, graph_id);
            //db.delete(OpenHelper.TABLE_POINT, OpenHelper.POINT_FROMGRAPH + " = ?", new String[]{String.valueOf(1)});
            db.insert(OpenHelper.TABLE_POINT, null, cv);
        }

        int linksSize = graph.getLinks().size();

        for (int i = 0; i < linksSize; ++i) {
            for (int j : Objects.requireNonNull(graph.getLinks().get(i))) {
                cv = new ContentValues();
                cv.put(OpenHelper.LINE_FROMPOINT, i+1);
                cv.put(OpenHelper.LINE_TOPOINT, j+1);
                cv.put(OpenHelper.LINE_FROMGRAPH, graph_id);
                //db.delete(OpenHelper.TABLE_LINE, OpenHelper.LINE_FROMGRAPH + " = ?", new String[]{String.valueOf(1)});
                db.insert(OpenHelper.TABLE_LINE, null, cv);
            }
        }
        db.close();
        Toast toast = Toast.makeText(this, "Граф успешно сохранён!", Toast.LENGTH_LONG);
        toast.show();
    }

    public synchronized void outGraph(View view) {
        long id = 1;
        if (id >= 0) {
            OpenHelper sqlHelper;
            SQLiteDatabase db;
            sqlHelper = new OpenHelper(this);
            db = sqlHelper.getReadableDatabase();
            Cursor cursor = db.query(OpenHelper.TABLE_GRAPH, null, null, null, null, null, null);
            cursor.moveToFirst();
            Cursor cursor1 = db.query(OpenHelper.TABLE_POINT, null, null, null, null, null, null);
            cursor1.moveToFirst();
            ArrayList<MyPoint> points = new ArrayList<>();
            if (!cursor1.isAfterLast()) {
                do {
                    MyPoint p = new MyPoint();
                    p.setX(cursor1.getInt(1));
                    p.setY(cursor1.getInt(2));
                    p.setB(cursor1.getInt(3) > 0);
                    p.setCur(cursor1.getInt(4) > 0);
                    points.add(p);
                } while (cursor1.moveToNext());
            }
            ConcurrentHashMap<Integer, ArrayList<Integer>> links = new ConcurrentHashMap<>();
            cursor1 = db.query(OpenHelper.TABLE_LINE, null, null, null, null, null, null);
            cursor1.moveToFirst();
            if (!cursor1.isAfterLast()) {
                do {
                    int index1 = cursor1.getInt(1) - 1;
                    int index2 = cursor1.getInt(2) - 1;
                    if (links.containsKey(index1)) {
                        if (!Objects.requireNonNull(links.get(index1)).contains(index2))
                            Objects.requireNonNull(links.get(index1)).add(index2);
                    } else {
                        links.put(index1, new ArrayList<Integer>());
                        Objects.requireNonNull(links.get(index1)).add(index2);
                    }
                    if (links.containsKey(index2)) {
                        if (!Objects.requireNonNull(links.get(index2)).contains(index1))
                            Objects.requireNonNull(links.get(index2)).add(index1);
                    } else {
                        links.put(index2, new ArrayList<Integer>());
                        Objects.requireNonNull(links.get(index2)).add(index1);
                    }


                } while (cursor1.moveToNext());
            }
            graph.setLinks(links);
            graph.setPoints(points);
        }
    }


}
