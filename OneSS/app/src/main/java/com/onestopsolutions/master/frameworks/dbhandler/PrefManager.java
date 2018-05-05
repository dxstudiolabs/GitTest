package com.onestopsolutions.master.frameworks.dbhandler;

import android.content.Context;
import android.content.SharedPreferences;

import com.onestopsolutions.master.R;

/**
 * Created by Darpan on 17-Feb-18.
 */

public class PrefManager {

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "OneSS";

    // All Shared Preferences Keys
    public static String PREF_KEY_DURATION_START_DATE = "pref_key_duration_start_date";
    public static String PREF_KEY_DURATION_END_DATE = "pref_key_duration_end_date";
    public static String PREF_KEY_GRAPH_VIEW = "pref_key_graph_view";
    public static String PREF_KEY_SUBJECT_NAME = "pref_key_subject_name";
    public static String PREF_KEY_BOOK_TYPE = "pref_key_book_type";
    public static String PREF_KEY_BOOK_NAME = "pref_key_book_name";
    protected static PrefManager mManager;

    private PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static PrefManager getInstance(Context context) {
        if (mManager == null) {
            mManager = new PrefManager(context);
        }
        return mManager;
    }

    public void putString(String id, String value) {
        editor.putString(id, value);
        editor.commit();
    }


    public String getString(String id, String defaultVal) {
        return pref.getString(id, defaultVal);
    }


    public String getStartDate() {
        return getString(PREF_KEY_DURATION_START_DATE, "");
    }

    public String getEndDate() {
        return getString(PREF_KEY_DURATION_END_DATE, "");
    }

    public String getDuration() {
        String[] durations = _context.getResources().getStringArray(R.array.graph_view_options);
        int index = Integer.parseInt(getString(PREF_KEY_GRAPH_VIEW, "0"));
        return durations[index].toLowerCase();
    }

    public String getSubjectName() {
        return getString(PREF_KEY_SUBJECT_NAME, "English");
    }

    public String getBookName() {
        return getString(PREF_KEY_BOOK_NAME, "");
    }

    public String getBookType() {
        return getString(PREF_KEY_BOOK_TYPE, "");
    }
}
