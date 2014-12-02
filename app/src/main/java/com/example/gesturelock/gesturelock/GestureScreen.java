package com.example.gesturelock.gesturelock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Home screen
 */
public class GestureScreen extends Activity {

    private Button mGotoSaveGestureButton;
    private Button mGotoGestureListButton;
    private Button mGotoGestureInputButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_screen);

        // Set up button to go to save gesture activity
        mGotoSaveGestureButton =  (Button) findViewById(R.id.goto_SaveGesture_button);
        mGotoSaveGestureButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestureScreen.this, SaveGesture.class);
                startActivity(intent);
            }
        });

        // Set up button to go to gesture list activity
        mGotoGestureListButton =  (Button) findViewById(R.id.goto_GestureList_button);
        mGotoGestureListButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestureScreen.this, GestureListActivity.class);
                startActivity(intent);
            }
        });

        mGotoGestureInputButton = (Button) findViewById(R.id.btnInputGesture);
        mGotoGestureInputButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GestureScreen.this, InputActivity.class);
                startActivity(intent);
            }
        });
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
