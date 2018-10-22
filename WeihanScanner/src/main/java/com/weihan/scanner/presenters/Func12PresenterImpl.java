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
import com.weihan.scanner.entities.WarehouseTransferMultiAddon;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func12ModelImpl;
import com.weihan.scanner.mvpviews.Func12MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;
import com.weihan.scanner.utils.TextUtils;
import com.weihan.scanner.utils.ViewHelper;

import java.util.List;
import java.util.Random;

public class Func12PresenterImpl extends BasePresenter<Func12MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();

    private GenericOdataCallback<BinContentInfo> callback1 = new GenericOdataCallback<BinContentInfo>() {
        @Override
        public void onDataAvailable(List<BinContentInfo> datas) {

            getView().fillRecycler(Func12ModelImpl.createPolymorphList(datas));
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };

    private AllFuncModelImpl.PolyChangeListener<WarehouseTransferMultiAddon, BinContentInfo> listener
            = new AllFuncModelImpl.PolyChangeListener<WarehouseTransferMultiAddon, BinContentInfo>() {

        private Random random = new Random();

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            getView().notifyAdapter();
            allFuncModel.buildingResultMsg(isFinished, msg);
        }

        @Override
        public void goCommitting(Polymorph<WarehouseTransferMultiAddon, BinContentInfo> poly) {
            WarehouseTransferMultiAddon addon = poly.getAddonEntity();
            addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setSubmitDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setLineNo(Math.abs(random.nextInt()));
            ApiTool.addWhseTransferMultiFromBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, listener));
        }

    };

    public void acquireDatas(String itemNo, String binCode) {

        if (itemNo.isEmpty() || binCode.isEmpty()) {
            ToastUtils.showToastLong("物料条码和从仓库条码不能为空");
            return;
        }
        String filter = "Item_No eq '" + itemNo + "' and Bin_Code eq '" + binCode + "'";

        ApiTool.callBinContent(filter, callback1);
    }


    public void submitDatas(List<Polymorph<WarehouseTransferMultiAddon, BinContentInfo>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }

    public static class BinContentListAdapter extends BaseQuickAdapter<Polymorph<WarehouseTransferMultiAddon, BinContentInfo>, BaseViewHolder> {

        public BinContentListAdapter(@Nullable List<Polymorph<WarehouseTransferMultiAddon, BinContentInfo>> datas) {
            super(R.layout.item_func12, datas);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<WarehouseTransferMultiAddon, BinContentInfo> item) {
            helper.setText(R.id.tv_item_func12_mcn, item.getInfoEntity().getItem_No());
            helper.setText(R.id.tv_item_func12_from_binname, item.getInfoEntity().getLocation_Code());
            helper.setText(R.id.tv_item_func12_from_bincode, item.getInfoEntity().getBin_Code());
            helper.setText(R.id.tv_item_func12_quantity0, item.getInfoEntity().getQuantity_Base());
            helper.setText(R.id.et_item_func12_quantity1, item.getAddonEntity().getQuantity());
            EditText et = helper.getView(R.id.et_item_func12_quantity1);
            ViewHelper.setIntOnlyInputFilterForEditText(et);

            final Polymorph<WarehouseTransferMultiAddon, BinContentInfo> polymorphItem = item;

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

            helper.addOnClickListener(R.id.tv_item_func12_delete);
            switch (item.getState()) {
                case FAILURE:
                    helper.setBackgroundColor(R.id.view_item_func12_state, Color.RED);
                    helper.setTextColor(R.id.tv_item_func12_state, Color.RED);
                    helper.setText(R.id.tv_item_func12_state, R.string.text_commit_fail);
                    break;
                case COMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func12_state, Color.GREEN);
                    helper.setTextColor(R.id.tv_item_func12_state, Color.GREEN);
                    helper.setText(R.id.tv_item_func12_state, R.string.text_committed);
                    break;
                case UNCOMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func12_state, Color.argb(0Xff, 0xff, 0x90, 0x40));
                    helper.setTextColor(R.id.tv_item_func12_state, Color.WHITE);
                    helper.setText(R.id.tv_item_func12_state, "");
                    break;
            }
        }
    }
}
