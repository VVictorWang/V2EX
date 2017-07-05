package com.example.victor.v2ex.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.victor.v2ex.R;

public class TabActivity extends android.app.TabActivity {
    private TabHost host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_host);
        host = getTabHost();
        Intent internt1 = new Intent();
        internt1.setClass(this,HotActivity.class);

        TabHost.TabSpec tabSpec = host.newTabSpec("act1");
        //指定选项卡的显示名称
        tabSpec.setIndicator("最热主题");
        //指定跳转方向
        tabSpec.setContent(internt1);
        host.addTab(tabSpec);
        Intent intent2 = new Intent();
        intent2.setClass(this, NewesetActivity.class);
        TabHost.TabSpec tabSpec1 = host.newTabSpec("act2");
        tabSpec1.setIndicator("最新主题");
        tabSpec1.setContent(intent2);
        host.addTab(tabSpec1);
        Intent intent3 = new Intent();
        intent3.setClass(this, AllNodeActivity.class);
        TabHost.TabSpec tabSpec2 = host.newTabSpec("act3");
        tabSpec2.setIndicator("所有节点");
        tabSpec2.setContent(intent3);
        host.addTab(tabSpec2);
    }
}
