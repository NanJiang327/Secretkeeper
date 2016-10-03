package com.example.lovej.secretkeeper;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by lovej on 2016/10/3 0003.
 */

public class ArcadeGameActivity extends AppCompatActivity implements Game2048GridLayout.OnGame2048Listener {

    private TextView scoreView;
    private String name;
    Game2048GridLayout game2048Layout;
    private static DataBase db;
    private int reward = 0, score, userCoins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game_layout);
        initalizeDB();
        Bundle bundle = this.getIntent().getExtras();
        name = bundle.getString("Name");
        System.out.println();
        game2048Layout = (Game2048GridLayout) findViewById(R.id.game);
        game2048Layout.setOnGame2048Listener(this);
        scoreView = (TextView) findViewById(R.id.score);
    }

    @Override
    public void onScoreChange(int score) {
        scoreView.setText("Score : "+score);
    }

    @Override
    public void onGameOver() {
        score = game2048Layout.getScore();
        rewardRules(score);
        new AlertDialog.Builder(this).setTitle("Game Over")
                .setMessage("you have marks is "+ score +", so you can get "+reward+" coin(s)!").setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reward(name);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                bundle.putInt("COIN", userCoins);
                intent.putExtras(bundle);
                sendBroadcast(intent);
                finish();
            }
        }).show();
    }

    public void reward(String userName) {
        SQLiteDatabase dbWrite = db.getWritableDatabase();
        String SQL = "select * from COIN";
        Cursor cursor = dbWrite.rawQuery(SQL, null);
        while (cursor.moveToNext()) {
            String nameCheck = cursor.getString(cursor.getColumnIndex("username"));
            int coinCheck = cursor.getInt(cursor.getColumnIndex("coin"));
            if (name.equals(nameCheck)) {
                userCoins = coinCheck;
            }
            userCoins += reward;
            ContentValues values = new ContentValues();
            values.put("username", userName);
            values.put("coin",  userCoins);
            dbWrite.insert("COIN", null, values);
        }
    }

    public void initalizeDB(){
        db = new DataBase(ArcadeGameActivity.this);
    }

    public void rewardRules(int score){
        if(score < 500){
            reward = 0;
        }
        if(score < 1000 && score >=500){
            reward = 1;
        }
        if(score> 2500 && score < 10000){
            reward = 2;
        }
        if(score >= 10000){
            reward = 3;
        }
    }
}
