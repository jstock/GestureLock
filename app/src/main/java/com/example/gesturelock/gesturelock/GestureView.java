package com.example.gesturelock.gesturelock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

/**
 * The view that allows users to save Gestures for enhanced phone control
 */
public class GestureView extends View implements OnTouchListener {

    private Paint paint = new Paint();
    private MotionEvent.PointerCoords pastLocation, curr;
    private List<Pair<Float, Float>> linePoints = new ArrayList<Pair<Float, Float>>();

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
                event.getPointerCoords(0, pastLocation);
                linePoints.add(new Pair<Float, Float>(pastLocation.x, pastLocation.y));
                return true;
            case MotionEvent.ACTION_MOVE:

                curr = new MotionEvent.PointerCoords();
                event.getPointerCoords(0, curr);

                if (Math.abs(curr.x - pastLocation.x) > 2 || Math.abs(curr.y - pastLocation.y) > 2) {

                    // Finger has moved more than 3 pixels in a direction, draw line and update
                    linePoints.add(new Pair<Float, Float>(curr.x, curr.y));
                    invalidate();
                    pastLocation.x = curr.x;
                    pastLocation.y = curr.y;
                }

                return true;
            case MotionEvent.ACTION_UP:
                pastLocation = new MotionEvent.PointerCoords();
                curr = new MotionEvent.PointerCoords();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

}
