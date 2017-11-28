package edu.orangecoastcollege.cs273.caffeinefinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

//TODO: Implement the following interfaces:
//TODO: GoogleApiClient.ConnectionCallbakcs, GoogleApiClient.OnConnectionFailedListener and LocationListener
public class CaffeineListActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DBHelper db;
    private List<CaffeineLocation> mAllCaffeineLocationsList;
    private ListView mCaffeineLocationsListView;
    private LocationListAdapter mCaffeineLocationListAdapter;
    private GoogleMap mMap;

    //TODO: Add member variables for the GoogleApiClient, CaffeineLocation and LocationRequest

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caffeine_list);

        deleteDatabase(DBHelper.DATABASE_NAME);
        db = new DBHelper(this);
        db.importLocationsFromCSV("locations.csv");

        mAllCaffeineLocationsList = db.getAllCaffeineLocations();
        mCaffeineLocationsListView = (ListView) findViewById(R.id.locationsListView);
        mCaffeineLocationListAdapter = new LocationListAdapter(this, R.layout.location_list_item, mAllCaffeineLocationsList);
        mCaffeineLocationsListView.setAdapter(mCaffeineLocationListAdapter);

        //TODO: If the connection to the GoogleApiClient is null, build a new one:

        //TODO: Make a LocationRequest every 10 seconds, with a fastest interval of 1 second with high accuracy

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.caffeineMapFragment);
        mapFragment.getMapAsync(this);
    }

    //TODO: Override the onStart method to connect to the GoogleApiClient

    //TODO: Override the onStop method to disconnect from the GoogleApiClient


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add special marker (blue) for "my" location
        //MBCC Building Lat/Lng (MBCC 135)  33.671028, -117.911305
        LatLng myCoordinate = new LatLng(33.671028, -117.911305);
        mMap.addMarker(new MarkerOptions()
                .position(myCoordinate)
                .title("Current Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker)));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(myCoordinate).zoom(15.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);

        // Add normal markers for all caffeine locations
        for (CaffeineLocation caffeineLocation : mAllCaffeineLocationsList) {
            LatLng coordinate = new LatLng(caffeineLocation.getLatitude(), caffeineLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(coordinate).title(caffeineLocation.getName()));
        }
    }

    //TODO: Define a new method: private void handleNewLocation(CaffeineLocation newLocation) that will
    //TODO: update the user's location (custom marker) on the Google Map and rebuild all markers for the Caffeine Locations (standard markers)


    public void viewLocationDetails(View view) {
        if (view instanceof LinearLayout) {
            LinearLayout selectedLayout = (LinearLayout) view;
            CaffeineLocation selectedCaffeineLocation = (CaffeineLocation) selectedLayout.getTag();

            Log.i("Caffeine Finder", selectedCaffeineLocation.toString());
            Intent detailsIntent = new Intent(this, CaffeineDetailsActivity.class);

            detailsIntent.putExtra("SelectedLocation", selectedCaffeineLocation);
            startActivity(detailsIntent);
        }
    }

    //TODO: In overriden onConnected method, get the last location, then request location updates and handle the new location

    //TODO: Override onRequestedPermissionsResult.  If fine/coarse location is granted, updated the last location
    //TODO: Else, initialize mLastLocation to a new location with latitude 0.0 and longitude 0.0
    //TODO: In either case, handle the new location.


    //TODO: Create a new method: public void findClosestCaffeine, which will be invoked when a user clicks on the button
    //TODO: Loop through all the caffeine locations and find the one with the minimum distance.
    //TODO: Then, fire off an Intent to the details page and put both the SelectedLocation and MyLocation
}
