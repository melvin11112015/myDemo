package com.weihan.ligong.presenters;

import com.common.utils.ToastUtils;
import com.weihan.ligong.BaseMVP.BasePresenter;
import com.weihan.ligong.entities.BinContentInfo;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.entities.WarehouseTransferMultiFromAddon;
import com.weihan.ligong.models.AllFuncModelImpl;
import com.weihan.ligong.models.Func12ModelImpl;
import com.weihan.ligong.mvpviews.Func12MvpView;
import com.weihan.ligong.net.ApiTool;
import com.weihan.ligong.net.GenericOdataCallback;

import java.util.List;
import java.util.Random;

public class Func12PresenterImpl extends BasePresenter<Func12MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();

    private GenericOdataCallback<BinContentInfo> callback1 = new GenericOdataCallback<BinContentInfo>() {
        @Override
        public void onDataAvailable(List<BinContentInfo> datas) {

            getView().fillRecycler(Func12ModelImpl.createPolymorphList(datas));
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };

    private AllFuncModelImpl.PolyChangeListener<WarehouseTransferMultiFromAddon, BinContentInfo> listener
            = new AllFuncModelImpl.PolyChangeListener<WarehouseTransferMultiFromAddon, BinContentInfo>() {

        private StringBuilder stringBuilder = new StringBuilder();
        private Random random = new Random();

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            getView().notifyAdapter();
            if (msg != null) stringBuilder.append("错误:").append(msg).append('\n');
            if (isFinished) ToastUtils.showToastLong("提交完成\n" + stringBuilder.toString());
        }

        @Override
        public void goCommitting(Polymorph<WarehouseTransferMultiFromAddon, BinContentInfo> poly) {
            WarehouseTransferMultiFromAddon addon = poly.getAddonEntity();
            addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setSubmitDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setLineNo(Math.abs(random.nextInt()));
            ApiTool.addWhseTransferMultiFromBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, listener));
        }

    };

    public void acquireDatas(String itemNo, String binCode) {

        if (itemNo.isEmpty() || binCode.isEmpty()) {
            ToastUtils.showToastLong("物料条码和从仓库条码不能为空");
            return;
        }
        String filter = "Item_No eq '" + itemNo + "' and Bin_Code eq '" + binCode + "'";

        ApiTool.callBinContent(filter, callback1);
    }


    public void submitDatas(List<Polymorph<WarehouseTransferMultiFromAddon, BinContentInfo>> datas) {
        ToastUtils.show("提交中");
        allFuncModel.processList(datas, listener);
    }

}
