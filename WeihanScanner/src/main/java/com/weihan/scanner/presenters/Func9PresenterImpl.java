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
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.OutputPutAwayAddon;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func9ModelImpl;
import com.weihan.scanner.mvpviews.Func9MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;
import com.weihan.scanner.utils.AdapterHelper;

import java.util.List;


public class Func9PresenterImpl extends BasePresenter<Func9MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();
    private AllFuncModelImpl.PolyChangeListener<OutputPutAwayAddon, OutputPutAwayAddon> listener
            = new AllFuncModelImpl.PolyChangeListener<OutputPutAwayAddon, OutputPutAwayAddon>() {

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            allFuncModel.onAllCommitted(isFinished, msg);
            getView().notifyAdapter();
        }

        @Override
        public void goCommitting(Polymorph<OutputPutAwayAddon, OutputPutAwayAddon> poly) {
            OutputPutAwayAddon addon = poly.getAddonEntity();
            addon.setSubmitDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setLineNo(AllFuncModelImpl.getTempInt());
            ApiTool.addOutputPutAwayBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, this));
        }

    };

    public void submitDatas(List<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }

    private GenericOdataCallback<BinContentInfo> callback1 = new GenericOdataCallback<BinContentInfo>() {
        @Override
        public void onDataAvailable(List<BinContentInfo> datas) {
            if (datas.isEmpty()) ToastUtils.show(R.string.toast_no_record);

            getView().fillRecyclerWithRecommandInfo(datas);
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };


    public void acquireDatas(String itemNo, String WBcode) {
        String filter = "";
        String bincode = AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_BIN);
        String locationCode = AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_LOCATION);

        if (!WBcode.isEmpty() && !itemNo.isEmpty()) {
            filter = "Item_No eq '" + itemNo + "' and Bin_Code eq '" + bincode + "' and Location_Code eq '" + locationCode + "' and Quantity_Base ne 0";
        } else if (!itemNo.isEmpty() && WBcode.isEmpty()) {
            filter = "Item_No eq '" + itemNo + "' and Quantity_Base ne 0";
        } else if (!WBcode.isEmpty() && itemNo.isEmpty()) {
            filter = "Bin_Code eq '" + bincode + "' and Location_Code eq '" + locationCode + "' and Quantity_Base ne 0";
        } else {
            ToastUtils.show("请输入库位条码或物料条码");
            return;
        }
        ApiTool.callBinContent(filter, callback1);

    }

    public void attemptToAddPoly(List<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>> datas, String itemno, String WBcode, String quantity) {

        String bincode = AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_BIN);
        String locationCode = AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_LOCATION);

        for (Polymorph<OutputPutAwayAddon, OutputPutAwayAddon> polymorph : datas)
            if (polymorph.getAddonEntity().getBinCode().equals(bincode) &&
                    polymorph.getAddonEntity().getLocationCode().equals(locationCode) &&
                    polymorph.getAddonEntity().getItemNo().equals(itemno))
                return;

        datas.add(Func9ModelImpl.createPoly(itemno, bincode, locationCode, quantity));

        getView().notifyAdapter();
    }

    public static class OutputPutAwayListAdapter extends BaseItemDraggableAdapter<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>, BaseViewHolder> {

        public OutputPutAwayListAdapter(@Nullable List<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>> datas) {
            super(R.layout.item_func3b, datas);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);

            AdapterHelper.initDraggableAdapter(recyclerView, this);
            AdapterHelper.addAdapterHeaderAndItemDivider(recyclerView, this, R.layout.item_func3b_header);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<OutputPutAwayAddon, OutputPutAwayAddon> item) {
            helper.setText(R.id.tv_item_func3_mcn, item.getAddonEntity().getItemNo());
            helper.setText(R.id.tv_item_func3_to_wbcode, item.getAddonEntity().getLocationCode() + item.getAddonEntity().getBinCode());
            helper.setText(R.id.et_item_func3_quantity1, item.getAddonEntity().getQuantity());
            EditText et = helper.getView(R.id.et_item_func3_quantity1);

            final Polymorph<OutputPutAwayAddon, OutputPutAwayAddon> polymorphItem = item;

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

            AllFuncModelImpl.setPolyAdapterItemStateColor(R.id.la_item_func3, item.getState(), helper, et);

        }
    }

}
