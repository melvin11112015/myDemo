package com.weihan.ligong.presenters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.ToastUtils;
import com.weihan.ligong.BaseMVP.BasePresenter;
import com.weihan.ligong.R;
import com.weihan.ligong.entities.BinContentInfo;
import com.weihan.ligong.models.Func9ModelImpl;
import com.weihan.ligong.mvpviews.Func11MvpView;
import com.weihan.ligong.net.ApiTool;
import com.weihan.ligong.net.GenericOdataCallback;

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

    private GenericOdataCallback<BinContentInfo> callback2 = new GenericOdataCallback<BinContentInfo>() {
        @Override
        public void onDataAvailable(List<BinContentInfo> datas) {

            List<BinContentInfo> tempList = Func9ModelImpl.filtStoreIssueItem(datas);

            if (tempList.isEmpty()) {
                getView().exitActivity();
                ToastUtils.show(R.string.toast_no_record);
            } else
                getView().fillRecycler(tempList);
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
            getView().exitActivity();
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

    public void acquireBincontentWithStoreIssue(String itemNo) {

        if (itemNo.isEmpty()) {
            ToastUtils.showToastLong("物料条码不能为空");
            return;
        }
        String filter = "Item_No eq '" + itemNo + "'";

        ApiTool.callBinContent(filter, callback2);
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
