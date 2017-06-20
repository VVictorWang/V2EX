package com.example.victor.v2ex.Member;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.victor.v2ex.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by victor on 2017/4/2.
 */

public class MemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private MemberTheme memberTheme;
    private List<ThemeInfor> themeInfors = new ArrayList<>();
    private RecyclerView recyclerView;
    private static int TYPE_FIR = 0;
    private static int TYPE_CON = 1;

    public static class First extends RecyclerView.ViewHolder {
        private CircleImageView imageView;
        private TextView member_name, member_infor;

        public First(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.member_image_infor);
            member_name = (TextView) itemView.findViewById(R.id.member_name_infor);
            member_infor = (TextView) itemView.findViewById(R.id.member_infor_main);
        }
    }

    public static class ConViewHolder extends RecyclerView.ViewHolder {
        private TextView theme_title, theme_infor;

        public ConViewHolder(View itemView) {
            super(itemView);
            theme_title = (TextView) itemView.findViewById(R.id.theme_name_infor);
            theme_infor = (TextView) itemView.findViewById(R.id.theme_information);
        }
    }

    public MemberAdapter(Context context, MemberTheme memberTheme,List<ThemeInfor> themeInfors,RecyclerView recyclerView) {
        this.context = context;
        this.memberTheme = memberTheme;
        this.themeInfors = themeInfors;
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context1 = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context1);
        if (viewType == TYPE_FIR) {
            return new First(layoutInflater.inflate(R.layout.member_infor, parent, false));
        } else if (viewType == TYPE_CON) {
            return new ConViewHolder(layoutInflater.inflate(R.layout.member_content, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == TYPE_FIR) {
            ((First) holder).imageView.setImageBitmap(memberTheme.getMember_image());
            ((First) holder).member_name.setText(memberTheme.getMember_name());
            ((First) holder).member_infor.setText(memberTheme.getMember_infor());
        } else if (type == TYPE_CON) {
            ThemeInfor themeInfor = themeInfors.get(position);
            ((ConViewHolder) holder).theme_title.setText(themeInfor.getTheme_title());
            ((ConViewHolder) holder).theme_infor.setText(themeInfor.getTheme_infor());
        }
    }

    @Override
    public int getItemCount() {
        return themeInfors.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_FIR;
        } else {
            return TYPE_CON;
        }
    }
}
