package com.example.gesturelock.gesturelock;

import android.app.AlertDialog;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO: document your custom view class.
 */
public class InputView extends View implements View.OnTouchListener {

    private Paint paint = new Paint();
    private MotionEvent.PointerCoords pastLocation, curr;
    private List<Pair<Float, Float>> linePoints = new ArrayList<Pair<Float, Float>>();
    private List<Character> gesture, currGesture;
    private long startTime = 0, endTime = 0;

    public InputView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);

        gesture = new ArrayList<Character>();
        pastLocation = new MotionEvent.PointerCoords();
        curr = new MotionEvent.PointerCoords();

        // Line tracing
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);

        // Placeholder text
        paint.setTextSize(36);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    public InputView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InputView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
                currGesture = new ArrayList<Character>();
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
                            if (currGesture.size() == 0 || currGesture.get(currGesture.size() - 1) != 'R') {
                                currGesture.add('R');
                            }
                        } else {
                            // Left movement
                            if (currGesture.size() == 0 || currGesture.get(currGesture.size() - 1) != 'L') {
                                currGesture.add('L');
                            }
                        }

                    } else {

                        // Vertical movement
                        if (diffY > 0) {
                            // Down movement
                            if (currGesture.size() == 0 || currGesture.get(currGesture.size() - 1) != 'D') {
                                currGesture.add('D');
                            }
                        } else {
                            // Up movement
                            if (currGesture.size() == 0 || currGesture.get(currGesture.size() - 1) != 'U') {
                                currGesture.add('U');
                            }
                        }

                    }
                }

                return true;
            case MotionEvent.ACTION_UP:
                pastLocation = new MotionEvent.PointerCoords();
                curr = new MotionEvent.PointerCoords();

                if (currGesture.size() == 0) {
                    currGesture.add('T');
                }
                gesture.addAll(currGesture);

                if (gesture.size() > 2) {

                    if (gesture.get(gesture.size() - 2).equals('T') && gesture.get(gesture.size() - 1).equals('T')) {
                        List<Character> parsed = gesture.subList(0, gesture.size() - 2);
                        SharedPreferences prefs = getContext().getSharedPreferences("SAVED_GESTURES", getContext().MODE_PRIVATE);
                        if (prefs.contains(parsed.toString())) {

                            String function = prefs.getString(parsed.toString(), "");
                            //Toast.makeText(getContext(), function, Toast.LENGTH_SHORT).show();

                            // TODO: Switch case on function string to determine action and call as appropriate
                            switch (function.charAt(0)) {
                                case 'c':
                                    // call police
                                    break;
                                case 'l':
                                    lockPhone();
                                    break;
                                case 'o':
                                    // open phone app, use Intents
                                    break;
                            }

                        }
                    }

                }

                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    public void lockPhone() {
        DevicePolicyManager dpm = (DevicePolicyManager) getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName name = new ComponentName(getContext(), LockReceiver.class);

        if (!dpm.isAdminActive(name)) {

            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, name);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "GestureLock");
            getContext().startActivity(intent);

        }

        dpm.lockNow();
    }
}
