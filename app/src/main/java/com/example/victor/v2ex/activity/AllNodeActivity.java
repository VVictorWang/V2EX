package com.example.victor.v2ex.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.victor.v2ex.adapters.AllNodeAdapter;
import com.example.victor.v2ex.data.NodeInfo;
import com.example.victor.v2ex.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AllNodeActivity extends AppCompatActivity {
    private String PATH = "https://www.v2ex.com/api/nodes/all.json";
    private List<NodeInfo> nodeInfos;
    private ProgressDialog dialog;
    private RecyclerView recyclerView;
    private AllNodeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_node);
        nodeInfos = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.all_node);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在加载中");
        new MyNodeTask().execute(PATH);
    }

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

    private List<NodeInfo> Show(String responses) {
        Gson gson = new Gson();
        List<NodeInfo> nodeInfos1 = gson.fromJson(responses, new TypeToken<List<NodeInfo>>() {
        }.getType());
        return nodeInfos1;
    }
    class MyNodeTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            String s = sendRequest();
            nodeInfos = Show(s);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
//                themes = Show(s);
                adapter = new AllNodeAdapter(AllNodeActivity.this, nodeInfos,recyclerView);
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();

//                dialog.dismiss();
            } else if (s == null) {
                Toast.makeText(AllNodeActivity.this, "请求数据失败", Toast.LENGTH_LONG);
            }
            dialog.dismiss();
        }
    }

}
