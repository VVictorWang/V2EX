package com.example.victor.v2ex.Themes;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Victor on 2017/3/20.
 */

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {
    private Context context;
    private List<Theme> themes=new ArrayList<>();
    private List<Bitmap> bitmaps = new ArrayList<>();
    public View.OnClickListener mListener = new myClickListener();
    private RecyclerView mRecyclerview;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private CircleImageView imageView;
        private TextView node_title;
        private TextView username;


        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.title_text);
            imageView = (CircleImageView) itemView.findViewById(R.id.image_show);
            node_title = (TextView) itemView.findViewById(R.id.node_title);
            username = (TextView) itemView.findViewById(R.id.username);
        }
    }

    public ViewAdapter(Context context,RecyclerView recyclerView,List<Theme> themesa,List<Bitmap> bitmaps) {
        this.context = context;
        mRecyclerview = recyclerView;
        this.themes = themesa;
        this.bitmaps = bitmaps;
//        if (!HotActivity.themes.isEmpty()) {
//            for (Theme th : themesa) {
//                this.themes.add(th);
//            }
//        } else {
//            themes.add(new Theme("我搞了个 PHP 培训，针对 3 年以下的 PHPer，然而他们真的让我很失望！我现在怀疑自己做错了吗？", new Member("//v2ex.assets.uxengine.net/gravatar/899ac8bf73dffcc9a5da08b2ce9c8370?s=48&d=retro")));
//        }
    }

    @Override
    public ViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context1 = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context1);
        View themeView = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(themeView);
        themeView.setOnClickListener(mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!themes.isEmpty()) {
            Theme theme = themes.get(position);
            Node node = theme.getNode();
            Member member = theme.getMember();
            Bitmap bitmap = bitmaps.get(position);
            holder.imageView.setImageBitmap(bitmap);
            holder.node_title.setText(node.getTitle());
            holder.username.setText(member.getUsername());
//            Log.e("dsef", member.getAvatar_normal());
//            Log.e("this", member.getId()+"  "+member.getUsername());
//            holder.imageView.setImageBitmap(member.getAvatar_normal());
            holder.textView.setText(theme.getTitle());
        }

    }

    class myClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int postion = mRecyclerview.getChildAdapterPosition(v);
            Theme theme = themes.get(postion);
            String link = theme.getUrl();
            Intent intent = new Intent(context, ContentActivity.class);
            intent.putExtra("link", link);
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {

            return themes.size();
    }

}
