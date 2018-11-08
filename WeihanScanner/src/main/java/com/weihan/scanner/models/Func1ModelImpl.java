package com.weihan.scanner.models;

import com.weihan.scanner.BaseMVP.IBaseModel;
import com.weihan.scanner.WApp;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.ConsumptionPickAddon;
import com.weihan.scanner.entities.InvPickingInfo;
import com.weihan.scanner.entities.Polymorph;

import java.util.ArrayList;
import java.util.List;

public class Func1ModelImpl implements IBaseModel {

    private Func1ModelImpl() {
    }

    public static List<Polymorph<List<Polymorph<ConsumptionPickAddon, BinContentInfo>>, InvPickingInfo>> createPolymorphList(List<InvPickingInfo> datas) {
        List<Polymorph<List<Polymorph<ConsumptionPickAddon, BinContentInfo>>, InvPickingInfo>> polymorphs = new ArrayList<>();
        for (InvPickingInfo info : datas) {
            List<Polymorph<ConsumptionPickAddon, BinContentInfo>> addons = new ArrayList<>();
            polymorphs.add(new Polymorph<>(addons, info, Polymorph.State.UNCOMMITTED));
        }
        return polymorphs;
    }


    public static Polymorph<ConsumptionPickAddon, BinContentInfo> createPoly(BinContentInfo info, InvPickingInfo infoHeader) {

        ConsumptionPickAddon addon = new ConsumptionPickAddon();
        addon.setItemNo(info.getItem_No());
        addon.setLine_No(infoHeader.getLine_No());
        addon.setTerminalID(WApp.userInfo.getUserid());
        addon.setDocument_No(infoHeader.getInv_Document_No());
        addon.setQuantity("0");
        addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
        addon.setLocationCode(info.getLocation_Code());
        addon.setBinCode(info.getBin_Code());

        return new Polymorph<>(addon, info, Polymorph.State.UNCOMMITTED);
    }


}
