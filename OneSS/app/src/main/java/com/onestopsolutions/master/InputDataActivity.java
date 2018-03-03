package com.onestopsolutions.master;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.onestopsolutions.master.frameworks.appsession.AppBaseApplication;
import com.onestopsolutions.master.frameworks.dbhandler.PrefManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InputDataActivity extends AppCompatActivity implements View.OnClickListener {
    TextView mStartDateView, mEndDateView, mGraphView, mSaveButton, mCancelButton;
    String[] mGraphViewAvailableItems;
    Context mContext;
    int mGraphViewSelectedItem;
    PrefManager mPrefManager;
    Date mStartDate, mEndDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);
        mContext = this;
        initInputData();
        loadSavedData(savedInstanceState);
    }

    private void initInputData() {
        mPrefManager = PrefManager.getInstance(AppBaseApplication.getApplication());
        mStartDateView = findViewById(R.id.duration_start_date);
        mEndDateView = findViewById(R.id.duration_end_date);
        mGraphView = findViewById(R.id.graph_summary);
        mGraphViewAvailableItems = mContext.getResources().getStringArray(R.array.graph_view_options);
        mCancelButton = findViewById(R.id.btn_cancel);
        mSaveButton = findViewById(R.id.btn_save);
        mSaveButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        mGraphView.setOnClickListener(this);
        mStartDateView.setOnClickListener(this);
        mEndDateView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mStartDateView || view == mEndDateView) {
            createCalenderDialog(view);
        } else if (view == mGraphView) {
            createGraphViewDialog(view);
        } else if (view == mSaveButton || view == mCancelButton) {
            finishActivity(view == mSaveButton);
        }
    }

    private void finishActivity(boolean saveData) {
        if (saveData) {
            mPrefManager.putString(PrefManager.PREF_KEY_DURATION_START_DATE, mStartDateView.getText().toString());
            mPrefManager.putString(PrefManager.PREF_KEY_DURATION_END_DATE, mEndDateView.getText().toString());
            mPrefManager.putString(PrefManager.PREF_KEY_GRAPH_VIEW, String.valueOf(mGraphViewSelectedItem));
        }
        finish();
    }

    private void loadSavedData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mStartDateView.setText(savedInstanceState.getString(PrefManager.PREF_KEY_DURATION_START_DATE, "Select Date"));
            mEndDateView.setText(savedInstanceState.getString(PrefManager.PREF_KEY_DURATION_END_DATE, "Select Date"));
            mGraphViewSelectedItem = savedInstanceState.getInt(PrefManager.PREF_KEY_GRAPH_VIEW, 0);
            mGraphView.setText(mGraphViewAvailableItems[mGraphViewSelectedItem]);
        } else {
            mStartDateView.setText(mPrefManager.getString(PrefManager.PREF_KEY_DURATION_START_DATE, "Select Date"));
            mEndDateView.setText(mPrefManager.getString(PrefManager.PREF_KEY_DURATION_END_DATE, "Select Date"));
            mGraphViewSelectedItem = Integer.parseInt(mPrefManager.getString(PrefManager.PREF_KEY_GRAPH_VIEW, "0"));
            mGraphView.setText(mGraphViewAvailableItems[mGraphViewSelectedItem]);
        }
        mStartDate = getDate(mStartDateView.getText().toString());
        mEndDate = getDate(mEndDateView.getText().toString());
    }

    private void createGraphViewDialog(final View graphView) {
        final int[] selectedItem = {getCheckedItem()};
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View titleBar = inflater.inflate(R.layout.dialog_title_view, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // builder.setTitle(R.string.text_graph_view);
        builder.setCustomTitle(titleBar);
        builder.setSingleChoiceItems(R.array.graph_view_options, selectedItem[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                selectedItem[0] = index;
            }
        });

        DialogInterface.OnClickListener listner = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == DialogInterface.BUTTON_POSITIVE) {
                    mGraphViewSelectedItem = selectedItem[0];
                }
                updateGraphView();
                dialogInterface.cancel();
            }
        };
        AlertDialog dialog = builder.setPositiveButton(R.string.text_ok, listner).setNegativeButton(R.string.btn_cancel_text, listner).create();
        dialog.show();

    }

    private void updateGraphView() {
        mGraphView.setText(mGraphViewAvailableItems[mGraphViewSelectedItem]);
    }

    private void createCalenderDialog(final View txtView) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Date date = getDate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        if (txtView == mStartDateView) {
                            mStartDate = date;
                        } else {
                            mEndDate = date;
                        }
                        ((TextView) txtView).setText(getTextFromDate(date));
                    }
                }, year, month, day);
        if (txtView == mEndDateView) {
            datePickerDialog.getDatePicker().setMinDate(mStartDate.getTime());
        }
        datePickerDialog.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(PrefManager.PREF_KEY_DURATION_START_DATE, mStartDateView.getText().toString());
        outState.putString(PrefManager.PREF_KEY_DURATION_END_DATE, mEndDateView.getText().toString());
        outState.putInt(PrefManager.PREF_KEY_GRAPH_VIEW, mGraphViewSelectedItem);
        super.onSaveInstanceState(outState);
    }

    public int getCheckedItem() {
        return mGraphViewSelectedItem;
    }

    private Date getDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTextFromDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
