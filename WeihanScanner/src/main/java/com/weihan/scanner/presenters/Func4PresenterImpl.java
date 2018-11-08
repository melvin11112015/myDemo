package com.weihan.scanner.presenters;

import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.ConsumptionPickAddon;
import com.weihan.scanner.entities.InvPickingInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.net.ApiTool;

import java.util.List;

public class Func4PresenterImpl extends Func1PresenterImpl {

    @Override
    public void submitDatas(List<Polymorph<List<Polymorph<ConsumptionPickAddon, BinContentInfo>>, InvPickingInfo>> polysdata) {

        for (Polymorph<List<Polymorph<ConsumptionPickAddon, BinContentInfo>>, InvPickingInfo> polys : polysdata) {

            List<Polymorph<ConsumptionPickAddon, BinContentInfo>> datas = polys.getAddonEntity();

            if (!AllFuncModelImpl.checkEmptyList(datas)) return;

            listener = new AllFuncModelImpl.PolyChangeListener<ConsumptionPickAddon, BinContentInfo>() {

                @Override
                public void onPolyChanged(boolean isFinished, String msg) {
                    allFuncModel.onAllCommitted(isFinished, msg);
                    getView().notifyAdapter();
                }

                @Override
                public void goCommitting(Polymorph<ConsumptionPickAddon, BinContentInfo> poly) {
                    ConsumptionPickAddon addon = poly.getAddonEntity();
                    addon.setSubmitDate(AllFuncModelImpl.getCurrentDatetime());
                    ApiTool.addConsumptionPickConfirm_Buffer(addon, allFuncModel.new AllFuncOdataCallback(poly, this));
                }

            };
            allFuncModel.processList(datas, listener);
        }
    }

}
