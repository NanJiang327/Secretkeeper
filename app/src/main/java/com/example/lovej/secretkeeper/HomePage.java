package com.example.lovej.secretkeeper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by FD-GHOST/lovej on 2016/9/01 1450.
 */
public class HomePage extends AppCompatActivity {
    private ScrollView secrets;
    private Button btn_home, btn_me;
    private ImageButton btn_newSec;
    private LinearLayout homeSecret;
    private EditText search;
    private TextView child;
    private String name;
    private DataBase db;
    private int lastid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_page);

        Bundle bundle = this.getIntent().getExtras();
        name = bundle.getString("Name");

        init();
        btn_newSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, PostPage.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage.this, "You are already in the home page.", Toast.LENGTH_SHORT).show();
            }
        });

        btn_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, MySecret.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void init() {
        secrets = (ScrollView) findViewById(R.id.home_mid);
        secrets.setOnTouchListener(new TouchListenerImpl());
        btn_newSec = (ImageButton) findViewById(R.id.btn_me_plus);
        btn_home = (Button) findViewById(R.id.home);
        btn_me = (Button) findViewById(R.id.me);
        homeSecret = (LinearLayout) findViewById(R.id.home_scorllV);
        db = new DataBase(HomePage.this);
        search = (EditText) findViewById(R.id.searchText);

    }

    private void addSecret(int id, String content) {
        child = new TextView(HomePage.this);
        child.setHeight(findViewById(R.id.first_home_textview).getHeight());
        child.setText("Secret Id: "+id+"  ||  Secret: "+content+"");
        child.setId(id);
        child.setTextColor(Color.BLACK);
        homeSecret.addView(child);
        //this function does not work
    }

    private void loadLatestTenSecreat(){
        SQLiteDatabase dbRead = db.getReadableDatabase();
        String[] SecInfo = {"secretid","content"};
        Cursor cursor = dbRead.query("SECRET",SecInfo,null,null,null,"secretid DESC",Integer.toString(10));
        int total = cursor.getCount();
        if(total>0) {
            while (cursor.moveToNext()) {
                int id = Integer.parseInt(cursor.getString(2));
                String content = cursor.getString(3);
                lastid=id;
                addSecret(id, content);
            }
        }else{
            addSecret(0,"No Secret yet, you can publish the first secret!!!");
        }
    }

    private void loadLatestSecret(){
        String content;
        int id;
        Toast.makeText(HomePage.this, "Loading secrets", Toast.LENGTH_SHORT).show();
        SQLiteDatabase dbRead = db.getReadableDatabase();
        String[] SecInfo = {"secretid","content"};
        Cursor cursor = dbRead.query("SECRET",null,null,null,null,null,null);
        int total = cursor.getCount();

        if(total>0) {
            while (cursor.moveToNext()) {
                content = cursor.getString(cursor.getColumnIndex("content"));
                id = cursor.getInt(cursor.getColumnIndex("secretid"));
                if (id != lastid) {
                    addSecret(id, content);
                } else {
                    Toast.makeText(HomePage.this, "No new secret", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }else{
            Toast.makeText(HomePage.this, "No new secret", Toast.LENGTH_SHORT).show();
        }

    }

    public void onBackPressed(){
        //Creat an alerdialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(HomePage.this);
        //Title
        dialog.setTitle("Secret");
        //Message
        dialog.setMessage("Quit???");
        //right side button
        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(HomePage.this, "Welcome Back 0.0", Toast.LENGTH_SHORT).show();
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

    private class TouchListenerImpl implements OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    break;
                case MotionEvent.ACTION_MOVE:
                    int scrollY = view.getScrollY();
                    int height = view.getHeight();
                    int scrollViewMeasuredHeight = secrets.getChildAt(0).getMeasuredHeight();
//                    if (scrollY== 0) {
//                        Toast.makeText(HomePage.this, "Top", Toast.LENGTH_SHORT).show();
//                    }
                    if ((scrollY + height) == scrollViewMeasuredHeight) {
                        //Toast.makeText(HomePage.this, "bot", Toast.LENGTH_SHORT).show();
                        loadLatestSecret();
                    }
                    break;

                default:
                    break;
            }
            return false;
        }

    }
}