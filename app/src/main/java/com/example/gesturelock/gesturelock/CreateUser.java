package com.example.gesturelock.gesturelock;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
    private DBHelper DB = new DBHelper(CreateUser.this);

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
                String uname = userLog.getText().toString();
                String passAttempt = pass.getText().toString();
                String verifyAttempt = passVerify.getText().toString();

                boolean invalid = false;

                if(uname.equals(""))
                {
                    invalid = true;
                    Toast.makeText(getApplicationContext(), "Please enter your Username", Toast.LENGTH_SHORT).show();
                }
                else
                if(passAttempt.equals(""))
                {
                    invalid = true;
                    Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();

                }
                else
                if(verifyAttempt.equals(""))
                {
                    invalid = true;
                    Toast.makeText(getApplicationContext(), "Please verify your password", Toast.LENGTH_SHORT).show();
                }
                else
                if (!passAttempt.equals(verifyAttempt))
                {
                    invalid = true;
                    Toast.makeText(getBaseContext(), "Your passwords don't match, please try again.", Toast.LENGTH_SHORT).show();
                }

                if(invalid == false)
                {
                    addEntry(uname, passAttempt);
                    Intent i_register = new Intent(CreateUser.this, LoginScreen.class);
                    startActivity(i_register);
                    //finish();
                }
            }
        });

    }

    public void onDestroy()
    {
        super.onDestroy();
        DB.close();
    }

    private void addEntry(String uname, String pass)
    {

        SQLiteDatabase db = DB.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("username", uname);
        values.put("password", pass);

        try
        {
            db.insert(DBHelper.DATABASE_TABLE_NAME, null, values);

            Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gesture_screen, menu);
        return true;
    }

}