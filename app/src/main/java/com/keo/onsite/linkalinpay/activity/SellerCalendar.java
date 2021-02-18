package com.keo.onsite.linkalinpay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.JsonObject;
import com.keo.onsite.linkalinpay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class SellerCalendar extends AppCompatActivity
//        implements OnChartGestureListener , OnChartValueSelectedListener {
{
    LineChart linechart;
    JSONObject data;
    TextView total;
    TextView yeartxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_calendar);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.sellercalendar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setTitle("Donation List");
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Intent g = getIntent();
        total = findViewById(R.id.total);

        total.setText(g.getStringExtra("total"));
        yeartxt = findViewById(R.id.years);


        yeartxt.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

        try {
            data = new JSONObject(g.getStringExtra("sellercal"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        linechart = findViewById(R.id.linechart);

//        linechart.setOnChartGestureListener(this);
//        linechart.setOnChartValueSelectedListener(this);

        linechart.setDragEnabled(true);
        linechart.setScaleEnabled(true);

        ArrayList<Entry> yValues = new ArrayList<>();


        for(int i = 1; i<data.length(); i++ )
        {
//            try {
//                Log.d("data>>",data.getString(""+i));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            try {
                yValues.add(new Entry(i-1,Float.parseFloat(data.getString(""+i))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        yValues.add(new Entry(0,1000));
//        yValues.add(new Entry(1,2000));
//        yValues.add(new Entry(2,3000));
//        yValues.add(new Entry(3,1000));
//        yValues.add(new Entry(4,2000));
//        yValues.add(new Entry(5,3000));
//        yValues.add(new Entry(6,3000));
//        yValues.add(new Entry(7,3000));
//        yValues.add(new Entry(8,0));
//        yValues.add(new Entry(9,0));
//        yValues.add(new Entry(10,0));
//        yValues.add(new Entry(11,0));

        LineDataSet set1 = new LineDataSet(yValues,"Revenue");
        set1.setFillAlpha(110);

        set1.setColor(R.color.color_red);
        set1.setLineWidth(2);
        set1.setValueTextSize(9f);
        set1.setCircleRadius(5f);
        ArrayList<ILineDataSet> datasets =  new ArrayList<>();
        datasets.add(set1);

        LineData data = new LineData(datasets);


        linechart.setData(data);

        String[] values = new String[] {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"};

        XAxis xAxis = linechart.getXAxis();
        xAxis.setValueFormatter(new MyXaxis(values));
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(12, true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis leftAxis = linechart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

    }


    private class MyXaxis extends ValueFormatter {
        private String[] mValue;
        public MyXaxis(String[] values) {
            this.mValue = values;
        }

        @Override
        public String getFormattedValue(float value) {
            return mValue[(int)value];
        }
    }
}