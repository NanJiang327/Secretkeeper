package com.example.lovej.secretkeeper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lovej on 2016/8/21 0021.
 * Description: This class is for the Login Page
 */
public class LoginPage extends AppCompatActivity {
    private Button login,register;
    private EditText Username,Password;
    private boolean checktext,loginState;
    private CheckBox checkBox;
    private DataBase db;
    private int flags;
    private User user;

    /**
     * Description: <This function will be execute when this class have been called, which is initialize the components>
     *
     * @param savedInstanceState The Bundle from previous activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_page);
        initControll();

        login.getBackground().setAlpha(40);

        /**
         * Description: <This function is to detect whether or not this user had be allowed to sigh in, and prepare the user info and start home page >
         */
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                user = new User(Username.getText().toString(),Password.getText().toString());
                errorMsg();

                if(checktext){
                login(user);
                if(loginState){
                    Intent intent = new Intent(LoginPage.this,HomePage.class);
                    finish();
                    Toast.makeText(LoginPage.this, "Login success", Toast.LENGTH_SHORT).show();

                    Bundle bundle = new Bundle();
                    bundle.putString("Name", Username.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
            }
        });

        checkBox  = (CheckBox)findViewById(R.id.login_checkbox_showPwd);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()){
                    Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        /**
         * Description: <This function will direct user to register a new account >
         */
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this,RegisterPage.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Description: <This function is initialize related page xml file to be editable by the code>
     */
    protected void initControll() {
        login = (Button) findViewById(R.id.login_btn_login);
        register = (Button) findViewById(R.id.login_btn_register);
        Username = (EditText) findViewById(R.id.login_et_username);
        Password = (EditText) findViewById(R.id.login_et_password);
        db = new DataBase(LoginPage.this);
        Username.requestFocus();
    }

    /**
     * Description: <This function will detected if the user press back button in home page, we will ask of the user wants to exit this app>
     */
    @Override
    public void onBackPressed(){
        //Creat an alerdialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginPage.this);
        //Title
        dialog.setTitle("Secret");
        //Message
        dialog.setMessage("Quit???");
        //right side button
        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(LoginPage.this, "Welcome Back 0.0", Toast.LENGTH_SHORT).show();
            }
        });
        //left side button
        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.show();
    }

    /**
     * Description: <This function is to detect the user input, and to notify if anything dose not meet the requirement>
     */
    protected boolean errorMsg(){
        checktext = true;
        boolean Sign1 =  Username.getText().length() < 21;
        boolean Sign2 =  Username.getText().length() > 4;
        boolean Sign3 = Password.getText().length() > 7;
        boolean Sign4 = Password.getText().length() < 21;
        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginPage.this);
        if(!(Sign1 & Sign2)) {
            dialog.setTitle("Input error");
            checktext = false;
            dialog.setMessage("The length of username should be greater than 5 and less than 20");
            dialog.setPositiveButton("Return", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            dialog.show();
        }else if(compileExChar(Username.getText().toString())){
            dialog.setTitle("Input error");
            checktext = false;
            dialog.setMessage("Illegal username");
            dialog.setPositiveButton("Return", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            dialog.show();
        } else if(!(Sign3 & Sign4)) {
            dialog.setTitle("Input error");
            checktext = false;
            dialog.setMessage("The length of password should be greater than 8 and less then 20");
            dialog.setPositiveButton("Return", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            dialog.show();
        }else if(compileExChar(Password.getText().toString())){
            dialog.setTitle("Input error");
            checktext = false;
            dialog.setMessage("Illegal password ");
            dialog.setPositiveButton("Return", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            dialog.show();
        }
        return checktext;
    }

    /**
     * @param user The user
     * Description: <This function is to connect to database and verify the user-info is same as recorded>
     */
    public void login(User user){
        String nameCheck,passwordCheck;
        SQLiteDatabase dbRead = db.getReadableDatabase();
        Cursor cursor = dbRead.query("user",null,null,null,null,null,null);
        while(cursor.moveToNext()){
            nameCheck = cursor.getString(cursor.getColumnIndex("username"));
            passwordCheck = cursor.getString(cursor.getColumnIndex("password"));
            if(user.getUsername().equals(nameCheck)){
                if(user.getPassword().equals(passwordCheck)){
                    flags = 2;
                }else{
                    flags = 1;
                }
                break;
            }else{
                flags = 0;
            }
        }
        switch (flags){
            case 1:
                Toast.makeText(LoginPage.this, "Username and password doesn't matched, try again!", Toast.LENGTH_SHORT).show();
                loginState = false;
                break;
            case 0:
                Toast.makeText(LoginPage.this, "User " + Username.getText().toString() + " doesn't exist!", Toast.LENGTH_SHORT).show();
                Username.requestFocus();
                loginState = false;
                break;
            case 2:
                loginState = true;
                break;
            default:
                break;
        }
        cursor.close();
        db.close();
    }

    /**
     * @param str The String
     * Description: <This function is to detect the illegal input>
     */
    public boolean compileExChar(String str){
        String limitEx="[`~ !#$%^&*()+=|{}':;',\\[\\]<>/?~#%&*—+|{}'\"]";
        Pattern pattern = Pattern.compile(limitEx);
        Matcher m = pattern.matcher(str);
        if( m.find()){
            return true;
        }
        return false;
    }

    /**
     * Description: <This function is unit test>
     */
    //Virtual database
    public Map checkDb_T(){
        Map<String,String> mockDb = new HashMap<String,String>();
        mockDb.put("j499521010","0");
        mockDb.put("1sdsad","1");
        return mockDb;
    }

    /**
     * Description: <This function is for unit test>
     */
    //to check if use in the right position of the database.
    public String testDb(Map virtualDB) throws Throwable{
        for(Object obj: virtualDB.keySet()){
            if(((String)obj).equals("j499521010")){
                return  virtualDB.get(obj).toString();
            }
        }
        return "";
    }

    /**
     * @param username The String of username
     * Description: <This function is to detect if the username's length fits the requirements.>
     */
    public boolean isVaildUserName(String username){
        if(username.length()>4&& username.length() <21){
            return true;
        }else{
            return false;
        }

    }

    /**
     * @param password the password string
     * Description: <This function is to detect if the password fits the requirements.>
     */
    public String checkPassword(String password){
        boolean sign1 = false,sign2 = false;
        for(int i =0;i<password.length();i++){
            char c = password.toString().charAt(i);
            if(Character.isUpperCase(c)){
                sign1 = true;
            }
            if(Character.isLowerCase(c)){
                sign2 =  true;
            }
        }
        if(!sign1&&sign2){
            password = "the password should be contain at least one lowercase and one uppercase";
            return password;
        }else{
            return password;
        }
    }

    /**
     * @param username The username string
     * @param password The passward string
     * Description: <This function is to detect if username exist and username match with password.>
     */
    public String checkMatch(String username, String password){
        String loginSatus;
        String usernameInDb = "j499521010", passwordInDb = "123.comJN";
        if(username.equals(usernameInDb)){
            if(password.equals(passwordInDb)){
                loginSatus = "login success!";
            }else{
                loginSatus = "username does't matched with password.";
            }
        }else{
            loginSatus = "username doesn't exist.";
        }
        return loginSatus;
    }
}
