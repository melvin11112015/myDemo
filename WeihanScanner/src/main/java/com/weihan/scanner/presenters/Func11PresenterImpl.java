package com.weihan.scanner.presenters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.ToastUtils;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.mvpviews.Func11MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;

import java.util.List;


public class Func11PresenterImpl extends BasePresenter<Func11MvpView> {


    private GenericOdataCallback<BinContentInfo> callback1 = new GenericOdataCallback<BinContentInfo>() {
        @Override
        public void onDataAvailable(List<BinContentInfo> datas) {

            getView().fillRecycler(datas);
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };




    public void acquireDatas0(String itemNo) {

        if (itemNo.isEmpty()) {
            ToastUtils.showToastLong("物料条码不能为空");
            return;
        }
        String filter = "Item_No eq '" + itemNo + "'";

        ApiTool.callBinContent(filter, callback1);
    }



    public void acquireDatas1(String binCode) {

        if (binCode.isEmpty()) {
            ToastUtils.showToastLong("仓库条码不能为空");
            return;
        }
        String filter = "Bin_Code eq '" + binCode + "'";

        ApiTool.callBinContent(filter, callback1);
    }

    public static class BinContentListAdapter extends BaseQuickAdapter<BinContentInfo, BaseViewHolder> {

        public BinContentListAdapter(List<BinContentInfo> datas) {
            super(R.layout.item_func11, datas);
        }

        @Override
        protected void convert(final BaseViewHolder helper, BinContentInfo item) {
            helper.setText(R.id.tv_item_func11_mcn, item.getItem_No());
            helper.setText(R.id.tv_item_func11_binname, item.getLocation_Code());
            helper.setText(R.id.tv_item_func11_bincode, item.getBin_Code());
            helper.setText(R.id.tv_item_func11_quantity, item.getQuantity_Base());
            helper.setText(R.id.tv_item_func11_name, item.getItem_No());
        }
    }

}