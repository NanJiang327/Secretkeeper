package com.example.lovej.secretkeeper;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by lovej on 2016/10/3 0003.
 */

public class ArcadeGameActivity extends AppCompatActivity implements Game2048GridLayout.OnGame2048Listener {

    private TextView scoreView;
    private Button play;
    private boolean found;
    private String name,nameCheck;
    private int flags,coinCheck;
    Game2048GridLayout game2048Layout;
    private DataBase db;
    private int reward = 0, score, userCoins;


    /**
     * @param savedInstanceState The Bundle from previous activity
     * @description: <This function will be execute when this class have been called, which is initialize related page xml file to be editable by the code>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        play = (Button) findViewById(R.id.game_btn_start);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game2048Layout.setVisibility(View.VISIBLE);
            }
        });
        db = new DataBase(ArcadeGameActivity.this);
        Bundle bundle = this.getIntent().getExtras();
        name = bundle.getString("Name");
        game2048Layout = (Game2048GridLayout) findViewById(R.id.game);
        game2048Layout.setOnGame2048Listener(this);
        scoreView = (TextView) findViewById(R.id.score);
        reward();
    }

    /**
     * @description: <This function will refresh the score on screen>
     */
    @Override
    public void onScoreChange(int score) {
        scoreView.setText("Score : "+score);
    }

    /**
     *
     * @description: <This function will be execute after the game is finished, and collect the game result back to the previous activity>
     */
    @Override
    public void onGameOver() {
        score = game2048Layout.getScore();
        rewardRules(score);
        new AlertDialog.Builder(this).setTitle("Game Over")
                .setMessage("Your marks is "+ score +", so you can get "+reward+" coin(s)!").setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reward();
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

    /**
     *
     * @description: <This function is to update the user info to database according to the game result>
     */
    public void reward() {
        SQLiteDatabase dbWrite = db.getWritableDatabase();
        String SQL = "select * from COIN";
        Cursor cursor = dbWrite.rawQuery(SQL, null);
        while(cursor.moveToNext()){
            nameCheck = cursor.getString(0);
            coinCheck = cursor.getInt(1);
            if (name.equals(nameCheck)) {
                flags = 0;
                found = true;
                break;
            }else{
                found = false;
            }
        }
        if(!found){
            flags = 1;
        }
        switch (flags) {
            case 0:
                userCoins = coinCheck;
                userCoins += reward;
                ContentValues values = new ContentValues();
                values.put("coin", userCoins);
                String condition = "username=?";
                String[] whereArgs = {name};
                dbWrite.update("COIN", values, condition, whereArgs);
                break;
            case 1:
                ContentValues cv = new ContentValues();
                cv.put("username", name);
                //Every new user will got 10 coins once they are registered
                cv.put("coin", 10);
                dbWrite.insert("COIN",null,cv);
                break;
            default:
                break;
        }

    }

    /**
     *
     * @description: <This function is the rules for detect reward>
     * @param   score The int score
     */
    public void rewardRules(int score){
        if(score < 500){
            reward = 0;
        }
        if(score < 2500 && score >=500){
            reward = 1;
        }
        if(score>= 2500 && score < 10000){
            reward = 2;
        }
        if(score >= 10000){
            reward = 3;
        }
    }
}
