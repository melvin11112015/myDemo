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
import com.weihan.scanner.entities.ConsumptionPickAddon;
import com.weihan.scanner.entities.InvPickingInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func1ModelImpl;
import com.weihan.scanner.mvpviews.Func1MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;
import com.weihan.scanner.utils.AdapterHelper;
import com.weihan.scanner.utils.TextUtils;

import java.util.List;

public class Func1PresenterImpl extends BasePresenter<Func1MvpView> {

    protected AllFuncModelImpl allFuncModel = new AllFuncModelImpl();

    AllFuncModelImpl.PolyChangeListener<ConsumptionPickAddon, BinContentInfo> listener;


    public void acquireDatas(String lineCode) {

        if (lineCode.isEmpty()) {
            ToastUtils.showToastLong("不能为空");
            return;
        }
        String filter = "Inv_Document_No eq '" + lineCode + "'";

        ApiTool.callInvPickingList(filter, callback1);
    }
    private GenericOdataCallback<InvPickingInfo> callback1 = new GenericOdataCallback<InvPickingInfo>() {
        @Override
        public void onDataAvailable(List<InvPickingInfo> datas) {
            if (datas.isEmpty()) {
                ToastUtils.show(R.string.toast_no_record);
                return;
            }
            getView().fillRecycler(Func1ModelImpl.createPolymorphList(datas));
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };

    public void submitDatas(List<Polymorph<List<Polymorph<ConsumptionPickAddon, BinContentInfo>>, InvPickingInfo>> polysdata) {

        for (Polymorph<List<Polymorph<ConsumptionPickAddon, BinContentInfo>>, InvPickingInfo> polys : polysdata) {

            List<Polymorph<ConsumptionPickAddon, BinContentInfo>> datas = polys.getAddonEntity();

            if (!AllFuncModelImpl.checkEmptyList(datas)) return;

            listener = new AllFuncModelImpl.PolyChangeListener<ConsumptionPickAddon, BinContentInfo>() {

                @Override
                public void onPolyChanged(boolean isFinished, String msg) {
                    allFuncModel.onAllCommitted(false, msg);
                    getView().notifyAdapter();
                }

                @Override
                public void goCommitting(Polymorph<ConsumptionPickAddon, BinContentInfo> poly) {
                    ConsumptionPickAddon addon = poly.getAddonEntity();
                    addon.setSubmitDate(AllFuncModelImpl.getCurrentDatetime());
                    //addon.setLine_No(AllFuncModelImpl.getTempInt());
                    ApiTool.addConsumptionPickBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, this));
                }

            };
            allFuncModel.processList(datas, listener);
        }
        allFuncModel.onAllCommitted(true, "");
    }


    public void attemptToAddPoly(List<Polymorph<ConsumptionPickAddon, BinContentInfo>> datas, BinContentInfo info, InvPickingInfo infoHeader) {

        for (Polymorph<ConsumptionPickAddon, BinContentInfo> polymorph : datas)
            if (polymorph.getInfoEntity().getBin_Code().equals(info.getBin_Code()) &&
                    polymorph.getInfoEntity().getLocation_Code().equals(info.getLocation_Code()))
                return;

        datas.add(Func1ModelImpl.createPoly(info, infoHeader));

        getView().notifyAdapter();
    }

    public static class NewInvPickingAdapter extends BaseQuickAdapter<Polymorph<List<Polymorph<ConsumptionPickAddon, BinContentInfo>>, InvPickingInfo>, BaseViewHolder> {

        public NewInvPickingAdapter(@Nullable List<Polymorph<List<Polymorph<ConsumptionPickAddon, BinContentInfo>>, InvPickingInfo>> datas) {
            super(R.layout.item_func1_headitem, datas);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<List<Polymorph<ConsumptionPickAddon, BinContentInfo>>, InvPickingInfo> item) {
            InvPickingInfo info = item.getInfoEntity();

            String quantityBaseStr = info.getQuantity_Base();

            helper.setText(R.id.tv_item_func1_mcn, info.getItem_No());
            helper.setText(R.id.tv_item_func1_name, info.getDescription());
            helper.setText(R.id.tv_item_func1_count0_tag, R.string.text_pick_quantity_colon);
            helper.setText(R.id.tv_item_func1_count0, quantityBaseStr);

            RecyclerView recyclerView = helper.getView(R.id.recycler_item_func1);
            recyclerView.setHasFixedSize(true);
            double quantityBase = 0.0;
            if (TextUtils.isNumeric(quantityBaseStr))
                quantityBase = Double.valueOf(quantityBaseStr);
            ConsumptionPickAdapter2 adapter = new ConsumptionPickAdapter2(item.getAddonEntity(), quantityBase);
            adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    if (view.getId() == R.id.tv_subitem_func1_delete)
                        buildDeleteDialog(adapter, position);
                }
            });
            AdapterHelper.setAdapterEmpty(mContext, adapter);
            recyclerView.setAdapter(adapter);

            helper.addOnClickListener(R.id.tv_item_func1_recommand);
            helper.setVisible(R.id.tv_item_func1_recommand, item.getState() != Polymorph.State.COMMITTED);

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

    public static class ConsumptionPickAdapter extends BaseQuickAdapter<Polymorph<ConsumptionPickAddon, BinContentInfo>, BaseViewHolder> {

        private double maxQuantity;

        public ConsumptionPickAdapter(@Nullable List<Polymorph<ConsumptionPickAddon, BinContentInfo>> datas, double maxQuantity) {
            super(R.layout.item_func1_subitem, datas);
            this.maxQuantity = maxQuantity;
        }

        private double getTotalQuantity(Polymorph<ConsumptionPickAddon, BinContentInfo> notCountingItem, double addend) {
            if (addend > maxQuantity) return addend;

            double totalQuantity = addend;
            for (Polymorph<ConsumptionPickAddon, BinContentInfo> polymorph : getData())
                if (polymorph != notCountingItem)
                    totalQuantity += Double.valueOf(polymorph.getAddonEntity().getQuantity());
            return totalQuantity;
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<ConsumptionPickAddon, BinContentInfo> item) {

            helper.setText(R.id.tv_item_func1_count3, item.getInfoEntity().getQuantity_Base());
            helper.setText(R.id.et_item_func1_count1, item.getAddonEntity().getQuantity());
            helper.setText(R.id.tv_item_func1_location, item.getAddonEntity().getLocationCode());
            helper.setText(R.id.tv_item_func1_bincode, item.getAddonEntity().getBinCode());
            helper.setText(R.id.tv_item_func1_count1_tag, R.string.text_pick_quantity_colon);

            EditText et = helper.getView(R.id.et_item_func1_count1);

            final Polymorph<ConsumptionPickAddon, BinContentInfo> polymorphItem = item;

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

    public static class ConsumptionPickAdapter2 extends BaseQuickAdapter<Polymorph<ConsumptionPickAddon, BinContentInfo>, BaseViewHolder> {

        private double maxQuantity;

        public ConsumptionPickAdapter2(@Nullable List<Polymorph<ConsumptionPickAddon, BinContentInfo>> datas, double maxQuantity) {
            super(R.layout.item_func1b_subitem, datas);
            this.maxQuantity = maxQuantity;
        }

        private double getTotalQuantity(Polymorph<ConsumptionPickAddon, BinContentInfo> notCountingItem, double addend) {
            if (addend > maxQuantity) return addend;

            double totalQuantity = addend;
            for (Polymorph<ConsumptionPickAddon, BinContentInfo> polymorph : getData())
                if (polymorph != notCountingItem)
                    totalQuantity += Double.valueOf(polymorph.getAddonEntity().getQuantity());
            return totalQuantity;
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<ConsumptionPickAddon, BinContentInfo> item) {

            helper.setText(R.id.tv_item_func1_count3, item.getInfoEntity().getQuantity_Base());
            helper.setText(R.id.et_item_func1_count1, item.getAddonEntity().getQuantity());
            helper.setText(R.id.tv_item_func1_wbcode, item.getAddonEntity().getLocationCode() + item.getAddonEntity().getBinCode());

            EditText et = helper.getView(R.id.et_item_func1_count1);

            final Polymorph<ConsumptionPickAddon, BinContentInfo> polymorphItem = item;

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

            AllFuncModelImpl.setPolyAdapterItemStateColor(R.id.la_item_func1, item.getState(), helper, et);
        }
    }


}
