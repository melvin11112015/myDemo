package com.weihan.scanner.presenters;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.ToastUtils;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehousePutAwayAddon;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func3ModelImpl;
import com.weihan.scanner.mvpviews.Func3MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;
import com.weihan.scanner.utils.TextUtils;
import com.weihan.scanner.utils.ViewHelper;

import java.util.List;
import java.util.Random;

public class Func3PresenterImpl extends BasePresenter<Func3MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();

    private String binCode = "";

    private GenericOdataCallback<BinContentInfo> callback1 = new GenericOdataCallback<BinContentInfo>() {
        @Override
        public void onDataAvailable(List<BinContentInfo> datas) {

            getView().fillRecycler(Func3ModelImpl.createPolymorphList(datas, binCode));
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };

    private AllFuncModelImpl.PolyChangeListener<WarehousePutAwayAddon, BinContentInfo> listener
            = new AllFuncModelImpl.PolyChangeListener<WarehousePutAwayAddon, BinContentInfo>() {

        private Random random = new Random();

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            getView().notifyAdapter();
            allFuncModel.buildingResultMsg(isFinished, msg);
        }

        @Override
        public void goCommitting(Polymorph<WarehousePutAwayAddon, BinContentInfo> poly) {
            WarehousePutAwayAddon addon = poly.getAddonEntity();
            addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setSubmitDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setLineNo(Math.abs(random.nextInt()));
            ApiTool.addWarehousePutAwayBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, listener));
        }

    };


    public void acquireDatas(String itemNo, String binCode) {

        if (itemNo.isEmpty() || binCode.isEmpty()) {
            ToastUtils.showToastLong("物料条码和从仓库条码不能为空");
            return;
        }
        final boolean isTemp_Receipt = false;//有bug,返回的值与传入的参数相反
        String filter = "Item_No eq '" + itemNo + "'";

        this.binCode = binCode;

        ApiTool.callBinContent(filter, callback1);
    }


    public void submitDatas(List<Polymorph<WarehousePutAwayAddon, BinContentInfo>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }

    public static class BinContentListAdapter extends BaseQuickAdapter<Polymorph<WarehousePutAwayAddon, BinContentInfo>, BaseViewHolder> {

        public BinContentListAdapter(@Nullable List<Polymorph<WarehousePutAwayAddon, BinContentInfo>> datas) {
            super(R.layout.item_func3, datas);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<WarehousePutAwayAddon, BinContentInfo> item) {
            helper.setText(R.id.tv_item_func3_mcn, item.getInfoEntity().getItem_No());
            helper.setText(R.id.tv_item_func3_to_binname, item.getAddonEntity().getLocationCode());
            helper.setText(R.id.tv_item_func3_to_bincode, item.getAddonEntity().getBinCode());
            helper.setText(R.id.tv_item_func3_quantity0, item.getInfoEntity().getQuantity_Base());
            helper.setText(R.id.et_item_func3_quantity1, item.getAddonEntity().getQuantity());
            EditText et = helper.getView(R.id.et_item_func3_quantity1);
            ViewHelper.setIntOnlyInputFilterForEditText(et);

            final Polymorph<WarehousePutAwayAddon, BinContentInfo> polymorphItem = item;

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

            helper.addOnClickListener(R.id.tv_item_func3_delete);
            switch (item.getState()) {
                case FAILURE:
                    helper.setBackgroundColor(R.id.view_item_func3_state, Color.RED);
                    helper.setTextColor(R.id.tv_item_func3_state, Color.RED);
                    helper.setText(R.id.tv_item_func3_state, R.string.text_commit_fail);
                    break;
                case COMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func3_state, Color.GREEN);
                    helper.setTextColor(R.id.tv_item_func3_state, Color.GREEN);
                    helper.setText(R.id.tv_item_func3_state, R.string.text_committed);
                    break;
                case UNCOMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func3_state, Color.argb(0Xff, 0xff, 0x90, 0x40));
                    helper.setTextColor(R.id.tv_item_func3_state, Color.WHITE);
                    helper.setText(R.id.tv_item_func3_state, "");
                    break;
            }
        }
    }

}
