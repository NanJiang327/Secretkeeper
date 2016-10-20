package com.example.lovej.secretkeeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by FD-GHOST on 2016/10/10.
 * Description: This class is for the Secret detail Page
 */

public class SecretDetail extends AppCompatActivity {
    private Bundle bundle;
    private String secret,name,bg,commenter;
    private int secretID, lastorder;
    private LinearLayout secretMid;
    private ScrollView scrollView;
    private Button send;
    private EditText commentText;
    private DataBase db;
    private TextView secretContent,first,child;

    /**
     * Description: <This function will be execute when this class have been called, which is initialize the components>
     *
     * @param savedInstanceState The Bundle from previous activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bundle = this.getIntent().getExtras();
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_secretdetail_page);
        init();
        readAllC();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });
    }

    /**
     * Description: <This function is initialize related page xml file to be editable by the code>
     */
    private void init() {
        secret = bundle.getString("secret");
        secretID = bundle.getInt("secretID");
        name = bundle.getString("name");
        bg = bundle.getString("bg");
        commentText = (EditText) findViewById(R.id.commentText);
        scrollView = (ScrollView) findViewById(R.id.secretMid);
        secretMid = (LinearLayout) findViewById(R.id.secret_scorllV);
        send = (Button) findViewById(R.id.secretSend);
        db = new DataBase(SecretDetail.this);
        secretContent = (TextView) findViewById(R.id.secretContent);
        secretContent.setText(secret);
        secretContent.setTextSize(20);
        secretContent.setTextColor(Color.BLACK);
        switch (bg) {
            case "dog":
                secretContent.setBackgroundResource(R.drawable.dog);
                break;
            case "nature":
                secretContent.setBackgroundResource(R.drawable.nature);
                break;
            case "wave":
                secretContent.setBackgroundResource(R.drawable.wave);
                break;
            case "Barries":
                secretContent.setBackgroundResource(R.drawable.barries);
                break;
            case "star":
                secretContent.setBackgroundResource(R.drawable.starwar);
                break;
            default:
                break;
        }
        secretContent.getBackground().setAlpha(180);
    }


    /**
     * Description: <This function is to read all the comment with this secret, and create new components to add to the comment list, will notify if no comment>
     */
    private void readAllC() {
        String content;
        int order;
        SQLiteDatabase dbRead = db.getReadableDatabase();
        Cursor cursor = dbRead.query("COMMENT", null,null,null,null,null,null);
        int total = cursor.getCount();
        if(total>0){
            while (cursor.moveToNext()) {
                content = cursor.getString(cursor.getColumnIndex("comment"));
                order = cursor.getInt(cursor.getColumnIndex("commentorder"));
                commenter = cursor.getString(cursor.getColumnIndex("commenter"));
                lastorder = order;
                child = new TextView(SecretDetail.this);
                child.setText("" + commenter + ": " + content);
                child.setId(order);
                child.setHeight(200);
                child.setTextSize(15);
                child.setTextColor(Color.BLACK);
                secretMid.addView(child);

            }

        }else {
            Toast.makeText(SecretDetail.this, "No comment yet", Toast.LENGTH_SHORT).show();
        }
        secretMid.removeView(first);
        cursor.close();
    }

    /**
     * @description: <This function will be called if the user slide down to the bottom, it will refresh new comment>
     */
    private void readLastComment(){
        String content;
        int order;
        SQLiteDatabase dbRead = db.getReadableDatabase();
        Cursor cursor = dbRead.query("COMMENT", null,null,null,null,null,null);
        cursor.moveToLast();
        content = cursor.getString(cursor.getColumnIndex("comment"));
        order = cursor.getInt(cursor.getColumnIndex("commentorder"));
        commenter = cursor.getString(cursor.getColumnIndex("commenter"));
        lastorder = order;
        child = new TextView(SecretDetail.this);
        child.setText("" + commenter + ": " + content);
        child.setId(order);
        child.setHeight((secretContent.getHeight()/2));
        child.setTextSize(15);
        child.setTextColor(Color.BLACK);
        secretMid.addView(child);

    }

    /**
     * @description: <This function will take the string from input field, and check if it is valid to be posted, if yes, it will connect to database, and update this comment to this secret >
     */
    private void postComment() {
        String comment = commentText.getText().toString();
        if (commentText.getText().toString().length()<1) {
            Toast.makeText(SecretDetail.this, "You need to say something.", Toast.LENGTH_SHORT).show();
        } else {
            SQLiteDatabase dbWrite = db.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("secretid", secretID);
            values.put("commentorder", lastorder + 1);
            values.put("commenter", name);
            lastorder++;
            values.put("comment", comment);
            dbWrite.insert("COMMENT", null, values);
            Toast.makeText(SecretDetail.this, "Posted", Toast.LENGTH_SHORT).show();
            commentText.setText("");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(commentText.getWindowToken(), 0) ;
            readLastComment();
        }
    }


}
