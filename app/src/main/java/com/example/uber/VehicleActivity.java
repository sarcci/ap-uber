package com.example.uber;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VehicleActivity extends AppCompatActivity {
    int id;
    SQLiteDatabase db;
    EditText m, r;
    TextView z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vehicle);

        db = openOrCreateDatabase("uberdb", MODE_PRIVATE, null);

        Intent intent = getIntent();
        id = intent.getIntExtra("ID", 0);
    }

    public void changeVehicle(View view) {
        m = (EditText) findViewById(R.id.model);
        r = (EditText) findViewById(R.id.reg);
        z = (TextView) findViewById(R.id.zacuvano);
        String mod, reg;
        mod = m.getText().toString();
        reg = r.getText().toString();
        db.execSQL("INSERT OR REPLACE INTO Driver(ID, model, reg) VALUES (?, ?, ?)", new Object[]{id, mod, reg});
        z.setText("Успешно внесовте возило.");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Intent intent2 = new Intent(this, DriverActivity.class);
        intent2.putExtra("ID", id);
        startActivity(intent2);
    }
}