package com.example.victor.myreminder;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;

import com.example.victor.myreminder.Database.Dbhelper;
import com.example.victor.myreminder.Game.Alarm;

/**
 * Created by Victor on 2017/3/13.
 */

public class AlarmService extends IntentService {
    public static final String CREATE = "CREATE";
    public static final String CANCEL = "CANCEL";
    public static final String DELETE = "DELETE";
    private IntentFilter matcher;

    public AlarmService() {
        super("AlarmService");
        matcher = new IntentFilter();
        matcher.addAction(CREATE);
        matcher.addAction(CANCEL);
        matcher.addAction(DELETE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        int id = intent.getIntExtra("id", 0);
        if (matcher.matchAction(action)) {
            execute(action, id);
        }
    }
    private void execute(String action, int id) {

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Dbhelper database = Dbhelper.getreminderDatabase(this);
        Cursor cursor = database.getItem(id);
        cursor.moveToFirst();

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("id", cursor.getInt(cursor.getColumnIndex(Dbhelper.DB_COLUMN_ID)));
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Intent intent2 = new Intent(this, Alarm.class);
        intent2.putExtra("alert", cursor.getString(cursor.getColumnIndex(Dbhelper.Alert_MUSIC)));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        long timeInMilliseconds = cursor.getLong(cursor.getColumnIndex(Dbhelper.DB_COLUMN_TIME));

        //如果是创建闹钟
        if (CREATE.equals(action)) {
            alarm.set(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pendingIntent);

        } else if (DELETE.equals(action)) {
            alarm.cancel(pendingIntent);
//            database.deleteItem(id);
            Intent refresh = new Intent(this, MainActivity.DeleteReciever.class);
            refresh.setAction("REFRESH");
            LocalBroadcastManager.getInstance(this).sendBroadcast(refresh);
        } else if (CANCEL.equals(action)) {
            alarm.cancel(pendingIntent);
        }
        database.close();
        cursor.close();
    }
}
