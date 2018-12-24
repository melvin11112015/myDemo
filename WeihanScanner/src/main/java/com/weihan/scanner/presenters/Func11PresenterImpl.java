package com.weihan.scanner.presenters;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.ToastUtils;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.mvpviews.Func11MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;
import com.weihan.scanner.utils.AdapterHelper;

import java.util.List;


public class Func11PresenterImpl extends BasePresenter<Func11MvpView> {


    private GenericOdataCallback<BinContentInfo> callback1 = new GenericOdataCallback<BinContentInfo>() {
        @Override
        public void onDataAvailable(List<BinContentInfo> datas) {
            if (datas.isEmpty())
                ToastUtils.show(R.string.toast_no_record);

            getView().fillRecycler(datas);
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
            ToastUtils.showFormatting(R.string.toast_please_enter_or, R.string.text_material_barcode, R.string.text_bin);
            return;
        }
        ApiTool.callBinContent(filter, callback1);

    }

    public static class BinContentListAdapter extends BaseQuickAdapter<BinContentInfo, BaseViewHolder> {

        public BinContentListAdapter(List<BinContentInfo> datas) {
            super(R.layout.item_func11b, datas);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            AdapterHelper.addAdapterHeaderAndItemDivider(recyclerView, this, R.layout.item_func11b_header);
        }

        @Override
        protected void convert(final BaseViewHolder helper, BinContentInfo item) {
            helper.setText(R.id.tv_item_func11_mcn, item.getItem_No());
            helper.getView(R.id.tv_item_func11_mcn).setSelected(true);
            helper.setText(R.id.tv_item_func11_wbcode, item.getLocation_Code() + item.getBin_Code());
            helper.getView(R.id.tv_item_func11_wbcode).setSelected(true);
            helper.setText(R.id.tv_item_func11_quantity, item.getQuantity_Base());
            helper.setText(R.id.tv_item_func11_name, item.getItem_No());
            helper.getView(R.id.tv_item_func11_name).setSelected(true);
        }
    }

}
