package com.weihan.scanner.models;

import com.weihan.scanner.BaseMVP.IBaseModel;
import com.weihan.scanner.WApp;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseTransferMultiAddon;
import com.weihan.scanner.entities.WhseTransferMultiInfo;
import com.weihan.scanner.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Func13ModelImpl implements IBaseModel {

    private Func13ModelImpl() {
    }

    public static List<Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo>> createPolymorphList(List<WhseTransferMultiInfo> datas, String WBcode) {
        List<Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo>> polymorphs = new ArrayList<>();
        for (WhseTransferMultiInfo info : datas) {

            String quantity = info.getQuantity();
            if (quantity == null || quantity.isEmpty() || !TextUtils.isNumeric(quantity))
                continue;
            ;
            if (Double.valueOf(quantity) == 0) continue;

            WarehouseTransferMultiAddon addon = new WarehouseTransferMultiAddon();
            addon.setItemNo(info.getItemNo());
            addon.setTerminalID(WApp.userInfo.getUserid());
            addon.setQuantity(quantity);
            addon.setToBinCode(AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_BIN));
            addon.setToLocationCode(AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_LOCATION));
            addon.setFromBinCode(info.getFromBinCode());
            addon.setFromLocationCode(info.getFromLocationCode());
            addon.setTransferNo(WApp.barcodeSettings.getMachineCode() + WApp.userInfo.getUserid() + AllFuncModelImpl.getTempInt());
            polymorphs.add(new Polymorph<>(addon, info, Polymorph.State.UNCOMMITTED));
        }
        return polymorphs;
    }
}
