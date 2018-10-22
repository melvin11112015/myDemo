package com.weihan.scanner.presenters;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.ToastUtils;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.OutstandingSalesLineInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseShipmentAddon;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func7ModelImpl;
import com.weihan.scanner.mvpviews.Func7MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;
import com.weihan.scanner.utils.TextUtils;

import java.util.List;

public class Func7PresenterImpl extends BasePresenter<Func7MvpView> {

    protected AllFuncModelImpl allFuncModel = new AllFuncModelImpl();
    protected AllFuncModelImpl.PolyChangeListener<WarehouseShipmentAddon, OutstandingSalesLineInfo> listener;
    private GenericOdataCallback<OutstandingSalesLineInfo> callback1 = new GenericOdataCallback<OutstandingSalesLineInfo>() {
        @Override
        public void onDataAvailable(List<OutstandingSalesLineInfo> datas) {
            getView().fillRecycler(Func7ModelImpl.createPolymorphList(datas));
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };

    public void acquireDatas(String lineCode) {

        if (lineCode.isEmpty()) {
            ToastUtils.showToastLong("不能为空");
            return;
        }
        String filter = "Document_No eq '" + lineCode + "'";

        ApiTool.callOutstandingSalesLineList(filter, callback1);
    }

    public void submitDatas(List<Polymorph<WarehouseShipmentAddon, OutstandingSalesLineInfo>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        listener
                = new AllFuncModelImpl.PolyChangeListener<WarehouseShipmentAddon, OutstandingSalesLineInfo>() {

            @Override
            public void onPolyChanged(boolean isFinished, String msg) {
                getView().notifyAdapter();
                allFuncModel.buildingResultMsg(isFinished, msg);
            }

            @Override
            public void goCommitting(Polymorph<WarehouseShipmentAddon, OutstandingSalesLineInfo> poly) {
                WarehouseShipmentAddon addon = poly.getAddonEntity();
                addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
                ApiTool.addWarehouseShipmentBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, listener));
            }

        };
        allFuncModel.processList(datas, listener);
    }

    public static class OutstandingSalesLineAdapter extends BaseQuickAdapter<Polymorph<WarehouseShipmentAddon, OutstandingSalesLineInfo>, BaseViewHolder> {

        InputFilter[] filters = new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                if (!TextUtils.isIntString(charSequence.toString()))
                    return "";
                else
                    return null;
            }
        }};


        public OutstandingSalesLineAdapter(@Nullable List<Polymorph<WarehouseShipmentAddon, OutstandingSalesLineInfo>> datas) {
            super(R.layout.item_func7, datas);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<WarehouseShipmentAddon, OutstandingSalesLineInfo> item) {
            helper.setText(R.id.tv_item_func7_mcn, item.getInfoEntity().getNo());
            helper.setText(R.id.tv_item_func7_name, item.getInfoEntity().getDescription());
            helper.setText(R.id.tv_item_func7_count0, item.getInfoEntity().getOutstanding_Quantity());
            helper.setText(R.id.et_item_func7_count1, item.getAddonEntity().getQuantity());
            EditText et = helper.getView(R.id.et_item_func7_count1);

            et.setFilters(filters);

            final Polymorph<WarehouseShipmentAddon, OutstandingSalesLineInfo> polymorphItem = item;

            View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isFocused) {
                    if (!isFocused) {
                        //((EditText)view).setText( polymorphItem.getAddonEntity().getQuantity());
                        String s = ((EditText) view).getText().toString();
                        if (TextUtils.isIntString(s) && Integer.valueOf(s) <= Integer.valueOf(polymorphItem.getInfoEntity().getOutstanding_Quantity())) {
                            polymorphItem.getAddonEntity().setQuantity(s);
                        } else {
                            ((EditText) view).setText(polymorphItem.getAddonEntity().getQuantity());
                            ToastUtils.show(R.string.toast_reach_upper_limit);
                        }
                    }
                }
            };
            et.setOnFocusChangeListener(focusChangeListener);

            helper.addOnClickListener(R.id.tv_item_func7_delete);
            switch (item.getState()) {
                case FAILURE:
                    helper.setBackgroundColor(R.id.view_item_func7_state, Color.RED);
                    helper.setTextColor(R.id.tv_item_func7_state, Color.RED);
                    helper.setText(R.id.tv_item_func7_state, R.string.text_commit_fail);
                    break;
                case COMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func7_state, Color.GREEN);
                    helper.setTextColor(R.id.tv_item_func7_state, Color.GREEN);
                    helper.setText(R.id.tv_item_func7_state, R.string.text_committed);
                    break;
                case UNCOMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func7_state, Color.argb(0Xff, 0xff, 0x90, 0x40));
                    helper.setTextColor(R.id.tv_item_func7_state, Color.WHITE);
                    helper.setText(R.id.tv_item_func7_state, "");
                    break;
            }
        }
    }

}
