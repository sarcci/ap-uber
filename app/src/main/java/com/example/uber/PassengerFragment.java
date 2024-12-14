package com.example.uber;

import static android.content.Context.MODE_PRIVATE;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PassengerFragment extends Fragment {

    int ID =0 ;
    double lat1, lng1, lat2, lng2;
    private SQLiteDatabase db;
    TextView ime ,rejting;


    public PassengerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_passenger, container, false);
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = getActivity().openOrCreateDatabase("uberdb", MODE_PRIVATE, null);
        Intent intent = getActivity().getIntent();
        ID = intent.getIntExtra("ID", 0);
        lat1 = intent.getDoubleExtra("lat1", 0.0);
        lng1 = intent.getDoubleExtra("lng1", 0.0);
        lat2 = intent.getDoubleExtra("lat2", 0.0);
        lng2 = intent.getDoubleExtra("lng2", 0.0);

        Cursor c1 = db.rawQuery("SELECT * FROM User WHERE User.ID = ?", new String[]{String.valueOf(ID)});
        c1.moveToFirst();
        ime = (TextView) getView().findViewById(R.id.profil);
        rejting = (TextView) getView().findViewById(R.id.rating);
        ime.setText(c1.getString(c1.getColumnIndexOrThrow("name")));
        float r = c1.getFloat(c1.getColumnIndexOrThrow("rating"));
        String rt = String.format("%.1f", r);
        rejting.setText(rt);
        c1.close();

        RvFragment frag = (RvFragment) getFragmentManager().findFragmentById(R.id.fragment2);
        frag.setID(ID, lat1, lng1, lat2, lng2);

        Button kopce = (Button) getActivity().findViewById(R.id.kopce);
        kopce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), HistoryActivity.class);
                intent2.putExtra("UID", ID);
                startActivity(intent2);
            }
        });
    }
}
