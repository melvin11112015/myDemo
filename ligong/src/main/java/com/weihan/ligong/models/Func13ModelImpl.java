package com.weihan.ligong.models;

import com.weihan.ligong.BaseMVP.IBaseModel;
import com.weihan.ligong.LiGongApp;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.entities.WarehouseTransferMultiAddon;
import com.weihan.ligong.entities.WhseTransferMultiInfo;
import com.weihan.ligong.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Func13ModelImpl implements IBaseModel {

    private Func13ModelImpl() {
    }

    public static List<Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo>> createPolymorphList(List<WhseTransferMultiInfo> datas, String bincode) {
        List<Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo>> polymorphs = new ArrayList<>();
        for (WhseTransferMultiInfo info : datas) {

            String quantity = info.getQuantity();
            if (quantity == null || quantity.isEmpty() || !TextUtils.isIntString(quantity))
                continue;
            ;
            if (Integer.valueOf(quantity) == 0) continue;

            WarehouseTransferMultiAddon addon = new WarehouseTransferMultiAddon();
            addon.setItemNo(info.getItemNo());
            addon.setTerminalID(LiGongApp.userInfo.getUserid());
            addon.setQuantity(quantity);
            addon.setToBinCode(bincode);
            addon.setToLocationCode(bincode);
            addon.setFromBinCode(info.getFromBinCode());
            addon.setFromLocationCode(info.getFromLocationCode());
            polymorphs.add(new Polymorph<>(addon, info, Polymorph.State.UNCOMMITTED));
        }
        return polymorphs;
    }
}
