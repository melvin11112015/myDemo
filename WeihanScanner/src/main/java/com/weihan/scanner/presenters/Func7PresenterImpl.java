package com.weihan.scanner.presenters;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.ToastUtils;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.OutstandingSalesLineInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseShipmentAddon;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func7ModelImpl;
import com.weihan.scanner.mvpviews.Func7MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;
import com.weihan.scanner.utils.AdapterHelper;
import com.weihan.scanner.utils.TextUtils;

import java.util.List;

public class Func7PresenterImpl extends BasePresenter<Func7MvpView> {


    protected AllFuncModelImpl allFuncModel = new AllFuncModelImpl();

    public void acquireDatas(String lineCode) {

        if (lineCode.isEmpty()) {
            ToastUtils.showToastLong("不能为空");
            return;
        }
        String filter = "Document_No eq '" + lineCode + "'";

        ApiTool.callOutstandingSalesLineList(filter, callback1);
    }

    protected AllFuncModelImpl.PolyChangeListener<WarehouseShipmentAddon, BinContentInfo> listener;
    private GenericOdataCallback<OutstandingSalesLineInfo> callback1 = new GenericOdataCallback<OutstandingSalesLineInfo>() {
        @Override
        public void onDataAvailable(List<OutstandingSalesLineInfo> datas) {
            if (datas.isEmpty()) {
                ToastUtils.show(R.string.toast_no_record);
                return;
            }
            getView().fillRecycler(Func7ModelImpl.createPolymorphList(datas));
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };

    public void submitDatas(List<Polymorph<List<Polymorph<WarehouseShipmentAddon, BinContentInfo>>, OutstandingSalesLineInfo>> polysdata) {

        for (Polymorph<List<Polymorph<WarehouseShipmentAddon, BinContentInfo>>, OutstandingSalesLineInfo> polys : polysdata) {

            List<Polymorph<WarehouseShipmentAddon, BinContentInfo>> datas = polys.getAddonEntity();

            if (!AllFuncModelImpl.checkEmptyList(datas)) return;

            listener = new AllFuncModelImpl.PolyChangeListener<WarehouseShipmentAddon, BinContentInfo>() {

                @Override
                public void onPolyChanged(boolean isFinished, String msg) {
                    allFuncModel.onAllCommitted(false, msg);
                    getView().notifyAdapter();
                }

                @Override
                public void goCommitting(Polymorph<WarehouseShipmentAddon, BinContentInfo> poly) {
                    WarehouseShipmentAddon addon = poly.getAddonEntity();
                    addon.setSubmitDate(AllFuncModelImpl.getCurrentDatetime());
                    addon.setWhseShptLineNo(AllFuncModelImpl.getTempInt());
                    ApiTool.addWarehouseShipmentBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, this));
                }

            };
            allFuncModel.processList(datas, listener);
        }
        allFuncModel.onAllCommitted(true, "");
    }

    public void attemptToAddPoly(List<Polymorph<WarehouseShipmentAddon, BinContentInfo>> datas, BinContentInfo info, OutstandingSalesLineInfo infoHeader) {

        for (Polymorph<WarehouseShipmentAddon, BinContentInfo> polymorph : datas)
            if (polymorph.getInfoEntity().getBin_Code().equals(info.getBin_Code()) &&
                    polymorph.getInfoEntity().getLocation_Code().equals(info.getLocation_Code()))
                return;

        datas.add(Func7ModelImpl.createPoly(info, infoHeader));

        getView().notifyAdapter();
    }

    public static class NewOutstandingSalesLineAdapter extends BaseQuickAdapter<Polymorph<List<Polymorph<WarehouseShipmentAddon, BinContentInfo>>, OutstandingSalesLineInfo>, BaseViewHolder> {

        private String tagName = "";

        public NewOutstandingSalesLineAdapter(@Nullable List<Polymorph<List<Polymorph<WarehouseShipmentAddon, BinContentInfo>>, OutstandingSalesLineInfo>> datas,
                                              String tagName) {
            super(R.layout.item_func7_headitem, datas);
            this.tagName = tagName;
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<List<Polymorph<WarehouseShipmentAddon, BinContentInfo>>, OutstandingSalesLineInfo> item) {
            OutstandingSalesLineInfo info = item.getInfoEntity();

            String quantityBaseStr = info.getOutstanding_Quantity();

            helper.setText(R.id.tv_item_func7_mcn, info.getNo());
            helper.setText(R.id.tv_item_func7_name, info.getDescription());
            helper.setText(R.id.tv_item_func7_count0_tag, tagName + "数:");
            helper.setText(R.id.tv_item_func7_count0, quantityBaseStr);
            helper.setText(R.id.tv_item_func7_listname, tagName + "列表");
            helper.setText(R.id.tv_item_func7_recommand, "库位" + tagName);


            RecyclerView recyclerView = helper.getView(R.id.recycler_item_func7);
            recyclerView.setHasFixedSize(true);
            double quantityBase = 0.0;
            if (TextUtils.isNumeric(quantityBaseStr))
                quantityBase = Double.valueOf(quantityBaseStr);
            WarehouseShipmentAdapter adapter = new WarehouseShipmentAdapter(item.getAddonEntity(), quantityBase, tagName);
            adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    if (view.getId() == R.id.tv_subitem_func1_delete)
                        buildDeleteDialog(adapter, position);
                }
            });
            AdapterHelper.setAdapterEmpty(mContext, adapter);
            recyclerView.setAdapter(adapter);

            helper.addOnClickListener(R.id.tv_item_func7_recommand);
            helper.setVisible(R.id.tv_item_func7_recommand, item.getState() != Polymorph.State.COMMITTED);

        }

        protected void buildDeleteDialog(final BaseQuickAdapter mAdapter, final int deleteIndex) {
            new AlertDialog
                    .Builder(mContext)
                    .setMessage(R.string.text_delete_confirmation)
                    .setPositiveButton(R.string.text_delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mAdapter.getData().remove(deleteIndex);
                            mAdapter.notifyItemRemoved(deleteIndex);
                        }
                    }).setNegativeButton(R.string.text_cancel, null)
                    .setCancelable(true)
                    .create()
                    .show();
        }
    }

    public static class WarehouseShipmentAdapter extends BaseQuickAdapter<Polymorph<WarehouseShipmentAddon, BinContentInfo>, BaseViewHolder> {

        private double maxQuantity;
        private String tagName;

        public WarehouseShipmentAdapter(@Nullable List<Polymorph<WarehouseShipmentAddon, BinContentInfo>> datas, double maxQuantity, String tagName) {
            super(R.layout.item_func1_subitem, datas);
            this.maxQuantity = maxQuantity;
            this.tagName = tagName;
        }

        private double getTotalQuantity(Polymorph<WarehouseShipmentAddon, BinContentInfo> notCountingItem, double addend) {
            if (addend > maxQuantity) return addend;

            double totalQuantity = addend;
            for (Polymorph<WarehouseShipmentAddon, BinContentInfo> polymorph : getData())
                if (polymorph != notCountingItem)
                    totalQuantity += Double.valueOf(polymorph.getAddonEntity().getQuantity());
            return totalQuantity;
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<WarehouseShipmentAddon, BinContentInfo> item) {

            helper.setText(R.id.tv_item_func1_count3, item.getInfoEntity().getQuantity_Base());
            helper.setText(R.id.et_item_func1_count1, item.getAddonEntity().getQuantity());
            helper.setText(R.id.tv_item_func1_location, item.getAddonEntity().getLocationCode());
            helper.setText(R.id.tv_item_func1_bincode, item.getAddonEntity().getBinCode());
            helper.setText(R.id.tv_item_func1_count1_tag, tagName + "数:");

            EditText et = helper.getView(R.id.et_item_func1_count1);

            final Polymorph<WarehouseShipmentAddon, BinContentInfo> polymorphItem = item;

            View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isFocused) {
                    if (!isFocused) {
                        String s = ((EditText) view).getText().toString();
                        if (TextUtils.isNumeric(s) &&
                                Double.valueOf(s) <= Double.valueOf(polymorphItem.getInfoEntity().getQuantity_Base()) &&
                                getTotalQuantity(polymorphItem, Double.valueOf(s)) <= maxQuantity) {
                            polymorphItem.getAddonEntity().setQuantity(s);
                        } else {
                            ((EditText) view).setText("0");
                            ToastUtils.show(R.string.toast_reach_upper_limit);
                        }
                    }
                }
            };
            et.setOnFocusChangeListener(focusChangeListener);

            helper.addOnClickListener(R.id.tv_subitem_func1_delete);
            switch (item.getState()) {
                case FAILURE:
                    helper.setBackgroundColor(R.id.view_item_func1_state, Color.RED);
                    helper.setTextColor(R.id.tv_item_func1_state, Color.RED);
                    helper.setText(R.id.tv_item_func1_state, R.string.text_commit_fail);
                    et.setEnabled(true);
                    break;
                case COMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func1_state, Color.GREEN);
                    helper.setTextColor(R.id.tv_item_func1_state, Color.GREEN);
                    helper.setText(R.id.tv_item_func1_state, R.string.text_committed);
                    et.setEnabled(false);
                    break;
                case UNCOMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func1_state, Color.argb(0Xff, 0xff, 0x90, 0x40));
                    helper.setTextColor(R.id.tv_item_func1_state, Color.WHITE);
                    helper.setText(R.id.tv_item_func1_state, "");
                    et.setEnabled(true);
                    break;
            }
        }
    }

}
