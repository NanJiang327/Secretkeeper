package com.example.lovej.secretkeeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lovej on 2016/9/1 0001.
 */
public class DataBase extends SQLiteOpenHelper {
    static String name = "user.db";
    static int dbVersion = 1;

    public DataBase(Context context){
        super(context,name,null,dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table user(username varchar(20) primary key,password varchar(20),email varchar(30),gender varchar(5))";
        sqLiteDatabase.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
