package com.gersoncardenas.pizzadoblepizza;

import android.app.FragmentManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DataBaseFragment extends Fragment implements
        View.OnClickListener {

    private View rootview;
    private Animation animScale;

    private DataBaseManager manager;
    public Cursor cursor;
    private SimpleCursorAdapter adapter;
    private ListView listView;

    private EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_data_base, container, false);

        animScale = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_scale);

        manager = new DataBaseManager(getActivity());

        listView = (ListView)rootview.findViewById(android.R.id.list);

        editText = (EditText)rootview.findViewById(R.id.EdText1);

        Button searchBtn = (Button) rootview.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(this);
        Button loadBtn = (Button) rootview.findViewById(R.id.load_btn);
        loadBtn.setOnClickListener(this);
        Button insertBtn = (Button) rootview.findViewById(R.id.insert_btn);
        insertBtn.setOnClickListener(this);
        Button deleteBtn = (Button) rootview.findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(this);
        Button updateBtn = (Button) rootview.findViewById(R.id.update_btn);
        updateBtn.setOnClickListener(this);

        String[] from = new String[]{manager.CN_NAME,manager.CN_LATITUDE,manager.CN_LONGITUDE};
        int[] to = new int[]{R.id.text1,R.id.text2,R.id.text3};

        cursor = manager.LoadCursorSites();

        adapter = new SimpleCursorAdapter(getActivity(),R.layout.info_layout,cursor,from,to,0);
        listView.setAdapter(adapter);

        return rootview;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentById(R.id.list)).commit();
    }

    @Override
    public void onClick(View v) {

        v.startAnimation(animScale);

        if (v.getId() == R.id.search_btn)
            new SearchTask().execute();

        if(v.getId() == R.id.load_btn){

            listView = (ListView) rootview.findViewById(android.R.id.list);
            //editText = (EditText) rootview.findViewById(R.id.EdText1);

            String[] from = new String[]{manager.CN_NAME,manager.CN_LATITUDE,manager.CN_LONGITUDE};
            int[] to = new int[]{R.id.text1,R.id.text2,R.id.text3};
            cursor = manager.LoadCursorSites();
            adapter = new SimpleCursorAdapter(getActivity(),R.layout.info_layout,cursor,from,to,0);
            listView.setAdapter(adapter);

        }

        if (v.getId() == R.id.insert_btn){

            EditText name = (EditText) rootview.findViewById(R.id.edit_name);
            EditText latitude = (EditText) rootview.findViewById(R.id.edit_latitude);
            EditText longitude = (EditText) rootview.findViewById(R.id.edit_longitude);
            manager.Insert(name.getText().toString(),latitude.getText().toString(),longitude.getText().toString());
            name.setText("");
            latitude.setText("");
            longitude.setText("");
            Toast.makeText(getActivity(),"Inserted", Toast.LENGTH_SHORT).show();
        }

        if(v.getId() == R.id.delete_btn){

            EditText name = (EditText) rootview.findViewById(R.id.edit_name);
            manager.Delete(name.getText().toString());
            Toast.makeText(getActivity(),"Deleted", Toast.LENGTH_SHORT).show();
            name.setText("");
        }

        if (v.getId() == R.id.update_btn){

            EditText name = (EditText) rootview.findViewById(R.id.edit_name);
            EditText latitude = (EditText) rootview.findViewById(R.id.edit_latitude);
            EditText longitude = (EditText) rootview.findViewById(R.id.edit_longitude);
            manager.ChangeCoordinates(name.getText().toString(),latitude.getText().toString(),longitude.getText().toString());
            Toast.makeText(getActivity(),"Updated", Toast.LENGTH_SHORT).show();
            name.setText("");
            latitude.setText("");
            longitude.setText("");
        }
    }

    private class SearchTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(getActivity(), "Searching...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            cursor = manager.SearchSite(editText.getText().toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getActivity(), "Search Completed", Toast.LENGTH_SHORT).show();
            adapter.changeCursor(cursor);
            GetData();
        }
    }

    public void GetData () {

        TextView tName = (TextView) rootview.findViewById(R.id.t_name);
        TextView tLatitude = (TextView) rootview.findViewById(R.id.t_latitude);
        TextView tLongitude = (TextView) rootview.findViewById(R.id.t_longitude);

        try {
            String dbName = cursor.getString(cursor.getColumnIndex(manager.CN_NAME));
            tName.setText(dbName);

            String dbLatitude = cursor.getString(cursor.getColumnIndex(manager.CN_LATITUDE));
            tLatitude.setText(dbLatitude);

            String dbLongitude = cursor.getString(cursor.getColumnIndex(manager.CN_LONGITUDE));
            tLongitude.setText(dbLongitude);

        } catch (CursorIndexOutOfBoundsException e) {
            tName.setText("Not Found");
            tLatitude.setText("Not Found");
            tLongitude.setText("Not Found");
        }
    }
}
