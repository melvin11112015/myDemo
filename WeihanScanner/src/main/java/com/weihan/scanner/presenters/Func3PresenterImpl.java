package com.weihan.scanner.presenters;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehousePutAwayAddon;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func3ModelImpl;
import com.weihan.scanner.mvpviews.Func3MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.utils.TextUtils;

import java.util.List;

public class Func3PresenterImpl extends BasePresenter<Func3MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();
    private AllFuncModelImpl.PolyChangeListener<WarehousePutAwayAddon, BinContentInfo> listener
            = new AllFuncModelImpl.PolyChangeListener<WarehousePutAwayAddon, BinContentInfo>() {

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            allFuncModel.onAllCommitted(isFinished, msg);
            getView().notifyAdapter();
        }

        @Override
        public void goCommitting(Polymorph<WarehousePutAwayAddon, BinContentInfo> poly) {
            WarehousePutAwayAddon addon = poly.getAddonEntity();
            addon.setSubmitDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setLineNo(AllFuncModelImpl.getTempInt());
            ApiTool.addWarehousePutAwayBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, this));
        }

    };

    public void attemptToAddPoly(List<Polymorph<WarehousePutAwayAddon, BinContentInfo>> datas, String WBcode, String itemno) {

        String bincode = AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_BIN);
        String locationCode = AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_LOCATION);

        for (Polymorph<WarehousePutAwayAddon, BinContentInfo> polymorph : datas)
            if (polymorph.getAddonEntity().getBinCode().equals(bincode) &&
                    polymorph.getAddonEntity().getLocationCode().equals(locationCode) &&
                    polymorph.getAddonEntity().getItemNo().equals(itemno))
                return;

        datas.add(Func3ModelImpl.createPoly(itemno, bincode, locationCode, "1"));

        getView().notifyAdapter();
    }

    public void submitDatas(List<Polymorph<WarehousePutAwayAddon, BinContentInfo>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }

    public static class WarehousePutAwayListAdapter extends BaseQuickAdapter<Polymorph<WarehousePutAwayAddon, BinContentInfo>, BaseViewHolder> {

        public WarehousePutAwayListAdapter(@Nullable List<Polymorph<WarehousePutAwayAddon, BinContentInfo>> datas) {
            super(R.layout.item_func3, datas);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<WarehousePutAwayAddon, BinContentInfo> item) {
            helper.setText(R.id.tv_item_func3_mcn, item.getAddonEntity().getItemNo());
            helper.setText(R.id.tv_item_func3_to_location, item.getAddonEntity().getLocationCode());
            helper.setText(R.id.tv_item_func3_to_bincode, item.getAddonEntity().getBinCode());
            helper.setText(R.id.et_item_func3_quantity1, item.getAddonEntity().getQuantity());
            EditText et = helper.getView(R.id.et_item_func3_quantity1);

            final Polymorph<WarehousePutAwayAddon, BinContentInfo> polymorphItem = item;

            View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isFocused) {
                    if (!isFocused) {
                        String s = ((EditText) view).getText().toString();
                        if (TextUtils.isNumeric(s)) {
                            polymorphItem.getAddonEntity().setQuantity(s);
                        }
                    }
                }
            };
            et.setOnFocusChangeListener(focusChangeListener);

            helper.addOnClickListener(R.id.tv_item_func3_delete);
            switch (item.getState()) {
                case FAILURE:
                    helper.setBackgroundColor(R.id.view_item_func3_state, Color.RED);
                    helper.setTextColor(R.id.tv_item_func3_state, Color.RED);
                    helper.setText(R.id.tv_item_func3_state, R.string.text_commit_fail);
                    et.setEnabled(true);
                    break;
                case COMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func3_state, Color.GREEN);
                    helper.setTextColor(R.id.tv_item_func3_state, Color.GREEN);
                    helper.setText(R.id.tv_item_func3_state, R.string.text_committed);
                    et.setEnabled(false);
                    break;
                case UNCOMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func3_state, Color.argb(0Xff, 0xff, 0x90, 0x40));
                    helper.setTextColor(R.id.tv_item_func3_state, Color.WHITE);
                    helper.setText(R.id.tv_item_func3_state, "");
                    et.setEnabled(true);
                    break;
            }
        }
    }

}
