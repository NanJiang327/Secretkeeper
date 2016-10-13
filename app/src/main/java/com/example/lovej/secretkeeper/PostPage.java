package com.example.lovej.secretkeeper;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Created by Tao Li on 13/09/2016.
 */
public class PostPage extends AppCompatActivity{
    private EditText secret;
    private Button btn_post, btn_home, btn_me;
    private static DataBase db;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_post_page);

        Bundle bundle = this.getIntent().getExtras();
        name = bundle.getString("Name");


        secret = (EditText)findViewById(R.id.edit_secret);
        secret.addTextChangedListener(new TextChange());

        initalizeBtn();
        initalizeDB();
        btn_post.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                enterSecret(getSecret());
                //Confirm Info
                AlertDialog.Builder dialog = new AlertDialog.Builder(PostPage.this);
                dialog.setTitle("Confirm your secret");
                dialog.setMessage(secret.getText().toString()+"*Username:"+name);
                dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final ProgressDialog pD = new ProgressDialog(PostPage.this);
                        pD.setTitle("Please wait");
                        pD.setMessage("Posting...");
                        pD.show();


                        saveSecret(name);


                        Thread thread = new Thread() {
                            public void run() {
                                try {
                                    sleep(1200);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                pD.dismiss();
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("Name", name);
                                intent.putExtras(bundle);
                                sendBroadcast(intent);
                                finish();
                            }
                        };
                        thread.start();
                        pD.setCancelable(true);

                    }
                });
                dialog.show();



            }
        });


        btn_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PostPage.this);
                dialog.setTitle("Post Page");
                dialog.setMessage("Quit post page?");
                dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
            }
                    });
                dialog.show();
        }
        });


        btn_me.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder dialog = new AlertDialog.Builder(PostPage.this);
                dialog.setTitle("Post Page");
                dialog.setMessage("Quit post page?");
                dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(PostPage.this, MePage.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("Name", name);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.show();
            }

        });
    }

    public void initalizeDB(){
        db = new DataBase(PostPage.this);
    }

    public void initalizeBtn(){
        btn_post = (Button) findViewById(R.id.post_secret);
        btn_home =(Button)findViewById(R.id.return_home);
        btn_me = (Button)findViewById(R.id.return_me);
    }


/*
    public String getDBInfo(User user){
        String nameCheck,passwordCheck;
        SQLiteDatabase dbRead = db.getReadableDatabase();
        Cursor cursor = dbRead.query("user",null,null,null,null,null,null);
        while(cursor.moveToNext()){
            nameCheck = cursor.getString(cursor.getColumnIndex("username"));
            passwordCheck = cursor.getString(cursor.getColumnIndex("password"));
            if(user.getUsername().equals(nameCheck)){
                if(user.getPassword().equals(passwordCheck)){
                    return nameCheck;
                }else{
                    System.out.println("POSTPAGE-->getDBInfo error");
                }
                break;
            }else{
                System.out.println("POSTPAGE-->getDBInfo error");
                break;
            }
        }
        return "";
    }
*/


    //Refactoring saveSecret() for testing purpose
    public String getDBInfo_T(Map mockDB, String userName, String password){
        for(Object obj : mockDB.keySet()){
            if(((String)obj) == userName) {
                if(mockDB.get(obj).toString()==password){
                    return userName+password;
                }

            }
        }
        return "";
    }

    public void saveSecret(String userName){
        SQLiteDatabase dbWrite = db.getWritableDatabase();
        int number = new Random().nextInt(99999);
        number = checkSecretID(number);
        String content = secret.getText().toString();
        ContentValues values=new ContentValues();
        values.put("secretid",number);
        values.put("username",userName);
        values.put("content",content);
        dbWrite.insert("SECRET",null,values);

    }

    //Refactoring saveSecret() for testing purpose
    public Map saveSecret_T(){
        //SQLiteDatabase dbWrite = db.getWritableDatabase();
        //int number = new Random().nextInt(99999);
        //number = checkSecretID(number);
        //String content = secret.getText().toString();
        // ContentValues values=new ContentValues();
        //values.put("secretid",number);
        //values.put("username",userName);
        //values.put("content",content);
        //System.out.println(number+userName+content);
        //dbWrite.insert("SECRET",null,values);
        //String sqlInsert = "insert into SECRET values ("+number+","+"'"+userName+"'"+","+"'"+content+"'"+")";
        // dbRead.execSQL(sqlInsert);
        Map<String,String> mockDB = new HashMap<String, String>();
        mockDB.put("user01","This is test 01");
        mockDB.put("user02","This is test 02");
        mockDB.put("user03","This is test 03");
        return mockDB;
    }
    //Coupled with saveSecret_T() for testing
    public String getSecretFromDB_T(Map mockDB, String userName){
        for(Object obj : mockDB.keySet()){
            if(((String)obj) == userName)
                return mockDB.get(obj).toString();
        }
        return "";
    }



    public static int checkSecretID(int number){

        int idCheck;
        boolean flage = true;
        SQLiteDatabase dbRead = db.getReadableDatabase();
        Cursor cursor = dbRead.query("SECRET",null,null,null,null,null,null);
        while(cursor.moveToNext()) {
            idCheck = cursor.getInt(cursor.getColumnIndex("secretid"));
            while(flage)
            if (number == idCheck) {
                number = new Random().nextInt(99999);
            }else{
                flage = false;
            }
        }
        return number;
    }

    //Refactor checkSecretID() for testing purpose
    public int checkSecretID_T(int number){
        int idCheck;
        boolean flage = true;
        //SQLiteDatabase dbRead = db.getReadableDatabase();
        //Cursor cursor = dbRead.query("SECRET",null,null,null,null,null,null);
        List<Integer> mockDB= new ArrayList<Integer>();
        mockDB.add(1);
        mockDB.add(2);
        mockDB.add(3);
        mockDB.add(4);

        Iterator<Integer> it = mockDB.iterator();
        while(it.hasNext()) {
            //idCheck = cursor.getInt(cursor.getColumnIndex("secretid"));
            while(flage)
                if (number == (Integer)it.next()) {
                    number = new Random().nextInt(99999);
                }else{
                    flage = false;
                }
        }
        return number;
    }

    public String getSecretFromDB(int id){
        DataBase db = new DataBase(PostPage.this);
        int idCheck;
        String content;
        SQLiteDatabase dbRead = db.getReadableDatabase();
        Cursor cursor = dbRead.query("SECRET",null,null,null,null,null,null);
        while(cursor.moveToNext()){
             idCheck = cursor.getInt(cursor.getColumnIndex("secretid"));
            if(id == idCheck){
                content = cursor.getString(cursor.getColumnIndex("content"));
                return content;
            }else {
            }
        }

        return "";
    }


    public String getSecret(){

        return secret.getText().toString();
    }

    public String enterSecret(String secretA){
        String wantedSecret = secretA;
        if("".equals(wantedSecret)){
            wantedSecret = "Re-input";
        }
        return wantedSecret;
    }

    class TextChange implements TextWatcher{
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
            boolean Sign = secret.getText().length() > 0;
            if (Sign) {
                btn_post.setBackgroundColor(0xFFFF9900);
                btn_post.setEnabled(true);

            }else{
                btn_post.setBackgroundColor(0xFFCCCCCC);
                btn_post.setEnabled(false);
            }
        }

    }




}
