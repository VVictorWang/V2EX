package com.example.victor.v2ex.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Victor on 2017/3/23.
 */

public abstract class ScrollClass extends RecyclerView.OnScrollListener {
    private LinearLayoutManager layoutManager;

    private int totalItemCount;
    private int previousTotal = 0;
    private int visibleItemCount;
    private int firstVIsibileItem;
    private boolean loading = true;
    private int currentpage = 1;

    public ScrollClass(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = layoutManager.getItemCount();
        firstVIsibileItem = layoutManager.findFirstVisibleItemPosition();


        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= firstVIsibileItem) {
            currentpage++;
            onLoad(currentpage);
            loading = true;
        }

    }

    public abstract void onLoad(int currentpage);
}
