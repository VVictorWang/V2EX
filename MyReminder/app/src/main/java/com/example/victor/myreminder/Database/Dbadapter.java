package com.example.victor.myreminder.Database;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.victor.myreminder.AlarmService;
import com.example.victor.myreminder.Create;
import com.example.victor.myreminder.R;

import java.text.SimpleDateFormat;

/**
 * Created by Victor on 2017/3/12.
 */

public class Dbadapter extends RecyclerView.Adapter<Dbadapter.ViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private Dbhelper database;
    private RecyclerView mRecyclerView;
    public View.OnClickListener mListener = new reminderClickListener();
    public View.OnLongClickListener mLongListener = new reminderLongClickListener();
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm, MMM d ''yy");


    public static class ViewHolder extends RecyclerView.ViewHolder {
        //得到此activity的所有控件
        public TextView title;
        public TextView content;
        public TextView time;
        public TextView time_created;
        public TextView accomplished;

        public ViewHolder(View view) {

            super(view);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.reminder);
            time = (TextView) view.findViewById(R.id.timeLabel);    //截止时间
            time_created = (TextView) view.findViewById(R.id.timecreated);
        }
    }

    public Dbadapter(Context context, Cursor cursor, RecyclerView recyclerView) {
        mContext = context;
        mCursor = cursor;
        mRecyclerView = recyclerView;
        database = Dbhelper.getreminderDatabase(context);
    }


    private Context getContext() {
        return mContext;
    }


    @Override
    public Dbadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //如果没有元素就加载empty的textview
        if (database.isEmpty()) {
            View emptyView = parent.findViewById(R.id.empty);
            return new ViewHolder(emptyView);
        } else {

            View reminderView = inflater.inflate(R.layout.list_item_layout, parent, false);
            reminderView.setOnClickListener(mListener); //轻触编辑
            reminderView.setOnLongClickListener(mLongListener);     //长按弹出删除的对话框

            ViewHolder viewHolder = new ViewHolder(reminderView);
            return viewHolder;
        }
    }


    @Override
    public void onBindViewHolder(Dbadapter.ViewHolder viewHolder, int id) {
        mCursor.moveToPosition(id);
        viewHolder.time.setText(timeFormat.format(mCursor.getLong(mCursor.getColumnIndex(Dbhelper.DB_COLUMN_TIME))));
        String a = mCursor.getString(mCursor.getColumnIndex(Dbhelper.DB_TIME_CREATED));
        viewHolder.time_created.setText("创建时间: " + a);
        viewHolder.title.setText(mCursor.getString(mCursor.getColumnIndex(Dbhelper.DB_COLUMN_TITLE)));
        viewHolder.content.setText(mCursor.getString(mCursor.getColumnIndex(Dbhelper.DB_COLUMN_CONTENT)));
    }

    public int getItemCount() {
        return mCursor.getCount();
    }

    //删除
    private AlertDialog deleteDialog(final int id, final int position) {
        final int deleteId = id;
        final Cursor cursor = database.getItem(id);
        cursor.moveToFirst();

        return new AlertDialog.Builder(mContext)
                .setTitle("确认")
                .setMessage("你确认删除吗?")

                .setPositiveButton("是", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int i) {

                        mRecyclerView.removeViewAt(position);
                        Intent delete = new Intent(mContext, AlarmService.class);
                        delete.putExtra("id", deleteId);
                        database.deleteItem(id);
                        delete.setAction(AlarmService.DELETE);
                        mContext.startService(delete);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();

                    }
                })
                .create();

    }

    //编辑界面
    class reminderClickListener implements View.OnClickListener {
        public void onClick(View view) {
            int position = mRecyclerView.getChildAdapterPosition(view);
            mCursor.moveToPosition(position);
            Intent intent;
            intent = new Intent(mContext, Create.class);

            intent.putExtra("ID", mCursor.getInt(mCursor.getColumnIndex(Dbhelper.DB_COLUMN_ID)));
            mContext.startActivity(intent);
        }
    }

    //长按删除
    class reminderLongClickListener implements View.OnLongClickListener {
        public boolean onLongClick(View view) {
            int position = mRecyclerView.getChildAdapterPosition(view);
            mCursor.moveToPosition(position);
            int id = mCursor.getInt(mCursor.getColumnIndex(Dbhelper.DB_COLUMN_ID));
            deleteDialog(id, position).show();
            return true;
        }
    }
}
