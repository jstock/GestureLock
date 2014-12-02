package com.example.gesturelock.gesturelock;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
* Created by Jimmy on 11/28/2014.
*/
public class GestureListActivity extends Activity {
    private final String TAG = ((Object) this).getClass().getSimpleName();

    private static final String OPTION_EDIT_ACTION = "Edit Action";
    private static final String OPTION_DELETE = "Delete";

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "++ In onCreate() ++");
        setContentView(R.layout.activity_gesture_list);

        mListView = (ListView)findViewById(R.id.list);

        // Fetch saved gesture+actions from Shared Preferences
        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.shared_pref_name), MODE_PRIVATE);

        // Test data
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.commit();

        //http://stackoverflow.com/questions/9310479/how-to-iterate-through-all-keys-of-shared-preferences
        Map<String,String> prefsMap = (Map<String,String>) prefs.getAll();

        final Resources res = getResources();
        final TypedArray actionsArray = res.obtainTypedArray(R.array.gestureActionMappings);

        // Create ArrayList of SavedGesture from SharedPreferences and gestureActionMappings array
        ArrayList<SavedGesture> savedGestures = new ArrayList<SavedGesture>();
        for(Map.Entry<String,String> entry : prefsMap.entrySet()){
            Log.i(TAG, entry.getKey() + ", "+ entry.getValue());

            // Since our SharedPreferences file only stores gesture and action key/text,
            // we look for each corresponding action value in gestureActionMappings from
            // arrays.xml (actionsArray) based on action key/text
            String gesture = entry.getKey();
            String actionKey = entry.getValue();
            String actionValue = null;
            for (int i=0; i<actionsArray.length(); i++) {
                String keyValue = actionsArray.getString(i);
                String key = keyValue.substring(0, keyValue.lastIndexOf(" "));
                if (entry.getValue().equals(key)) {
                    actionValue = keyValue.substring(keyValue.lastIndexOf(" "));
                    break;
                }
            }
            if (actionValue == null) {
                Log.e(TAG, "Could not find action key: " + actionKey + " in gestureActionMappings array!");
            }
            savedGestures.add(new SavedGesture(gesture, actionKey, actionValue));
        }

        // Load arraylist of saved gestures into list
        ListActivityAdapter adapter = new ListActivityAdapter(this, savedGestures);

        if(mListView == null){
            Log.e(TAG, "mListView is null");
        }

        mListView.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "++ In onStart() ++");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "++ In onPause() ++");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "++ In onResume() ++");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "++ In onStop() ++");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "++ In onDestroy() ++");
    }

    /**
     * Adapts gesture + action data to individual entries in list.
     * Handles sorting if any.
     */
    public class ListActivityAdapter extends ArrayAdapter<SavedGesture> {
        private LayoutInflater mInflater;
        private ArrayList<SavedGesture> mSavedGestures;

        private final Context mContext;



        public ListActivityAdapter(Context context, ArrayList<SavedGesture> savedGestures) {
            super(context, R.layout.list_view_row_layout, savedGestures);
            Log.i(TAG, "++ In ListActivityAdapter() ++");
            this.mContext = context;
            mSavedGestures = savedGestures;
            mInflater = LayoutInflater.from(context);
        }

        // Display:
            // Gesture (sequence of directions)
            // Action (name of action)
            // Image of action
        @Override
        public View getView (final int position, View convertView, ViewGroup parent) {
            Log.i(TAG, "++ In getView() ++");

            LayoutInflater mInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = mInflater.inflate(R.layout.list_view_row_layout, parent, false);
            TextView gestureTextView = (TextView) rowView.findViewById(R.id.list_row_gesture_textView);
            TextView actionTextView = (TextView) rowView.findViewById(R.id.list_row_action_textView);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

            gestureTextView.setText(mSavedGestures.get(position).getGesture());
            actionTextView.setText(mSavedGestures.get(position).getActionText());

            Log.i(TAG, "GestureTextView = "+ gestureTextView.getText().toString());
            Log.i(TAG, "ActionTextView = "+ actionTextView.getText().toString());

            // TODO: Set up a unique image for each action.
            imageView.setImageResource(R.drawable.ic_launcher);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    Log.i(TAG, "++ In rowView onClick() ++");
                    createEditDialog(position);
                }
            });
            return rowView;
        }

        private void createEditDialog(final int position) {
//            final Resources res = getResources();

            final String[] options = {
                OPTION_EDIT_ACTION,
                OPTION_DELETE
            };

            AlertDialog.Builder build = new AlertDialog.Builder(getContext());
            build.setTitle("Edit Saved Gesture");
            build.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i) {
                        case 0:
                            openEditActionDialog(position);
                            break;
                        case 1:
                            SharedPreferences prefs = getSharedPreferences(
                                    getString(R.string.shared_pref_name), MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = prefs.edit();
                            prefsEditor.remove(mSavedGestures.get(position).getGesture());
                            prefsEditor.commit();

                            // remove saved gesture from list
                            SavedGesture savedGesture = getItem(position);
                            Toast.makeText(getContext(), "Deleted gesture "+
                                    savedGesture.getGesture() + " mapped to action "+
                                    savedGesture.getActionText(), Toast.LENGTH_LONG).show();
                            remove(savedGesture);
                            break;
                    }
                }
            });
            build.show();
        }

        private void openEditActionDialog(final int position) {
            final Resources res = getResources();
            final TypedArray actionsArray = res.obtainTypedArray(R.array.gestureActionMappings);

            CharSequence[] keys = new CharSequence[actionsArray.length()];
            for (int i = 0; i < actionsArray.length(); i++) {
                String line = actionsArray.getString(i);
                keys[i] = line.substring(0, line.lastIndexOf(" "));
            }

            AlertDialog.Builder build = new AlertDialog.Builder(getContext());
            build.setTitle("select an action");
            build.setItems(keys, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String keyValue = actionsArray.getString(i);
                    String key = keyValue.substring(0, keyValue.lastIndexOf(" "));
                    String value = keyValue.substring(keyValue.lastIndexOf(" "));

                    Toast.makeText(getContext(), value, Toast.LENGTH_SHORT).show();

                    // Change action of saved gesture in SharedPreferences
                    SharedPreferences prefs = getSharedPreferences(
                            getString(R.string.shared_pref_name), MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = prefs.edit();
                    prefsEditor.putString(mSavedGestures.get(position).getGesture(),
                            actionsArray.getString(i));
                    prefsEditor.commit();

                    // Change action of saved gesture in gesture list
                    SavedGesture savedGesture =  getItem(position);
                    String oldActionKey = savedGesture.getActionText();
                    remove(getItem(position));
                    savedGesture.setActionText(key);
                    savedGesture.setActionValue(value);
                    insert(savedGesture, position);
                    notifyDataSetChanged();

                    Toast.makeText(getContext(), "Gesture "+ key +
                            " successfully remapped from action "+ oldActionKey +
                            " to action "+ value, Toast.LENGTH_LONG).show();
                }
            });
            build.show();
        }

    }
}
