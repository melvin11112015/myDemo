package com.weihan.scanner.presenters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.ToastUtils;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.PhysicalInvtAddon;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func2ModelImpl;
import com.weihan.scanner.mvpviews.Func6MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.utils.AdapterHelper;

import java.util.List;

public class Func2PresenterImpl extends BasePresenter<Func6MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();
    private AllFuncModelImpl.PolyChangeListener<PhysicalInvtAddon, PhysicalInvtAddon> listener
            = new AllFuncModelImpl.PolyChangeListener<PhysicalInvtAddon, PhysicalInvtAddon>() {

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            allFuncModel.onAllCommitted(isFinished, msg);
            getView().notifyAdapter();
        }

        @Override
        public void goCommitting(Polymorph<PhysicalInvtAddon, PhysicalInvtAddon> poly) {
            PhysicalInvtAddon addon = poly.getAddonEntity();
            addon.setSubmitDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setLineNo(AllFuncModelImpl.getTempInt());
            ApiTool.addPhysicalInvtBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, this));
        }

    };

    public void attemptToAddPoly(List<Polymorph<PhysicalInvtAddon, PhysicalInvtAddon>> datas, String WBcode, String itemno) {

        if (WBcode.isEmpty() || itemno.isEmpty()) {
            ToastUtils.show("库位条码和物料编号不能为空");
            return;
        }

        String bincode = AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_BIN);
        String locationCode = AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_LOCATION);

        for (Polymorph<PhysicalInvtAddon, PhysicalInvtAddon> polymorph : datas)
            if (polymorph.getAddonEntity().getBinCode().equals(bincode) &&
                    polymorph.getAddonEntity().getLoaction_Code().equals(locationCode) &&
                    polymorph.getAddonEntity().getItemNo().equals(itemno))
                return;

        datas.add(Func2ModelImpl.createPoly(itemno, bincode, locationCode));

        getView().notifyAdapter();
    }

    public void submitDatas(List<Polymorph<PhysicalInvtAddon, PhysicalInvtAddon>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }

    public static class PhysicalInvtAdapter extends BaseItemDraggableAdapter<Polymorph<PhysicalInvtAddon, PhysicalInvtAddon>, BaseViewHolder> {

        public PhysicalInvtAdapter(@Nullable List<Polymorph<PhysicalInvtAddon, PhysicalInvtAddon>> datas) {
            super(R.layout.item_func2b, datas);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);

            AdapterHelper.initDraggableAdapter(recyclerView, this);
            AdapterHelper.addAdapterHeaderAndItemDivider(recyclerView, this, R.layout.item_func2b_header);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<PhysicalInvtAddon, PhysicalInvtAddon> item) {
            helper.setText(R.id.tv_item_func2_wbcode, item.getAddonEntity().getLoaction_Code() + item.getAddonEntity().getBinCode());
            helper.setText(R.id.tv_item_func2_itemno, item.getAddonEntity().getItemNo());
            helper.setText(R.id.et_item_func2_count1, item.getAddonEntity().getQuantity());
            EditText et = helper.getView(R.id.et_item_func2_count1);

            final Polymorph<PhysicalInvtAddon, PhysicalInvtAddon> polymorphItem = item;

            View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isFocused) {
                    if (!isFocused) {
                        String s = ((EditText) view).getText().toString();
                        polymorphItem.getAddonEntity().setQuantity(s);
                    }
                }
            };
            et.setOnFocusChangeListener(focusChangeListener);

            AllFuncModelImpl.setPolyAdapterItemStateColor(R.id.la_item_func2, item.getState(), helper, et);
        }
    }

}
