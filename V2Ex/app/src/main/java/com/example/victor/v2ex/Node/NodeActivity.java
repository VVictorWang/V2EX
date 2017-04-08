package com.example.victor.v2ex.Node;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.victor.v2ex.Containers.Member;
import com.example.victor.v2ex.Containers.Node;
import com.example.victor.v2ex.Containers.Theme;
import com.example.victor.v2ex.Image.HttpDownLoad;
import com.example.victor.v2ex.R;
import com.example.victor.v2ex.ScrollClass;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class NodeActivity extends AppCompatActivity {
    private String topicurl;
    private ProgressDialog dialog;
    private Bitmap bitmap;
    private String node_ti;
    private RecyclerView recyclerView;
    private NodeAdapter viewAdapter;
    private List<Theme> themes = new ArrayList<>();
    private List<Bitmap> bitmaps = new ArrayList<>();
    private NodeContenet nodeContenet = new NodeContenet();
    private String[] information;
    private int length,current;
    private Document document;
    private Elements elements;
    private SwipeRefreshLayout swipeRefreshLayout;
    android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0x123) {
                viewAdapter.notifyDataSetChanged();
            } else if (msg.what == 0x124) {
                viewAdapter.showiiLoadMore();
            } else if (msg.what == 0x125) {
                viewAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
            return true;
        }

    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node);
        Toolbar toolbar = (Toolbar) findViewById(R.id.node_toolbar);
        setSupportActionBar(toolbar);
//        node_title = (TextView) findViewById(R.id.node_title_show);
//        node_image = (CircleImageView) findViewById(R.id.node_image);
//        node_description = (TextView) findViewById(R.id.node_description);
//        theme_number = (TextView) findViewById(R.id.theme_number);
        recyclerView = (RecyclerView) findViewById(R.id.content_node);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_node);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new ScrollClass(layoutManager) {
            @Override
            public void onLoad(final int currentpage) {
                viewAdapter.showiiLoading();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int k = current;
                        if (k >= 10 && k < length) {
                            for (; k < (10 + current) && k < length; k++) {
                                Element element6 = elements.get(k);
                                Theme theme = new Theme();
                                Member member = new Member();
                                Node node = new Node();
                                Element element2 = element6.select("span[class=item_title]").first();
                                String title = element2.text();
                                String url = element2.select("a[href~=/t/]").toString();
                                int i = url.indexOf("/t");
                                int j = url.indexOf("#");
                                url = url.substring(i, j);
                                theme.setTitle(title);
                                theme.setUrl("https://www.v2ex.com" + url);
                                String imgurl = element6.select("img").toString();
                                imgurl = subImageurlwithClass(imgurl);
                                Bitmap bitmap1 = HttpDownLoad.getBitmap("http://" + imgurl);
                                bitmaps.add(bitmap1);
                                String some_information = element6.select("span[class~=fade]").text();
                                information = some_information.split("•");
                                int length = information.length;
                                node.setTitle(information[0]);
                                StringBuilder builder = new StringBuilder();
                                for (int m = 1; m < length; m++) {
                                    builder.append(information[m] + " ");
                                }
                                member.setUsername(builder.toString());
                                theme.setMember(member);
                                theme.setNode(node);
                                themes.add(theme);
                            }
                            current = k;
                            handler.sendEmptyMessage(0x123);

                        } else {
                            handler.sendEmptyMessage(0x124);
                        }
                    }
                }).start();

            }
        });
        Intent intent = getIntent();
        String topic = intent.getStringExtra("linkname");
        if (topic.contains("http")) {
            topicurl = topic;
        } else {
            topicurl = "https://www.v2ex.com" + topic;
        }
        Log.e("xs", topicurl);
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在加载中");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Elements elements1 = document.select("div[class~=cell from]");
                        for (Element element5 : elements1) {
                            if (element5.equals(elements.first())) {
                                break;
                            } else {
                                Theme theme = new Theme();
                                Member member = new Member();
                                Node node = new Node();
                                Element element2 = element5.select("span[class=item_title]").first();
                                String title = element2.text();
                                String url = element2.select("a[href~=/t/]").toString();
                                int i = url.indexOf("/t");
                                int j = url.indexOf("#");
                                url = url.substring(i, j);
                                theme.setTitle(title);
                                theme.setUrl("https://www.v2ex.com" + url);
                                String imgurl = element5.select("img").toString();
                                imgurl = subImageurlwithClass(imgurl);
                                Bitmap bitmap1 = HttpDownLoad.getBitmap("http://" + imgurl);
                                bitmaps.add(bitmap1);
                                String some_information = element5.select("span[class~=fade]").text();
                                information = some_information.split("•");
                                int length = information.length;
                                node.setTitle(information[0]);
                                StringBuilder builder = new StringBuilder();
                                for (int m = 1; m < length; m++) {
                                    builder.append(information[m] + " ");
                                }
                                member.setUsername(builder.toString());
                                theme.setMember(member);
                                theme.setNode(node);
                                themes.add(theme);
                            }
                        }
                        handler.sendEmptyMessage(0x125);
                    }
                }).start();
            }
        });
        new NodeTask().execute(topicurl);

    }

    class NodeTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                document = Jsoup.connect(topicurl).get();
                Element element = document.select("div.header").first();
                String bimapString = element.select("img").toString();
                bimapString = subImageurl(bimapString);
                bitmap = HttpDownLoad.getBitmap("http://" + bimapString);
                nodeContenet.setTheme_num(element.select("span[class=snow]").text() + " " + element.select("strong[class=gray]").text());
                node_ti = document.title();
                nodeContenet.setNode_ti(node_ti);
                nodeContenet.setNode_des(element.select("span[class~=f12]").text());
                elements = document.select("div[class~=cell from]");
                length = elements.size();
                int k=0;
                for (; k < 10 && k < length; k++) {
                    Element element1 = elements.get(k);
                    Theme theme = new Theme();
                    Member member = new Member();
                    Node node = new Node();
                    Element element2 = element1.select("span[class=item_title]").first();
                    String title = element2.text();
                    String url = element2.select("a[href~=/t/]").toString();
                    int i = url.indexOf("/t");
                    int j = url.indexOf("#");
                    url = url.substring(i, j);
                    theme.setTitle(title);
                    theme.setUrl("https://www.v2ex.com" + url);
                    String imgurl = element1.select("img").toString();
                    imgurl = subImageurlwithClass(imgurl);
                    Bitmap bitmap1 = HttpDownLoad.getBitmap("http://" + imgurl);
                    bitmaps.add(bitmap1);
                    String some_information = element1.select("span[class~=fade]").text();
                    information = some_information.split("•");
                    int length = information.length;
                    node.setTitle(information[0]);
                    StringBuilder builder = new StringBuilder();
                    for (int m = 1; m < length; m++) {
                        builder.append(information[m] + " ");
                    }
                    member.setUsername(builder.toString());
                    theme.setMember(member);
                    theme.setNode(node);
                    themes.add(theme);
                }
                current = k;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "ok";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                nodeContenet.setBitmap(bitmap);
                getSupportActionBar().setTitle(node_ti);
                viewAdapter = new NodeAdapter(NodeActivity.this, themes, bitmaps, recyclerView, nodeContenet);
                recyclerView.setAdapter(viewAdapter);
                viewAdapter.notifyDataSetChanged();
            }
            dialog.dismiss();
        }
    }

    private String subImageurl(String raw) {
        int i = raw.indexOf("\" border");
        int j = raw.indexOf("v2ex");
        return raw.substring(j, i);
    }

    private String subImageurlwithClass(String raw) {
        int i = raw.indexOf("\" class");
        int j = raw.indexOf("v2ex");
        return raw.substring(j, i);
    }
}
