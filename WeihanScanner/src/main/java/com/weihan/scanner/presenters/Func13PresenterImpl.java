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
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseTransferMultiAddon;
import com.weihan.scanner.entities.WhseTransferMultiInfo;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func13ModelImpl;
import com.weihan.scanner.mvpviews.Func13MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;
import com.weihan.scanner.utils.TextUtils;
import com.weihan.scanner.utils.ViewHelper;

import java.util.List;
import java.util.Random;

public class Func13PresenterImpl extends BasePresenter<Func13MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();
    private AllFuncModelImpl.PolyChangeListener<WarehouseTransferMultiAddon, WhseTransferMultiInfo> listener
            = new AllFuncModelImpl.PolyChangeListener<WarehouseTransferMultiAddon, WhseTransferMultiInfo>() {

        private Random random = new Random();

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            getView().notifyAdapter();
            allFuncModel.buildingResultMsg(isFinished, msg);
        }

        @Override
        public void goCommitting(Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo> poly) {
            WarehouseTransferMultiAddon addon = poly.getAddonEntity();
            addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setSubmitDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setLineNo(Math.abs(random.nextInt()));
            ApiTool.addWhseTransferMultiFromBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, listener));
        }

    };
    private String binCode;
    private GenericOdataCallback<WhseTransferMultiInfo> callback1 = new GenericOdataCallback<WhseTransferMultiInfo>() {
        @Override
        public void onDataAvailable(List<WhseTransferMultiInfo> datas) {

            getView().fillRecycler(Func13ModelImpl.createPolymorphList(datas, binCode));
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };

    public void acquireDatas(String itemNo, String binCode) {

        if (itemNo.isEmpty() || binCode.isEmpty()) {
            ToastUtils.showToastLong("物料条码和从仓库条码不能为空");
            return;
        }
        String filter = "ItemNo eq '" + itemNo + "'";

        this.binCode = binCode;

        ApiTool.callWhseTransferMultiList(filter, callback1);
    }


    public void submitDatas(List<Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }

    public static class WhseTransferMultiListAdapter extends BaseQuickAdapter<Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo>, BaseViewHolder> {

        public WhseTransferMultiListAdapter(@Nullable List<Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo>> datas) {
            super(R.layout.item_func13, datas);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo> item) {
            helper.setText(R.id.tv_item_func13_mcn, item.getInfoEntity().getItemNo());
            helper.setText(R.id.tv_item_func13_to_binname, item.getAddonEntity().getToLocationCode());
            helper.setText(R.id.tv_item_func13_to_bincode, item.getAddonEntity().getToBinCode());
            helper.setText(R.id.tv_item_func13_quantity0, item.getInfoEntity().getQuantity());
            helper.setText(R.id.et_item_func13_quantity1, item.getAddonEntity().getQuantity());
            EditText et = helper.getView(R.id.et_item_func13_quantity1);
            ViewHelper.setIntOnlyInputFilterForEditText(et);

            final Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo> polymorphItem = item;

            View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isFocused) {
                    if (!isFocused) {
                        //((EditText)view).setText( polymorphItem.getAddonEntity().getQuantity());
                        String s = ((EditText) view).getText().toString();
                        if (TextUtils.isIntString(s) && Integer.valueOf(s) <= Integer.valueOf(polymorphItem.getInfoEntity().getQuantity())) {
                            polymorphItem.getAddonEntity().setQuantity(s);
                        } else {
                            ((EditText) view).setText(polymorphItem.getAddonEntity().getQuantity());
                            ToastUtils.show(R.string.toast_reach_upper_limit);
                        }
                    }
                }
            };
            et.setOnFocusChangeListener(focusChangeListener);

            helper.addOnClickListener(R.id.tv_item_func13_delete);
            switch (item.getState()) {
                case FAILURE:
                    helper.setBackgroundColor(R.id.view_item_func13_state, Color.RED);
                    helper.setTextColor(R.id.tv_item_func13_state, Color.RED);
                    helper.setText(R.id.tv_item_func13_state, R.string.text_commit_fail);
                    break;
                case COMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func13_state, Color.GREEN);
                    helper.setTextColor(R.id.tv_item_func13_state, Color.GREEN);
                    helper.setText(R.id.tv_item_func13_state, R.string.text_committed);
                    break;
                case UNCOMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func13_state, Color.argb(0Xff, 0xff, 0x90, 0x40));
                    helper.setTextColor(R.id.tv_item_func13_state, Color.WHITE);
                    helper.setText(R.id.tv_item_func13_state, "");
                    break;
            }
        }
    }

}
