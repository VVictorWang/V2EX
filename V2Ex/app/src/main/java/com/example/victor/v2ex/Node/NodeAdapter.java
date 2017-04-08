package com.example.victor.v2ex.Node;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.victor.v2ex.Containers.Member;
import com.example.victor.v2ex.Containers.Node;
import com.example.victor.v2ex.Containers.Theme;
import com.example.victor.v2ex.R;
import com.example.victor.v2ex.Reply.ContentActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Victor on 2017/3/25.
 */

public class NodeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ProgressBar pbLoading;
    private TextView tvLoadMore;
    private List<Theme> themes=new ArrayList<>();
    private List<Bitmap> bitmaps = new ArrayList<>();
    public View.OnClickListener mListener = new myClickListener();
    private RecyclerView mRecyclerview;
    private NodeContenet contenet;
    private static final int TYPE_CONTENT = 0;
    private static final int TYPE_LIST = 1;
    private static int TYPE_FOOT = 2;
    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLoadMore;
        private ProgressBar pbLoading;
        public FooterViewHolder(View itemView) {
            super(itemView);
            tvLoadMore = (TextView) itemView.findViewById(R.id.tv_item_footer_load_more);
            pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_item_footer_loading);
        }
    }

    public static class ContentHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private CircleImageView imageView;
        private TextView node_title;
        private TextView username;


        public ContentHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.title_text);
            imageView = (CircleImageView) itemView.findViewById(R.id.image_show);
            node_title = (TextView) itemView.findViewById(R.id.node_title);
            username = (TextView) itemView.findViewById(R.id.username);
        }
    }

    public static class FirstHolder extends RecyclerView.ViewHolder {
        private TextView node_title,node_description,theme_number;
        private CircleImageView node_image;

        public FirstHolder(View itemView) {
            super(itemView);
            node_title = (TextView) itemView.findViewById(R.id.node_title_show);
            node_image = (CircleImageView) itemView.findViewById(R.id.node_image);
            node_description = (TextView) itemView.findViewById(R.id.node_description);
            theme_number = (TextView) itemView.findViewById(R.id.theme_number);
        }
    }

    public NodeAdapter(Context context, List<Theme> themes, List<Bitmap> bitmaps, RecyclerView mRecyclerview, NodeContenet contenet) {
        this.context = context;
        this.themes = themes;
        this.bitmaps = bitmaps;
        this.mRecyclerview = mRecyclerview;
        this.contenet = contenet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context2 = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context2);
        if (viewType == TYPE_CONTENT) {
            return new NodeAdapter.FirstHolder(layoutInflater.inflate(R.layout.node_content, parent, false));
        } else if (viewType == TYPE_LIST) {
            View themeView = layoutInflater.inflate(R.layout.list_item, parent, false);
            ContentHolder viewHolder = new ContentHolder(themeView);
            themeView.setOnClickListener(mListener);
            return viewHolder;
        } else if (viewType == TYPE_FOOT) {
            return new NodeAdapter.FooterViewHolder(layoutInflater.inflate(R.layout.list_footer, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == TYPE_LIST) {
            if (!themes.isEmpty()) {
                Theme theme = themes.get(position-1);
                Node node = theme.getNode();
                Member member = theme.getMember();
                Bitmap bitmap = bitmaps.get(position-1);
                ((ContentHolder)holder).imageView.setImageBitmap(bitmap);
                ((ContentHolder)holder).node_title.setText(node.getTitle());
                ((ContentHolder)holder).username.setText(member.getUsername());
//            Log.e("dsef", member.getAvatar_normal());
//            Log.e("this", member.getId()+"  "+member.getUsername());
//            holder.imageView.setImageBitmap(member.getAvatar_normal());
                ((ContentHolder) holder).textView.setText(theme.getTitle());
            }
        } else if (type == TYPE_CONTENT) {
            ((FirstHolder) holder).node_title.setText(contenet.getNode_ti());
            ((FirstHolder) holder).node_image.setImageBitmap(contenet.getBitmap());
            ((FirstHolder) holder).node_description.setText(contenet.getNode_des());
            ((FirstHolder) holder).theme_number.setText(contenet.getTheme_num());
        } else if (type == TYPE_FOOT) {
            pbLoading = ((NodeAdapter.FooterViewHolder) holder).pbLoading;
            tvLoadMore = ((NodeAdapter.FooterViewHolder) holder).tvLoadMore;
        }
    }

    @Override
    public int getItemCount() {
        return themes.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_CONTENT;
        } else if (position + 1 == getItemCount()) {
            return TYPE_FOOT;
        } else {
            return TYPE_LIST;
        }
    }

    class myClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int postion = mRecyclerview.getChildAdapterPosition(v);
            Theme theme = themes.get(postion-1);
            String link = theme.getUrl();
            Intent intent = new Intent(context, ContentActivity.class);
            intent.putExtra("link", link);
            context.startActivity(intent);
        }
    }
    public void showiiLoadMore() {
        if (pbLoading != null && tvLoadMore != null) {
            pbLoading.setVisibility(View.GONE);
            tvLoadMore.setVisibility(View.VISIBLE);
        }
    }public void showiiLoading() {
        if (pbLoading != null && tvLoadMore != null) {
            pbLoading.setVisibility(View.VISIBLE);
            tvLoadMore.setVisibility(View.GONE);
        }
    }

}
