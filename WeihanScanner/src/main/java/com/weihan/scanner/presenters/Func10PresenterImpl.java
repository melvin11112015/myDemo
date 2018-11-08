package com.weihan.scanner.presenters;

import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.OutstandingSalesLineInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseShipmentAddon;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.net.ApiTool;

import java.util.List;

public class Func10PresenterImpl extends Func7PresenterImpl {

    @Override
    public void submitDatas(List<Polymorph<List<Polymorph<WarehouseShipmentAddon, BinContentInfo>>, OutstandingSalesLineInfo>> polysdata) {

        for (Polymorph<List<Polymorph<WarehouseShipmentAddon, BinContentInfo>>, OutstandingSalesLineInfo> polys : polysdata) {

            List<Polymorph<WarehouseShipmentAddon, BinContentInfo>> datas = polys.getAddonEntity();

            if (!AllFuncModelImpl.checkEmptyList(datas)) return;

            listener = new AllFuncModelImpl.PolyChangeListener<WarehouseShipmentAddon, BinContentInfo>() {

                @Override
                public void onPolyChanged(boolean isFinished, String msg) {
                    allFuncModel.onAllCommitted(false, msg);
                    getView().notifyAdapter();
                }

                @Override
                public void goCommitting(Polymorph<WarehouseShipmentAddon, BinContentInfo> poly) {
                    WarehouseShipmentAddon addon = poly.getAddonEntity();
                    addon.setSubmitDate(AllFuncModelImpl.getCurrentDatetime());
                    addon.setWhseShptLineNo(AllFuncModelImpl.getTempInt());
                    ApiTool.addWarehouseShptConfirmBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, this));
                }

            };
            allFuncModel.processList(datas, listener);
        }
        allFuncModel.onAllCommitted(true, "");
    }

}
