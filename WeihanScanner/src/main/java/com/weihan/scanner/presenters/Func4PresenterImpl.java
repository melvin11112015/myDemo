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
import com.weihan.scanner.entities.ConsumptionPickConfirmAddon;
import com.weihan.scanner.entities.InvPickingInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func4ModelImpl;
import com.weihan.scanner.mvpviews.Func4MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;
import com.weihan.scanner.utils.AdapterHelper;

import java.util.List;

public class Func4PresenterImpl extends BasePresenter<Func4MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();

    private GenericOdataCallback<InvPickingInfo> callback1 = new GenericOdataCallback<InvPickingInfo>() {
        @Override
        public void onDataAvailable(List<InvPickingInfo> datas) {
            if (datas.isEmpty()) ToastUtils.show(R.string.toast_no_record);
            getView().fillRecycler(Func4ModelImpl.createPolymorphList(datas));
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };

    private AllFuncModelImpl.PolyChangeListener<ConsumptionPickConfirmAddon, InvPickingInfo> listener
            = new AllFuncModelImpl.PolyChangeListener<ConsumptionPickConfirmAddon, InvPickingInfo>() {

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            allFuncModel.onAllCommitted(isFinished, msg);
            if (isFinished) getView().uncheckAdpaterBox();
            getView().notifyAdapter();
        }

        @Override
        public void goCommitting(Polymorph<ConsumptionPickConfirmAddon, InvPickingInfo> poly) {
            if (poly.getState() == Polymorph.State.FAILURE) {
                allFuncModel.onAllCommitted(allFuncModel.decreaseTaskCount() < 0, null);
                return;//不提交failure记录，即没有确认的项目
            }
            ConsumptionPickConfirmAddon addon = poly.getAddonEntity();
            ApiTool.addConsumptionPickConfirm_Buffer(addon, allFuncModel.new AllFuncOdataCallback(poly, this));
        }

    };

    public void acquireDatas(String lineCode) {

        if (lineCode.isEmpty()) {
            ToastUtils.showToastLong("不能为空");
            return;
        }
        String filter = "Inv_Document_No eq '" + lineCode + "'";

        ApiTool.callInvPickingList(filter, callback1);
    }

    public void submitDatas(List<Polymorph<ConsumptionPickConfirmAddon, InvPickingInfo>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }

    public static class ConsumptionAdapter extends BaseQuickAdapter<Polymorph<ConsumptionPickConfirmAddon, InvPickingInfo>, BaseViewHolder> {

        public ConsumptionAdapter(@Nullable List<Polymorph<ConsumptionPickConfirmAddon, InvPickingInfo>> datas) {
            super(R.layout.item_func4b, datas);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);

            //AdapterHelper.initDraggableAdapter(recyclerView, this);
            AdapterHelper.addAdapterHeaderAndItemDivider(recyclerView, this, R.layout.item_func4b_header);
        }

        public void uncheckAllBoxes(RecyclerView recyclerView) {
            for (int i = 0; i < this.getItemCount(); i++) {
                View view = this.getViewByPosition(recyclerView, i, R.id.checkBox_item_func4);
                CheckBox checkBox;
                if (view != null) {
                    System.out.println("uncheck:" + i);
                    checkBox = (CheckBox) view;
                    checkBox.setChecked(false);
                }
            }
        }

        @Override
        protected void convert(final BaseViewHolder helper, final Polymorph<ConsumptionPickConfirmAddon, InvPickingInfo> item) {
            CheckBox checkBox = helper.getView(R.id.checkBox_item_func4);
            checkBox.setOnCheckedChangeListener(null);

            helper.setText(R.id.tv_item_func4_mcn, item.getInfoEntity().getItem_No());
            helper.getView(R.id.tv_item_func4_mcn).setSelected(true);
            helper.setText(R.id.tv_item_func4_name, item.getInfoEntity().getDescription());
            helper.getView(R.id.tv_item_func4_name).setSelected(true);
            helper.setText(R.id.et_item_func4_count1, item.getAddonEntity().getQuantity());

            final Polymorph<ConsumptionPickConfirmAddon, InvPickingInfo> polymorphItem = item;

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    polymorphItem.setState(isChecked ? Polymorph.State.UNCOMMITTED : Polymorph.State.UNCOMMITTED_UNCHECKED);
                    AllFuncModelImpl.setPolyAdapterItemStateColor(R.id.la_item_func4, item.getState(), helper, compoundButton);
                }
            });

            item.setState(checkBox.isChecked() ? Polymorph.State.UNCOMMITTED : Polymorph.State.UNCOMMITTED_UNCHECKED);
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
