package com.example.lovej.secretkeeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lovej on 2016/9/1 0001.
 */
public class DataBase extends SQLiteOpenHelper {
    private static String name = "user1.db";
    private static int dbVersion = 1;

    public DataBase(Context context){
        super(context,name,null,dbVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table user(username varchar(20) primary key,password varchar(20),email varchar(30),gender varchar(5))";
        String setUpDBForSecret = "create table SECRET(secretid number(5) primary key, username varchar(20), content varchar(300), background varchar(20))";
        String setUpDBForCoin = "create table COIN(username varchar(20) primary key, coin number(5))";
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(setUpDBForCoin);
        sqLiteDatabase.execSQL(setUpDBForSecret);
        ///Secrets for test///
//        String secreststest = " INSERT into SECRET(secretid, username, content)\n" +
//                "        VALUES (1,'ML','Secret N1');";
//        sqLiteDatabase.execSQL(secreststest);
//        secreststest = " INSERT into SECRET(secretid, username, content)\n" +
//                "        VALUES (2'ML','Secret N2');";
//        sqLiteDatabase.execSQL(secreststest);
//        secreststest = " INSERT into SECRET(secretid, username, content)\n" +
//                "        VALUES (3,'ML','Secret N3');";
//        sqLiteDatabase.execSQL(secreststest);
//        secreststest = " INSERT into SECRET(secretid, username, content)\n" +
//                "        VALUES (4,'ML','Secret N4');";
//        sqLiteDatabase.execSQL(secreststest);
//        secreststest = " INSERT into SECRET(secretid, username, content)\n" +
//                "        VALUES (5,'ML','Secret N5');";
//        sqLiteDatabase.execSQL(secreststest);
//        secreststest = " INSERT into SECRET(secretid, username, content)\n" +
//                "        VALUES (6,'ML','Secret N6');";
//        sqLiteDatabase.execSQL(secreststest);
//        secreststest = " INSERT into SECRET(secretid, username, content)\n" +
//                "        VALUES (7,'ML','Secret N7');";
//        sqLiteDatabase.execSQL(secreststest);
//        secreststest = " INSERT into SECRET(secretid, username, content)\n" +
//                "        VALUES (8,'ML','Secret N8');";
//        sqLiteDatabase.execSQL(secreststest);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
