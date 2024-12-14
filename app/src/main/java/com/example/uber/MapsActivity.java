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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.uber.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener {

    double distance;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    SQLiteDatabase db;

    double myLat = 0.0;
    double myLng = 0.0;

    double destLat = 0.0;
    double destLng = 0.0;
    private LatLng myLocation;
    private static final int req_code = 1;
    int ID;
    TextView ime, rejting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        ID = intent.getIntExtra("ID", 0);
        db = openOrCreateDatabase("uberdb", MODE_PRIVATE, null);

        Button kopce = (Button) findViewById(R.id.kopce);
        kopce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MapsActivity.this, PassengerActivity.class);
                intent2.putExtra("ID", ID);
                intent2.putExtra("lat1", myLat);
                intent2.putExtra("lng1", myLng);
                intent2.putExtra("lat2", destLat);
                intent2.putExtra("lng2", destLng);
                startActivityForResult(intent2, 100);
            }
        });

        setDet(ID);
    }

    private void setDet(int id) {
        float rr = 0;
        Cursor c3 = db.rawQuery("SELECT SUM(rating1) * 1.0 / COUNT(rating1) AS rr1, COUNT(rating1) as num1 FROM Route WHERE Route.driver = ?", new String[]{String.valueOf(id)});
        Cursor c4 = db.rawQuery("SELECT SUM(rating2) * 1.0 / COUNT(rating2) AS rr2, COUNT(rating2) as num2 FROM Route WHERE Route.passenger = ?", new String[]{String.valueOf(id)});
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

        Cursor c1 = db.rawQuery("SELECT * FROM User WHERE User.ID = ?", new String[]{String.valueOf(ID)});
        c1.moveToFirst();
        ime = (TextView) findViewById(R.id.profil);
        rejting = (TextView) findViewById(R.id.rating);
        ime.setText(c1.getString(c1.getColumnIndexOrThrow("name")));
        float r = c1.getFloat(c1.getColumnIndexOrThrow("rating"));
        String rt = String.format("%.1f", r);
        rejting.setText(rt);
        c1.close();
    }

    @Override
    public void onRequestPermissionsResult(int reqCode, @NonNull String permissions[], @NonNull int[] grantResults) {
    }



    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        if (myLocation != null) {
            LatLng markerLatLng = marker.getPosition();
            mMap.addPolyline(new PolylineOptions()
                    .add(myLocation)
                    .add(markerLatLng)
            );
            destLat = markerLatLng.latitude;
            destLng = markerLatLng.longitude;
            return true;
        } else {
            return false;
        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Handle map click
                googleMap.addMarker(new MarkerOptions().position(latLng).title("Дестинација").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addPolyline(new PolylineOptions()
                        .add(myLocation)
                        .add(latLng)
                );
                destLat = latLng.latitude;
                destLng = latLng.longitude;
                float[] results = new float[1];
                Location.distanceBetween(myLocation.latitude, myLocation.longitude,
                        destLat, destLng, results);
                float distance = results[0] / 1000;
                ContentValues values = new ContentValues();
                values.put("distance", distance);
                int effected = db.update("Passenger", values,"ID = ?", new String[]{String.valueOf(ID)});
            }
        });

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
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
        }
    }

}