package creativex.com.myapplication.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import creativex.com.myapplication.Utility.DividerItemDecoration;
import creativex.com.myapplication.Utility.GPSTracker;
import creativex.com.myapplication.Utility.GooglePlaces;

public class MainActivity extends ActionBarActivity {



//    TEst commit

//    second commit test

//    third commit test with github-webhook url



//    fourth commit test with github-webhook url

//    fifth commit test with github-webhook url

    //    sixth commit test with github-webhook url

    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private AlertDialogManager alert = new AlertDialogManager();
    private GooglePlaces googlePlaces;
    private PlacesList nearPlaces;
    private GPSTracker gps;
    private Button btnShowOnMap;
    private ProgressDialog pDialog;
    private String selectedCatagory;
    private RecyclerView mRvPlaces;
    private PlaceAdapter mPlaceAdapter;
    private ArrayList<String> placeList;
    private ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, Place.Location>> placesListLocation = new ArrayList<HashMap<String, Place.Location>>();
    private LinkedHashMap<String, String> referenceMap = new LinkedHashMap<String, String>();
    private HashMap<String, Place.Location> locationMap = new HashMap<String, Place.Location>();


    double latitude;
    double longitude;


    // KEY Strings
    public static String KEY_REFERENCE = "reference"; // id of the place
    public static String KEY_NAME = "name"; // name of the place

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_place);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.tool_bar);
        mToolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolBar);


        Intent intent = getIntent();
        selectedCatagory = intent.getStringExtra("CATAGORY");

        cd = new ConnectionDetector(getApplicationContext());

        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            alert.showAlertDialog(MainActivity.this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            return;
        }

        gps = new GPSTracker(this);

        if (gps.canGetLocation()) {
            Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            alert.showAlertDialog(MainActivity.this, "GPS Status",
                    "Couldn't get location information. Please enable GPS",
                    false);
            return;
        }

        mRvPlaces = (RecyclerView) findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRvPlaces.setLayoutManager(mLayoutManager);
        mRvPlaces.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mRvPlaces.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRvPlaces, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String keyforReferenceMap = placeList.get(position);
                String reference = referenceMap.get(keyforReferenceMap);
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(getApplicationContext(),
                        SinglePlaceActivity.class);

                in.putExtra(KEY_REFERENCE, reference);
                startActivity(in);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // button show on map
        btnShowOnMap = (Button) findViewById(R.id.btn_show_map);

        new LoadPlaces().execute();

        btnShowOnMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(MainActivity.this, PlaceMapActivity.class);
                i.putExtra("LAT", latitude);
                i.putExtra("LNG", longitude);
                i.putExtra("LIST", placesListLocation);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

    }


    class LoadPlaces extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            // creating Places class object
            googlePlaces = new GooglePlaces();

            try {
                double radius = 1000; // 1000 meters
                nearPlaces = googlePlaces.search(gps.getLatitude(),
                        gps.getLongitude(), radius, selectedCatagory);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    String status = nearPlaces.status;
                    if (status.equals("OK")) {
                        if (nearPlaces.results != null) {
                            for (Place p : nearPlaces.results) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put(KEY_REFERENCE, p.reference);
                                map.put(KEY_NAME, p.name);
                                locationMap.put(p.name, p.geometry.location);
                                placesListItems.add(map);
                                placesListLocation.add(locationMap);
                            }
                            placeList = new ArrayList<String>();

                            for (int i = 0; i < placesListItems.size(); i++) {
                                Map<String, String> map = placesListItems.get(i);
                                for (Map.Entry<String, String> entry : map.entrySet()) {
                                    String key = entry.getKey();
                                    String value = entry.getValue();
                                    if (!referenceMap.containsKey(map.get("name"))) {
                                        referenceMap.put(map.get("name"), map.get("reference"));
                                    }
                                    Log.e("PLACES", "KEY : " + key + "  value : " + value);
                                    if (!placeList.contains(map.get("name"))) {
                                        placeList.add(map.get("name"));
                                    }
                                }
                            }

                            for (Map.Entry<String, String> entry : referenceMap.entrySet()) {
                                String key = entry.getKey();
                                String value = entry.getValue();

                                Log.e("PLACES reference MAP", "KEY : " + key + "  value : " + value);
                            }

                            mPlaceAdapter = new PlaceAdapter(MainActivity.this, placeList);

                            mRvPlaces.setItemAnimator(new DefaultItemAnimator());
                            mRvPlaces.setAdapter(mPlaceAdapter);

                        }
                    } else if (status.equals("ZERO_RESULTS")) {
                        alert.showAlertDialog(MainActivity.this, "Near Places",
                                "Sorry no places found. Try to change the types of places",
                                false);
                    } else if (status.equals("UNKNOWN_ERROR")) {
                        alert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry unknown error occured.",
                                false);
                    } else if (status.equals("OVER_QUERY_LIMIT")) {
                        alert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry query limit to google places is reached",
                                false);
                    } else if (status.equals("REQUEST_DENIED")) {
                        alert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry error occured. Request is denied",
                                false);
                    } else if (status.equals("INVALID_REQUEST")) {
                        alert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry error occured. Invalid Request",
                                false);
                    } else {
                        alert.showAlertDialog(MainActivity.this, "Places Error",
                                "Sorry error occured.",
                                false);
                    }
                }
            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

    }

}
