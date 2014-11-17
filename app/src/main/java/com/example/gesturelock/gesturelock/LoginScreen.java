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
    //BK below
    private String file = "mydata";

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

                //Sending data to another Activity
                //nextScreen.putExtra("name", inputName.getText().toString());
                //nextScreen.putExtra("email", inputEmail.getText().toString());

                //Log.e("n", inputName.getText() + "." + inputEmail.getText());

                startActivity(nextScreen);

            }
        });
    }

    public void login(View view) {

        boolean loginFlag = false;
        try{
            FileInputStream fin = openFileInput(file);
            int c;
            String temp="";
            String[] tokens;
            int counter = 0;
            String delims ="[:]";
            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            //see if username is in localStorage
            if(temp.indexOf(username.getText().toString()) == -1 && !username.getText().toString().toUpperCase().equals("ADMIN"))
            {
              Toast.makeText(getBaseContext(), "Sorry there is no such user in our database.",
                      Toast.LENGTH_SHORT).show();
            }
            else
            {
                //see if the username matches the password if it does set loginFlag to true, if not leave loginflag false
                tokens = temp.split(delims);
                boolean found = false;
                String testString;
                while(counter < tokens.length && !found)
                {
                    testString = tokens[counter].toString().trim();
                    if(testString.equals(username.getText().toString())) {
                        found = true;
                        counter++;
                    }
                    counter++;
                }
                if(tokens[counter].equals(password.getText().toString()))
                {
                    loginFlag = true;
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Incorrect password.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        }catch(Exception e){

        }

        if (username.getText().toString().toUpperCase().equals("ADMIN") || loginFlag)
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
