package com.weihan.scanner.presenters;

import com.weihan.scanner.entities.ConsumptionPickAddon;
import com.weihan.scanner.entities.InvPickingInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.net.ApiTool;

import java.util.List;

public class Func4PresenterImpl extends Func1PresenterImpl {

    @Override
    public void submitDatas(List<Polymorph<ConsumptionPickAddon, InvPickingInfo>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        listener
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
        allFuncModel.processList(datas, listener);
    }

}
