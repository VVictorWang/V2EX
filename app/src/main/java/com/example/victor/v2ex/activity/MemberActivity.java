package com.example.victor.v2ex.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.victor.v2ex.utils.HttpDownLoad;
import com.example.victor.v2ex.adapters.MemberAdapter;
import com.example.victor.v2ex.data.MemberTheme;
import com.example.victor.v2ex.data.ThemeInfor;
import com.example.victor.v2ex.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MemberActivity extends AppCompatActivity {

    private Document document;
    private ProgressDialog dialog;
    private String PATH;
    private String name, information;
    private Bitmap image;
    private MemberTheme memberTheme = new MemberTheme();
    private List<ThemeInfor> themeInfors = new ArrayList<>();
    private RecyclerView recyclerView;
    private MemberAdapter memberAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_activity);
        recyclerView = (RecyclerView) findViewById(R.id.member_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Intent intent = getIntent();
        String name = intent.getStringExtra("member");
        PATH = "https://www.v2ex.com/member/" + name;
        Log.e("xd", PATH);
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在加载中....");
        dialog.show();
        new MemberTask().execute(PATH);
    }

    class MemberTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                document = Jsoup.connect(PATH).get();
                Element element = document.select("table[cellpadding=0]").get(1);
                String tempimage = element.select("img").toString();
                tempimage = "http://" + subImageurl(tempimage);
                image = HttpDownLoad.getBitmap(tempimage);
                memberTheme.setMember_image(image);
                name = element.select("h1[style~=ma]").text();
                memberTheme.setMember_name(name);
                information = element.select("span[class=gray]").text();
                memberTheme.setMember_infor(information);
                Elements elements = document.select("div[class=cell item]");
                for (Element element1 : elements) {
                    ThemeInfor themeInfor = new ThemeInfor();
                    String title = element1.select("span[class=item_title]").text();
                    String infor = element1.select("span[class=small fade]").text();
                    themeInfor.setTheme_title(title);
                    themeInfor.setTheme_infor(infor);
                    themeInfors.add(themeInfor);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "ok";
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                memberAdapter = new MemberAdapter(MemberActivity.this,memberTheme,themeInfors,recyclerView);
                recyclerView.setAdapter(memberAdapter);
                memberAdapter.notifyDataSetChanged();

            }
            dialog.dismiss();
        }
        private String subImageurl(String raw) {
            int i = raw.indexOf("\" class");
            int j = raw.indexOf("v2ex");
            return raw.substring(j, i);
        }
    }

}
