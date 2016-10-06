package com.example.lovej.secretkeeper;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lovej/FD-GHOST on 2016/8/21 0021.
 */
public class RegisterPage extends AppCompatActivity {
    RadioGroup RadioGroup_gender;
    private EditText Username, Password, PasswordConfirm, Email;
    private TextView MsgPasswordConfirm;
    private Button register;
    private boolean radio, checklength, pwd, isExist;
    private int flags;
    private DataBase db;
    private String gender;
    private User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_page);
        initControl();

        RadioGroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.reg_btn_male) {
                    gender = "male";
                    radio = true;
                } else if (checkedId == R.id.reg_btn_female) {
                    gender = "female";
                    radio = true;
                }
            }
        });

        PasswordConfirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!Password.getText().toString().equals(PasswordConfirm.getText().toString())) {
                        MsgPasswordConfirm.setText("Password doesn't matched!");
                        pwd = false;
                    } else {
                        MsgPasswordConfirm.setText(" ");
                        pwd = true;
                    }

                }
            }
        });

        Password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (!Password.getText().toString().equals(PasswordConfirm.getText().toString()) & PasswordConfirm.getText().length() != 0) {
                        MsgPasswordConfirm.setText("Password doesn't matched!");
                        pwd = false;
                    } else {
                        MsgPasswordConfirm.setText(" ");
                        pwd = true;
                    }

                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checklength = true;
                errorMsg();
                user = new User(Username.getText().toString(),Password.getText().toString(),gender,Email.getText().toString());
                if (checklength&pwd) {
                    AlertDialog.Builder dialog2 = new AlertDialog.Builder(RegisterPage.this);
                    //set title
                    dialog2.setTitle("Confirm your username");
                    //set message
                    dialog2.setMessage("Use " + Username.getText().toString() + " as your username? Once you confirm you can't change it!");
                    //button at right
                    dialog2.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    //button at left
                    dialog2.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            addUser(user);
                            if(!isExist) {
                                Toast.makeText(RegisterPage.this, "Register success", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                    dialog2.show();
                }
            }
        });
    }

    protected void initControl() {
        Username = (EditText) findViewById(R.id.reg_et_username);
        Password = (EditText) findViewById(R.id.reg_et_password);
        PasswordConfirm = (EditText) findViewById(R.id.reg_et_passwordCheck);
        Email = (EditText) findViewById(R.id.reg_et_email);
        MsgPasswordConfirm = (TextView) findViewById(R.id.reg_tv_notmatch);
        RadioGroup_gender = (RadioGroup) findViewById(R.id.radioGroup1);
        register = (Button) findViewById(R.id.reg_btn_register);
        db = new DataBase(RegisterPage.this);
        Username.requestFocus();
    }

    protected void errorMsg() {
        boolean Sign1 = Username.getText().length() < 20;
        boolean Sign2 = Username.getText().length() > 4;
        boolean Sign3 = Password.getText().length() > 7;
        boolean Sign4 = Password.getText().length() < 20;
        boolean Sign5 = PasswordConfirm.getText().length() > 0;
        boolean Sign6 = Email.getText().length() > 7;
        boolean Sign7 = Email.getText().length() < 25;
        boolean Sign8 = (Email.getText().toString()).indexOf("@") > 0;
        boolean Sign9 = (Email.getText().toString()).indexOf(".") > 0;
        boolean Sign10 = false;
        boolean Sign11 = false;
        for(int i =0;i<Password.getText().length();i++){
            char c = Password.getText().toString().charAt(i);
            if(Character.isUpperCase(c)){
                Sign10 = true;
            }
            if(Character.isLowerCase(c)){
                Sign11 =  true;
            }
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterPage.this);
        if (!(Sign1 & Sign2)) {
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
        } else if (compileExChar(Username.getText().toString())) {
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
        } else if (!(Sign3 & Sign4 & Sign5)) {
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
        } else if (compileExChar(Password.getText().toString()) & compileExChar(PasswordConfirm.getText().toString())) {
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
        } else if (!(Sign6 & Sign7)) {
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
        } else if (compileExChar(Email.getText().toString())) {
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
        } else if (!(Sign8 & Sign9)) {
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
        } else if(!(Sign10 & Sign11)){
            dialog.setTitle("Input error");
            checklength = false;
            dialog.setMessage("Password should be at least contain one uppercase and lowercase.");
            dialog.setPositiveButton("Return", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            dialog.show();

        } else if(!radio){
            dialog.setTitle("Input error");
            checklength = false;
            dialog.setMessage("Please select your gender.");
            dialog.setPositiveButton("Return", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            dialog.show();
        } else if(!pwd){
            dialog.setTitle("Input error");
            checklength = false;
            dialog.setMessage("Please check your password.");
            dialog.setPositiveButton("Return", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            dialog.show();
        }

    }

    private boolean compileExChar(String str) {
        String limitEx = "[`~ !#$%^&*()+=|{}':;',\\[\\]<>/?~#%&*—+|{}'\"]";
        Pattern pattern = Pattern.compile(limitEx);
        Matcher m = pattern.matcher(str);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

    public void addUser(User user) {
        flags = 0;
        String nameCheck,emailCheck;
        String userCheck = "select * from user";
        AlertDialog.Builder dialogForDb = new AlertDialog.Builder(RegisterPage.this);
        SQLiteDatabase dbWrite = db.getWritableDatabase();
        Cursor cursor = dbWrite.rawQuery(userCheck,null);
        while(cursor.moveToNext()){
            nameCheck = cursor.getString(0);
            emailCheck = cursor.getString(2);
            if(user.getUsername().equals(nameCheck)){
                flags = 1;
                break;
            }
            if(user.getEmail().equals(emailCheck)){
                flags = 2;
                break;
            }
        }
        switch (flags){
            case 0:
                ContentValues cv = new ContentValues();
                cv.put("username", user.getUsername());
                cv.put("password", user.getPassword());
                cv.put("gender", user.getGender());
                cv.put("email", user.getEmail());
                dbWrite.insert("user", null, cv);
                cv.clear();
                isExist = false;
                break;
            case 1:
                //Title
                dialogForDb.setTitle("Error Message");
                //Message
                dialogForDb.setMessage("Use " + Username.getText().toString() + " already existed, try another one!");
                //Button
                dialogForDb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                Username.setText("");
                Username.requestFocus();
                isExist = true;
                break;
            case 2:
                dialogForDb.setMessage("Email address " + Email.getText().toString() + " already existed, try another one!");
                //Button
                dialogForDb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                Email.setText("");
                Email.requestFocus();
                isExist = true;
                break;
            default:
                break;
           }
            dbWrite.close();
            dialogForDb.show();
        }

    @Override
    public void onBackPressed(){
        //Creat an alerdialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterPage.this);
        //Title
        dialog.setTitle("Register");
        //Message
        dialog.setMessage("Cancel register?");
        //right side button
        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

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
    }


