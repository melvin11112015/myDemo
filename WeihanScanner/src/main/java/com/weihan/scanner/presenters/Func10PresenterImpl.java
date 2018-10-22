package com.weihan.scanner.presenters;

import com.weihan.scanner.entities.OutstandingSalesLineInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseShipmentAddon;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.net.ApiTool;

import java.util.List;

public class Func10PresenterImpl extends Func7PresenterImpl {

    @Override
    public void submitDatas(List<Polymorph<WarehouseShipmentAddon, OutstandingSalesLineInfo>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        listener
                = new AllFuncModelImpl.PolyChangeListener<WarehouseShipmentAddon, OutstandingSalesLineInfo>() {

            @Override
            public void onPolyChanged(boolean isFinished, String msg) {
                getView().notifyAdapter();
                allFuncModel.buildingResultMsg(isFinished, msg);
            }

            @Override
            public void goCommitting(Polymorph<WarehouseShipmentAddon, OutstandingSalesLineInfo> poly) {
                WarehouseShipmentAddon addon = poly.getAddonEntity();
                addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
                ApiTool.addWarehouseShptConfirmBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, listener));
            }

        };
        allFuncModel.processList(datas, listener);
    }

}
