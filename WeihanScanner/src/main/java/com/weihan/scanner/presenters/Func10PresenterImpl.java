package com.weihan.scanner.presenters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.ToastUtils;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.OutstandingSalesLineInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseShipmentAddon;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func10ModelImpl;
import com.weihan.scanner.mvpviews.Func10MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;
import com.weihan.scanner.utils.AdapterHelper;

import java.util.List;

public class Func10PresenterImpl extends BasePresenter<Func10MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();

    private GenericOdataCallback<OutstandingSalesLineInfo> callback1 = new GenericOdataCallback<OutstandingSalesLineInfo>() {
        @Override
        public void onDataAvailable(List<OutstandingSalesLineInfo> datas) {
            if (datas.isEmpty()) ToastUtils.show(R.string.toast_no_record);
            getView().fillRecycler(Func10ModelImpl.createPolymorphList(datas));
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };

    private AllFuncModelImpl.PolyChangeListener<WarehouseShipmentAddon, OutstandingSalesLineInfo> listener
            = new AllFuncModelImpl.PolyChangeListener<WarehouseShipmentAddon, OutstandingSalesLineInfo>() {

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            allFuncModel.onAllCommitted(isFinished, msg);
            if (isFinished) getView().uncheckAdpaterBox();
            getView().notifyAdapter();
        }

        @Override
        public void goCommitting(Polymorph<WarehouseShipmentAddon, OutstandingSalesLineInfo> poly) {
            if (poly.getState() == Polymorph.State.FAILURE) {
                allFuncModel.onAllCommitted(allFuncModel.decreaseTaskCount() < 0, null);
                return;//不提交failure记录，即没有确认的项目
            }
            WarehouseShipmentAddon addon = poly.getAddonEntity();
            ApiTool.addWarehouseShptConfirmBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, this));
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
        allFuncModel.processList(datas, listener);
    }

    public static class NewOutstandingSalesLineAdapter extends BaseQuickAdapter<Polymorph<WarehouseShipmentAddon, OutstandingSalesLineInfo>, BaseViewHolder> {

        public NewOutstandingSalesLineAdapter(@Nullable List<Polymorph<WarehouseShipmentAddon, OutstandingSalesLineInfo>> datas) {
            super(R.layout.item_func4b, datas);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);

            AdapterHelper.addAdapterHeaderAndItemDivider(recyclerView, this, R.layout.item_func4b_header);
        }

        public void uncheckAllBoxes(RecyclerView recyclerView) {
            for (int i = 0; i < this.getItemCount(); i++) {
                View view = this.getViewByPosition(recyclerView, i, R.id.checkBox_item_func4);
                CheckBox checkBox;
                if (view != null) {
                    checkBox = (CheckBox) view;
                    checkBox.setChecked(false);
                }
            }
        }

        @Override
        protected void convert(final BaseViewHolder helper, final Polymorph<WarehouseShipmentAddon, OutstandingSalesLineInfo> item) {
            helper.setText(R.id.tv_item_func4_mcn, item.getInfoEntity().getNo());
            helper.getView(R.id.tv_item_func4_mcn).setSelected(true);
            helper.setText(R.id.tv_item_func4_name, item.getInfoEntity().getDescription());
            helper.getView(R.id.tv_item_func4_name).setSelected(true);
            helper.setText(R.id.et_item_func4_count1, item.getAddonEntity().getQuantity());

            CheckBox checkBox = helper.getView(R.id.checkBox_item_func4);

            final Polymorph<WarehouseShipmentAddon, OutstandingSalesLineInfo> polymorphItem = item;

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    polymorphItem.setState(isChecked ? Polymorph.State.UNCOMMITTED : Polymorph.State.FAILURE);
                    AllFuncModelImpl.setPolyAdapterItemStateColor(R.id.la_item_func4, item.getState(), helper, compoundButton);
                }
            });

            item.setState(checkBox.isChecked() ? Polymorph.State.UNCOMMITTED : Polymorph.State.FAILURE);
            /*
           EditText et = helper.getView(R.id.et_item_func4_count1);

            View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isFocused) {
                    if (!isFocused) {
                        String s = ((EditText) view).getText().toString();
                        if (TextUtils.isNumeric(s)
                                && Double.valueOf(s) <= Double.valueOf(polymorphItem.getInfoEntity().getQuantity_Base())
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
            */

            AllFuncModelImpl.setPolyAdapterItemStateColor(R.id.la_item_func4, item.getState(), helper, checkBox);

        }
    }

}
