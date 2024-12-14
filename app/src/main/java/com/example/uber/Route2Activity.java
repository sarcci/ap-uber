package com.example.uber;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Route2Activity extends AppCompatActivity {

    SQLiteDatabase db;

    TextView txt1, txt, patnik, mapa;
    ProgressBar progressBar;
    int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_route2);

        db = openOrCreateDatabase("uberdb", MODE_PRIVATE, null);

        Intent intent = getIntent();
        ID = intent.getIntExtra("ID", 0);

        txt1 = (TextView) findViewById(R.id.txt1);
        txt = (TextView) findViewById(R.id.txt);
        patnik = (TextView) findViewById(R.id.patnik);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mapa = (TextView) findViewById(R.id.mapa);

        //check(ID);

    }

    private void check(int id) {
        Cursor c1 = db.rawQuery("SELECT * FROM Route LEFT JOIN User ON Route.passenger = User.ID WHERE Route.driver = ? AND Route.active = 1", new String[]{String.valueOf(id)});
        if (c1.getCount() == 0) {
            txt1.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            txt.setVisibility(View.INVISIBLE);
            txt1.setVisibility(View.INVISIBLE);
        } else {
            c1.moveToFirst();
            if (!c1.isNull(c1.getColumnIndexOrThrow("driver"))) {
                txt1.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                txt.setVisibility(View.VISIBLE);
                txt1.setText(c1.getString(c1.getColumnIndexOrThrow("name")));
                txt1.setVisibility(View.VISIBLE);
            }
            c1.close();
        }
        c1.close();
    }

}