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
import com.weihan.scanner.entities.PhysicalInvtCheckAddon;
import com.weihan.scanner.entities.PhysicalInvtInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func5ModelImpl;
import com.weihan.scanner.mvpviews.Func5MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;
import com.weihan.scanner.utils.TextUtils;

import java.util.List;

public class Func5PresenterImpl extends BasePresenter<Func5MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();

    private GenericOdataCallback<PhysicalInvtInfo> callback1 = new GenericOdataCallback<PhysicalInvtInfo>() {
        @Override
        public void onDataAvailable(List<PhysicalInvtInfo> datas) {
            if (datas.isEmpty()) {
                ToastUtils.show(R.string.toast_no_record);
                return;
            }
            getView().fillRecycler(Func5ModelImpl.createPolymorphList(datas));
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };

    private AllFuncModelImpl.PolyChangeListener<PhysicalInvtCheckAddon, PhysicalInvtInfo> listener
            = new AllFuncModelImpl.PolyChangeListener<PhysicalInvtCheckAddon, PhysicalInvtInfo>() {

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            allFuncModel.onAllCommitted(isFinished, msg);
            getView().notifyAdapter();
        }

        @Override
        public void goCommitting(Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo> poly) {
            PhysicalInvtCheckAddon addon = poly.getAddonEntity();
            addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
            ApiTool.addPhysicalInvtCheckBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, this));
        }

    };

    public void acquireDatas(String itemNo, String WBcode) {

        if (itemNo.isEmpty() || WBcode.isEmpty()) {
            ToastUtils.showToastLong("不能为空");
            return;
        }

        String locationCode = AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_LOCATION);
        String bincode = AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_BIN);

        String filter = "Item_No eq '" +
                itemNo +
                "' and Journal_Batch_Name eq '" +
                locationCode +
                "' and Location_Code eq '" +
                locationCode +
                "' and Bin_Code eq '" +
                bincode +
                "'";

        ApiTool.callPhysicalInvtInfoList(filter, callback1);
    }

    public void submitDatas(List<Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }

    public static class PhysicalInvtAdapter extends BaseQuickAdapter<Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo>, BaseViewHolder> {

        public PhysicalInvtAdapter(@Nullable List<Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo>> datas) {
            super(R.layout.item_func5, datas);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo> item) {
            helper.setText(R.id.tv_item_func5_itemno, item.getAddonEntity().getItemNo());
            helper.setText(R.id.tv_item_func5_location, item.getAddonEntity().getLocationCode());
            helper.setText(R.id.tv_item_func5_bin, item.getAddonEntity().getBinCode());
            helper.setText(R.id.tv_item_func5_count0, item.getInfoEntity().getQty_Phys_Inventory());
            helper.setText(R.id.et_item_func5_count1, item.getAddonEntity().getQuantity());
            EditText et = helper.getView(R.id.et_item_func5_count1);

            final Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo> polymorphItem = item;

            View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isFocused) {
                    if (!isFocused) {
                        //((EditText)view).setText( polymorphItem.getAddonEntity().getQuantity());
                        String s = ((EditText) view).getText().toString();
                        if (TextUtils.isNumeric(s) && Double.valueOf(s) <= Double.valueOf(polymorphItem.getInfoEntity().getQty_Phys_Inventory())) {
                            polymorphItem.getAddonEntity().setQuantity(s);
                        } else {
                            ((EditText) view).setText(polymorphItem.getAddonEntity().getQuantity());
                            ToastUtils.show(R.string.toast_reach_upper_limit);
                        }
                    }
                }
            };
            et.setOnFocusChangeListener(focusChangeListener);

            helper.addOnClickListener(R.id.tv_item_func5_delete);
            switch (item.getState()) {
                case FAILURE:
                    helper.setBackgroundColor(R.id.view_item_func5_state, Color.RED);
                    helper.setTextColor(R.id.tv_item_func5_state, Color.RED);
                    helper.setText(R.id.tv_item_func5_state, R.string.text_commit_fail);
                    et.setEnabled(true);
                    break;
                case COMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func5_state, Color.GREEN);
                    helper.setTextColor(R.id.tv_item_func5_state, Color.GREEN);
                    helper.setText(R.id.tv_item_func5_state, R.string.text_committed);
                    et.setEnabled(false);
                    break;
                case UNCOMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func5_state, Color.argb(0Xff, 0xff, 0x90, 0x40));
                    helper.setTextColor(R.id.tv_item_func5_state, Color.WHITE);
                    helper.setText(R.id.tv_item_func5_state, "");
                    et.setEnabled(true);
                    break;
            }
        }
    }

}
