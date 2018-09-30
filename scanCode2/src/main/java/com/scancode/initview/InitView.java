
package com.scancode.initview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

import com.scancode.utils.ScreenUtils;
import com.scancode.utils.SettingsManager;
import com.scancode.widget.swipelistview.SwipeListView;

public class InitView {
    private static InitView initView;

    public static InitView instance() {
        if (initView == null) {
            initView = new InitView();
        }
        return initView;
    }

    /**
     * 设置下拉刷新控件颜色
     * 
     * @param swipeLayout
     */
    public void initSwipeRefreshLayout(SwipeRefreshLayout swipeLayout) {
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    /**
     * 初始化listview
     * 
     * @param mListView
     * @param context
     */
    public void initListView(SwipeListView mListView, Context context) {
        SettingsManager settings = SettingsManager.getInstance();
        mListView.setSwipeMode(SwipeListView.SWIPE_MODE_NONE);
        mListView.setSwipeActionLeft(settings.getSwipeActionLeft());
        mListView.setSwipeActionRight(settings.getSwipeActionRight());
        mListView.setOffsetLeft(ScreenUtils.convertDpToPixel(context,
                settings.getSwipeOffsetLeft()));
        mListView.setOffsetRight(ScreenUtils.convertDpToPixel(context,
                settings.getSwipeOffsetRight()));
        mListView.setAnimationTime(settings.getSwipeAnimationTime());
        mListView.setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress());
    }

}
