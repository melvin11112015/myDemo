package com.weihan.scanner.models;

import com.weihan.scanner.BaseMVP.IBaseModel;
import com.weihan.scanner.WApp;
import com.weihan.scanner.entities.OutstandingSalesLineInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseShipmentAddon;
import com.weihan.scanner.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Func7ModelImpl implements IBaseModel {

    private Func7ModelImpl() {
    }

    public static List<Polymorph<WarehouseShipmentAddon, OutstandingSalesLineInfo>> createPolymorphList(List<OutstandingSalesLineInfo> datas) {
        List<Polymorph<WarehouseShipmentAddon, OutstandingSalesLineInfo>> polymorphs = new ArrayList<>();
        for (OutstandingSalesLineInfo info : datas) {

            String quantity = info.getOutstanding_Quantity();
            if (quantity == null || quantity.isEmpty() || !TextUtils.isIntString(quantity))
                continue;
            if (Integer.valueOf(quantity) == 0) continue;

            WarehouseShipmentAddon addon = new WarehouseShipmentAddon();
            addon.setItemNo(info.getNo());
            addon.setTerminalID(WApp.userInfo.getUserid());
            addon.setWhseShptNo(info.getDocument_No());
            addon.setWhseShptLineNo(info.getLine_No());
            addon.setQuantity(quantity);
            polymorphs.add(new Polymorph<>(addon, info, Polymorph.State.UNCOMMITTED));
        }
        return polymorphs;
    }


}
