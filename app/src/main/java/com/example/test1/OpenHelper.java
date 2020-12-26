package com.example.test1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class OpenHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "graphs_algorithms.db"; // название бд
    static final int SCHEMA = 1; // версия базы данных

    static final String TABLE_GRAPH = "Graph";
    static final String GRAPH_ID = "_id";
    static final String GRAPH_NAME = "name";

    static final String TABLE_POINT = "Point";
    static final String POINT_ID = "_id";
    static final String POINT_X = "x";
    static final String POINT_Y = "y";
    static final String POINT_B = "b";
    static final String POINT_CUR = "cur";
    static final String POINT_FROMGRAPH = "fromgraph";

    static final String TABLE_LINE = "Line";
    static final String LINE_ID = "_id";
    static final String LINE_FROMPOINT = "frompoint";
    static final String LINE_TOPOINT = "topoint";
    static final String LINE_FROMGRAPH = "fromgraph";


    public OpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_GRAPH + " ("
                + GRAPH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GRAPH_NAME + " TEXT"
                + ");"
        );
        db.execSQL("CREATE TABLE " + TABLE_POINT + " ("
                + POINT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + POINT_X + " INTEGER, "
                + POINT_Y + " INTEGER, "
                + POINT_B + " INTEGER, "
                + POINT_CUR + " INTEGER, "
                + POINT_FROMGRAPH + " INTEGER, "
                + "FOREIGN KEY (" + POINT_FROMGRAPH + ") REFERENCES " + TABLE_GRAPH + " (" + GRAPH_ID + ")"
                + ");"
        );
        db.execSQL("CREATE TABLE " + TABLE_LINE + " ("
                + LINE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LINE_FROMPOINT + " INTEGER, "
                + LINE_TOPOINT + " INTEGER, "
                + LINE_FROMGRAPH + " INTEGER, "
                + "FOREIGN KEY (" + LINE_FROMPOINT + ") REFERENCES " + TABLE_POINT + " (" + POINT_ID + "), "
                + "FOREIGN KEY (" + LINE_TOPOINT + ") REFERENCES " + TABLE_POINT + " (" + POINT_ID + "), "
                + "FOREIGN KEY (" + LINE_FROMGRAPH + ") REFERENCES " + TABLE_GRAPH + " (" + GRAPH_ID + ")"
                + ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
