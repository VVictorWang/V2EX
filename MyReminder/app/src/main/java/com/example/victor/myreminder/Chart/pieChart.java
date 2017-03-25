package com.example.victor.myreminder.Chart;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.victor.myreminder.Database.Dbhelper;
import com.example.victor.myreminder.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class pieChart extends AppCompatActivity {
    private Dbhelper database;
    private Cursor cursor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);
        database = Dbhelper.getreminderDatabase(this);
        cursor = database.getAllItems();
        PieChart piechart = (PieChart) findViewById(R.id.piechart);
        cursor.moveToFirst();

        float accomplished = 0, unaccomplished = 0;
        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex(Dbhelper.DB_ACCOMPLISHED)) == 1) {
                accomplished++;
            } else {
                unaccomplished++;
            }
        }
        float total = accomplished + unaccomplished;
        float a = (accomplished * 100 / total);
        float b = (unaccomplished * 100 / total);
        List<PieEntry> entris = new ArrayList<>();
        PieEntry pie1 = new PieEntry(a, "已完成");
        PieEntry pie2 = new PieEntry(b, "未完成");
        entris.add(pie1);
        entris.add(pie2);
        PieDataSet set = new PieDataSet(entris, "完成情况");
        set.setColors(Color.GREEN, Color.BLUE);
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(35);
        PieData data = new PieData(set);
        piechart.setData(data);
        piechart.setVisibility(View.VISIBLE);
        piechart.setTouchEnabled(true);
        piechart.setLogEnabled(true);
        piechart.animateX(5000);
        piechart.invalidate();
    }


}
