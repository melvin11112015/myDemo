package com.weihan.scanner.models;

import com.weihan.scanner.BaseMVP.IBaseModel;
import com.weihan.scanner.WApp;
import com.weihan.scanner.entities.ConsumptionPickAddon;
import com.weihan.scanner.entities.InvPickingInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Func1ModelImpl implements IBaseModel {

    private Func1ModelImpl() {
    }

    public static List<Polymorph<ConsumptionPickAddon, InvPickingInfo>> createPolymorphList(List<InvPickingInfo> datas) {
        List<Polymorph<ConsumptionPickAddon, InvPickingInfo>> polymorphs = new ArrayList<>();
        for (InvPickingInfo info : datas) {

            String quantity = info.getQuantity_Base();
            if (quantity == null || quantity.isEmpty() || !TextUtils.isIntString(quantity))
                continue;
            if (Integer.valueOf(quantity) == 0) continue;

            ConsumptionPickAddon addon = new ConsumptionPickAddon();
            addon.setItemNo(info.getItem_No());
            addon.setTerminalID(WApp.userInfo.getUserid());
            addon.setDocument_No(info.getInv_Document_No());
            addon.setLine_No(info.getLine_No());
            addon.setQuantity(quantity);
            polymorphs.add(new Polymorph<>(addon, info, Polymorph.State.UNCOMMITTED));
        }
        return polymorphs;
    }


}
