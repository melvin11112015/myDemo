package com.weihan.scanner.models;

import com.weihan.scanner.BaseMVP.IBaseModel;
import com.weihan.scanner.WApp;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.OutstandingSalesLineInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseShipmentAddon;

import java.util.ArrayList;
import java.util.List;

public class Func7ModelImpl implements IBaseModel {

    private Func7ModelImpl() {
    }

    public static List<Polymorph<List<Polymorph<WarehouseShipmentAddon, BinContentInfo>>, OutstandingSalesLineInfo>> createPolymorphList(List<OutstandingSalesLineInfo> datas) {
        List<Polymorph<List<Polymorph<WarehouseShipmentAddon, BinContentInfo>>, OutstandingSalesLineInfo>> polymorphs = new ArrayList<>();
        for (OutstandingSalesLineInfo info : datas) {
            List<Polymorph<WarehouseShipmentAddon, BinContentInfo>> addons = new ArrayList<>();
            polymorphs.add(new Polymorph<>(addons, info, Polymorph.State.UNCOMMITTED));
        }
        return polymorphs;
    }


    public static Polymorph<WarehouseShipmentAddon, BinContentInfo> createPoly(BinContentInfo info, OutstandingSalesLineInfo infoHeader) {

        WarehouseShipmentAddon addon = new WarehouseShipmentAddon();
        addon.setItemNo(info.getItem_No());
        addon.setTerminalID(WApp.userInfo.getUserid());
        addon.setWhseShptNo(infoHeader.getDocument_No());
        addon.setQuantity("0");
        addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
        addon.setLocationCode(info.getLocation_Code());
        addon.setBinCode(info.getBin_Code());

        return new Polymorph<>(addon, info, Polymorph.State.UNCOMMITTED);
    }

}
