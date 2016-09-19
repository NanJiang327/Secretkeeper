package com.example.lovej.secretkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by FD-GHOST on 2016/9/18 1450.
 */
public class MySecret extends AppCompatActivity {
    private ScrollView secrets;
    private Button btn_home, btn_me;
    private ImageButton btn_newSec;
    private LinearLayout mySecrets;
    private TextView child;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_secret);

        Bundle bundle = this.getIntent().getExtras();
        name = bundle.getString("Name");

        init();
        btn_newSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MySecret.this, PostPage.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MySecret.this, HomePage.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btn_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MySecret.this, "You are already in the Me page.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void init() {
        secrets = (ScrollView) findViewById(R.id.me_mid);
        secrets.setOnTouchListener(new TouchListenerImpl());
        btn_newSec = (ImageButton) findViewById(R.id.btn_me_plus);
        btn_home = (Button) findViewById(R.id.btn_me_home);
        btn_me = (Button) findViewById(R.id.btn_me_me);
        mySecrets = (LinearLayout) findViewById(R.id.my_scorllV);
    }

    private void addSecret() {
        child = new TextView(MySecret.this);
        child.setHeight(findViewById(R.id.textView).getHeight());
        child.setText("Go Go Go");
        mySecrets.addView(child);
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
                        Toast.makeText(MySecret.this, "Top", Toast.LENGTH_SHORT).show();
                    }
                    if ((scrollY + height) == scrollViewMeasuredHeight) {
                        Toast.makeText(MySecret.this, "bot", Toast.LENGTH_SHORT).show();
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