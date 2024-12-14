package com.example.uber;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailsActivity extends AppCompatActivity {
    SQLiteDatabase db;

    TextView date, rating1, rating2, vozac, cena, patnik, model, reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        int RID = intent.getIntExtra("RID", 0);

        db = openOrCreateDatabase("uberdb", MODE_PRIVATE, null);

        date = (TextView) findViewById(R.id.date);
        rating1 = (TextView) findViewById(R.id.rating1);
        rating2 = (TextView) findViewById(R.id.rating2);
        vozac = (TextView) findViewById(R.id.vozac);
        cena = (TextView) findViewById(R.id.cena);
        patnik = (TextView) findViewById(R.id.patnik);
        model = (TextView) findViewById(R.id.model);
        reg = (TextView) findViewById(R.id.reg);

        Cursor c1 = db.rawQuery("SELECT * FROM Route LEFT JOIN Driver ON Route.driver = Driver.ID LEFT JOIN User ON Route.passenger = User.ID WHERE Route.ID = ?", new String[]{String.valueOf(RID)});
        c1.moveToFirst();
        date.setText(c1.getString(c1.getColumnIndexOrThrow("datum")));
        rating1.setText(c1.getString(c1.getColumnIndexOrThrow("rating1")));
        rating2.setText(c1.getString(c1.getColumnIndexOrThrow("rating2")));
        model.setText(c1.getString(c1.getColumnIndexOrThrow("model")));
        reg.setText(c1.getString(c1.getColumnIndexOrThrow("reg")));
        patnik.setText(c1.getString(c1.getColumnIndexOrThrow("name")));
        cena.setText(c1.getString(c1.getColumnIndexOrThrow("price")));
        c1.close();

        Cursor c2 = db.rawQuery("SELECT name FROM Route LEFT JOIN User ON Route.driver = User.ID WHERE Route.ID = ?" , new String[]{String.valueOf(RID)});
        c2.moveToFirst();
        vozac.setText(c2.getString(c2.getColumnIndexOrThrow("name")));
        c2.close();
    }
}