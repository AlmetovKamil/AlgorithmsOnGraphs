package com.example.test1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class GraphsDbDialogFragment extends DialogFragment {


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.favorite_graphs, null);

        return builder
                .setTitle("Выберите граф:")
                .setView(R.layout.favorite_graphs)
                .setPositiveButton("OK", null)
                .setNegativeButton("Отмена", null)
                .create();
    }



}
