package com.example.uber;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    EditText username;
    EditText password;
    boolean dr;
    int id = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // rating1 оцена за возач
        // rating2 оцена за патник


        db = openOrCreateDatabase("uberdb", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS User (ID INTEGER PRIMARY KEY AUTOINCREMENT, username varchar(50), password varchar(50), name varchar(50), rating float DEFAULT 5.0);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Passenger (ID INTEGER PRIMARY KEY,  distance float DEFAULT 0.0, FOREIGN KEY (ID) REFERENCES User(ID));");
        db.execSQL("CREATE TABLE IF NOT EXISTS Driver (ID INTEGER PRIMARY KEY, model varchar(100) DEFAULT NULL, reg varchar(50) DEFAULT NULL, FOREIGN KEY (ID) REFERENCES User(ID));");
        db.execSQL("CREATE TABLE IF NOT EXISTS Offer (ID INTEGER PRIMARY KEY AUTOINCREMENT, driver INTEGER, car varchar(50), start_price int, price_km int, interval int, active int, CONSTRAINT FK_Driver FOREIGN KEY (driver) REFERENCES User(ID));");
        db.execSQL("CREATE TABLE IF NOT EXISTS Route (ID INTEGER PRIMARY KEY AUTOINCREMENT, datum TEXT, driver INTEGER DEFAULT NULL, passenger INTEGER, lat1 double, lat2 double, lng1 double, lng2 double, rating1 float DEFAULT 0.0, rating2 float DEFAULT 0.0, active int DEFAULT 0, price int DEFAULT 0, CONSTRAINT FK_Driver FOREIGN KEY (driver) REFERENCES User(ID), CONSTRAINT FK_Passenger FOREIGN KEY (passenger) REFERENCES User(ID) );");

        RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        int checkedID = group.getCheckedRadioButtonId();
        if (checkedID == R.id.radio_driver) {
            dr = true;
        } else {
            dr = false;
        }

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_driver) {
                    dr = true;
                } else {
                    dr = false;
                }
            }
        });

    }

    public void database_input(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    public void database_check(View view) {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        if (username.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Внесете корисничко име.", Toast.LENGTH_SHORT).show();
        } else if (password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Внесете лозинка.", Toast.LENGTH_SHORT).show();
        } else {
            // Проверка на корисничко име
            Cursor c1 = db.rawQuery("SELECT password FROM User WHERE username = '" + username.getText().toString() + "'", null);
            if (c1.moveToFirst()) {
                // Проверка на лозинка
                if (c1.getString(0).equals(password.getText().toString())) {
                    Toast.makeText(this, "Успешно се најавивте.", Toast.LENGTH_SHORT).show();
                    Cursor c2 = db.rawQuery("SELECT ID FROM User WHERE username = '" + username.getText().toString() + "'", null);
                    c2.moveToFirst();
                    id = c2.getInt(0);
                    c2.close();
                    if (dr) {
                        // Проверка ако веќе е внесен
                        db.execSQL("INSERT OR IGNORE INTO Driver(ID) VALUES (?)", new Object[]{id});
                        Cursor c3 = db.rawQuery("SELECT * FROM Route WHERE driver= ?", new String[]{String.valueOf(id)});
                        if (c3.getCount() == 0) {
                            // првпат се најавува
                            Intent intent = new Intent(this, DriverActivity.class);
                            intent.putExtra("ID", id);
                            startActivity(intent);
                        } else {
                            c3.moveToLast();
                            int active = c3.getInt(c3.getColumnIndexOrThrow("active"));
                            float rating2 = c3.getFloat(c3.getColumnIndexOrThrow("rating2"));
                            int RID = c3.getInt(c3.getColumnIndexOrThrow("ID"));
                            if (active == 1) {
                                // патник ја има прифатено понудата на возачот
                                Intent intent2 = new Intent(this, Maps2Activity.class);
                                intent2.putExtra("RID", RID);
                                startActivity(intent2);
                            } else if (rating2 == 0) {
                                // патникот ја завршил рутата, возачот го нема оценето патникот
                                Intent intent3 = new Intent(this, Evaluate2Activity.class);
                                intent3.putExtra("RID", RID);
                                startActivity(intent3);
                            } else {
                                Intent intent = new Intent(this, DriverActivity.class);
                                intent.putExtra("ID", id);
                                startActivity(intent);
                            }
                            c3.close();
                        }
                    } else {
                        // Проверка ако веќе е внесен
                        db.execSQL("INSERT OR IGNORE INTO Passenger(ID) VALUES (?)", new Object[]{id});
                        Cursor cc = db.rawQuery("SELECT * FROM Route WHERE passenger = ?", new String[]{String.valueOf(id)});
                        if (cc.getCount()==0) {
                            Intent intent = new Intent(this, MapsActivity.class);
                            intent.putExtra("ID", id);
                            startActivity(intent);
                        } else {
                            cc.moveToLast();
                            int active = cc.getInt(cc.getColumnIndexOrThrow("active"));
                            int RID = cc.getInt(cc.getColumnIndexOrThrow("ID"));
                            if (active==1){
                                int VID = cc.getInt(cc.getColumnIndexOrThrow("driver"));
                                Intent intent3 = new Intent(this, RouteActivity.class);
                                intent3.putExtra("RID",RID);
                                intent3.putExtra("VID",VID);
                                startActivity(intent3);
                            } else { // active = 0
                                Intent intent2 = new Intent(this, MapsActivity.class);
                                intent2.putExtra("ID", id);
                                startActivity(intent2);
                            }
                        }
                        cc.close();
                    }
                    c1.close();
                } else {
                    Toast.makeText(this, "Погрешна лозинка.", Toast.LENGTH_SHORT).show();
                    c1.close();
                }
                c1.close();
            } else {
                Toast.makeText(this, "Не постои корисник со тоа корисничко име.", Toast.LENGTH_LONG).show();
                c1.close();
            }
            c1.close();
        }
    }

}