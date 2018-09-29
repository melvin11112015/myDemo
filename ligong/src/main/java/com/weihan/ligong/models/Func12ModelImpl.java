package com.weihan.ligong.models;

import com.weihan.ligong.BaseMVP.IBaseModel;
import com.weihan.ligong.LiGongApp;
import com.weihan.ligong.entities.BinContentInfo;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.entities.WarehouseTransferMultiFromAddon;
import com.weihan.ligong.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Func12ModelImpl implements IBaseModel {

    private Func12ModelImpl() {
    }

    public static List<Polymorph<WarehouseTransferMultiFromAddon, BinContentInfo>> createPolymorphList(List<BinContentInfo> datas) {
        List<Polymorph<WarehouseTransferMultiFromAddon, BinContentInfo>> polymorphs = new ArrayList<>();
        for (BinContentInfo info : datas) {

            String quantity = info.getQuantity_Base();
            if (quantity == null || quantity.isEmpty() || !TextUtils.isIntString(quantity))
                continue;
            ;
            if (Integer.valueOf(quantity) == 0) continue;

            WarehouseTransferMultiFromAddon addon = new WarehouseTransferMultiFromAddon();
            addon.setItemNo(info.getItem_No());
            addon.setTerminalID(LiGongApp.userInfo.getUserid());
            addon.setQuantity(quantity);
            addon.setToBinCode("");
            addon.setToLocationCode("");
            addon.setFromBinCode(info.getBin_Code());
            addon.setFromLocationCode(info.getLocation_Code());
            polymorphs.add(new Polymorph<>(addon, info, Polymorph.State.UNCOMMITTED));
        }
        return polymorphs;
    }
}
