package com.gersoncardenas.pizzadoblepizza;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends ActionBarActivity {

    private MainFragment publicityFragment = new MainFragment();
    private DataBaseFragment dataFragment = new DataBaseFragment();
    private MapLocationFragment mapFragment = new MapLocationFragment();

    protected FragmentManager fragmentManager;
    protected FragmentTransaction fragmentTransaction;

    public static FragmentManager fragmentManagerMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){

            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(android.R.id.content, publicityFragment).commit();
        }

        fragmentManagerMap = getFragmentManager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home_menu)
            fragmentTransaction.replace(android.R.id.content, publicityFragment).commit();

        if (id == R.id.new_menu)
            fragmentTransaction.replace(android.R.id.content, dataFragment).commit();

        if (id == R.id.map_menu)
            fragmentTransaction.replace(android.R.id.content, mapFragment).commit();

        return super.onOptionsItemSelected(item);
    }
}
