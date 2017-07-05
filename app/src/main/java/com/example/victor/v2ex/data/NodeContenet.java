package com.example.victor.v2ex.data;

import android.graphics.Bitmap;

/**
 * Created by Victor on 2017/3/25.
 */

public class NodeContenet {
    private String theme_num,node_ti,node_des;
    private Bitmap bitmap;


    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setTheme_num(String theme_num) {
        this.theme_num = theme_num;
    }

    public void setNode_ti(String node_ti) {
        this.node_ti = node_ti;
    }

    public void setNode_des(String node_des) {
        this.node_des = node_des;
    }



    public String getTheme_num() {
        return theme_num;
    }

    public String getNode_ti() {
        return node_ti;
    }

    public String getNode_des() {
        return node_des;
    }


}
