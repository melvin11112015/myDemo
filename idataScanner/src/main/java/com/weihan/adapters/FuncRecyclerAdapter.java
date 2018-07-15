package com.weihan.adapters;

import android.graphics.Color;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weihan.R;

import java.util.List;
import java.util.Map;

public class FuncRecyclerAdapter extends BaseQuickAdapter<Map<String, Object>, BaseViewHolder> {

    public static final String KEY_MAP_CODE = "code";
    public static final String KEY_MAP_NUM = "num";
    public static final String KEY_MAP_CHECKED = "checked";

    public FuncRecyclerAdapter(@Nullable List<Map<String, Object>> data) {
        super(R.layout.item_func_line, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Map<String, Object> item) {
        helper.setText(R.id.tv_item_func0_column0, (String) item.get(KEY_MAP_CODE));
        helper.setText(R.id.tv_item_func0_column1, (String) item.get(KEY_MAP_NUM));
        if (item.containsKey(KEY_MAP_CHECKED))
            helper.setTextColor(R.id.tv_item_func0_column0, (boolean) item.get(KEY_MAP_CHECKED) ? Color.GREEN : Color.RED);
    }
}
