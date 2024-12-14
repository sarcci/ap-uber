package com.example.uber;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class RegisterActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    EditText name;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        db = openOrCreateDatabase("uberdb", MODE_PRIVATE, null);
       }

    public void database_input(View view) {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.name);
        if (username.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Внесете корисничко име.", Toast.LENGTH_SHORT).show();
        } else {
            Cursor c1 = db.rawQuery("SELECT username FROM User WHERE username = '" + username.getText().toString() + "'", null);
            if(c1.getCount() != 0)  {
                Toast.makeText(this, "Веќе постои корисник со тоа корисничко име.", Toast.LENGTH_LONG).show();
                c1.close();
            } else if (password.getText().length() == 0) {
                Toast.makeText(this, "Внесете лозинка.", Toast.LENGTH_SHORT).show();
                c1.close();
            } else {
                db.execSQL("INSERT INTO User (username, password, name) VALUES ('" + username.getText() + "', '" + password.getText() + "', '" + name.getText()+ "');");
                Toast.makeText(this, "Успешно се регистриравте во системот.", Toast.LENGTH_LONG).show();
                c1.close();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            c1.close();
        }
    }
}