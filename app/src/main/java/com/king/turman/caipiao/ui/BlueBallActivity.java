package com.king.turman.caipiao.ui;

import android.graphics.Typeface;
import android.os.Bundle;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.king.turman.caipiao.R;
import com.king.turman.caipiao.net.NetRequest;
import com.king.turman.caipiao.net.bean.LotteryBean;
import com.king.turman.caipiao.net.utils.HtmlUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlueBallActivity extends BaseActivity {

    protected Typeface mTfRegular;
    protected Typeface mTfLight;

    protected HorizontalBarChart mChart;

    private List<LotteryBean> datas;

    private int currentShowCount = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        mChart = (HorizontalBarChart) findViewById(R.id.chart);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(1);
        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);
        mChart.setDrawGridBackground(false);

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(mTfLight);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(1f);

        YAxis yl = mChart.getAxisLeft();
        yl.setTypeface(mTfLight);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = mChart.getAxisRight();
        yr.setTypeface(mTfLight);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setStartAtZero(true); // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        getData(currentShowCount);
        mChart.setFitBars(true);
        mChart.animateY(2500);
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(1f);

        datas = new ArrayList<>();
    }

    private void getData(int currentShowCount) {
        NetRequest.getLottery("list_1.html")
                .compose(this.bindToLifecycle())
                .subscribe(s -> {
                    datas.addAll(HtmlUtil.getLotteryListFromString(s));
                    setData();
                },throwable -> {
                    throwable.printStackTrace();
                });
    }

    private void setData() {
        float barWidth = 0.5f;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        Map<String,Integer> numsMap = new HashMap<>();
        for (int i = 0; i < datas.size(); i++) {
            int blueBallIndex = datas.get(i).getNumbers().size()-1;
            String numKey = datas.get(i).getNumbers().get(blueBallIndex);
            if (numsMap.containsKey(numKey)) {
                numsMap.put(numKey, numsMap.get(numKey) + 1);
            } else {
                numsMap.put(datas.get(i).getNumbers().get(blueBallIndex),1);
            }
        }

        for (int i=0;i<16;i++) {
            int count = 0;
            for (String s : numsMap.keySet()) {
                if ((i == Integer.parseInt(s))) {
                    count = numsMap.get(s);
                    break;
                }
            }
            yVals1.add(new BarEntry(i+1, count,
                    getResources().getDrawable(R.drawable.star)));
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Blue Ball Count For "+currentShowCount);

            set1.setDrawIcons(false);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTfLight);
            data.setBarWidth(barWidth);
            mChart.setData(data);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_blue_ball;
    }
}
