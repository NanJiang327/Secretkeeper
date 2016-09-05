package com.example.lovej.secretkeeper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    private boolean checklength;
    private CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_page);
        initControll();
        login.getBackground().setAlpha(40);
        //TODO connect to server..
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorMsg();
                if(checklength) {
                    Intent intent = new Intent(LoginPage.this, PostPage.class);
                    startActivity(intent);
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

        Username.addTextChangedListener(new TextChange());
        Password.addTextChangedListener(new TextChange());

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
    protected void errorMsg(){
        checklength = true;
        boolean Sign1 =  Username.getText().length() < 20;
        boolean Sign2 =  Username.getText().length() > 4;
        boolean Sign3 = Password.getText().length() > 7;
        boolean Sign4 = Password.getText().length() < 20;
        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginPage.this);
        if(!(Sign1 & Sign2)) {
            dialog.setTitle("Input error");
            checklength = false;
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
            checklength = false;
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
            checklength = false;
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
            checklength = false;
            dialog.setMessage("Illegal password ");
            dialog.setPositiveButton("Return", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            dialog.show();
        }
    }
    //method used to get the text changed event
    class TextChange implements TextWatcher {

        @Override
        public void afterTextChanged(Editable arg0) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

        }

        @Override
        public void onTextChanged(CharSequence cs, int start, int before,
                                  int count) {
            boolean Sign1 = Username.getText().length() > 0;
            boolean Sign2 = Password.getText().length() > 0;
            if (Sign1 & Sign2) {
                    login.setBackgroundColor(0xFFFF9900);
                    login.setEnabled(true);

            }else{
                login.setBackgroundColor(0xFFCCCCCC);
                login.setEnabled(false);
            }
        }

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
