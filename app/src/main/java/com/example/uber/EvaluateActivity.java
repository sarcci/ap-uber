package com.example.uber;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class EvaluateActivity extends AppCompatActivity {
    int RID;
    SQLiteDatabase db;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Оцена од патникот кон возачот

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_evaluate);

        Intent intent = getIntent();
        RID = intent.getIntExtra("RID", 0);

        final RatingBar rb = (RatingBar) findViewById(R.id.bar);
        Button submit = (Button) findViewById(R.id.kopce);

        db = openOrCreateDatabase("uberdb", MODE_PRIVATE, null);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float r = rb.getRating();
                ContentValues values = new ContentValues();
                values.put("rating1", r);
                values.put("active", 0);
                int effected = db.update("Route", values,"ID = ?", new String[]{String.valueOf(RID)});
                txt = (TextView) findViewById(R.id.txt);
                txt.setText("Успешно го оценивте возачот.");
            }
        });

    }
}