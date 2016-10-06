package com.example.lovej.secretkeeper.test;

import com.example.lovej.secretkeeper.RegisterPageT;
import com.example.lovej.secretkeeper.User;

import org.junit.Test;

import static junit.framework.Assert.assertSame;

/**
 * Created by FD-GHOST on 2016/9/23.
 */

public class RegisterPageTest {
    private RegisterPageT rp = new RegisterPageT();

    @Test
    //This method is used to check if the username or password have illegal chart
    public void testUserNameHasIllegalChar() throws Exception {
//No illegal chart.
        assertSame(false, rp.compileExChar("Testtest"));
        //Has illegal chart '!'
        assertSame(true, rp.compileExChar("!Testtest"));
    }

    @Test
    //This method is used to check if the username is already exist
    public void testAddUserUserExist() throws Exception {
        //This is a user should exist already
        User testa = new User("RegisterPageA", "RegisterPageA123", "Male", "Testa@Test.com");
        assertSame(true, rp.addUser(testa));
        //This is a new user, should not exist
        User testb = new User("RegisterPageAA", "RegisterPageA123", "Male", "Testaa@Test.com");
        assertSame(false, rp.addUser(testb));
    }

    @Test
    //This method is used to check if the email is already exist
    public void testAddUserEmailExist() throws Exception {
        //This is a user should exist already
        User testa = new User("RegisterPageD", "RegisterPageA123", "Male", "Testa@Test.com");
        assertSame(true, rp.addUser(testa));
        //This is a new user, should not exist
        User testb = new User("RegisterPageD", "RegisterPageA123", "Male", "Testaa@Test.com");
        assertSame(false, rp.addUser(testb));

    }

}