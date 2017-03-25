package com.example.victor.v2ex;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.victor.v2ex.Containers.Member;
import com.example.victor.v2ex.Containers.Theme;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HotActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ViewAdapter adapter;
    public List<Theme> themes;
    private LinearLayoutManager layoutManager;
    private ProgressDialog dialog;
    private List<Bitmap> bitmaps;
    private int length;
    private String PATH = "https://www.v2ex.com/api/topics/hot.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.hideOverflowMenu();
        getSupportActionBar().setTitle("最热主题");
        themes = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.show_theme);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在加载中");
        begin();
    }

    private void begin() {
        new MyTask().execute(PATH);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.item_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.hot_item:
//                break;
//            case R.id.new_item:
//                Intent intent1 = new Intent(HotActivity.this, Neweset.class);
//                startActivity(intent1);
//                finish();
//                break;
//            default:
//                break;
//        }
//        return true;
//    }

    public String sendRequest() {
        HttpURLConnection connection = null;
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(PATH);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(8000);
            connection.setConnectTimeout(8000);
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return builder.toString();
    }


    private List<Theme> Show(String responses) {
        Gson gson = new Gson();
        List<Theme> themesa = gson.fromJson(responses, new TypeToken<List<Theme>>() {
        }.getType());
        return themesa;

    }

    private List<Bitmap> getShow(List<Theme> themes) {
        List<Bitmap> bitmaps = new ArrayList<>();
        for (Theme theme : themes) {
            Member member = theme.getMember();
            Bitmap bitmap = HttpDownLoad.getBitmap("http:"+member.getAvatar_large());
            bitmaps.add(bitmap);
        }
        return bitmaps;
    }

    class MyTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            String s = sendRequest();
            themes = Show(s);
            bitmaps = getShow(themes);
            length = themes.size();
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
//                themes = Show(s);

                adapter = new ViewAdapter(HotActivity.this,recyclerView, themes,bitmaps);
                recyclerView.setAdapter(adapter);


                adapter.notifyDataSetChanged();

//                dialog.dismiss();
            } else if (s == null) {
                Toast.makeText(HotActivity.this, "请求数据失败", Toast.LENGTH_LONG);
            }
            dialog.dismiss();
        }
    }

}


