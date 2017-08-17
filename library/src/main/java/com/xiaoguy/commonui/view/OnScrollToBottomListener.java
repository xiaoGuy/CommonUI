package com.xiaoguy.commonui.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;

/**
 * Created by Xiaoguy on 2017/8/17.
 */

public abstract class OnScrollToBottomListener extends OnScrollListener {

    public abstract void onScrollToBottom();

    @Override
    public final void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState != RecyclerView.SCROLL_STATE_IDLE) {
            return;
        }
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
            int childCount = lm.getChildCount();
            if (childCount != 0 &&
                    lm.findLastVisibleItemPosition() == recyclerView.getAdapter().getItemCount() - 1 &&
                    lm.getChildAt(childCount - 1).getBottom() <= recyclerView.getBottom()) {

                onScrollToBottom();

            }
        }
    }
}
