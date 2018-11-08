package com.weihan.scanner.models;

import com.weihan.scanner.BaseMVP.IBaseModel;
import com.weihan.scanner.WApp;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseTransferSingleAddon;
import com.weihan.scanner.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Func8ModelImpl implements IBaseModel {

    private Func8ModelImpl() {
    }

    public static List<Polymorph<WarehouseTransferSingleAddon, BinContentInfo>> createPolymorphList(List<BinContentInfo> datas) {
        List<Polymorph<WarehouseTransferSingleAddon, BinContentInfo>> polymorphs = new ArrayList<>();
        for (BinContentInfo info : datas) {

            String quantity = info.getQuantity_Base();
            if (quantity == null || quantity.isEmpty() || !TextUtils.isNumeric(quantity))
                continue;
            ;
            if (Double.valueOf(quantity) == 0) continue;

            WarehouseTransferSingleAddon addon = new WarehouseTransferSingleAddon();
            addon.setItemNo(info.getItem_No());
            addon.setTerminalID(WApp.userInfo.getUserid());
            addon.setTransferNo(WApp.barcodeSettings.getMachineCode() + WApp.userInfo.getUserid() + AllFuncModelImpl.getTempInt());
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
