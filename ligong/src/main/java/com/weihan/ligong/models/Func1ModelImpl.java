package com.weihan.ligong.models;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.ToastUtils;
import com.weihan.ligong.BaseMVP.IBaseModel;
import com.weihan.ligong.LiGongApp;
import com.weihan.ligong.R;
import com.weihan.ligong.entities.ConsumptionPickAddon;
import com.weihan.ligong.entities.InvPickingInfo;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Func1ModelImpl implements IBaseModel {

    private Func1ModelImpl() {
    }

    public static List<Polymorph<ConsumptionPickAddon, InvPickingInfo>> createPolymorphList(List<InvPickingInfo> datas) {
        List<Polymorph<ConsumptionPickAddon, InvPickingInfo>> polymorphs = new ArrayList<>();
        for (InvPickingInfo info : datas) {

            String quantity = info.getQuantity_Base();
            if (quantity == null || quantity.isEmpty() || !TextUtils.isIntString(quantity))
                continue;
            if (Integer.valueOf(quantity) == 0) continue;

            ConsumptionPickAddon addon = new ConsumptionPickAddon();
            addon.setItemNo(info.getItem_No());
            addon.setTerminalID(LiGongApp.userInfo.getUserid());
            addon.setDocument_No(info.getInv_Document_No());
            addon.setLine_No(info.getLine_No());
            addon.setQuantity(quantity);
            polymorphs.add(new Polymorph<>(addon, info, Polymorph.State.UNCOMMITTED));
        }
        return polymorphs;
    }

    public static class InvPickingAdapter extends BaseQuickAdapter<Polymorph<ConsumptionPickAddon, InvPickingInfo>, BaseViewHolder> {

        InputFilter[] filters = new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                if (!TextUtils.isIntString(charSequence.toString()))
                    return "";
                else
                    return null;
            }
        }};


        public InvPickingAdapter(@Nullable List<Polymorph<ConsumptionPickAddon, InvPickingInfo>> datas) {
            super(R.layout.item_func1, datas);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<ConsumptionPickAddon, InvPickingInfo> item) {
            helper.setText(R.id.tv_item_func1_mcn, item.getInfoEntity().getItem_No());
            helper.setText(R.id.tv_item_func1_name, item.getInfoEntity().getDescription());
            helper.setText(R.id.tv_item_func1_count0, item.getInfoEntity().getQuantity_Base());
            helper.setText(R.id.et_item_func1_count1, item.getAddonEntity().getQuantity());
            EditText et = helper.getView(R.id.et_item_func1_count1);

            et.setFilters(filters);

            final Polymorph<ConsumptionPickAddon, InvPickingInfo> polymorphItem = item;

            View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isFocused) {
                    if (!isFocused) {
                        //((EditText)view).setText( polymorphItem.getAddonEntity().getQuantity());
                        String s = ((EditText) view).getText().toString();
                        if (TextUtils.isIntString(s) && Integer.valueOf(s) <= Integer.valueOf(polymorphItem.getInfoEntity().getQuantity_Base())) {
                            polymorphItem.getAddonEntity().setQuantity(s);
                        } else {
                            ((EditText) view).setText(polymorphItem.getAddonEntity().getQuantity());
                            ToastUtils.show(R.string.toast_reach_upper_limit);
                        }
                    }
                }
            };
            et.setOnFocusChangeListener(focusChangeListener);

            helper.addOnClickListener(R.id.tv_item_func1_delete);
            switch (item.getState()) {
                case FAILURE:
                    helper.setBackgroundColor(R.id.view_item_func1_state, Color.RED);
                    helper.setTextColor(R.id.tv_item_func1_state, Color.RED);
                    helper.setText(R.id.tv_item_func1_state, R.string.text_commit_fail);
                    break;
                case COMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func1_state, Color.GREEN);
                    helper.setTextColor(R.id.tv_item_func1_state, Color.GREEN);
                    helper.setText(R.id.tv_item_func1_state, R.string.text_committed);
                    break;
                case UNCOMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func1_state, Color.argb(0Xff, 0xff, 0x90, 0x40));
                    helper.setTextColor(R.id.tv_item_func1_state, Color.WHITE);
                    helper.setText(R.id.tv_item_func1_state, "");
                    break;
            }
        }
    }
}
