package com.example.gesturelock.gesturelock;

import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.List;

/**
 * Created by Jimmy on 11/29/2014.
 */
public class SavedGesture {

    // TODO: Figure out best way to display Gesture to user
    private String mGesture;
    private String mActionText;
    private String mActionValue;

    /**
     *
     * @param gesture       sequence of directions each represented as a character (e.g. down = d)
     * @param actionText    key in gestureActionMappings array (arrays.xml)
     * @param actionValue   value in gestureActionMappings array (arrays.xml)
     */
    public SavedGesture(String gesture, String actionText, String actionValue) {
        mGesture = gesture;
        mActionText = actionText;
        mActionValue = actionValue;
    }

    public String toString() {
        return "Gesture: "+ mGesture +", Action: "+ mActionText;
    }

    public String getActionText() {
        return mActionText;
    }

    public void setActionText(String actionText) {
        this.mActionText = actionText;
    }

    public String getActionValue() {
        return mActionValue;
    }

    public void setActionValue(String actionValue) {
        this.mActionValue = actionValue;
    }

    public String getGesture() {
        return mGesture;
    }

    public void setGesture(String mGesture) {
        this.mGesture = mGesture;
    }


}
