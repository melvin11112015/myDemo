package com.weihan.scanner.models;

import com.weihan.scanner.BaseMVP.IBaseModel;
import com.weihan.scanner.WApp;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseTransferMultiAddon;
import com.weihan.scanner.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Func12ModelImpl implements IBaseModel {

    private Func12ModelImpl() {
    }

    public static List<Polymorph<WarehouseTransferMultiAddon, BinContentInfo>> createPolymorphList(List<BinContentInfo> datas) {
        List<Polymorph<WarehouseTransferMultiAddon, BinContentInfo>> polymorphs = new ArrayList<>();
        for (BinContentInfo info : datas) {

            String quantity = info.getQuantity_Base();
            if (quantity == null || quantity.isEmpty() || !TextUtils.isNumeric(quantity))
                continue;
            if (Double.valueOf(quantity) == 0) continue;

            WarehouseTransferMultiAddon addon = new WarehouseTransferMultiAddon();
            addon.setItemNo(info.getItem_No());
            addon.setTerminalID(WApp.userInfo.getUserid());
            addon.setQuantity(quantity);
            addon.setToBinCode("");
            addon.setToLocationCode("");
            addon.setFromBinCode(info.getBin_Code());
            addon.setFromLocationCode(info.getLocation_Code());
            addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
            polymorphs.add(new Polymorph<>(addon, info, Polymorph.State.UNCOMMITTED));
        }
        return polymorphs;
    }
}
