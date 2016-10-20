package com.example.lovej.secretkeeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lovej on 2016/9/1 0001.
 * Description: This class is to setup all the local database, which will be used as remount database
 */
public class DataBase extends SQLiteOpenHelper {
    private static String name = "user1.db";
    private static int dbVersion = 1;

    public DataBase(Context context){
        super(context,name,null,dbVersion);
    }

    /**
     * @param sqLiteDatabase The SQLiteDatabase
     * @description: <This function will be execute when this class have been called, and create local database if the database is not exist>
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table user(username varchar(20) primary key,password varchar(20),email varchar(30),gender varchar(5))";
        String setUpDBForSecret = "create table SECRET(secretid number(5) primary key, username varchar(20), content varchar(300), background varchar(20))";
        String setUpDBForCoin = "create table COIN(username varchar(20) primary key, coin number(5))";
        String setComment = "create table COMMENT(secretid number(5), commentorder number(3), comment varchar(200), commenter varchar(20))";
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(setUpDBForCoin);
        sqLiteDatabase.execSQL(setUpDBForSecret);
        sqLiteDatabase.execSQL(setComment);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
