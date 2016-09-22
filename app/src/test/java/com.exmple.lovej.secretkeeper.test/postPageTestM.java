package com.exmple.lovej.secretkeeper.test;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import com.example.lovej.secretkeeper.DataBase;
import com.example.lovej.secretkeeper.PostPage;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by SpencerLee on 13/09/2016.
 */
public class postPageTestM extends AndroidTestCase {

    private PostPage pg = new PostPage();

    public void testDuplicateId(){
        //If it is in DB, Create a dummy secret id 1 and insert into db
       /* SQLiteDatabase db1 = new DataBase(
                this.mContext).getWritableDatabase();
        int number = 1;
        ContentValues values=new ContentValues();
        values.put("secretid",number);
        values.put("username","PersonalA");
        values.put("content","Testing is fun");
        db1.insert("SECRET",null,values);
        */

        //Here for testing purpose, using checkSecretID_T() as mock testing function

        //Test after method checkSecretID(),the secret id is still gonna be inserting as 1 or not.
        assertNotSame(1,pg.checkSecretID_T(1));

    }

    //This is for testing the logic of getSecretFromDB() and saveSecret() by using the refactor function with _T()
    public void testGetDBInfo(){
        Map<String,String> mockDB = new HashMap<String, String>();
        //key=userName, value = password
        mockDB.put("user01","123456");
        mockDB.put("user02","234567");
        mockDB.put("user03","345678");
        assertEquals("user01123456",pg.getDBInfo_T(mockDB,"user01","123456"));
    }

    //This is for testing the logic of getSecretFromDB() and saveSecret() by using the refactor function with _T()
    public void testSecretIsSame(){
        String content = pg.getSecretFromDB_T(pg.saveSecret_T(),"user02");
        assertEquals("This is test 02",content);
    }

    //After using mockito, it is possible to test db using this code
    public void testSaveState(){
        SQLiteDatabase db1 = new DataBase(
                this.mContext).getWritableDatabase();
        int number = 1;
        ContentValues values=new ContentValues();
        values.put("secretid",number);
        values.put("username","PersonalA");
        values.put("content","Testing is fun");
        db1.insert("SECRET",null,values);
        assertEquals("Testing is fun",pg.getSecretFromDB(1));
    }

    //This is for testing the logic of connecting DB in DataBase class
    public void testCreateDb() throws Throwable{
        SQLiteDatabase db = new DataBase(
                this.mContext).getWritableDatabase();
        assertEquals(true,db.isOpen());
        db.close();
    }

}
