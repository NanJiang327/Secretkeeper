package com.example.lovej.secretkeeper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.text.TextWatcher;

/**
 * Created by Tao Li on 2016/8/21 0021.
 */
public class PostPage extends AppCompatActivity {

    private EditText secret;
    private Button btn_post, btn_home, btn_me;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_post_page);

        secret = (EditText)findViewById(R.id.edit_secret);
        secret.addTextChangedListener(new TextChange());

        initalizeBtn();
        btn_post.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                enterSecret(getSecret());
                //Confirm Info
                AlertDialog.Builder dialog = new AlertDialog.Builder(PostPage.this);
                dialog.setTitle("Confirm your secret");
                dialog.setMessage(secret.getText().toString());
                dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final ProgressDialog pD = new ProgressDialog(PostPage.this);
                        pD.setTitle("Please wait");
                        pD.setMessage("Posting...");
                        pD.show();
                        Thread thread = new Thread() {
                            public void run() {
                                try {
                                    sleep(1200);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                pD.dismiss();
                                Intent intent = new Intent(PostPage.this, HomePage.class);
                                startActivity(intent);
                            }
                        };
                        thread.start();
                        pD.setCancelable(true);

                    }
                });
                dialog.show();



            }
        });


        btn_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostPage.this, HomePage.class);
                startActivity(intent);
            }
        });


        btn_me.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

            }

        });
    }

    public void initalizeBtn(){
        btn_post = (Button) findViewById(R.id.post_secret);
        btn_home =(Button)findViewById(R.id.return_home);
        btn_me = (Button)findViewById(R.id.return_me);
    }



    public String getSecret(){
        return secret.getText().toString();
    }

    public String enterSecret(String secretA){
        String wantedSecret = secretA;
        if("".equals(wantedSecret)){
            wantedSecret = "Re-input";
        }
        return wantedSecret;
    }

    class TextChange implements TextWatcher{
        @Override
        public void afterTextChanged(Editable arg0) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence cs, int start, int before,
                                  int count) {
            boolean Sign = secret.getText().length() > 0;
            if (Sign) {
                btn_post.setBackgroundColor(0xFFFF9900);
                btn_post.setEnabled(true);

            }else{
                btn_post.setBackgroundColor(0xFFCCCCCC);
                btn_post.setEnabled(false);
            }
        }

    }






}
