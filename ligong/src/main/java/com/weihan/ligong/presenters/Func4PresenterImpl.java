package com.weihan.ligong.presenters;

import com.common.utils.ToastUtils;
import com.weihan.ligong.BaseMVP.BasePresenter;
import com.weihan.ligong.entities.ConsumptionPickAddon;
import com.weihan.ligong.entities.InvPickingInfo;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.models.AllFuncModelImpl;
import com.weihan.ligong.models.Func1ModelImpl;
import com.weihan.ligong.mvpviews.Func1MvpView;
import com.weihan.ligong.net.ApiTool;
import com.weihan.ligong.net.GenericOdataCallback;

import java.util.List;

public class Func4PresenterImpl extends BasePresenter<Func1MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();

    private GenericOdataCallback<InvPickingInfo> callback1 = new GenericOdataCallback<InvPickingInfo>() {
        @Override
        public void onDataAvailable(List<InvPickingInfo> datas) {
            getView().fillRecycler(Func1ModelImpl.createPolymorphList(datas));
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };

    private AllFuncModelImpl.PolyChangeListener<ConsumptionPickAddon, InvPickingInfo> listener
            = new AllFuncModelImpl.PolyChangeListener<ConsumptionPickAddon, InvPickingInfo>() {

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            getView().notifyAdapter();
            allFuncModel.buildingResultMsg(isFinished, msg);
        }

        @Override
        public void goCommitting(Polymorph<ConsumptionPickAddon, InvPickingInfo> poly) {
            ConsumptionPickAddon addon = poly.getAddonEntity();
            addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
            ApiTool.addConsumptionPickConfirm_Buffer(addon, allFuncModel.new AllFuncOdataCallback(poly, listener));
        }

    };

    public void acquireDatas(String lineCode) {

        if (lineCode.isEmpty()) {
            ToastUtils.showToastLong("不能为空");
            return;
        }
        String filter = "Inv_Document_No eq '" + lineCode + "'";

        ApiTool.callInvPickingList(filter, callback1);
    }

    public void submitDatas(List<Polymorph<ConsumptionPickAddon, InvPickingInfo>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }


}
