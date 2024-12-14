package com.example.uber;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class RvFragment extends Fragment implements RecyclerViewInterface {

    private SQLiteDatabase db;
    private RecyclerView mRecyclerView;
    private int ID = 0;

    double lat1r, lng1r, lat2r, lng2r;
    int PassID = 1;
    private ArrayList<Model> models = new ArrayList<>();

    public RvFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rv, container, false);
    }

    public void setID(int ID, double lat1, double lng1, double lat2, double lng2) {
        PassID = ID;
        lat1r = lat1;
        lng1r = lng1;
        lat2r = lat2;
        lng2r = lng2;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = getActivity().getIntent();
        ID = intent.getIntExtra("ID", 0);

        db = getActivity().openOrCreateDatabase("uberdb", Context.MODE_PRIVATE, null);

        mRecyclerView = getView().findViewById(R.id.mRecyclerView);
        setUpModels();

        myAdapter adapter = new myAdapter(getActivity(), models, this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setUpModels() {
        Cursor c1 = db.rawQuery("SELECT * FROM Offer LEFT JOIN Driver ON Offer.driver = Driver.ID LEFT JOIN User ON Offer.driver = User.ID", null);
        Cursor c2 = db.rawQuery("SELECT distance FROM Passenger WHERE ID = ?", new String[]{String.valueOf(PassID)});
        c2.moveToFirst();
        float distance = c2.getFloat(c2.getColumnIndexOrThrow("distance"));
        c2.close();
        int count = c1.getCount();

        String[] names = new String[count];
        String[] vehicles = new String[count];
        Float[] ratings = new Float[count];
        int[] drivers = new int[count];
        int[] prices = new int[count];

        c1.moveToFirst();
        int i = 0;
        int start_price, price_km;
        while (!c1.isAfterLast()) {
            names[i] = c1.getString(c1.getColumnIndexOrThrow("name")); // име на возачот
            ratings[i] = c1.getFloat(c1.getColumnIndexOrThrow("rating")); // рејтинг на возачот
            vehicles[i] = c1.getString(c1.getColumnIndexOrThrow("model")); // модел на автомобил
            // да се додаде и регистрација
            start_price = c1.getInt(c1.getColumnIndexOrThrow("start_price"));
            price_km = c1.getInt(c1.getColumnIndexOrThrow("price_km"));
            prices[i] = (int)(start_price + price_km*distance);
            drivers[i] = c1.getInt(c1.getColumnIndexOrThrow("driver")); // не знам дали е потребно
            c1.moveToNext();
            i++;
        }
        c1.close();

        // Сортирање на возачите по рејтинг


        for (i = 0; i < count; i++) {
            models.add(new Model(ratings[i], names[i], vehicles[i], prices[i], drivers[i]));
        }

        Collections.sort(models, new Comparator<Model>() {
            @Override
            public int compare(Model o1, Model o2) {
                return Float.compare(o2.getRating(), o1.getRating());
            }
        });
    }

    public void history(View view) {
        Intent intent = new Intent(getActivity(), HistoryActivity.class);
        intent.putExtra("ID", ID);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        int VID = models.get(position).getDriver();
        int price = models.get(position).getPrice();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(calendar.getTime());
        db.execSQL("INSERT INTO Route(datum, driver, passenger, active, lat1, lng1, lat2, lng2, price) VALUES (?, ?, ?, 1, ?, ?, ?, ?, ?)", new Object[]{date, VID, PassID, lat1r, lng1r, lat2r, lng2r, price});
        Cursor c = db.rawQuery("SELECT ID FROM Route", null);
        c.moveToLast();
        int RID = c.getInt(c.getColumnIndexOrThrow("ID"));
        c.close();
        Intent intent = new Intent(getActivity(), RouteActivity.class);
        intent.putExtra("RID", RID);
        intent.putExtra("VID", VID);
        startActivity(intent);
    }
}
