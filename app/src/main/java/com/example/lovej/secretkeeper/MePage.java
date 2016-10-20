package com.example.lovej.secretkeeper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by FD-GHOST on 2016/9/18 1450.
 * Description: This class is for the Me Page
 */
public class MePage extends AppCompatActivity {
    private ScrollView secrets;
    private Button btn_home, btn_me;
    private ImageButton btn_newSec;
    private LinearLayout mySecrets;
    private TextView child;
    private String name;
    private DataBase db;

    /**
     * @param savedInstanceState The Bundle from previous activity
     * @description: <This function will be execute when this class have been called, which is initialize the components>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_me_page);

        Bundle bundle = this.getIntent().getExtras();
        name = bundle.getString("Name");

        init();
        btn_newSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MePage.this, PostPage.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btn_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MePage.this, "You are already in the me page.", Toast.LENGTH_SHORT).show();
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MePage.this, HomePage.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        getMySecrets();
    }

    /**
     * @description: <This function is initialize related page xml file to be editable by the code>
     */
    private void init() {
        secrets = (ScrollView) findViewById(R.id.me_mid);
        btn_newSec = (ImageButton) findViewById(R.id.btn_me_plus);
        btn_home = (Button) findViewById(R.id.btn_me_home);
        btn_me = (Button) findViewById(R.id.btn_me_me);
        mySecrets = (LinearLayout) findViewById(R.id.my_scorllV);
        db = new DataBase(MePage.this);
    }
    /**
     * @description: <This function is to receive the all the secret from database which this user has been posted, create a new component and add it to me page secret list>
     */
    private void getMySecrets(){
        String mysecret,namecheck,bg;
        int mysecretID;
        SQLiteDatabase dbRead = db.getReadableDatabase();
        Cursor cursor = dbRead.query("SECRET", null,null,null,null,null,null);
        int total = cursor.getCount();
        if(total>0){
            while (cursor.moveToNext()) {
                namecheck = cursor.getString(cursor.getColumnIndex("username"));
                mysecret = cursor.getString(cursor.getColumnIndex("content"));
                mysecretID = cursor.getInt(cursor.getColumnIndex("secretid"));
                bg = cursor.getString(cursor.getColumnIndex("background"));
                if(name.equals(namecheck)) {
                    child = new TextView(MePage.this);
                    child.setText("" + mysecretID + " \n " + mysecret + "");
                    child.setHeight(250);
                    child.setTextSize(15);
                    child.setTextColor(Color.BLACK);
                    switch (bg) {
                        case "dog":
                            child.setBackgroundResource(R.drawable.dog);
                            break;
                        case "nature":
                            child.setBackgroundResource(R.drawable.nature);
                            break;
                        case "wave":
                            child.setBackgroundResource(R.drawable.wave);
                            break;
                        case "Barries":
                            child.setBackgroundResource(R.drawable.barries);
                            break;
                        case "star":
                            child.setBackgroundResource(R.drawable.starwar);
                            break;
                        default:
                            break;
                    }
                    child.getBackground().setAlpha(180);
                    mySecrets.addView(child);
                }
            }

        }
        cursor.close();

    }

}