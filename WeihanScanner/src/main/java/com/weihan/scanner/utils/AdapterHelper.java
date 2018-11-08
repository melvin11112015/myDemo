package com.weihan.scanner.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.weihan.scanner.R;


public class AdapterHelper {
    public static void setAdapterEmpty(Context context, BaseQuickAdapter adapter) {
        View view = LayoutInflater.from(context).inflate(R.layout.item2_tv, null);
        TextView tv = view.findViewById(R.id.tv2_item);
        tv.setText("没有任何记录");
        adapter.setEmptyView(view);
    }

    public static void addAdapterHeaderAndItemDivider(RecyclerView recyclerView, BaseQuickAdapter adapter, @LayoutRes int layoutResId) {
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        View headerview = LayoutInflater.from(recyclerView.getContext()).inflate(layoutResId, null);
        adapter.addHeaderView(headerview);
    }

    public static void initDraggableAdapter(RecyclerView recyclerView, BaseItemDraggableAdapter adapter) {
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        adapter.enableSwipeItem();
        adapter.setOnItemSwipeListener(new OnItemSwipeListener() {

                                           @Override
                                           public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

                                           }

                                           @Override
                                           public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {

                                           }

                                           @Override
                                           public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {

                                           }

                                           @Override
                                           public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                                               canvas.save();
                                               TextPaint textPaint = new TextPaint();
                                               textPaint.setTextSize(25);
                                               textPaint.setColor(Color.RED);
                                               textPaint.setAntiAlias(true);
                                               canvas.drawText(viewHolder.itemView.getContext().getString(R.string.canvas_swipe_remove), 10, viewHolder.itemView.getHeight() * 2.0f / 3.0f, textPaint);
                                               canvas.restore();
                                           }
                                       }
        );
    }
}

