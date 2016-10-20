package com.example.lovej.secretkeeper;

import android.app.AlertDialog;
import android.content.ContentValues;
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
 * Description: This class is for the control of homepage
 */
public class HomePage extends AppCompatActivity {
    private ScrollView secrets;
    private Button btn_home, btn_me, btn_game;
    private ImageButton btn_newSec,btn_search;
    private LinearLayout homeSecret;
    private EditText search;
    private String bg;
    private TextView first, child;
    private String name,content,background;
    private DataBase db;
    private int id, numOfSecret = 0, coins;
    private boolean found;
    private float y1 = 0,y2 = 0;


    /**
     * @param savedInstanceState The Bundle from previous activity
     * @description: <This function will be execute when this class have been called, which is initialize the components>
     */
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
                Intent intent = new Intent(HomePage.this, MePage.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btn_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, ArcadeGameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btn_game.getBackground().setAlpha(200);


        /**
         *
         * @description: <Here is the that we allow user to entry a secret id, and it cost 1 coin each time,
         * we have defined that each id has 5 digits, or report if this id dost not exist>
         */
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String secrets = search.getText().toString();
                if(secrets.length()<5){
                    Toast.makeText(HomePage.this, "The ID of secret is five digits.", Toast.LENGTH_SHORT).show();
                    search.setText("");
                }else{
                    int searchId = Integer.parseInt(secrets);
                    SQLiteDatabase dbRead = db.getReadableDatabase();
                    final Cursor cursor = dbRead.query("SECRET",null,null,null,null,null,null);
                    Cursor cursor2 =dbRead.query("COIN",null,null,null,null,null,null);
                    while (cursor2.moveToNext()){
                        String nameInDb = cursor2.getString(cursor2.getColumnIndex("username"));
                        coins = cursor2.getInt(cursor2.getColumnIndex("coin"));
                        if(name.equals(nameInDb)){
                            break;
                        }
                    }
                    while(cursor.moveToNext()){
                        final int IDInDb = cursor.getInt(cursor.getColumnIndex("secretid"));
                        final String secretDetails = cursor.getString(cursor.getColumnIndex("content"));
                        final String bg = cursor.getString(cursor.getColumnIndex("background"));
                        if(searchId == IDInDb){
                            AlertDialog.Builder dialog = new AlertDialog.Builder(HomePage.this);
                            final Intent intent = new Intent(HomePage.this, SecretDetail.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("secret", secretDetails);
                            bundle.putInt("secretID", IDInDb);
                            bundle.putString("name", name);
                            bundle.putString("bg", bg);
                            intent.putExtras(bundle);
                            dialog.setTitle("Secret");
                            dialog.setMessage("Secret found! Click OK to view the details, it will takes your 1 coin.");
                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(coins < 1){
                                        Toast.makeText(HomePage.this, "You don't have any coins yet, play the game to get coins", Toast.LENGTH_SHORT).show();
                                    }else{
                                        coins--;
                                        SQLiteDatabase dbWrite = db.getWritableDatabase();
                                        ContentValues values = new ContentValues();
                                        values.put("coin",coins);
                                        String condition = "username=?";
                                        String[] whereArgs = {name};
                                        dbWrite.update("COIN", values, condition, whereArgs);
                                        startActivity(intent);
                                    }
                                    dialogInterface.dismiss();
                                }
                            });
                            dialog.show();
                            found = true;
                            break;
                        }else{
                            found = false;
                        }
                    }
                    cursor.close();
                    search.setText("");
                    if(!found){
                        Toast.makeText(HomePage.this, "This ID does not exist.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * @description: <This function is initialize related page xml file to be editable by the code>
     */
    private void init() {
        secrets = (ScrollView) findViewById(R.id.home_mid);
        secrets.setOnTouchListener(new TouchListenerImpl());
        btn_newSec = (ImageButton) findViewById(R.id.btn_me_plus);
        btn_search = (ImageButton) findViewById(R.id.home_btn_search);
        btn_game = (Button)findViewById(R.id.home_btn_Arcade);
        btn_home = (Button) findViewById(R.id.home);
        btn_me = (Button) findViewById(R.id.me);
        homeSecret = (LinearLayout) findViewById(R.id.home_scorllV);
        db = new DataBase(HomePage.this);
        search = (EditText) findViewById(R.id.searchText);
        first = (TextView) findViewById(R.id.first_home_textview);
    }

    /**
     * @param id The secret id
     * @param content The content of Secret
     * @param background The background image define (String) of this Secret
     * @description: <This function is to receive the info of a secret to create a new component and add it to home page secret list>
     */
    private void addSecret(int id, String content, String background) {
        bg = background;
        child = new TextView(HomePage.this);
        child.setHeight(77);
        child.setText("Secret Id: "+id+"  \nSecret: "+content+"");
        child.setId(id);
        child.setTextSize(20);
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
        child.setTextColor(Color.BLACK);
        homeSecret.addView(child);
        homeSecret.removeView(first);
        numOfSecret += 1;

    }

    /**
     * @description: <This function will be called if the user slide down to the bottom>
     */
    private void loadLatestSecret(){
        SQLiteDatabase dbRead = db.getReadableDatabase();
        Cursor cursor = dbRead.query("SECRET",null,null,null,null,null,null);
        int total = cursor.getCount();

        if(total>0) {
            while (cursor.moveToNext()) {
                content = cursor.getString(cursor.getColumnIndex("content"));
                id = cursor.getInt(cursor.getColumnIndex("secretid"));
                background = cursor.getString(cursor.getColumnIndex("background"));
                if (numOfSecret < total) {
                    if(!(total - numOfSecret ==1)){
                        addSecret(id, content, background);
                    }else{
                         cursor.moveToLast();
                         content = cursor.getString(cursor.getColumnIndex("content"));
                         id = cursor.getInt(cursor.getColumnIndex("secretid"));
                         background = cursor.getString(cursor.getColumnIndex("background"));
                        addSecret(id, content, background);
                    }

                } else {
                    Toast.makeText(HomePage.this, "No new secret", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }else{
            Toast.makeText(HomePage.this, "No new secret", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    /**
     * @description: <This function will detected if the user press back button in home page, we will ask of the user wants to exit this app>
     */
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

    /**
     * @description: <This is the listener for the Sliding detection. if it has been detected to the bottom, it will try to load newly posted secret>
     */
    private class TouchListenerImpl implements OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    y1 = motionEvent.getY();
                    int scrollY = view.getScrollY();
                    int height = view.getHeight();
                    int scrollViewMeasuredHeight = secrets.getChildAt(0).getMeasuredHeight();
                    if (scrollY== 0) {

                    }
                    if ((scrollY + height-view.getPaddingTop()-view.getPaddingBottom()) == scrollViewMeasuredHeight) {
                        //Toast.makeText(HomePage.this, "bot", Toast.LENGTH_SHORT).show();
                        if(y2 - y1 < 0) {
                            loadLatestSecret();

                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    y2 = motionEvent.getY();
                    break;

                default:
                    break;
            }
            return false;
        }

    }
}