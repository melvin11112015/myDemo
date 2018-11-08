package com.weihan.scanner.presenters;

import com.common.utils.ToastUtils;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.mvpviews.ChooseListMvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;

import java.util.List;


public class ChooseListPresenterImpl extends BasePresenter<ChooseListMvpView> {

    private GenericOdataCallback<BinContentInfo> callback1 = new GenericOdataCallback<BinContentInfo>() {
        @Override
        public void onDataAvailable(List<BinContentInfo> datas) {

            if (datas.isEmpty()) {
                getView().exitActivity();
                ToastUtils.show(R.string.toast_no_record);
            } else
                getView().fillChooseListRecycler(datas);
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            getView().exitActivity();
            ToastUtils.showToastLong(msg);
        }
    };

    public void acquireDatas(String itemNo) {
        String filter = "";

        if (!itemNo.isEmpty()) {
            filter = "Item_No eq '" + itemNo + "' and Quantity_Base ne 0";
        } else {
            ToastUtils.show("请输入物料条码");
            return;
        }
        ApiTool.callBinContent(filter, callback1);

    }

    public void lookupDatas(List<BinContentInfo> datas, String WBcode) {
        if (WBcode.isEmpty() || datas.isEmpty()) {
            ToastUtils.show("请输入库位条码");
            return;
        }
        String bincode = AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_BIN);
        String locationCode = AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_LOCATION);

        for (BinContentInfo info : datas) {
            if (info.getLocation_Code().equals(locationCode) && info.getBin_Code().equals(bincode)) {
                getView().foundItem(info);
                return;
            }
        }
        ToastUtils.show("没有找到对应记录");
    }

}
