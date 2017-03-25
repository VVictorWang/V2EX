package com.example.victor.myreminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.example.victor.myreminder.Database.Dbhelper;

import java.util.Calendar;

/**
 * Created by Victor on 2017/3/13.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver{
    private final int HOURLY = 1, DAILY = 2, WEEKLY = 3, MONTHLY = 4, YEARLY = 5;

    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra("id", 0);
        Dbhelper database = Dbhelper.getreminderDatabase(context);
        Cursor cursor = database.getItem(id);
        cursor.moveToFirst();
        int frequency = cursor.getInt(cursor.getColumnIndex(Dbhelper.DB_COLUMN_FREQUENCY));
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(Dbhelper.DB_COLUMN_TIME)));

        //根据重复次数设置闹钟提醒
        if (frequency > 0) {
            if (frequency == HOURLY) {
                time.add(Calendar.HOUR, 1);
            } else if (frequency == DAILY) {
                time.add(Calendar.DATE, 1);
            } else if (frequency == WEEKLY) {
                time.add(Calendar.DATE, 7);
            } else if (frequency == MONTHLY) {
                time.add(Calendar.MONTH, 1);

            } else if (frequency == YEARLY) {
                time.add(Calendar.YEAR, 1);
            }
            database.updateTime(id, time.getTimeInMillis());
            Intent setAlarm = new Intent(context, AlarmService.class);
            setAlarm.putExtra("id", id);
            setAlarm.setAction(AlarmService.CREATE);
            context.startService(setAlarm);
        }
    }

}
