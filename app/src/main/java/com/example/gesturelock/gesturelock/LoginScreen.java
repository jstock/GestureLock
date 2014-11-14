package com.example.gesturelock.gesturelock;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Time;


/**
 * Created by Zak on 10/31/2014.
 */
public class LoginScreen extends Activity {

    private Time loginTime;
    private EditText username=null;
    private EditText  password=null;
    private TextView loginAttempts;
    private Button login;
    int counter = 3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        username = (EditText)findViewById(R.id.editTextUsername);
        password = (EditText)findViewById(R.id.editTextPassword);
        loginAttempts = (TextView)findViewById(R.id.textViewAttemptCount);
        loginAttempts.setText(Integer.toString(counter));
        login = (Button)findViewById(R.id.buttonLogin);
    }

    public void login(View view) {

        if (username.getText().toString().toUpperCase().equals("ADMIN"))
        {
            startActivity(new Intent(getApplicationContext(), GestureScreen.class));
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
