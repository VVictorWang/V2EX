package com.example.victor.myreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.victor.myreminder.Chart.BarChart;
import com.example.victor.myreminder.Chart.pieChart;
import com.example.victor.myreminder.Database.Dbadapter;
import com.example.victor.myreminder.Database.Dbhelper;
import com.github.clans.fab.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private Dbhelper database;
    private TextView empty;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Dbadapter adapter;
    private Cursor cursor;

    @Override
    protected void onRestart() {
        super.onRestart();
        cursor = database.getAllItemsOrderByPriority();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = Dbhelper.getreminderDatabase(this);
        cursor = database.getAllItemsOrderByPriority();
//        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
//        IntentFilter filter = new IntentFilter("REFRESH");
//        broadcastManager.registerReceiver(deleteReceiver, filter);
        mRecyclerView = (RecyclerView) findViewById(R.id.reminderList);
        empty = (TextView) findViewById(R.id.empty);
        emptyCheck();
        init();
        FloatingActionButton addAlert = (FloatingActionButton) findViewById(R.id.add_alert);
        addAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), Create.class));
            }
        });

    }

 public class DeleteReciever extends WakefulBroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if (intent1.getAction().equals("REFRESH")) {
                emptyCheck();
                init();
//            }
        }
    }

    public void init() {
        adapter = new Dbadapter(this, cursor, mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_priority:
                cursor = database.getAllItemsOrderByPriority();
                init();
                break;
            case R.id.menu_main_time:
                cursor = database.getAllItems();
                init();
                break;
            case R.id.menu_main_before:
                cursor = database.getAllItemsOrderByTime();
                init();
                break;
            case R.id.chartto:
                Intent intent = new Intent(MainActivity.this, pieChart.class);
                startActivity(intent);
                break;
            case R.id.chartto_priority:
                Intent intent1 = new Intent(MainActivity.this, BarChart.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
        return true;
    }

    private void emptyCheck() {
        if (database.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }


//    private BroadcastReceiver deleteReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//                emptyCheck();
//                init();
//        }
//    };
}
