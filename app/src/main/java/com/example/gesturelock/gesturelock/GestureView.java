package com.example.gesturelock.gesturelock;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * The view that allows users to save Gestures for enhanced phone control
 */
public class GestureView extends View implements OnTouchListener {

    private Paint paint = new Paint();
    private MotionEvent.PointerCoords pastLocation, curr;
    private List<Pair<Float, Float>> linePoints = new ArrayList<Pair<Float, Float>>();
    private List<Character> gesture;

    public GestureView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);

        pastLocation = new MotionEvent.PointerCoords();
        curr = new MotionEvent.PointerCoords();

        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < linePoints.size() - 1; i++) {
            canvas.drawLine(linePoints.get(i).first, linePoints.get(i).second, linePoints.get(i + 1).first, linePoints.get(i + 1).second, paint);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                gesture = new ArrayList<Character>();
                linePoints = new ArrayList<Pair<Float, Float>>();
                event.getPointerCoords(0, pastLocation);
                linePoints.add(new Pair<Float, Float>(pastLocation.x, pastLocation.y));
                return true;
            case MotionEvent.ACTION_MOVE:

                curr = new MotionEvent.PointerCoords();
                event.getPointerCoords(0, curr);

                float diffX, diffY;
                diffX = curr.x - pastLocation.x;
                diffY = curr.y - pastLocation.y;

                if (Math.abs(diffX) > 2 || Math.abs(diffY) > 2) {

                    // Finger has moved more than 3 pixels in a direction, draw line and update
                    linePoints.add(new Pair<Float, Float>(curr.x, curr.y));
                    invalidate();
                    pastLocation.x = curr.x;
                    pastLocation.y = curr.y;

                }

                if ((Math.abs(diffX) > 5 || Math.abs(diffY) > 5)) {
                    if (Math.abs(diffX) >= Math.abs(diffY)) {

                        // Horizontal movement
                        if (diffX > 0) {
                            // Right movement
                            if (gesture.size() == 0 || gesture.get(gesture.size() - 1) != 'R') {
                                gesture.add('R');
                            }
                        } else {
                            // Left movement
                            if (gesture.size() == 0 || gesture.get(gesture.size() - 1) != 'L') {
                                gesture.add('L');
                            }
                        }

                    } else {

                        // Vertical movement
                        if (diffY > 0) {
                            // Down movement
                            if (gesture.size() == 0 || gesture.get(gesture.size() - 1) != 'D') {
                                gesture.add('D');
                            }
                        } else {
                            // Up movement
                            if (gesture.size() == 0 || gesture.get(gesture.size() - 1) != 'U') {
                                gesture.add('U');
                            }
                        }

                    }
                }

                return true;
            case MotionEvent.ACTION_UP:
                pastLocation = new MotionEvent.PointerCoords();
                curr = new MotionEvent.PointerCoords();

                // Print out the gesture movements to the screen
                String result = "";
                for (Character c : gesture) {result += c.toString();}
                if (result.equals("RDLU")) {
                    // Open up image viewing applications via Intent on RDLU gesture
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_VIEW);
                    getContext().startActivity(Intent.createChooser(intent, "Quick Photo Gallery"));
                } else {
                    Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                }

                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

}
