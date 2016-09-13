package com.example.lovej.secretkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by FD-GHOST/lovej on 2016/9/01 1450.
 */
public class HomePage extends AppCompatActivity {
    private ScrollView secrets;
    private Button btn_newSec, btn_home, btn_me;
    private LinearLayout l;
    private TextView child;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_page);
        init();
        btn_newSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, PostPage.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        secrets = (ScrollView) findViewById(R.id.home_mid);
        secrets.setOnTouchListener(new TouchListenerImpl());
        btn_newSec = (Button) findViewById(R.id.plus);
        btn_home = (Button) findViewById(R.id.home);
        btn_me = (Button) findViewById(R.id.me);
        l = (LinearLayout) findViewById(R.id.home_l);
    }

    private void addSecret() {
        child = new TextView(HomePage.this);
        child.setHeight(findViewById(R.id.textView).getHeight());
        child.setText("Go Go Go");
        l.addView(child);
        //this function does not work
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
                    if (scrollY == 0) {
                        Toast.makeText(HomePage.this, "Top", Toast.LENGTH_SHORT).show();
                    }
                    if ((scrollY + height) == scrollViewMeasuredHeight) {
                        Toast.makeText(HomePage.this, "bot", Toast.LENGTH_SHORT).show();
                        addSecret();
                    }
                    break;

                default:
                    break;
            }
            return false;
        }

    }
}