package com.example.victor.myreminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.victor.myreminder.Database.Dbhelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Create extends AppCompatActivity {
    private SimpleAdapter adapter;
    private Dbhelper database;
    private EditText content, title;
    private CheckBox checkBox;
    private Button alarm_choose;
    private TextView alarm_seleted;
    private String time, date, time_created;
    private int id, repeatMode, priority, accomplished; //表示id，重复次数，重要程度，是否已完成
    private Map<String, String> item1, item2, item3, item4;
    private DateFormat df, df1;
    private Calendar alertTime, nowtime;
    private String[] repeatModes;
    private String[] prioritities;
    private static String name, data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        final List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        item1 = new HashMap<String, String>();
        item2 = new HashMap<String, String>();
        item3 = new HashMap<String, String>();
        item4 = new HashMap<String, String>();

        database = Dbhelper.getreminderDatabase(this);
        content = (EditText) findViewById(R.id.alertContent);
        title = (EditText) findViewById(R.id.alertTitle);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        alarm_choose = (Button) findViewById(R.id.alarm_choice);    //选择闹钟铃声的按钮

        alarm_seleted = (TextView) findViewById(R.id.text_alarm_selsected); //闹钟音乐名字
        alarm_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Cursor mCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
                BaseAdapter adapter = new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return mCursor.getCount();
                    }

                    @Override
                    public Object getItem(int position) {
                        return position;
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        mCursor.moveToPosition(position);
                        CheckBox rb = new CheckBox(Create.this);
                        final String name = mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                        rb.setText(name);
                        return rb;
                    }
                };


                View selectView = getLayoutInflater().inflate(R.layout.selectlist, null);
                final ListView listView = (ListView) selectView.findViewById(R.id.select_list);
                listView.setAdapter(adapter);
                new AlertDialog.Builder(Create.this)
                        .setView(selectView)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                data = new String();
                                for (int i = 0; i < listView.getChildCount(); i++) {

                                    CheckBox checkbox = (CheckBox) listView.getChildAt(i);
                                    if (checkbox.isChecked()) {
                                        name = checkbox.getText().toString();
                                        mCursor.moveToPosition(i);
                                        data = mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                                    }
                                }
                                if (name.equals(null)) {
                                    alarm_seleted.setText("虚拟");
                                } else {
                                    alarm_seleted.setText(name);
                                }
                                dialog.dismiss();
                            }
                        })
                        .show();


            }
        });
        repeatModes = new String[]{"无", "每小时", "每天", "每周", "每月", "每年"};
        prioritities = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        repeatMode = 0;
        priority = 1;
        accomplished = 0;
        Intent intent = getIntent();
        id = intent.getIntExtra("ID", 0);
        alertTime = Calendar.getInstance();
        nowtime = Calendar.getInstance();
        df = new SimpleDateFormat("hh:mm aa");
        df1 = new SimpleDateFormat("dd/MM/yy");
        if (id > 0) {
            Cursor cursor = database.getItem(id);
            cursor.moveToFirst();
            String contentString = cursor.getString(cursor.getColumnIndex
                    (Dbhelper.DB_COLUMN_CONTENT));
            String titleString = cursor.getString(cursor.getColumnIndex(Dbhelper.DB_COLUMN_TITLE));
            String alert_music = cursor.getString(cursor.getColumnIndex(Dbhelper.Alert_MUSIC_NAME));
            accomplished = cursor.getInt(cursor.getColumnIndex(Dbhelper.DB_ACCOMPLISHED));
            content.setText(contentString);
            title.setText(titleString);
            alarm_seleted.setText(alert_music);
            if (accomplished == 1) {
                checkBox.setChecked(true);
            } else if (accomplished == 0) {
                checkBox.setChecked(false);
            }
            long timeInMilliseconds = cursor.getLong(cursor.getColumnIndex(Dbhelper.DB_COLUMN_TIME));
            repeatMode = cursor.getInt(cursor.getColumnIndex(Dbhelper.DB_COLUMN_FREQUENCY));
            priority = cursor.getInt(cursor.getColumnIndex(Dbhelper.DB_PRIORITY));
            alertTime.setTimeInMillis(timeInMilliseconds);
            nowtime.setTimeInMillis(System.currentTimeMillis());

            DateFormat df = new SimpleDateFormat("hh:mm aa");
            DateFormat df1 = new SimpleDateFormat("dd/MM/yy");
            time_created = df.format(nowtime.getTime());

            time = df.format(alertTime.getTime());
            date = df1.format(alertTime.getTime());
            getSupportActionBar().setTitle("编辑备忘录");
            cursor.close();
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        accomplished = 1;
                    } else {
                        accomplished = 0;
                    }
                }
            });

        } else {
            Calendar current = Calendar.getInstance();
            time = df.format(current.getTime());
            time_created = time;
            date = df1.format(current.getTime());
            alarm_seleted.setText("固执");
            alertTime.setTimeInMillis(current.getTimeInMillis());
            getSupportActionBar().setTitle("新建备忘录");
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        accomplished = 1;
                    } else {
                        accomplished = 0;
                    }
                }
            });
        }

        item1.put("title", "截止时间");
        item1.put("subtext", time);
        item2.put("title", "截止日期");
        item2.put("subtext", date);
        item3.put("title", "重复次数");
        item3.put("subtext", repeatModes[repeatMode]);
        item4.put("title", "重要程度");
        item4.put("subtext", prioritities[priority]);
        mapList.add(item1);
        mapList.add(item2);
        mapList.add(item3);
        mapList.add(item4);
        adapter = new SimpleAdapter(this, mapList, android.R.layout.simple_list_item_2,
                new String[]{"title", "subtext"}, new int[]{android.R.id.text1, android.R.id.text2});
        ListView listView = (ListView) findViewById(R.id.alertSettings);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    TimePickerDialog timePicker = timePicker();
                    timePicker.show();
                } else if (i == 1) {
                    DatePickerDialog datePicker = datePicker();
                    datePicker.show();
                } else if (i == 2) {
                    repeatDialog().show();
                } else {
                    priorityDialog().show();
                }

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onBackPressed() {
        saveAlert();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_or_edit_alert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_save_alert:
                saveAlert();
                break;

            case android.R.id.home:
                saveAlert();
                break;

            default:
                break;

        }

        return true;
    }

    private TimePickerDialog timePicker() {
        return new TimePickerDialog(Create.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        alertTime.set(Calendar.HOUR_OF_DAY, hour);
                        alertTime.set(Calendar.MINUTE, minute);
                        alertTime.set(Calendar.SECOND, 0);
                        time = df.format(alertTime.getTime());
                        item1.put("subtext", time);
                        adapter.notifyDataSetChanged();
                    }
                }, alertTime.get(Calendar.HOUR_OF_DAY), alertTime.get(Calendar.MINUTE), false);
    }

    private DatePickerDialog datePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(Create.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        alertTime.set(Calendar.YEAR, year);
                        alertTime.set(Calendar.MONTH, month);
                        alertTime.set(Calendar.DAY_OF_MONTH, day);
                        date = df1.format(alertTime.getTime());
                        item2.put("subtext", date);
                        adapter.notifyDataSetChanged();
                    }
                }, alertTime.get(Calendar.YEAR), alertTime.get(Calendar.MONTH), alertTime.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return datePicker;
    }

    private AlertDialog saveDialog(int id, String title, String content, long time) {
        final int saveId = id;
        final long saveTime = time;
        final String saveMessage = content;
        final String saveTitle = title;


        return new AlertDialog.Builder(this)

                .setTitle("确认")
                .setMessage("你想要保存吗?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int i) {
                        if (saveId > 0) {
                            //如果原来存在，则先取消原来的闹钟
                            Intent cancelPrevious = new Intent(Create.this,
                                    AlarmService.class);
                            cancelPrevious.putExtra("id", saveId);
                            cancelPrevious.setAction(AlarmService.CANCEL);
                            startService(cancelPrevious);
                            if (data == null) { //如果用户未选择铃声，则使用默认铃声
                                database.updateAlert(saveId, saveTitle, saveMessage, saveTime, repeatMode, time_created, priority, accomplished, "no_alarm", "固执");
                            } else {
                                database.updateAlert(saveId, saveTitle, saveMessage, saveTime, repeatMode, time_created, priority, accomplished, data, name);
                            }
                            //如果已完成，则不创建闹钟
                            if (!checkBox.isChecked()) {
                                createAlarm(saveId);
                            }

                        } else {
                            int n;
                            if (data == null) {
                                n = (int) database.insertAlert(saveTitle, saveMessage, saveTime, repeatMode, time_created, priority, accomplished, "no_alarm", "固执");
                            } else {
                                n = (int) database.insertAlert(saveTitle, saveMessage, saveTime, repeatMode, time_created, priority, accomplished, data, name);
                            }
                            if (!checkBox.isChecked()) {
                                createAlarm(n);
                            }
                        }
                        terminateActivity();
                        dialog.dismiss();
                    }

                })


                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        terminateActivity();
                        database.close();
                        dialog.dismiss();

                    }
                })
                .create();
    }

    private AlertDialog repeatDialog() {
        final int prevRepeat = repeatMode;
        return new AlertDialog.Builder(this)
                .setTitle("重复")
                .setSingleChoiceItems(repeatModes, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        repeatMode = i;
                    }
                })
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // set label to selected repeat mode
                        item3.put("subtext", repeatModes[repeatMode]);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        repeatMode = prevRepeat;
                    }
                })
                .create();
    }

    private AlertDialog priorityDialog() {
        final int prevPriority = priority;
        return new AlertDialog.Builder(this).setTitle("重要程度")
                .setSingleChoiceItems(prioritities, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        priority = which;
                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        item4.put("subtext", prioritities[priority]);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        priority = prevPriority;
                    }
                })
                .create();
    }

    private void createAlarm(int id) {
        Intent alarm = new Intent(this, AlarmService.class);
        alarm.putExtra("id", id);
        alarm.setAction(AlarmService.CREATE);
        startService(alarm);
        database.close();
    }

    private void terminateActivity() {
        NavUtils.navigateUpFromSameTask(this);
    }

    private void saveAlert() {
        String contentString = content.getText().toString();
        String titleString = title.getText().toString();
        //如果设置的时间小于当前时间，则设置提醒时间为当前时间
        if (!(alertTime.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())) {
            saveDialog(id, titleString, contentString, alertTime.getTimeInMillis()).show();

        } else {
            saveDialog(id, titleString, contentString, Calendar.getInstance().getTimeInMillis()).show();
        }
    }
}

