package com.gersoncardenas.pizzadoblepizza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseManager {

    public static final String TABLE_NAME = "sites2";
    public static final String CN_ID = "_id";  // Column name
    public static final String CN_NAME = "name";
    public static final String CN_LATITUDE = "latitude";
    public static final String CN_LONGITUDE = "longitude";

    // create table contacts(
    //                          _id integer primary key autoincrement,
    //                          name text not null,
    //                          phone text);
    public static final String CREATE_TABLE = "create table "+ TABLE_NAME+ " ("
            + CN_ID + " integer primary key autoincrement,"
            + CN_NAME + " text not null,"
            + CN_LATITUDE + " text,"
            + CN_LONGITUDE + " text);";

    private DataBaseHelper helper;
    private SQLiteDatabase db;

    public DataBaseManager(Context context) {
        helper = new DataBaseHelper(context);
        db = helper.getWritableDatabase();
    }

    public ContentValues GenerateContentValues (String name, String latitude, String longitude){

        ContentValues values = new ContentValues();
        values.put(CN_NAME,name);
        values.put(CN_LATITUDE,latitude);
        values.put(CN_LONGITUDE,longitude);
        return values;
    }

    public void Insert(String name, String latitude, String longitude){
        db.insert(TABLE_NAME, null, GenerateContentValues(name, latitude, longitude));
    }

    public Cursor LoadCursorSites(){
        String[] columns = new String[]{CN_ID,CN_NAME,CN_LATITUDE,CN_LONGITUDE};
        return db.query(TABLE_NAME,columns,null,null,null,null,null);
    }

    public Cursor SearchSite(String name){
        String[] columns = new String[]{CN_ID,CN_NAME,CN_LATITUDE,CN_LONGITUDE};

        try {
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return db.query(TABLE_NAME,columns,CN_NAME + "=?",new String[]{name},null,null,null);
    }

    public void Delete(String name){
        db.delete(TABLE_NAME,CN_NAME + "=?", new String[]{name});
    }

    public void ChangeCoordinates(String name, String newLatitude, String newLongitude){
        db.update(TABLE_NAME,GenerateContentValues(name,newLatitude,newLongitude),CN_NAME+"=?",new String[]{name});
    }
}
