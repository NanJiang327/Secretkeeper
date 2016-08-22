package com.example.lovej.secretkeeper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lovej on 2016/8/21 0021.
 */
public class RegisterPage extends AppCompatActivity {
    RadioGroup RadioGroup_gender;
    private EditText Username,Password,PasswordConfirm,Email;
    private TextView MsgPasswordConfirm;
    private Button register;
    private boolean radio,text,checklength,pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_page);
        initControl();

        RadioGroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.reg_btn_male){
                    radio = true;
                    if(text&pwd){
                        register.setBackgroundColor(0xFFFF9900);
                        register.setEnabled(true);
                    }
                }
                else if(checkedId==R.id.reg_btn_female){
                    radio = true;
                    if(text&pwd){
                        register.setBackgroundColor(0xFFFF9900);
                        register.setEnabled(true);
                    }
                }
            }
        });

        Username.addTextChangedListener(new TextChange());
        Password.addTextChangedListener(new TextChange());
        PasswordConfirm.addTextChangedListener(new TextChange());
        Email.addTextChangedListener(new TextChange());


        //TODO check email on database use setOnfocus if exist same as last one.

        Username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                //TODO check username on database use setOnFocus if exist then set repeat username to true and use it on TextChanged method
                if(hasFocus){

                }else{
                    if(Username.getText().length()>5 & Username.getText().length()<20){
                        //Username.setError("Username should be at least 6 char and less than 20 char");
                    }
                }
            }
        });

        PasswordConfirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){

                }else{
                    if(!Password.getText().toString().equals(PasswordConfirm.getText().toString())){
                        MsgPasswordConfirm.setText("Password doesn't matched!");
                        pwd = false;
                    }else{
                        MsgPasswordConfirm.setText(" ");
                        pwd = true;
                    }

                }
            }
        });

        Password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){

                }else{
                    if(!Password.getText().toString().equals(PasswordConfirm.getText().toString()) & PasswordConfirm.getText().length()!=0){
                        MsgPasswordConfirm.setText("Password doesn't matched!");
                        pwd = false;
                    }else{
                        MsgPasswordConfirm.setText(" ");
                    }

                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checklength = true;
                errorMsg();
                if (checklength) {
                    AlertDialog.Builder dialog2 = new AlertDialog.Builder(RegisterPage.this);
                    //确认信息的标题
                    dialog2.setTitle("Confirm your username");
                    //确认信息
                    dialog2.setMessage("Use " + Username.getText().toString() + " as your username? Once you confirm you can't change it!");
                    //右边按钮
                    dialog2.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    //左边按钮
                    dialog2.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final ProgressDialog pD = new ProgressDialog(RegisterPage.this);
                            pD.setTitle("Message!");
                            pD.setMessage("Working on it xD");
                            pD.show();
                            Thread thread = new Thread() {
                                public void run() {
                                    try {
                                        sleep(2000);
                                        finish();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    pD.dismiss();
                                }
                            };
                            thread.start();
                            pD.setCancelable(true);

                        }
                    });
                    dialog2.show();
                }
            }
        });
    }

    protected void initControl(){
        Username = (EditText)findViewById(R.id.reg_et_username);
        Password = (EditText)findViewById(R.id.reg_et_password);
        PasswordConfirm = (EditText)findViewById(R.id.reg_et_passwordCheck);
        Email = (EditText)findViewById(R.id.reg_et_email);
        MsgPasswordConfirm = (TextView)findViewById(R.id.reg_tv_notmatch);
        RadioGroup_gender=(RadioGroup)findViewById(R.id.radioGroup1);
        register = (Button)findViewById(R.id.reg_btn_register);

    }

    protected void errorMsg(){
        boolean Sign1 =  Username.getText().length() < 20;
        boolean Sign2 =  Username.getText().length() > 4;
        boolean Sign3 = Password.getText().length() > 7;
        boolean Sign4 = Password.getText().length() < 20;
        boolean Sign5 = PasswordConfirm.getText().length() > 0;
        boolean Sign6 = Email.getText().length() > 7;
        boolean Sign7 = Email.getText().length() < 25;
        boolean Sign8 = (Email.getText().toString()).indexOf("@") > 0;
        boolean Sign9 = (Email.getText().toString()).indexOf(".") >  0;
        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterPage.this);
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
        } else if(!(Sign3 & Sign4 & Sign5)) {
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
        }else if(compileExChar(Password.getText().toString()) & compileExChar(PasswordConfirm.getText().toString())){
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
        }else if(!(Sign6 & Sign7)){
                dialog.setTitle("Input error");
                checklength = false;
                dialog.setMessage("The length of email should be greater than 8 and less then 20");
                dialog.setPositiveButton("Return", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            dialog.show();
        }else if(compileExChar(Email.getText().toString())) {
            dialog.setTitle("Input error");
            checklength = false;
            dialog.setMessage("Illegal Email input");
            dialog.setPositiveButton("Return", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            dialog.show();
            }else if(!(Sign8 & Sign9)){
            dialog.setTitle("Input error");
            checklength = false;
            dialog.setMessage("Email format is wrong!");
            dialog.setPositiveButton("Return", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            dialog.show();
        }

    }

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
            boolean Sign1 =  Username.getText().length() > 0;
            boolean Sign2 = Password.getText().length() > 0;
            boolean Sign3 = PasswordConfirm.getText().length() > 0;
            boolean Sign4 = Email.getText().length() > 0;


            if (Sign1 & Sign2 & Sign3 & Sign4) {
                text = true;
                if(radio&pwd){
                    register.setBackgroundColor(0xFFFF9900);
                    register.setEnabled(true);
                }
            } else{
                    register.setBackgroundColor(0xFFCCCCCC);
                    text = false;
                    register.setEnabled(false);
                }
            }

        }
         private boolean compileExChar(String str) {
             String limitEx = "[`~ !#$%^&*()+=|{}':;',\\[\\]<>/?~#%&*—+|{}'\"]";
             Pattern pattern = Pattern.compile(limitEx);
             Matcher m = pattern.matcher(str);
             if (m.find()) {
                 return true;
             }else{
                 return false;
             }
         }
    }



