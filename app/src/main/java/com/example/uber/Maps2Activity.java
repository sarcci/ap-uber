package com.example.uber;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uber.databinding.ActivityMaps2Binding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class Maps2Activity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {



    private GoogleMap mMap;
    private ActivityMaps2Binding binding;
    SQLiteDatabase db;
    private LatLng myLocation;
    double myLat = 0.0;
    double myLng = 0.0;

    double lat1 = 0.0;
    double lng1 = 0.0;

    double lat2 = 0.0;
    double lng2 = 0.0;
    private static final int req_code = 1;
    int RID;
    TextView ime, rejting, patnik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        RID = intent.getIntExtra("RID", 0);
        db = openOrCreateDatabase("uberdb", MODE_PRIVATE, null);

        Cursor c2 = db.rawQuery("SELECT name FROM User LEFT JOIN Route ON Route.passenger = User.ID WHERE Route.ID = ?", new String[]{String.valueOf(RID)});
        c2.moveToFirst();
        String name = c2.getString(c2.getColumnIndexOrThrow("name"));
        patnik = (TextView) findViewById(R.id.patnik);
        patnik.setText(name);
        c2.close();

        Cursor c = db.rawQuery("SELECT * FROM Route WHERE ID = ?", new String[]{String.valueOf(RID)});
        c.moveToFirst();
        int id = c.getInt(c.getColumnIndexOrThrow("driver"));
        lat1 = c.getDouble(c.getColumnIndexOrThrow("lat1"));
        lat2 = c.getDouble(c.getColumnIndexOrThrow("lat2"));
        lng1 = c.getDouble(c.getColumnIndexOrThrow("lng1"));
        lng2 = c.getDouble(c.getColumnIndexOrThrow("lng2"));
        c.close();
        setDet(id);
    }

    private void setDet(int ID) {

        Cursor c3 = db.rawQuery("SELECT (SUM(rating1) + SUM(rating2)) * 1.0 / (COUNT(rating1) + COUNT(rating2)) AS rr FROM Route WHERE Route.passenger = ? OR Route.driver = ?",new String[]{String.valueOf(ID), String.valueOf(ID)});
        c3.moveToFirst();
        float rr = c3.getFloat(c3.getColumnIndexOrThrow("rr"));
        ContentValues values = new ContentValues();
        values.put("rating", rr);
        int effected = db.update("User", values,"ID = ?", new String[]{String.valueOf(ID)});

        Cursor c1 = db.rawQuery("SELECT * FROM User WHERE User.ID = ?", new String[]{String.valueOf(ID)});
        c1.moveToFirst();
        ime = (TextView) findViewById(R.id.profil);
        rejting = (TextView) findViewById(R.id.rating);
        ime.setText(c1.getString(c1.getColumnIndexOrThrow("name")));
        float r = c1.getFloat(c1.getColumnIndexOrThrow("rating"));
        String rt = String.format("%.1f", r);
        rejting.setText(rt);
        c1.close();
        c3.close();
    }

    @Override
    public void onRequestPermissionsResult(int reqCode, @NonNull String permissions[], @NonNull int[] grantResults) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(this);

    }

    private LatLng getMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, req_code);
        }

        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc == null) {
            loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (loc == null) {
            loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }
        if (loc == null) {
            return null;
        } else {
            myLat = loc.getLatitude();
            myLng = loc.getLongitude();
            return new LatLng(myLat, myLng);
        }
    }
    public void onMapLoaded() {

        myLocation = getMyLocation();
        if (myLocation == null) {
            Toast.makeText(this, "Не може да се пристапи до локација. Проверете Settings.", Toast.LENGTH_LONG).show();
        } else {
            mMap.addMarker(new MarkerOptions()
                    .position(myLocation)
                    .title("ЈАС!")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
            );
        }
        LatLng start = new LatLng(lat1, lng1);
        LatLng end = new LatLng(lat2, lng2);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start,15));

        mMap.addMarker(new MarkerOptions()
                .position(start)
                .title("Почетна точка")
        );
        mMap.addMarker(new MarkerOptions()
                .position(end)
                .title("Дестинација")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
        );

        mMap.addPolyline(new PolylineOptions()
                .add(myLocation)
                .add(start)
                .add(end)
        );
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

}