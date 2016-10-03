package com.example.lovej.secretkeeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lovej on 2016/9/1 0001.
 */
public class DataBase extends SQLiteOpenHelper {
    static String name = "user2.db";
    static int dbVersion = 2;

    public DataBase(Context context){
        super(context,name,null,dbVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table user(username varchar(20) primary key,password varchar(20),email varchar(30),gender varchar(5))";
        String setUpDBForSecret = "create table SECRET(secretid number(5) primary key, username varchar(20), content varchar(300))";
        String setUpDBForCoin = "create table COIN(username varchar(20), coin number(5))";
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(setUpDBForCoin);
        sqLiteDatabase.execSQL(setUpDBForSecret);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
