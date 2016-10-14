package com.example.lovej.secretkeeper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by FD-GHOST on 2016/10/10.
 */

public class SecretDetail extends AppCompatActivity {
    Bundle bundle;
    String secret;
    int secretID, lastorder;
    private LinearLayout secretMid;
    private Button send;
    private EditText commentText;
    private DataBase db;
    private TextView secretContent, child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bundle = this.getIntent().getExtras();
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_secretdetail_page);
        init();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });
    }

    private void init() {
        secret = bundle.getString("secret");
        secretID = bundle.getInt("secretID");
        System.out.println(secret);
        System.out.println(secretID);
        commentText = (EditText) findViewById(R.id.commentText);
        secretMid = (LinearLayout) findViewById(R.id.secret_scorllV);
        send = (Button) findViewById(R.id.secretSend);
        db = new DataBase(SecretDetail.this);
        secretContent = (TextView) findViewById(R.id.secretContent);
        System.out.println(secretContent);
        System.out.println(commentText);
        System.out.println(secretMid);
        System.out.println(send);
        secretContent.setText(secret);
        secretContent.setHeight(secretContent.getLineHeight() * secretContent.getLineCount());
        secretContent.setTextSize(20);
        secretContent.setTextColor(Color.BLACK);
    }

    private void readAllC() {
        String content;
        int order;

        Toast.makeText(SecretDetail.this, "Loading comment", Toast.LENGTH_SHORT).show();
        SQLiteDatabase dbRead = db.getReadableDatabase();
        String[] SecInfo = {"secretid", "commentorder", "comment"};
        Cursor cursor = dbRead.query("COMMENT", new String[]{"secretid，commentorder,comment"}, "secretid=?", new String[]{"secretID"}, null, "commentorder ASC", null);
        Toast.makeText(SecretDetail.this, "database query", Toast.LENGTH_SHORT).show();
        int total = cursor.getCount();

        if (total > 0) {
            while (cursor.moveToNext()) {
                content = cursor.getString(cursor.getColumnIndex("comment"));
                order = cursor.getInt(cursor.getColumnIndex("commentorder"));
                lastorder = order;
                child = new TextView(SecretDetail.this);
                child.setText(content);
                child.setId(order);
                child.setHeight(child.getLineHeight() * child.getLineCount());
                child.setTextSize(20);
                child.setTextColor(Color.BLACK);
                secretMid.addView(child);
            }

        } else {
            Toast.makeText(SecretDetail.this, "No comment yet", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }


    private void postComment() {
        String comment = commentText.getText().toString();
        String check = comment.replaceAll(" ", "");
        if (check.equals(null) || check.equals("") || check.length() == 0 || check.isEmpty()) {
            Toast.makeText(SecretDetail.this, "The comment is empty", Toast.LENGTH_SHORT).show();
        } else {
            SQLiteDatabase dbWrite = db.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("secretid", secretID);
            values.put("commentorder", lastorder + 1);
            lastorder++;
            values.put("comment", comment);
            dbWrite.insert("COMMENT", null, values);
            Toast.makeText(SecretDetail.this, "Posted", Toast.LENGTH_SHORT).show();
            child = new TextView(SecretDetail.this);
            child.setText(comment);
            child.setId(lastorder);
            child.setHeight(child.getLineHeight() * child.getLineCount());
            child.setTextSize(20);
            child.setTextColor(Color.BLACK);
            secretMid.addView(child);
        }
    }


}
