package com.weihan.ligong.presenters;

import com.common.utils.ToastUtils;
import com.weihan.ligong.BaseMVP.BasePresenter;
import com.weihan.ligong.entities.OutstandingPurchLineInfo;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.entities.WarehouseReceiptAddon;
import com.weihan.ligong.models.AllFuncModelImpl;
import com.weihan.ligong.models.Func0ModelImpl;
import com.weihan.ligong.mvpviews.Func0MvpView;
import com.weihan.ligong.net.ApiTool;
import com.weihan.ligong.net.GenericOdataCallback;

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

        private StringBuilder stringBuilder = new StringBuilder();

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            getView().notifyAdapter();
            if (msg != null) stringBuilder.append("错误:").append(msg).append('\n');
            if (isFinished) ToastUtils.showToastLong("提交完成\n" + stringBuilder.toString());
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
        ToastUtils.show("提交中");
        allFuncModel.processList(datas, listener);
    }


}
