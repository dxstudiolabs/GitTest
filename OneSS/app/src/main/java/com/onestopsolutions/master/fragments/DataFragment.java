package com.onestopsolutions.master.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.onestopsolutions.master.R;
import com.onestopsolutions.master.bean.GrossIncome;
import com.onestopsolutions.master.bean.NetIncome;
import com.onestopsolutions.master.bean.Tax;
import com.onestopsolutions.master.frameworks.retrofit.ResponseResolver;
import com.onestopsolutions.master.frameworks.retrofit.RestError;
import com.onestopsolutions.master.frameworks.retrofit.WebServicesWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;


public class DataFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private FrameLayout mGraphLayout;
    private BarChart mChart;
    private Context mContext;
    private String[] mXaxisLables;

    public DataFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mParam1 == "1") {
            loadEntriesForGrossIncome();
        } else if (mParam1 == "2") {
            loadEntriesForTax();
        } else if (mParam1 == "3") {
            loadEntriesNetIncome();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    public static DataFragment newInstance(String param1, String param2) {
        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mContext = getActivity();
    }

    private void loadEntriesForGrossIncome() {
        WebServicesWrapper.getInstance().getGrossIncome(new ResponseResolver<List<GrossIncome>>() {
            @Override
            public void onSuccess(List<GrossIncome> grossIncomes, Response response) {
                float x = 0;
                List<BarEntry> entries = new ArrayList<>();
                mXaxisLables = new String[grossIncomes.size()];
                for (GrossIncome income : grossIncomes) {
                    entries.add(new BarEntry(x, income.getGrossIncome()));
                    mXaxisLables[(int) x] = income.getDate();
                    x++;
                }
                BarDataSet barDataSet = new BarDataSet(entries, "Gross Income");
                barDataSet.setColor(Color.GREEN);
                showGraph(barDataSet);
            }

            @Override
            public void onFailure(RestError error, String msg) {

            }
        });
    }

    private void loadEntriesForTax() {
        WebServicesWrapper.getInstance().getTax(new ResponseResolver<List<Tax>>() {
            @Override
            public void onSuccess(List<Tax> taxes, Response response) {
                float x = 0;
                List<BarEntry> entries = new ArrayList<>();
                mXaxisLables = new String[taxes.size()];
                for (Tax tax : taxes) {
                    entries.add(new BarEntry(x, tax.getTax()));
                    mXaxisLables[(int) x] = tax.getDate();
                    x++;
                }
                BarDataSet barDataSet = new BarDataSet(entries, "Tax");
                barDataSet.setColor(Color.RED);
                showGraph(barDataSet);
            }

            @Override
            public void onFailure(RestError error, String msg) {

            }
        });
    }

    private void loadEntriesNetIncome() {
        WebServicesWrapper.getInstance().getNetIncome(new ResponseResolver<List<NetIncome>>() {
            @Override
            public void onSuccess(List<NetIncome> taxes, Response response) {
                float x = 0;
                List<BarEntry> entries = new ArrayList<>();
                mXaxisLables = new String[taxes.size()];
                for (NetIncome netIncome : taxes) {
                    entries.add(new BarEntry(x, netIncome.getNetIncome()));
                    mXaxisLables[(int) x] = netIncome.getDate();
                    x++;
                }
                BarDataSet barDataSet = new BarDataSet(entries, "Net Income");
                barDataSet.setColor(Color.BLUE);
                showGraph(barDataSet);
            }

            @Override
            public void onFailure(RestError error, String msg) {

            }
        });
    }

    @SuppressLint("NewApi")
    private void showGraph(BarDataSet barDataSet) {
        mChart = new BarChart(mContext);
        mGraphLayout.removeAllViews();
        mGraphLayout.addView(mChart);
        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.9f);
        mChart.setData(data);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.getXAxis().setAvoidFirstLastClipping(true);
        mChart.getXAxis().setGranularity(1.0f);
        mChart.getXAxis().setLabelCount(barDataSet.getEntryCount());
        mChart.getXAxis().setValueFormatter(valueFormatter);
        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisLeft().setAxisMinimum(0);
        mChart.setFitBars(true);
        mChart.setDescription(null);
        mChart.animateX(500, Easing.EasingOption.Linear);
        mChart.setContextClickable(false);
        mChart.invalidate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_data, container, false);
        mGraphLayout = v.findViewById(R.id.graph);
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    IAxisValueFormatter valueFormatter = new IAxisValueFormatter() {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            if (value < mXaxisLables.length && value >= 0) {
                return mXaxisLables[((int) value)];
            }
            return "";
        }
    };
}
