package com.example.victor.v2ex.Reply;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.victor.v2ex.ContentMainHolder;
import com.example.victor.v2ex.Node.NodeActivity;
import com.example.victor.v2ex.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Victor on 2017/3/22.
 */

public class ReplyAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_CONTENT = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_FIRST = 2;
    private Context context;
    private ProgressBar pbLoading;
    private TextView tvLoadMore;
    private ContentMainHolder mainHolder = new ContentMainHolder();
    private View.OnClickListener mclicklistenner = new myclicklistenner();
    private List<ReplyMember> members = new ArrayList<>();
    private RecyclerView recyclerView;

    public static class FirstViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private TextView theme_name, host_name, post_information, content_main;
        private ImageView host_iamge;

        public FirstViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.content_title);
            theme_name = (TextView)itemView. findViewById(R.id.theme_name);
            content_main = (TextView) itemView.findViewById(R.id.content_main);
            host_iamge = (ImageView) itemView.findViewById(R.id.host_image);
            host_name = (TextView) itemView.findViewById(R.id.host_name);
            post_information = (TextView) itemView.findViewById(R.id.post_information);

        }
    }
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private TextView member;
        private TextView time;
        private CircleImageView member_image;
        private TextView number;


        public ContentViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.reply);
            this.member = (TextView) itemView.findViewById(R.id.member_list);
            this.time = (TextView) itemView.findViewById(R.id.time_created);
            this.member_image = (CircleImageView) itemView.findViewById(R.id.member_image);
            this.number = (TextView) itemView.findViewById(R.id.number);
        }
    }
    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLoadMore;
        private ProgressBar pbLoading;
        public FooterViewHolder(View itemView) {
            super(itemView);
            tvLoadMore = (TextView) itemView.findViewById(R.id.tv_item_footer_load_more);
            pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_item_footer_loading);
        }
    }

    public ReplyAdpter(Context context,List<ReplyMember> members, ContentMainHolder mainHolder,RecyclerView recyclerView) {
        this.context = context;
        this.members = members;
        this.recyclerView = recyclerView;
        this.mainHolder = mainHolder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context1 = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context1);
        if (viewType == TYPE_CONTENT) {
            return new ContentViewHolder(layoutInflater.inflate(R.layout.replie_list, parent, false));
        } else if (viewType == TYPE_FOOTER) {
            return new FooterViewHolder(layoutInflater.inflate(R.layout.list_footer, parent, false));
        } else if (viewType == TYPE_FIRST) {
            return new FirstViewHolder(layoutInflater.inflate(R.layout.content_main, parent, false));
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == TYPE_CONTENT) {
            if (!members.isEmpty()) {
                ReplyMember member = members.get(position);
                ((ContentViewHolder)holder).textView.setText(member.getReply());
                ((ContentViewHolder)holder).member.setText(member.getName());
                ((ContentViewHolder)holder).time.setText(member.getTime());
                ((ContentViewHolder)holder).member_image.setImageBitmap(member.getBitmap());
                ((ContentViewHolder)holder).number.setText("第" + position + "楼");
            }
        } else if (type == TYPE_FOOTER) {
            pbLoading = ((FooterViewHolder) holder).pbLoading;
            tvLoadMore = ((FooterViewHolder) holder).tvLoadMore;
        } else if (type == TYPE_FIRST) {
            ((FirstViewHolder) holder).textView.setText(mainHolder.getTitle());
            ((FirstViewHolder) holder).host_iamge.setImageBitmap(mainHolder.getBitmap());
            ((FirstViewHolder) holder).content_main.setText(mainHolder.getContent());
            ((FirstViewHolder)holder).theme_name.setText(mainHolder.getTheme_na());
            ((FirstViewHolder)holder).theme_name.setOnClickListener(mclicklistenner);
            String[] temp = mainHolder.getInformation();
            ((FirstViewHolder) holder).host_name.setText(temp[0]);
            StringBuilder builder = new StringBuilder();
            for (int i=1;i<temp.length;i++) {
                builder.append(temp[i] + "  ");
            }
            ((FirstViewHolder) holder).post_information.setText(builder.toString());
        }
    }

    @Override
    public int getItemCount() {
        return members.size()+1;

    }
    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else if (position == 0) {

            return TYPE_FIRST;
        } else {
            return TYPE_CONTENT;
        }
    }

    public void showLoading() {
        if (pbLoading != null && tvLoadMore != null) {
            pbLoading.setVisibility(View.VISIBLE);
            tvLoadMore.setVisibility(View.GONE);
        }
    }
    /**
     * 显示上拉加载的文字，当数据加载完毕，调用该方法，隐藏进度条，显示“上拉加载更多”
     */
    public void showLoadMore() {
        if (pbLoading != null && tvLoadMore != null) {
            pbLoading.setVisibility(View.GONE);
            tvLoadMore.setVisibility(View.VISIBLE);
        }
    }

    class myclicklistenner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent5 = new Intent(context, NodeActivity.class);
            intent5.putExtra("linkname", mainHolder.getTopic());
            context.startActivity(intent5);
        }
    }
}
