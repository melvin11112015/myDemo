package com.weihan.ligong.models;

import com.weihan.ligong.BaseMVP.IBaseModel;
import com.weihan.ligong.LiGongApp;
import com.weihan.ligong.entities.BinContentInfo;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.entities.WarehousePutAwayAddon;
import com.weihan.ligong.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Func3ModelImpl implements IBaseModel {

    private Func3ModelImpl() {
    }

    public static List<Polymorph<WarehousePutAwayAddon, BinContentInfo>> createPolymorphList(List<BinContentInfo> datas, String binCode) {
        List<Polymorph<WarehousePutAwayAddon, BinContentInfo>> polymorphs = new ArrayList<>();
        for (BinContentInfo info : datas) {

            String quantity = info.getQuantity_Base();
            if (quantity == null || quantity.isEmpty() || !TextUtils.isIntString(quantity))
                continue;
            if (Integer.valueOf(quantity) == 0) continue;

            if (!info.isTemp_Receipt()) continue;

            WarehousePutAwayAddon addon = new WarehousePutAwayAddon();
            addon.setItemNo(info.getItem_No());
            addon.setTerminalID(LiGongApp.userInfo.getUserid());
            addon.setQuantity(quantity);
            addon.setBinCode(binCode);
            polymorphs.add(new Polymorph<>(addon, info, Polymorph.State.UNCOMMITTED));
        }
        return polymorphs;
    }
}
