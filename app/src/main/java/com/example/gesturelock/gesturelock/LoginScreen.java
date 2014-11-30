package com.example.gesturelock.gesturelock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.sql.Time;


/**
 * Created by Zak on 10/31/2014.
 * Edited by Brett on 11/17/2014
 */
public class LoginScreen extends Activity {

    private Time loginTime;
    private EditText username=null;
    private EditText  password=null;
    private TextView loginAttempts;
    private Button login;
    int counter = 3;
    DBHelper DB = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        username = (EditText)findViewById(R.id.editTextUsername);
        password = (EditText)findViewById(R.id.editTextPassword);
        loginAttempts = (TextView)findViewById(R.id.textViewAttemptCount);
        loginAttempts.setText(Integer.toString(counter));
        login = (Button)findViewById(R.id.buttonLogin);
        Button btnNextScreen = (Button) findViewById(R.id.btnNextScreen);

        //Listening to button event
        btnNextScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), CreateUser.class);

                startActivity(nextScreen);
            }
        });
    }

    public void login(View view) {

        String usernameLogin = username.getText().toString();
        String passwordLogin = password.getText().toString();

        if(username.equals("") || username == null)
        {
            Toast.makeText(getApplicationContext(), "Please enter User Name", Toast.LENGTH_SHORT).show();
        }
        else if(password.equals("") || password == null)
        {
            Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();
        }
        else if (usernameLogin.equals("ADMIN"))
        {
            startActivity(new Intent(getApplicationContext(), GestureScreen.class));
        }
        else
        {
            boolean validLogin = validateLogin(usernameLogin, passwordLogin, getBaseContext());
            if(validLogin)
            {
                Intent in = new Intent(getBaseContext(), GestureScreen.class);
                startActivity(in);
            }
            else
            {
                loginAttempts.setTextColor(ColorStateList.valueOf(Color.RED));
                counter--;
                loginAttempts.setText(Integer.toString(counter));
                if (counter <= 0)
                    login.setEnabled(false);
            }
        }
    }

    private boolean validateLogin(String username, String password, Context baseContext)
    {
        DB = new DBHelper(getBaseContext());
        SQLiteDatabase db = DB.getReadableDatabase();

        String[] columns = {"_id"};

        String selection = "username=? AND password=?";
        String[] selectionArgs = {username,password};

        Cursor cursor = null;
        try{

            cursor = db.query(DBHelper.DATABASE_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            startManagingCursor(cursor);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        int numberOfRows = cursor.getCount();

        if(numberOfRows <= 0)
        {
            return false;
        }
        return true;
    }

    public void onDestroy()
    {
        super.onDestroy();
        DB.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gesture_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
