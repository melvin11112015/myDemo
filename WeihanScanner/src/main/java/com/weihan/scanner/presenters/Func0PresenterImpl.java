package com.weihan.scanner.presenters;

import com.common.utils.ToastUtils;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.entities.OutstandingPurchLineInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseReceiptAddon;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func0ModelImpl;
import com.weihan.scanner.mvpviews.Func0MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;

import java.util.List;

public class Func0PresenterImpl extends BasePresenter<Func0MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();

    private GenericOdataCallback<OutstandingPurchLineInfo> callback1 = new GenericOdataCallback<OutstandingPurchLineInfo>() {
        @Override
        public void onDataAvailable(List<OutstandingPurchLineInfo> datas) {
            getView().fillRecycler(Func0ModelImpl.createPolymorphList(datas));
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };

    private AllFuncModelImpl.PolyChangeListener<WarehouseReceiptAddon, OutstandingPurchLineInfo> listener
            = new AllFuncModelImpl.PolyChangeListener<WarehouseReceiptAddon, OutstandingPurchLineInfo>() {

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            getView().notifyAdapter();
            allFuncModel.buildingResultMsg(isFinished, msg);
        }

        @Override
        public void goCommitting(Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo> poly) {
            WarehouseReceiptAddon addon = poly.getAddonEntity();
            addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
            ApiTool.addWarehouseReceipt(addon, allFuncModel.new AllFuncOdataCallback(poly, listener));
        }

    };

    public void acquireDatas(String lineCode) {

        if (lineCode.isEmpty()) {
            ToastUtils.showToastLong("不能为空");
            return;
        }
        String filter = "Document_No eq '" + lineCode + "'";

        ApiTool.callPurchaseLine(filter, callback1);
    }

    public void submitDatas(List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }


}
