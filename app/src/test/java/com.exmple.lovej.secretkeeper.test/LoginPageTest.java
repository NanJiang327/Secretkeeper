package com.exmple.lovej.secretkeeper.test;

/**
 * Created by lovej on 2016/9/22 0022.
 */
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.lovej.secretkeeper.DataBase;
import com.example.lovej.secretkeeper.LoginPage;

import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginPageTest extends AndroidTestCase {
    private LoginPage lp = new LoginPage();

    //This method is used to check if use in the right position of the database.
    public void testDb() throws Throwable {
        //In the actual database the user j499521010 should be in the position 0;
        assertEquals("0",lp.testDb(lp.checkDb_T()));

    }

    public void testUserNameHasIllegalChar(){
        assertSame(false,lp.compileExChar("sdadsabd"));
    }


  //  public void testUserNameLength{
}








