package com.example.victor.v2ex.Node;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.victor.v2ex.Containers.Member;
import com.example.victor.v2ex.Containers.Node;
import com.example.victor.v2ex.Containers.Theme;
import com.example.victor.v2ex.HttpDownLoad;
import com.example.victor.v2ex.R;
import com.example.victor.v2ex.ViewAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NodeActivity extends AppCompatActivity {
    private String topicurl;
    private ProgressDialog dialog;
    private Bitmap bitmap;
    private TextView node_title,node_description,theme_number;
    private String theme_num,node_ti,node_des;
    private CircleImageView node_image;
    private RecyclerView recyclerView;
    private ViewAdapter viewAdapter;
    private List<Theme> themes = new ArrayList<>();
    private List<Bitmap> bitmaps = new ArrayList<>();
    private String[] information ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node);
        Toolbar toolbar = (Toolbar) findViewById(R.id.node_toolbar);
        setSupportActionBar(toolbar);
        node_title = (TextView) findViewById(R.id.node_title_show);
        node_image = (CircleImageView) findViewById(R.id.node_image);
        node_description = (TextView) findViewById(R.id.node_description);
        theme_number = (TextView) findViewById(R.id.theme_number);
        recyclerView = (RecyclerView) findViewById(R.id.content_node);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        Intent intent = getIntent();
        String topic = intent.getStringExtra("linkname");
        topicurl = "https://www.v2ex.com" + topic;
        Log.e("xs", topicurl);
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在加载中");
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
                Document document = Jsoup.connect(topicurl).get();
                Element element = document.select("div.header").first();
                String bimapString = element.select("img").toString();
                bimapString = subImageurl(bimapString);
                bitmap = HttpDownLoad.getBitmap("http://" + bimapString);
                theme_num = element.select("span[class=snow]").text() + " " + element.select("strong[class=gray]").text();
                node_ti = document.title();
                node_des = element.select("span[class~=f12]").text();
                Elements elements = document.select("div[class~=cell from]");
                for (Element element1 : elements) {
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
                    for (int k=1;k<length;k++) {
                        builder.append(information[k]+" ");
                    }
                    member.setUsername(builder.toString());
                    theme.setMember(member);
                    theme.setNode(node);
                    themes.add(theme);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "ok";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                node_title.setText(node_ti);
                node_description.setText(node_des);
                theme_number.setText(theme_num);
                node_image.setImageBitmap(bitmap);
                getSupportActionBar().setTitle(node_ti);
                viewAdapter = new ViewAdapter(NodeActivity.this, recyclerView,themes, bitmaps);
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
