package com.weihan.ligong.presenters;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.ToastUtils;
import com.weihan.ligong.BaseMVP.BasePresenter;
import com.weihan.ligong.R;
import com.weihan.ligong.entities.ConsumptionPickAddon;
import com.weihan.ligong.entities.InvPickingInfo;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.models.AllFuncModelImpl;
import com.weihan.ligong.models.Func1ModelImpl;
import com.weihan.ligong.mvpviews.Func1MvpView;
import com.weihan.ligong.net.ApiTool;
import com.weihan.ligong.net.GenericOdataCallback;
import com.weihan.ligong.utils.TextUtils;

import java.util.List;

public class Func1PresenterImpl extends BasePresenter<Func1MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();

    private GenericOdataCallback<InvPickingInfo> callback1 = new GenericOdataCallback<InvPickingInfo>() {
        @Override
        public void onDataAvailable(List<InvPickingInfo> datas) {
            getView().fillRecycler(Func1ModelImpl.createPolymorphList(datas));
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };

    private AllFuncModelImpl.PolyChangeListener<ConsumptionPickAddon, InvPickingInfo> listener
            = new AllFuncModelImpl.PolyChangeListener<ConsumptionPickAddon, InvPickingInfo>() {

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            getView().notifyAdapter();
            allFuncModel.buildingResultMsg(isFinished, msg);
        }

        @Override
        public void goCommitting(Polymorph<ConsumptionPickAddon, InvPickingInfo> poly) {
            ConsumptionPickAddon addon = poly.getAddonEntity();
            addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
            ApiTool.addConsumptionPickBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, listener));
        }

    };

    public void acquireDatas(String lineCode) {

        if (lineCode.isEmpty()) {
            ToastUtils.showToastLong("不能为空");
            return;
        }
        String filter = "Inv_Document_No eq '" + lineCode + "'";

        ApiTool.callInvPickingList(filter, callback1);
    }

    public void submitDatas(List<Polymorph<ConsumptionPickAddon, InvPickingInfo>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
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
