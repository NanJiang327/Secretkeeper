package com.example.lovej.secretkeeper.test;

/**
 * Created by lovej on 2016/9/22 0022.
 */

import android.test.AndroidTestCase;

import com.example.lovej.secretkeeper.LoginPage;

public class LoginPageTest extends AndroidTestCase {
    private LoginPage lp = new LoginPage();

    //This method is used to check if use in the right position of the database.
    public void testDb() throws Throwable {
        //In the actual database the user j499521010 should be in the position 0;
        assertEquals("0", lp.testDb(lp.checkDb_T()));

    }
    //This method is used to check if the username or password have illegal chart.
    public void testUserNameHasIllegalChar() {
        //No illegal chart.
        assertSame(false, lp.compileExChar("sdadsabd"));
        //Has illegal chart '!'
        assertSame(true, lp.compileExChar("!323"));
    }
    //This method is used to check if the length of username meets the requirement
    public void testUserNameLength() {
        //length is 10, passed
        assertSame(true, lp.isVaildUserName("j499521010"));
        //length is 2, no passed
        assertSame(false, lp.isVaildUserName("sd"));

    }
    //This method is used to check if the password contain at least one lowercase and one uppercase.
    public void testPassword() {
        //password doesn't contain an uppercase
        assertSame("the password should be contain at least one lowercase and one uppercase", lp.checkPassword("dsdsd"));
        //password with at least one　lowercase and one uppercase
        assertSame("123.comJ", lp.checkPassword("123.comJ"));
    }
    //This method is used to check if the username exist and matched with password.
    public void testUsernameAndPwd() {
        //The username exist and matched with matched with password.
        assertSame("login success!", lp.checkMatch("j499521010", "123.comJN"));
        //The username exist but password is wrong
        assertSame("username does't matched with password.", lp.checkMatch("j499521010", "123sds"));
        //The username doesn't exist.
        assertSame("username doesn't exist.", lp.checkMatch("sdasdas", "dsds"));
    }
}








