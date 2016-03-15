package creativex.com.myapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import creativex.com.myapplication.Adapter.PlaceAdapter;
import creativex.com.myapplication.DataModel.Place;
import creativex.com.myapplication.DataModel.PlacesList;
import creativex.com.myapplication.R;
import creativex.com.myapplication.Utility.AlertDialogManager;
import creativex.com.myapplication.Utility.ConnectionDetector;
import creativex.com.myapplication.Utility.GPSTracker;
import creativex.com.myapplication.Utility.GooglePlaces;

/**
 * Created by karthik on 14/3/16.
 */
public class PlaceMapActivity extends ActionBarActivity {

    private GoogleMap googleMap;
    private double latitude;
    private double longitude;

    private ArrayList<HashMap<String, Place.Location>> placesListLocation = new ArrayList<HashMap<String, Place.Location>>();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_place_map);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.tool_bar);
        mToolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolBar);

        Intent i = getIntent();
        placesListLocation = (ArrayList<HashMap<String, Place.Location>>) i.getSerializableExtra("LIST");
        latitude = i.getDoubleExtra("LAT", 0.0);
        longitude = i.getDoubleExtra("LNG", 0.0);

        initilizeMap();

    }


    private void initilizeMap() {

        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();


            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            } else {
                placeMarker();
                placeMultipleMarker();
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

    private void placeMarker() {
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Its YOU");
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        googleMap.addMarker(marker);
        googleMap.setMyLocationEnabled(true);
        LatLng myLocation = new LatLng(latitude, longitude);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                15));
    }

    private void placeMultipleMarker() {
        for (int i = 0; i < placesListLocation.size(); i++) {
            Map<String, Place.Location> map = placesListLocation.get(i);
            for (Map.Entry<String, Place.Location> entry : map.entrySet()) {
                String key = entry.getKey();
                Double lat = entry.getValue().lat;
                Double lng = entry.getValue().lng;
                MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lng)).title(key);
                googleMap.addMarker(marker);
                Log.e("placeMultipleMarker", "key : " + key + " lat : " + lat + " lng : " + lng);
            }
        }
    }
}
