package com.gersoncardenas.pizzadoblepizza;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapLocationFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    public static GoogleMap map;
    private CameraUpdate cameraUpdate;
    private static final LatLng coordinates = new LatLng(6.236249f, -75.580482f);

    public GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    public LatLng currentLocation;

    private DataBaseManager manager;
    private DataBaseFragment db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_location, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(map == null){

            map = ((MapFragment)MainActivity.fragmentManagerMap.findFragmentById(R.id.map)).getMap();

            buildGoogleApiClient();
            createLocationRequest();

            if (map != null) {
                SetUpMap();
                GetDataFromDataBase();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (map != null) {

            MainActivity.fragmentManagerMap.beginTransaction()
                    .remove(MainActivity.fragmentManagerMap.findFragmentById(R.id.map)).commit();

            map = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    private void SetUpMap(){

        map.setMyLocationEnabled(true);

        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinates, 13);
        map.animateCamera(cameraUpdate);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null)
            setNewLocation(mLastLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {}

    private void setNewLocation(Location location){

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        currentLocation = new LatLng(latitude, longitude);

        map.addMarker(new MarkerOptions().position(currentLocation).title("You're here!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    public void GetDataFromDataBase(){

        manager = new DataBaseManager(getActivity());
        db = new DataBaseFragment();

        db.cursor = manager.LoadCursorSites();
        db.cursor.moveToFirst();

        try{

            do {
                String dbName = db.cursor.getString(db.cursor.getColumnIndex(manager.CN_NAME));
                double dbLatitude = db.cursor.getDouble(db.cursor.getColumnIndex(manager.CN_LATITUDE));
                double dbLongitude = db.cursor.getDouble(db.cursor.getColumnIndex(manager.CN_LONGITUDE));

                LatLng latLng = new LatLng(dbLatitude, dbLongitude);

                map.addMarker(new MarkerOptions().position(latLng).title(dbName)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

            }while(db.cursor.moveToNext());

        } catch (CursorIndexOutOfBoundsException e) {

            Toast.makeText(getActivity(), "No sites in database", Toast.LENGTH_SHORT).show();
        }
    }
}
