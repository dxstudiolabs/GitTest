package com.onestopsolutions.master;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.onestopsolutions.master.bean.Book;
import com.onestopsolutions.master.bean.BookType;
import com.onestopsolutions.master.bean.Subject;
import com.onestopsolutions.master.frameworks.appsession.AppBaseApplication;
import com.onestopsolutions.master.frameworks.dbhandler.PrefManager;
import com.onestopsolutions.master.frameworks.retrofit.ResponseResolver;
import com.onestopsolutions.master.frameworks.retrofit.RestError;
import com.onestopsolutions.master.frameworks.retrofit.WebServicesWrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Response;

public class InputDataActivity extends AppCompatActivity implements View.OnClickListener {
    TextView mStartDateView, mEndDateView, mGraphView, mSaveButton, mCancelButton, mSubjectView, mBookView, mBookType;
    String[] mGraphViewAvailableItems;
    Context mContext;
    int mGraphViewSelectedItem;
    int mSubjectListSelectedItem;
    PrefManager mPrefManager;
    Date mStartDate, mEndDate;
    ArrayList<Subject> mSubjectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);
        mContext = this;
        initInputData();
        loadSubjectData(savedInstanceState);
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
        mSubjectView = findViewById(R.id.subject_summary);
        mBookView = findViewById(R.id.bookName_summary);
        mBookType = findViewById(R.id.bookType_summary);
        mBookView.setOnClickListener(this);
        mSubjectView.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        mGraphView.setOnClickListener(this);
        mStartDateView.setOnClickListener(this);
        mEndDateView.setOnClickListener(this);
        mBookType.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mStartDateView || view == mEndDateView) {
            createCalenderDialog(view);
        } else if (view == mGraphView) {
            createGraphViewDialog(view);
        } else if (view == mSaveButton || view == mCancelButton) {
            finishActivity(view == mSaveButton);
        } else if (view == mSubjectView) {
            createSubjectListDialog(view);
        } else if (view == mBookView) {
            createBookListDialog(view);
        } else if (view == mBookType) {
            createBookTypeDialog(view);
        }
    }


    private void finishActivity(boolean saveData) {
        if (saveData) {
            mPrefManager.putString(PrefManager.PREF_KEY_DURATION_START_DATE, mStartDateView.getText().toString());
            mPrefManager.putString(PrefManager.PREF_KEY_DURATION_END_DATE, mEndDateView.getText().toString());
            mPrefManager.putString(PrefManager.PREF_KEY_GRAPH_VIEW, String.valueOf(mGraphViewSelectedItem));
            mPrefManager.putString(PrefManager.PREF_KEY_SUBJECT_NAME, mSubjectView.getText().toString());
            String book = mBookView.getText().toString();
            mPrefManager.putString(PrefManager.PREF_KEY_BOOK_NAME, "Select Book".equals(book) ? "" : book);
            String bookType = mBookType.getText().toString();
            mPrefManager.putString(PrefManager.PREF_KEY_BOOK_TYPE, "Select Book Type".equals(bookType) ? "" : bookType);
        }
        finish();
    }

    private void loadSavedData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mStartDateView.setText(savedInstanceState.getString(PrefManager.PREF_KEY_DURATION_START_DATE, "Select Date"));
            mEndDateView.setText(savedInstanceState.getString(PrefManager.PREF_KEY_DURATION_END_DATE, "Select Date"));
            mGraphViewSelectedItem = savedInstanceState.getInt(PrefManager.PREF_KEY_GRAPH_VIEW, 0);
            mGraphView.setText(mGraphViewAvailableItems[mGraphViewSelectedItem]);
            mSubjectView.setText(savedInstanceState.getString(PrefManager.PREF_KEY_SUBJECT_NAME, "Select Subject"));
            mBookView.setText(savedInstanceState.getString(PrefManager.PREF_KEY_BOOK_NAME, "Select Book"));
            mBookType.setText(savedInstanceState.getString(PrefManager.PREF_KEY_BOOK_TYPE, "Select Book Type"));
        } else {
            mStartDateView.setText(mPrefManager.getString(PrefManager.PREF_KEY_DURATION_START_DATE, "Select Date"));
            mEndDateView.setText(mPrefManager.getString(PrefManager.PREF_KEY_DURATION_END_DATE, "Select Date"));
            mGraphViewSelectedItem = Integer.parseInt(mPrefManager.getString(PrefManager.PREF_KEY_GRAPH_VIEW, "0"));
            mGraphView.setText(mGraphViewAvailableItems[mGraphViewSelectedItem]);
            mSubjectView.setText(mPrefManager.getString(PrefManager.PREF_KEY_SUBJECT_NAME, "Select Subject"));
            mBookView.setText(mPrefManager.getString(PrefManager.PREF_KEY_BOOK_NAME, "Select Book"));
            mBookType.setText(mPrefManager.getString(PrefManager.PREF_KEY_BOOK_TYPE, "Select Book Type"));
        }
        mStartDate = getDate(mStartDateView.getText().toString());
        mEndDate = getDate(mEndDateView.getText().toString());
    }

    private void loadSubjectData(Bundle savedInstanceState) {
        WebServicesWrapper.getInstance().getSubjectList(new ResponseResolver<List<Subject>>() {
            @Override
            public void onSuccess(List<Subject> subjects, Response response) {
                mSubjectList = new ArrayList<>(subjects);

            }

            @Override
            public void onFailure(RestError error, String msg) {

            }
        });
    }

    private void createGraphViewDialog(final View graphView) {
        final int[] selectedItem = {getCheckedItemForGraphView()};
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

    private void createSubjectListDialog(View view) {
        CharSequence[] subjectList = new CharSequence[mSubjectList.size()];
        for (int i = 0; i < mSubjectList.size(); i++) {
            subjectList[i] = mSubjectList.get(i).getSubjectName();
        }

        final int[] selectedItem = {mSubjectListSelectedItem};
        //  LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        //View titleBar = inflater.inflate(R.layout.dialog_title_view, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Subject");
        //   builder.setCustomTitle(titleBar);
        builder.setSingleChoiceItems(subjectList, selectedItem[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                selectedItem[0] = index;
            }
        });

        DialogInterface.OnClickListener listner = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == DialogInterface.BUTTON_POSITIVE) {
                    mSubjectListSelectedItem = selectedItem[0];
                    updateSubjectView();
                    setBookName("Select Book");
                }
                dialogInterface.cancel();
            }
        };
        AlertDialog dialog = builder.setPositiveButton(R.string.text_ok, listner).setNegativeButton(R.string.btn_cancel_text, listner).create();
        dialog.show();
    }

    ArrayList<Book> mBookList;

    private void createBookListDialog(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final int[] selectedItem = {0};

        //  LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        //View titleBar = inflater.inflate(R.layout.dialog_title_view, null);
        builder.setTitle("Books");
        //   builder.setCustomTitle(titleBar);
        final DialogInterface.OnClickListener listner = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == DialogInterface.BUTTON_POSITIVE) {
                    if (mBookList.size() > 0)
                        setBookName(mBookList.get(selectedItem[0]).getBookName());
                }
                dialogInterface.cancel();
            }
        };
        WebServicesWrapper.getInstance().getBooksForSubject(new ResponseResolver<List<Book>>() {
            @Override
            public void onSuccess(List<Book> books, Response response) {
                mBookList = new ArrayList<>(books);
                CharSequence[] bookList = new CharSequence[mBookList.size()];
                for (int i = 0; i < mBookList.size(); i++) {
                    bookList[i] = mBookList.get(i).getBookName();
                }

                builder.setSingleChoiceItems(bookList, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int index) {
                        selectedItem[0] = index;
                    }
                });
                AlertDialog dialog = builder.setPositiveButton(R.string.text_ok, listner).setNegativeButton(R.string.btn_cancel_text, listner).create();
                dialog.show();
            }

            @Override
            public void onFailure(RestError error, String msg) {
            }
        }, mSubjectView.getText().toString());

    }

    ArrayList<BookType> mBookTypes;

    private void createBookTypeDialog(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final int[] selectedItem = {-1};

        //  LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        //View titleBar = inflater.inflate(R.layout.dialog_title_view, null);
        builder.setTitle("Book Type");
        //   builder.setCustomTitle(titleBar);
        final DialogInterface.OnClickListener listner = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == DialogInterface.BUTTON_POSITIVE) {
                    if (selectedItem[0] > 0)
                        setBookType(mBookTypes.get(selectedItem[0]).getBookType());
                }
                dialogInterface.cancel();
            }
        };
        WebServicesWrapper.getInstance().getBookTypes(new ResponseResolver<List<BookType>>() {
            @Override
            public void onSuccess(List<BookType> types, Response response) {
                mBookTypes = new ArrayList<>(types);
                CharSequence[] bookTypes = new CharSequence[mBookTypes.size()];
                for (int i = 0; i < mBookTypes.size(); i++) {
                    bookTypes[i] = mBookTypes.get(i).getBookType();
                }

                builder.setSingleChoiceItems(bookTypes, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int index) {
                        selectedItem[0] = index;
                    }
                });
                AlertDialog dialog = builder.setPositiveButton(R.string.text_ok, listner).setNegativeButton(R.string.btn_cancel_text, listner).create();
                dialog.show();
            }

            @Override
            public void onFailure(RestError error, String msg) {
                Log.d("darpan", "failed ");
            }
        });

    }

    private void setBookName(String name) {
        mBookView.setText(name);
        setBookType("Select Book Type");
    }

    private void updateGraphView() {
        mGraphView.setText(mGraphViewAvailableItems[mGraphViewSelectedItem]);
    }

    private void updateSubjectView() {
        mSubjectView.setText(mSubjectList.get(mSubjectListSelectedItem).getSubjectName());
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
        outState.putString(PrefManager.PREF_KEY_SUBJECT_NAME, mSubjectList.get(mSubjectListSelectedItem).getSubjectName());
        outState.putString(PrefManager.PREF_KEY_BOOK_NAME, mBookView.getText().toString());
        outState.putString(PrefManager.PREF_KEY_BOOK_TYPE, mBookType.getText().toString());
        super.onSaveInstanceState(outState);
    }

    public int getCheckedItemForGraphView() {
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

    public void setBookType(String bookType) {
        mBookType.setText(bookType);
    }
}
