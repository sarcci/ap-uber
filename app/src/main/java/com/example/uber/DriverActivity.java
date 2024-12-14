package com.example.uber;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DriverActivity extends AppCompatActivity {

    SQLiteDatabase db;
    int ID;

    TextView ime, rejting, veh_shown;

    TextView offer_prices, offer_pricekm, offer_time;
    boolean car = false; // Ако нема внесено возило, да не започнува понуда - недовршено
    String vehicleModel = "car";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        db = openOrCreateDatabase("uberdb", MODE_PRIVATE, null);

        Intent intent = getIntent();
        ID = intent.getIntExtra("ID", 0);

        Button kopce = (Button) findViewById(R.id.kopce);
        kopce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(DriverActivity.this, HistoryActivity.class);
                intent2.putExtra("UID", ID);
                startActivity(intent2);
            }
        });

        setDet(ID);
    }

    private void setDet(int id) {
        float rr = 0;
        Cursor c3 = db.rawQuery("SELECT SUM(rating1) * 1.0 / COUNT(rating1) AS rr1, COUNT(rating1) as num1 FROM Route WHERE Route.driver = ? AND Route.active=0", new String[]{String.valueOf(id)});
        Cursor c4 = db.rawQuery("SELECT SUM(rating2) * 1.0 / COUNT(rating2) AS rr2, COUNT(rating2) as num2 FROM Route WHERE Route.passenger = ? AND Route.active=0", new String[]{String.valueOf(id)});
        c3.moveToFirst();
        c4.moveToFirst();
        int num1 = c3.getInt(c3.getColumnIndexOrThrow("num1"));
        int num2 = c4.getInt(c4.getColumnIndexOrThrow("num2"));
        float rr1 = c3.getFloat(c3.getColumnIndexOrThrow("rr1"));
        float rr2 = c4.getFloat(c4.getColumnIndexOrThrow("rr2"));
        if (num1 == 0 && num2 != 0) {
            rr = rr2;
        } else if (num2 == 0 && num1 != 0) {
            rr = rr1;
        } else if (num1 != 0 && num2 != 0) {
            rr = (rr1 * num1 + rr2 * num2) / (num1 + num2);
        } else {
            rr = 0;
        }
        c3.close();
        c4.close();

        ContentValues values = new ContentValues();
        values.put("rating", rr);
        int effected = db.update("User", values,"ID = ?", new String[]{String.valueOf(id)});

        Cursor c1 = db.rawQuery("SELECT * FROM User WHERE User.ID = ?", new String[]{String.valueOf(id)});
        Cursor c2 = db.rawQuery("SELECT * FROM Driver WHERE Driver.ID = ?", new String[]{String.valueOf(id)});
        c1.moveToFirst();
        c2.moveToFirst();
        ime = (TextView) findViewById(R.id.profil);
        rejting = (TextView) findViewById(R.id.rating);
        ime.setText(c1.getString(c1.getColumnIndexOrThrow("name")));
        float r = c1.getFloat(c1.getColumnIndexOrThrow("rating"));
        String rt = String.format("%.1f", r);
        rejting.setText(rt);
        veh_shown = findViewById(R.id.vozilo);

        if (!c2.isNull(c2.getColumnIndexOrThrow("model"))) {
            vehicleModel = c2.getString(c2.getColumnIndexOrThrow("model"));
            car = true;
            veh_shown.setText(vehicleModel);
        }
        c1.close();
        c2.close();
    }
    public void changeVehicle(View view) {

        Intent intent = new Intent(this, VehicleActivity.class);
        intent.putExtra("ID", ID);
        startActivity(intent);
    }
    public void offerStart(View view) {
        if (!car) {
            Toast.makeText(this, "Внесете возило.", Toast.LENGTH_LONG).show();
        }
        else {
            String ops, opkm;
            int t;
            offer_prices = (TextView) findViewById(R.id.pricestart);
            offer_pricekm = (TextView) findViewById(R.id.pricekm);
            offer_time = (TextView) findViewById(R.id.interval);
            ops = offer_prices.getText().toString();
            opkm = offer_pricekm.getText().toString();
            t = Integer.parseInt(offer_time.getText().toString());
            db.execSQL("INSERT INTO Offer (driver, car, start_price, price_km, interval, active) VALUES (?, ?, ?, ?, ?, ?)", new Object[]{ID, vehicleModel, ops, opkm, t, 1});
            Toast.makeText(this, "Почнавте понуда.", Toast.LENGTH_LONG).show();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Intent intent2 = new Intent(this, Route2Activity.class);
            intent2.putExtra("ID", ID);
            startActivity(intent2);
        }
    }
}
