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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPage extends AppCompatActivity {
    private Button login,register;
    private EditText Username,Password;
    private boolean checktext,loginState;
    private CheckBox checkBox;
    private DataBase db;
    private int flags;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_page);
        initControll();

        login.getBackground().setAlpha(40);
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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this,RegisterPage.class);
                startActivity(intent);
            }
        });
    }

    //initial
    protected void initControll() {
        login = (Button) findViewById(R.id.login_btn_login);
        register = (Button) findViewById(R.id.login_btn_register);
        Username = (EditText) findViewById(R.id.login_et_username);
        Password = (EditText) findViewById(R.id.login_et_password);
        db = new DataBase(LoginPage.this);
        Username.requestFocus();
    }

    //override the back button
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

    //used to see if the input fits the requirements.
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

    public void login(User user){
        String nameCheck,passwordCheck;
        AlertDialog.Builder dialogForDb = new AlertDialog.Builder(LoginPage.this);
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
                break;
            }
        }
        switch (flags){
            case 1:
                //Title
                dialogForDb.setTitle("Error Message");
                //Message
                dialogForDb.setMessage("Username and password doesn't matched, try again!");
                //Button
                dialogForDb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                loginState = false;
                break;
            case 0:
                //Title
                dialogForDb.setTitle("Error Message");
                //Message
                dialogForDb.setMessage("User " + Username.getText().toString() + " doesn't exist!");
                //Button
                dialogForDb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
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
        dialogForDb.show();
    }

    //method used to detect the illegal input
    private boolean compileExChar(String str){
        String limitEx="[`~ !#$%^&*()+=|{}':;',\\[\\]<>/?~#%&*—+|{}'\"]";
        Pattern pattern = Pattern.compile(limitEx);
        Matcher m = pattern.matcher(str);
        if( m.find()){
            return true;
        }
        return false;
    }
}
