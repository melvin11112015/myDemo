package com.weihan.scanner.presenters;

import com.common.utils.ToastUtils;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseTransferMultiAddon;
import com.weihan.scanner.entities.WhseTransferMultiInfo;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func13ModelImpl;
import com.weihan.scanner.mvpviews.Func13MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;

import java.util.List;
import java.util.Random;

public class Func13PresenterImpl extends BasePresenter<Func13MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();
    private AllFuncModelImpl.PolyChangeListener<WarehouseTransferMultiAddon, WhseTransferMultiInfo> listener
            = new AllFuncModelImpl.PolyChangeListener<WarehouseTransferMultiAddon, WhseTransferMultiInfo>() {

        private Random random = new Random();

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            getView().notifyAdapter();
            allFuncModel.buildingResultMsg(isFinished, msg);
        }

        @Override
        public void goCommitting(Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo> poly) {
            WarehouseTransferMultiAddon addon = poly.getAddonEntity();
            addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setSubmitDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setLineNo(Math.abs(random.nextInt()));
            ApiTool.addWhseTransferMultiFromBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, listener));
        }

    };
    private String binCode;
    private GenericOdataCallback<WhseTransferMultiInfo> callback1 = new GenericOdataCallback<WhseTransferMultiInfo>() {
        @Override
        public void onDataAvailable(List<WhseTransferMultiInfo> datas) {

            getView().fillRecycler(Func13ModelImpl.createPolymorphList(datas, binCode));
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };

    public void acquireDatas(String itemNo, String binCode) {

        if (itemNo.isEmpty() || binCode.isEmpty()) {
            ToastUtils.showToastLong("物料条码和从仓库条码不能为空");
            return;
        }
        String filter = "ItemNo eq '" + itemNo + "'";

        this.binCode = binCode;

        ApiTool.callWhseTransferMultiList(filter, callback1);
    }


    public void submitDatas(List<Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }

}
