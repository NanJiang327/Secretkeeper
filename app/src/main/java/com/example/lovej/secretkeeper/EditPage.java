package com.example.lovej.secretkeeper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lovej on 2016/9/19 0019.
 */
public class EditPage extends AppCompatActivity {
    private Button btn_edit_home,btn_edit_me,btn_edit_edit,btn_edit_delete;
    private TextView secret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_page);
        init();
        btn_edit_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditPage.this);
                //Title
                dialog.setTitle("Edit Page");
                //Message
                dialog.setMessage("Quit???");
                //right side button
                dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(EditPage.this, "Welcome Back 0.0", Toast.LENGTH_SHORT).show();
                    }
                });
                //left side button
                dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(EditPage.this, HomePage.class);
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        });
        btn_edit_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditPage.this);
                //Title
                dialog.setTitle("Edit Page");
                //Message
                dialog.setMessage("Quit???");
                //right side button
                dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(EditPage.this, "Welcome Back 0.0", Toast.LENGTH_SHORT).show();
                    }
                });
                //left side button
                dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(EditPage.this, MySecret.class);
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        });
        btn_edit_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO
            }
        });
        //TODO
        //secret.setText();

    }

    public void onBackPressed(){
        //Creat an alerdialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(EditPage.this);
        //Title
        dialog.setTitle("Edit Page");
        //Message
        dialog.setMessage("Quit???");
        //right side button
        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(EditPage.this, "Welcome Back 0.0", Toast.LENGTH_SHORT).show();
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

    public void init(){
        btn_edit_home = (Button)findViewById(R.id.btn_edit_home);
        btn_edit_me  = (Button)findViewById(R.id.btn_edit_me);
        btn_edit_edit  = (Button)findViewById(R.id.btn_edit_edit);
        btn_edit_delete  = (Button)findViewById(R.id.btn_edit_delete);
        secret = (TextView)findViewById(R.id.tv_edit_secretview);

    }
}
