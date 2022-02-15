package com.example.lepton;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.leptontest.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
//import com.lepton.locationsdk.databinding.ActivityLeptonMapsBinding;
//com.lepton.locationsdksample

public class LeptonMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener {

    private GoogleMap mMap;
    // private ActivityLeptonMapsBinding binding;
    Location mLocation;
    Marker selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*binding = ActivityLeptonMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());*/
        setContentView(R.layout.activity_lepton_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findViewById(R.id.ivsearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(LeptonMapsActivity.this, LocationSearchActivityV2.class),101);
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mLocation = getLastKnownLocation(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
         onMyLocationChange(mLocation);

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
            @Override
            public boolean onMyLocationButtonClick() {
                if (mLocation!=null) {
                    LatLng cutLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(cutLatLng, 15);
                    mMap.animateCamera(update);
                }
                return false;
            }
        });

    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
    @Override
    public void onMapClick(LatLng latLng) {
        if (selected==null)
      selected=  mMap.addMarker(new MarkerOptions().position(latLng).title(""));
        else
            selected.setPosition(latLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
});

mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        selected=marker;
      //  selected=  mMap.addMarker(new MarkerOptions().position(marker.getPosition()).title(""));
    }
});
        // Add a marker in Sydney and move the camera


    }

    public static Location getLastKnownLocation(Context ctx) {
        if (ctx == null) return null;
        LocationManager mLocationManagerGLOBAL = (LocationManager) ctx.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManagerGLOBAL.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location lastKnownLocation = mLocationManagerGLOBAL.getLastKnownLocation(provider);
            if (lastKnownLocation == null) {
                continue;
            }
            if (bestLocation == null || lastKnownLocation.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = lastKnownLocation;
            }
        }
        return bestLocation;
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (location!=null) {
            LatLng cutLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(cutLatLng, 15);
            mMap.animateCamera(update);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(data != null){
                double lat = (double) data.getDoubleExtra("latt",0);
                double lng = (double) data.getDoubleExtra("lng",0);
                String searchedText = data.getStringExtra("searched_text");

                if(selected != null)selected.remove();
                selected = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat,lng))
                        .title(searchedText)
                     //   .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow_marker))
                );
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selected.getPosition(),15));

            }
        }
    }
}