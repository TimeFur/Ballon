package com.example.wayne.pdor;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Wayne on 2018/3/7.
 */

public class DB_Function {

    String TAG = "DataBase";

    static String TABLE_NAME = "URL_TABLE";

    static String KEY_ID = "_id";
    static String DATETIME = "_time";
    static String URL = "_url";
    static String NAME = "_name";

    //Create db command
    //INTEGER -> int, short, long, byte
    //TEXT -> String
    //REAL -> float, double
    public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
                                            + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " //set as auto increase
                                            + DATETIME + " INTEGER NOT NULL, "
                                            + URL + " TEXT NOT NULL, "
                                            + NAME + " TEXT NOT NULL" + ")";
    //
    private SQLiteDatabase db;

    public DB_Function(Context context){
        //Get the database from the SQLiteManage
        db = SQLiteManage.getDataBase(context);
    }


    //Insert data
    public void InsertData (String _url){
        ContentValues c = new ContentValues();

        c.put(URL, _url);
        c.put(DATETIME, 20180308);
        c.put(NAME, "Google");

        db.insert(TABLE_NAME, null, c);

        Log.w(TAG, "Insert data" + _url);
    }
    //Get data
    public String GetData (long _id){
        String _url = "";
        String locate = KEY_ID + " = " + _id;
        Cursor cursor = db.query(TABLE_NAME, null, locate, null, null, null, null, null);

        if (cursor.moveToFirst())
        {
            Log.w(TAG, "First " + _url + "---");
            _url = cursor.getString(2);
        }
        Log.w(TAG, "Get data " + _url + "---");
        return _url;
    }
    //Revise data
    //Delete data



}
