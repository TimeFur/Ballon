package com.example.wayne.pdor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Wayne on 2018/3/7.
 */

class SQLiteManage extends SQLiteOpenHelper {

    static String DATABASE_NAME = "DATABASE";
    static int VERSION = 1;
    public static SQLiteDatabase db = null;

    public SQLiteManage(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_Function.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static SQLiteDatabase getDataBase(Context context)
    {
        if (db == null)
            db = new SQLiteManage(context, DATABASE_NAME,null, VERSION).getWritableDatabase();

        return db;
    }
}
