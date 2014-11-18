package com.example.gesturelock.gesturelock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;


/**
 * Created by Brett on 11/17/2014.
 */
public class CreateUser extends Activity {

    private EditText userLog;
    private EditText pass;
    private String data;
    private EditText passVerify;
    private String file = "mydata";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_create_screen);
        userLog = (EditText)(findViewById(R.id.username));
        pass = (EditText)(findViewById(R.id.createPassword));
        passVerify = (EditText)(findViewById(R.id.verifyPassword));

        Button btnClose = (Button) findViewById(R.id.button1);
        Intent i = getIntent();
        // Binding Click event to Button
        btnClose.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                data = "\n:Username:" + userLog.getText().toString() + ":password:" + pass.getText().toString() + ":\n";
                boolean flag = false;
                if(!pass.getText().toString().equals(passVerify.getText().toString()))
                {
                    flag = true;
                    Toast.makeText(getBaseContext(), "Your passwords don't match, please try again.",
                            Toast.LENGTH_SHORT).show();
                }
                if(!flag) {
                    try {
                        FileInputStream fin = openFileInput(file);
                        int c;
                        String temp = "";
                        while ((c = fin.read()) != -1) {
                            temp = temp + Character.toString((char) c);
                        }
                        if (pass.getText().toString().isEmpty() || userLog.getText().toString().isEmpty()) {
                            flag = true;
                            Toast.makeText(getBaseContext(), "You must enter a username and a password.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if (temp.indexOf(userLog.getText().toString()) > -1) {
                            flag = true;
                            Toast.makeText(getBaseContext(), "A user with that name already exists, please try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if (!flag) {
                            //needs to be Context.MODE_APPEND
                            FileOutputStream fOut = openFileOutput(file, Context.MODE_APPEND);
                            fOut.write(data.getBytes());
                            fOut.close();
                            Toast.makeText(getBaseContext(), "User created",
                                    Toast.LENGTH_SHORT).show();
                            //Closing SecondScreen Activity
                            finish();
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        //Closing SecondScreen Activity
                        finish();
                    }
                }

            }
        });

    }
    public void read(View view){
        try{
            FileInputStream fin = openFileInput(file);
            int c;
            String temp="";
            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            userLog.setText(temp);
            Toast.makeText(getBaseContext(), "file read",
                    Toast.LENGTH_SHORT).show();

        }catch(Exception e){

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gesture_screen, menu);
        return true;
    }

}