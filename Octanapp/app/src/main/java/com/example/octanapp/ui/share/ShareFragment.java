package com.example.octanapp.ui.share;

import android.Manifest;
import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.octanapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShareFragment extends SupportMapFragment implements OnMapReadyCallback {

    private LocationManager locationManager;
    private GoogleMap mMap;

    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);


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
    @Override
    public void onMapReady(GoogleMap googleMap) {

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        requestPermissions(LOCATION_PERMS, 1340);


        String provider = locationManager.getBestProvider(criteria, true);
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng curitiba = new LatLng(-25.4284, -49.2733);
        mMap.addMarker(new MarkerOptions().position(curitiba).title("Marker in Curitiba"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curitiba, 18));
        mMap.setMyLocationEnabled(true);
    }
}