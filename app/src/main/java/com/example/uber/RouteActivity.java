package com.example.uber;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class RouteActivity extends AppCompatActivity {

    SQLiteDatabase db;
    int VID, RID, price;
    TextView vozac, vozilo, tablicki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        db = openOrCreateDatabase("uberdb", MODE_PRIVATE, null);

        Intent intent = getIntent();
        RID = intent.getIntExtra("RID", 0);
        VID = intent.getIntExtra("VID", 3);

//        ContentValues values = new ContentValues();
//        values.put("price", price);
//        int effected = db.update("Route", values,"ID = ?", new String[]{String.valueOf(RID)});

        Cursor c1 = db.rawQuery("SELECT * FROM Driver LEFT JOIN User ON Driver.ID = User.ID WHERE Driver.ID = ?", new String[]{String.valueOf(VID)});
        c1.moveToFirst();
        vozac = (TextView) findViewById(R.id.vozac);
        vozac.setText(c1.getString(c1.getColumnIndexOrThrow("name")));
        vozilo = (TextView) findViewById(R.id.vozilo);
        vozilo.setText(c1.getString(c1.getColumnIndexOrThrow("model")));
        tablicki = (TextView) findViewById(R.id.tablicki);
        tablicki.setText(c1.getString(c1.getColumnIndexOrThrow("reg")));
        c1.close();
    }

    public void evaluate(View view) {
        Intent intent = new Intent(this, EvaluateActivity.class);
        intent.putExtra("RID", RID);
        startActivity(intent);
    }
}