package com.weihan.scanner.presenters;

import com.common.utils.ToastUtils;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehousePutAwayAddon;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func3ModelImpl;
import com.weihan.scanner.mvpviews.Func3MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;

import java.util.List;
import java.util.Random;

public class Func3PresenterImpl extends BasePresenter<Func3MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();

    private String binCode = "";

    private GenericOdataCallback<BinContentInfo> callback1 = new GenericOdataCallback<BinContentInfo>() {
        @Override
        public void onDataAvailable(List<BinContentInfo> datas) {

            getView().fillRecycler(Func3ModelImpl.createPolymorphList(datas, binCode));
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };

    private AllFuncModelImpl.PolyChangeListener<WarehousePutAwayAddon, BinContentInfo> listener
            = new AllFuncModelImpl.PolyChangeListener<WarehousePutAwayAddon, BinContentInfo>() {

        private Random random = new Random();

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            getView().notifyAdapter();
            allFuncModel.buildingResultMsg(isFinished, msg);
        }

        @Override
        public void goCommitting(Polymorph<WarehousePutAwayAddon, BinContentInfo> poly) {
            WarehousePutAwayAddon addon = poly.getAddonEntity();
            addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setSubmitDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setLineNo(Math.abs(random.nextInt()));
            ApiTool.addWarehousePutAwayBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, listener));
        }

    };


    public void acquireDatas(String itemNo, String binCode) {

        if (itemNo.isEmpty() || binCode.isEmpty()) {
            ToastUtils.showToastLong("物料条码和从仓库条码不能为空");
            return;
        }
        final boolean isTemp_Receipt = false;//有bug,返回的值与传入的参数相反
        String filter = "Item_No eq '" + itemNo + "'";

        this.binCode = binCode;

        ApiTool.callBinContent(filter, callback1);
    }


    public void submitDatas(List<Polymorph<WarehousePutAwayAddon, BinContentInfo>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }

}
