package com.weihan.scanner.models;

import com.weihan.scanner.BaseMVP.IBaseModel;
import com.weihan.scanner.WApp;
import com.weihan.scanner.entities.ConsumptionPickConfirmAddon;
import com.weihan.scanner.entities.InvPickingInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Func4ModelImpl implements IBaseModel {

    private Func4ModelImpl() {
    }

    public static List<Polymorph<ConsumptionPickConfirmAddon, InvPickingInfo>> createPolymorphList(List<InvPickingInfo> datas) {
        List<Polymorph<ConsumptionPickConfirmAddon, InvPickingInfo>> polymorphs = new ArrayList<>();
        for (InvPickingInfo info : datas) {

            String quantity = info.getQuantity_Base();
            if (quantity == null || quantity.isEmpty() || !TextUtils.isNumeric(quantity))
                continue;
            if (Double.valueOf(quantity) == 0) continue;

            ConsumptionPickConfirmAddon addon = new ConsumptionPickConfirmAddon();
            addon.setItemNo(info.getItem_No());
            addon.setLine_No(info.getLine_No());
            addon.setDocument_No(info.getInv_Document_No());
            addon.setTerminalID(WApp.userInfo.getUserid());
            addon.setQuantity(quantity);
            addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
            polymorphs.add(new Polymorph<>(addon, info, Polymorph.State.FAILURE));//特别处理
        }
        return polymorphs;
    }
}
