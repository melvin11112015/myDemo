package com.weihan.ligong.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.weihan.ligong.R;


public class AdapterHelper {
    public static void setAdapterEmpty(Context context, BaseQuickAdapter adapter) {
        View view = LayoutInflater.from(context).inflate(R.layout.item2_tv, null);
        TextView tv = view.findViewById(R.id.tv2_item);
        tv.setText("没有任何记录");
        adapter.setEmptyView(view);
    }
}
