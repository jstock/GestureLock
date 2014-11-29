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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Toast.makeText(this.getApplicationContext(), "Down", Toast.LENGTH_SHORT).show();
                return true;
            case (MotionEvent.ACTION_MOVE) :
                Toast.makeText(this.getApplicationContext(), "Move", Toast.LENGTH_SHORT).show();
                return true;
            case (MotionEvent.ACTION_UP) :
                Toast.makeText(this.getApplicationContext(), "Up", Toast.LENGTH_SHORT).show();
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                Toast.makeText(this.getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                Toast.makeText(this.getApplicationContext(), "Outside", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }
}
