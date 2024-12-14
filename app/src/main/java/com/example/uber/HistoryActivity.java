package com.example.uber;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HistoryActivity extends AppCompatActivity implements HRecyclerViewInterface {

    SQLiteDatabase db;
    int ID;
    ArrayList<HModel> hmodels = new ArrayList<>();
    private RecyclerView hRecyclerView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_history, container, false);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        Intent intent = getIntent();
        ID = intent.getIntExtra("UID", 0);
        db = openOrCreateDatabase("uberdb", MODE_PRIVATE, null);

        hRecyclerView = (RecyclerView) findViewById(R.id.hRecyclerView);
        setUpModels(ID);

        HAdapter adapter = new HAdapter(this, hmodels, this);
        hRecyclerView.setAdapter(adapter);
        hRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpModels(int id) {

        Cursor c1 = db.rawQuery("SELECT Route.ID as ID, Route.datum as datum, User.name as name FROM Route JOIN User ON (Route.passenger = User.ID or Route.driver=User.ID) WHERE User.ID = ?", new String[]{String.valueOf(id)});
        int count = c1.getCount();
        String[] dates = new String[count];
        String[] names = new String[count];
        int[] RIDs = new int[count];
        c1.moveToFirst();
        Cursor c2 = null;
        int i = 0;
        while (!c1.isAfterLast()) {
            dates[i] = c1.getString(c1.getColumnIndexOrThrow("datum"));
            RIDs[i] = c1.getInt(c1.getColumnIndexOrThrow("ID"));
            c2 = db.rawQuery("SELECT User.name as name FROM Driver LEFT JOIN User ON User.ID = Driver.ID LEFT JOIN Route ON Driver.ID = Route.driver WHERE Route.ID = ?",new String[]{String.valueOf(RIDs[i])});
            c2.moveToFirst();
            names[i] = c2.getString(c2.getColumnIndexOrThrow("name"));
            c1.moveToNext();
            i++;
        }

        c1.close();
        c2.close();

        for (i = 0; i < count; i++) {
            hmodels.add(new HModel(names[i], dates[i], RIDs[i]));
        }
    }

    @Override
    public void onItemClick(int position) {
        int RID = hmodels.get(position).getID();
        Intent intent2 = new Intent(this, DetailsActivity.class);
        intent2.putExtra("RID", RID);
        startActivity(intent2);
    }
}

