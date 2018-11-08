package com.weihan.scanner.presenters;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.ToastUtils;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.OutstandingPurchLineInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseReceiptAddon;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func0ModelImpl;
import com.weihan.scanner.mvpviews.Func0MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;
import com.weihan.scanner.utils.AdapterHelper;
import com.weihan.scanner.utils.TextUtils;

import java.util.List;

public class Func0PresenterImpl extends BasePresenter<Func0MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();

    private GenericOdataCallback<OutstandingPurchLineInfo> callback1 = new GenericOdataCallback<OutstandingPurchLineInfo>() {
        @Override
        public void onDataAvailable(List<OutstandingPurchLineInfo> datas) {
            if (datas.isEmpty()) {
                ToastUtils.show(R.string.toast_no_record);
                return;
            }
            getView().fillRecycler(Func0ModelImpl.createPolymorphList(datas));
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };

    private AllFuncModelImpl.PolyChangeListener<WarehouseReceiptAddon, OutstandingPurchLineInfo> listener
            = new AllFuncModelImpl.PolyChangeListener<WarehouseReceiptAddon, OutstandingPurchLineInfo>() {

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            allFuncModel.onAllCommitted(isFinished, msg);
            getView().notifyAdapter();
        }

        @Override
        public void goCommitting(Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo> poly) {
            WarehouseReceiptAddon addon = poly.getAddonEntity();
            ApiTool.addWarehouseReceipt(addon, allFuncModel.new AllFuncOdataCallback(poly, this));
        }

    };

    public void acquireDatas(String lineCode) {

        if (lineCode.isEmpty()) {
            ToastUtils.showToastLong("不能为空");
            return;
        }
        String filter = "Document_No eq '" + lineCode + "'";

        ApiTool.callPurchaseLine(filter, callback1);
    }

    public void submitDatas(List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }

    public static class PurchaseListAdapter extends BaseQuickAdapter<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>, BaseViewHolder> {

        public PurchaseListAdapter(@Nullable List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>> datas) {
            super(R.layout.item_func0, datas);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo> item) {
            helper.setText(R.id.tv_item_func0_mcn, item.getInfoEntity().getNo());
            helper.setText(R.id.tv_item_func0_name, item.getInfoEntity().getDescription());
            helper.setText(R.id.tv_item_func0_count0, item.getInfoEntity().getOutstanding_Quantity());
            helper.setText(R.id.et_item_func0_count1, item.getAddonEntity().getQuantity());
            EditText et = helper.getView(R.id.et_item_func0_count1);

            final Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo> polymorphItem = item;

            View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isFocused) {
                    if (!isFocused) {
                        String s = ((EditText) view).getText().toString();
                        if (TextUtils.isNumeric(s)
                                && Double.valueOf(s) <= Double.valueOf(polymorphItem.getInfoEntity().getOutstanding_Quantity())
                                ) {
                            polymorphItem.getAddonEntity().setQuantity(s);
                        } else {
                            ((EditText) view).setText(polymorphItem.getAddonEntity().getQuantity());
                            ToastUtils.show(R.string.toast_reach_upper_limit);
                        }
                    }
                }
            };
            et.setOnFocusChangeListener(focusChangeListener);

            helper.addOnClickListener(R.id.tv_item_func0_delete);
            switch (item.getState()) {
                case FAILURE:
                    helper.setBackgroundColor(R.id.view_item_func0_state, Color.RED);
                    helper.setTextColor(R.id.tv_item_func0_state, Color.RED);
                    helper.setText(R.id.tv_item_func0_state, R.string.text_commit_fail);
                    et.setEnabled(true);
                    break;
                case COMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func0_state, Color.GREEN);
                    helper.setTextColor(R.id.tv_item_func0_state, Color.GREEN);
                    helper.setText(R.id.tv_item_func0_state, R.string.text_committed);
                    et.setEnabled(false);
                    break;
                case UNCOMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func0_state, Color.argb(0Xff, 0xff, 0x90, 0x40));
                    helper.setTextColor(R.id.tv_item_func0_state, Color.WHITE);
                    helper.setText(R.id.tv_item_func0_state, "");
                    et.setEnabled(true);
                    break;
            }
        }
    }

    public static class PurchaseListAdapter2 extends BaseItemDraggableAdapter<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>, BaseViewHolder> {

        public PurchaseListAdapter2(@Nullable List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>> datas) {
            super(R.layout.item_func0b, datas);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);

            AdapterHelper.initDraggableAdapter(recyclerView, this);
            AdapterHelper.addAdapterHeaderAndItemDivider(recyclerView, this, R.layout.item_func0b_headitem);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo> item) {
            helper.setText(R.id.tv_item_func0_mcn, item.getInfoEntity().getNo());
            helper.setText(R.id.tv_item_func0_name, item.getInfoEntity().getDescription());
            helper.setText(R.id.tv_item_func0_count0, item.getInfoEntity().getOutstanding_Quantity());
            helper.setText(R.id.et_item_func0_count1, item.getAddonEntity().getQuantity());
            EditText et = helper.getView(R.id.et_item_func0_count1);

            final Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo> polymorphItem = item;

            View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isFocused) {
                    if (!isFocused) {
                        String s = ((EditText) view).getText().toString();
                        if (TextUtils.isNumeric(s)
                                && Double.valueOf(s) <= Double.valueOf(polymorphItem.getInfoEntity().getOutstanding_Quantity())
                                ) {
                            polymorphItem.getAddonEntity().setQuantity(s);
                        } else {
                            ((EditText) view).setText(polymorphItem.getAddonEntity().getQuantity());
                            ToastUtils.show(R.string.toast_reach_upper_limit);
                        }
                    }
                }
            };
            et.setOnFocusChangeListener(focusChangeListener);


            switch (item.getState()) {
                case FAILURE:
                    helper.setBackgroundColor(R.id.la_item_func0, Color.argb(0Xff, 0xff, 0xcc, 0xcc));
                    et.setEnabled(true);
                    break;
                case COMMITTED:
                    helper.setBackgroundColor(R.id.la_item_func0, Color.argb(0Xff, 0xcc, 0xff, 0xcc));
                    et.setEnabled(false);
                    break;
                case UNCOMMITTED:
                    helper.setBackgroundColor(R.id.la_item_func0, Color.argb(0Xff, 0xff, 0xff, 0xff));
                    et.setEnabled(true);
                    break;
            }

        }
    }

}
