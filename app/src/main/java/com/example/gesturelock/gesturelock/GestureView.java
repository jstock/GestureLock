package com.example.gesturelock.gesturelock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
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
    private List<Character> gesture, currGesture;

    private boolean showPlaceholder = true;

    public GestureView(Context context) {
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

    @Override
    protected void onDraw(Canvas canvas) {
        if (showPlaceholder) {
            int centerHeight = (int) ((canvas.getHeight() / 2) - (paint.descent() + paint.ascent()));
            canvas.drawText("double tap to save", canvas.getWidth() / 2, centerHeight, paint);
        }
        for (int i = 0; i < linePoints.size() - 1; i++) {
            canvas.drawLine(linePoints.get(i).first, linePoints.get(i).second, linePoints.get(i + 1).first, linePoints.get(i + 1).second, paint);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showPlaceholder = false;
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

                if (currGesture.size() == 0) { currGesture.add('T'); }
                gesture.addAll(currGesture);

                if (gesture.size() > 2) {

                    if (gesture.get(gesture.size() - 2).equals('T') && gesture.get(gesture.size() - 1).equals('T')) {

                        String result = "";
                        for (int i = 0; i < gesture.size() - 2; i++)
                            result += gesture.get(i).toString();

                        // Ended with double tap, alert user for save confirm
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setMessage("Would you like to save gesture " + result + "?");
                        alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                showGestureMappingDialog();
                            }
                        });
                        alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alert.show();



                    }

                }

                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    private void showGestureMappingDialog() {

        final Resources res = getResources();
        final TypedArray items = res.obtainTypedArray(R.array.gestureActionMappings);

        CharSequence[] keys = new CharSequence[items.length()];
        for (int i = 0; i < items.length(); i++) {
            String line = items.getString(i);

            keys[i] = line.substring(0, line.lastIndexOf(" "));
        }

        AlertDialog.Builder build = new AlertDialog.Builder(getContext());
        build.setTitle("select an action");
        build.setItems(keys, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String keyValue = items.getString(i);
                String key = keyValue.substring(0, keyValue.lastIndexOf(" "));
                String value = keyValue.substring(keyValue.lastIndexOf(" "));

                // Store newly created gesture and action mapping
                SharedPreferences prefs = getContext().getSharedPreferences(
                        getContext().getString(R.string.shared_pref_name), getContext().MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putString(gesture.toString(), key);
                prefsEditor.commit();
                Toast.makeText(getContext(), "Gesture "+ gesture.toString() +"" +
                        " successfully mapped to action "+ key, Toast.LENGTH_LONG).show();
                // Reset gesture
                gesture.clear();
            }
        });
        build.show();

    }

}
