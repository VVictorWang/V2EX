package com.example.victor.myreminder.Game;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Victor on 2017/3/17.
 */

public class Card extends FrameLayout{
    private int num;
    private TextView tv;
    private int color = 0xff996600;

    public Card(Context context) {
        super(context);
        tv = new TextView(context);
        tv.setTextSize(32);
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundColor(0x2341a3aa);
        LayoutParams params = new LayoutParams(-1, -1);
        params.setMargins(10, 10, 0, 0);
        addView(tv, params);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        if (num <= 0) {
            tv.setText(0 + "");
        } else {
            tv.setText(num + "");
        }
        if (num == 0) {

            tv.setBackgroundColor(0xfaa96600);
        } else if (num == 2) {
            tv.setBackgroundColor(0xff445999);
        } else if (num == 4) {
            tv.setBackgroundColor(0xffec6666);
        } else if (num == 8) {
            tv.setBackgroundColor(0xffa36699);
        } else if (num == 16) {
            tv.setBackgroundColor(0xfffa3333);
        } else {
            tv.setBackgroundColor(0xffac0000);
        }
    }

    public boolean equals(Card card) {
        return card.getNum() == this.getNum();
    }}
