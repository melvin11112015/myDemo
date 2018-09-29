package com.weihan.ligong.presenters;

import com.common.utils.ToastUtils;
import com.weihan.ligong.BaseMVP.BasePresenter;
import com.weihan.ligong.entities.BinContentInfo;
import com.weihan.ligong.mvpviews.Func11MvpView;
import com.weihan.ligong.net.ApiTool;
import com.weihan.ligong.net.GenericOdataCallback;

import java.util.List;


public class Func9PresenterImpl extends BasePresenter<Func11MvpView> {


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


}
