package com.weihan.ligong.models;

import com.weihan.ligong.BaseMVP.IBaseModel;
import com.weihan.ligong.LiGongApp;
import com.weihan.ligong.entities.BinContentInfo;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.entities.WarehouseTransferSingleAddon;

import java.util.ArrayList;
import java.util.List;

public class Func8ModelImpl implements IBaseModel {

    private Func8ModelImpl() {
    }

    public static List<Polymorph<WarehouseTransferSingleAddon, BinContentInfo>> createPolymorphList(List<BinContentInfo> datas) {
        List<Polymorph<WarehouseTransferSingleAddon, BinContentInfo>> polymorphs = new ArrayList<>();
        for (BinContentInfo info : datas) {
            WarehouseTransferSingleAddon addon = new WarehouseTransferSingleAddon();
            addon.setItemNo(info.getItem_No());
            addon.setTerminalID(LiGongApp.userInfo.getUserid());
            addon.setQuantity("");
            addon.setToBinCode("");
            addon.setToLocationCode("");
            addon.setFromBinCode(info.getBin_Code());
            addon.setFromLocationCode(info.getLocation_Code());
            polymorphs.add(new Polymorph<>(addon, info, Polymorph.State.UNCOMMITTED));
        }
        return polymorphs;
    }
}
