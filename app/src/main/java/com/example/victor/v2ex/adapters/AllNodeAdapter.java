package com.example.victor.v2ex.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.victor.v2ex.data.NodeInfo;
import com.example.victor.v2ex.R;
import com.example.victor.v2ex.activity.NodeActivity;

import java.util.List;

/**
 * Created by victor on 2017/3/26.
 */

public class AllNodeAdapter extends RecyclerView.Adapter<AllNodeAdapter.ViewHolder> {
    private Context context;
    private RecyclerView recyclerView;
    private List<NodeInfo> nodeInfos;
    private View.OnClickListener mlistenner = new MyClickListenner();
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView node_title, node_topics, node_header;

        public ViewHolder(View itemView) {
            super(itemView);
            node_header = (TextView) itemView.findViewById(R.id.all_node_header);
            node_title = (TextView) itemView.findViewById(R.id.all_node_title);
            node_topics = (TextView) itemView.findViewById(R.id.all_node_topics);
        }
    }

    public AllNodeAdapter(Context context, List<NodeInfo> nodeInfos,RecyclerView recyclerView) {
        this.context = context;
        this.nodeInfos = nodeInfos;
        this.recyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context1 = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context1);
        View view = layoutInflater.inflate(R.layout.all_node_list, parent, false);
        view.setOnClickListener(mlistenner);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NodeInfo nodeInfo = nodeInfos.get(position);
        holder.node_topics.setText("主题总数：" + nodeInfo.getTopics());
        holder.node_title.setText(nodeInfo.getTitle());
        String header = nodeInfo.getHeader();
        StringBuilder builder = new StringBuilder();
        if (header != null) {
            char[] head = header.toCharArray();
            for (int i=0;i<head.length;i++) {
                if (head[i] == '<') {
                    do {
                        i++;
                    } while (head[i] != '>');
                }
                else
                    builder.append(head[i]);
            }
        }

        if (header == null) {
            holder.node_header.setText("暂无描述");
        } else {

            holder.node_header.setText(builder.toString());
        }
    }


    @Override
    public int getItemCount() {
        return nodeInfos.size();
    }

    class MyClickListenner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int postion = recyclerView.getChildAdapterPosition(v);
            NodeInfo nodeInfo = nodeInfos.get(postion);

            String link =nodeInfo.getUrl();
            Log.e("de", link);
            Intent intent = new Intent(context, NodeActivity.class);
            intent.putExtra("linkname", link);
            context.startActivity(intent);
        }
    }
}
