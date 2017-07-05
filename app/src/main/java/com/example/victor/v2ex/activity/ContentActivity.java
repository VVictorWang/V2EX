package com.example.victor.v2ex.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.victor.v2ex.utils.HttpDownLoad;
import com.example.victor.v2ex.R;
import com.example.victor.v2ex.data.ContentMainHolder;
import com.example.victor.v2ex.adapters.ReplyAdpter;
import com.example.victor.v2ex.data.ReplyMember;
import com.example.victor.v2ex.utils.ScrollClass;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ContentActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    private String url, el_name, theme_na, topic;
    private Bitmap bitmap;
    private String[] information;
    private String content = new String();
    private List<ReplyMember> members = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ReplyAdpter replyAdpter;
    private int length, current;
    private Elements element2;
    private Document document;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ContentMainHolder mainHolder;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0x123) {
                replyAdpter.notifyDataSetChanged();
            } else if (msg.what == 0x124) {
                replyAdpter.showLoadMore();
            } else if (msg.what == 0x125) {
                replyAdpter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
            return true;
        }

    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.content_toolbar);
        setSupportActionBar(toolbar);
        toolbar.hideOverflowMenu();
        mainHolder = new ContentMainHolder();
        recyclerView = (RecyclerView) findViewById(R.id.reply_list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_content);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在加载中");
        getSupportActionBar().setTitle("主题信息");
        Intent intent = getIntent();
        url = intent.getStringExtra("link");
        new LoadTask().execute(url);
        recyclerView.addOnScrollListener(new ScrollClass(layoutManager) {
            @Override
            public void onLoad(int currentpage) {
                replyAdpter.showLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int m = current;
                        if (m >= 10 && m < length) {
                            for (; m < (20 + current) && m < length; m++) {
                                ReplyMember replyMember = new ReplyMember();
                                Element element1 = element2.get(m);
                                String time = element1.select("span[class~=fade]").text();
                                String name = element1.select("a[href~=/member/]").text();
                                String reply = element1.select("div.reply_content").toString();
                                String replytext = element1.select("div.reply_content").text();
                                if (reply.contains("img src")) {
                                    Bitmap replyBitmap;
                                    int i = reply.indexOf("http:");
                                    int j = reply.indexOf("\" class");
                                    String imgurl = reply.substring(i, j);
                                    replyBitmap = HttpDownLoad.getBitmap(imgurl);
                                    replyMember.setReplyBitmap(replyBitmap);
                                    replyMember.setHasbitmap(true);
                                }
                                String url = element1.select("img").toString();
                                String src = subImageurl(url);
                                if (time.contains("via")) {
                                    int k = time.indexOf("via");
                                    time = time.substring(0, k);
                                }
                                Bitmap bitmap = HttpDownLoad.getBitmap("http://" + src);
                                replyMember.setBitmap(bitmap);
                                replyMember.setName(name);
                                replyMember.setReply(replytext);
                                replyMember.setTime(time);
                                members.add(replyMember);
                            }
                            current = m;

                            handler.sendEmptyMessage(0x123);
                        } else {
                            handler.sendEmptyMessage(0x124);
                        }
                    }
                }).start();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Elements elements4 = document.select("div[id~=^[a-z]][class=cell]");
                        for (Element element5 : elements4) {
                            if (element5.equals(element2.first())) {
                                break;
                            } else {
                                String time = element5.select("span[class~=fade]").text();
                                String name = element5.select("a[href~=/member/]").text();
                                String reply = element5.select("div.reply_content").text();
                                String url = element5.select("img").toString();
                                String src = subImageurl(url);
                                if (time.contains("via")) {
                                    int k = time.indexOf("via");
                                    time = time.substring(0, k);
                                }
                                Bitmap bitmap = HttpDownLoad.getBitmap("http://" + src);
                                ReplyMember replyMember = new ReplyMember();
                                replyMember.setBitmap(bitmap);
                                replyMember.setName(name);
                                replyMember.setReply(reply);
                                replyMember.setTime(time);
                                members.add(0, replyMember);
                            }
                        }
                        handler.sendEmptyMessage(0x125);
                    }
                }).start();
            }
        });

    }

    class LoadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String title = new String();
            try {
                document = Jsoup.connect(url).get();
                title = document.title();
                Element elements = document.select("small[class=gray]").first();
                el_name = elements.text();
                information = el_name.split("·");
                Element element3 = document.select("div[class=box][style~=border]").first();
                String host_photo = subImageurl(element3.select("img").toString());
                bitmap = HttpDownLoad.getBitmap("http://" + host_photo);
                topic = element3.select("a[href~=/go/]").toString();
                int i = topic.indexOf("/go/");
                int j = topic.indexOf("\">");
                topic = topic.substring(i, j);
                theme_na = element3.select("a[href~=/go/]").text();
                if (title.contains("V2EX")) {
                    int index = title.indexOf('V');
                    title = title.substring(0, index - 2);
                }
                element2 = document.select("div[id~=^[a-z]][class=cell]");
                length = element2.size();
                int m = 0;
                for (; m < 20 && m < length; m++) {
                    Element element1 = element2.get(m);
                    ReplyMember replyMember = new ReplyMember();
//                    Element element1 = element2.get(m);
                    String time = element1.select("span[class~=fade]").text();
                    String name = element1.select("a[href~=/member/]").text();
                    String reply = element1.select("div.reply_content").toString();
                    String replytext =  element1.select("div.reply_content").text();
                    if (reply.contains("img")) {
                        Bitmap replyBitmap;
                        int o = reply.indexOf("http:");
                        int p = reply.indexOf("\" class");
                        String imgurl = reply.substring(o, p - 1);
                        replyBitmap = HttpDownLoad.getBitmap(imgurl);
                        replyMember.setReplyBitmap(replyBitmap);
                        replyMember.setHasbitmap(true);
                    }
                    String url = element1.select("img").toString();
                    String src = subImageurl(url);
                    if (time.contains("via")) {
                        int k = time.indexOf("via");
                        time = time.substring(0, k);
                    }
                    Bitmap bitmap = HttpDownLoad.getBitmap("http://" + src);
                    replyMember.setBitmap(bitmap);
                    replyMember.setName(name);
                    replyMember.setReply(replytext);
                    replyMember.setTime(time);
                    members.add(replyMember);
                }
                current = m;
                mainHolder.setTitle(title);
                mainHolder.setTopic(topic);
                mainHolder.setInformation(information);
                mainHolder.setBitmap(bitmap);
                mainHolder.setTheme_na("V2EX > " + theme_na);
                Element element = document.select("div.topic_content").first();
                content = element.text();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return title;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null && content != null) {
                mainHolder.setContent(content);
                replyAdpter = new ReplyAdpter(ContentActivity.this, members,mainHolder, recyclerView);
                recyclerView.setAdapter(replyAdpter);

            }
            dialog.dismiss();
        }
    }
    private String subImageurl(String raw) {
        int i = raw.indexOf("\" class");
        int j = raw.indexOf("v2ex");
        return raw.substring(j, i);
    }
}
