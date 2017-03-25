package com.example.victor.myreminder.Chart;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.victor.myreminder.Database.Dbhelper;
import com.example.victor.myreminder.R;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class BarChart extends AppCompatActivity {

    private Dbhelper dbase;
    private Cursor cursor;
    private com.github.mikephil.charting.charts.BarChart barChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_line);
        barChart = (com.github.mikephil.charting.charts.BarChart) findViewById(R.id.linechart);
        dbase = Dbhelper.getreminderDatabase(this);
        cursor = dbase.getAllItems();
        float[] priority = new float[cursor.getCount()];
        final String[] tiles = new String[cursor.getCount()];
        cursor.moveToFirst();
        int i = 0;
        while (cursor.moveToNext()) {
            tiles[i] = new String();
            priority[i] = (float) cursor.getInt(cursor.getColumnIndex(Dbhelper.DB_PRIORITY));
            tiles[i] = cursor.getString(cursor.getColumnIndex(Dbhelper.DB_COLUMN_TITLE));
            i++;
        }

        List<BarEntry> entries = new ArrayList<>();
        for (i = 0; i <tiles.length-1 ; i++) {
            entries.add(new BarEntry((float) i, priority[i]));
        }
        BarDataSet set = new BarDataSet(entries, "重要程度");
        set.setValueTextSize(20);
        set.setValueTextColor(Color.BLACK);
        set.setColors(Color.GREEN, Color.BLUE, Color.GRAY, Color.YELLOW, Color.RED, Color.BLACK, Color.LTGRAY, Color.CYAN);
        BarData data = new BarData(set);
        data.setBarWidth(0.9f);
        barChart.setData(data);
        IAxisValueFormatter formmater = new TitleFormatter(barChart, tiles);
        XAxis xaxis = barChart.getXAxis();
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xaxis.setValueFormatter(formmater);
        barChart.setFitBars(true);
        barChart.setTouchEnabled(true);
        barChart.setLogEnabled(true);
        barChart.animateY(5000);
        barChart.invalidate();
    }
}

class TitleFormatter implements IAxisValueFormatter {
    protected String[] titles;
    private BarLineChartBase<?> chartBase;

    public TitleFormatter(BarLineChartBase<?> chartBase,String[] titles) {
        this.chartBase = chartBase;
        this.titles = new String[titles.length];
        for (int i = 0; i < titles.length; i++) {
            this.titles[i] = titles[i];
        }
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int a = (int) value;
        return titles[a];
    }
}

