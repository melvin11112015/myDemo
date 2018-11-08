package com.weihan.scanner.models;

import com.weihan.scanner.BaseMVP.IBaseModel;
import com.weihan.scanner.WApp;
import com.weihan.scanner.entities.OutstandingPurchLineInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseReceiptAddon;
import com.weihan.scanner.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Func0ModelImpl implements IBaseModel {

    private Func0ModelImpl() {
    }

    public static List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>> createPolymorphList(List<OutstandingPurchLineInfo> datas) {
        List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>> polymorphs = new ArrayList<>();
        for (OutstandingPurchLineInfo info : datas) {

            String quantity = info.getOutstanding_Quantity();
            if (quantity == null || quantity.isEmpty() || !TextUtils.isNumeric(quantity))
                continue;
            if (Double.valueOf(quantity) == 0) continue;

            WarehouseReceiptAddon addon = new WarehouseReceiptAddon();
            addon.setItemNo(info.getNo());
            addon.setLineNo(info.getLine_No());
            addon.setPurchOrderNo(info.getDocument_No());
            addon.setTerminalID(WApp.userInfo.getUserid());
            addon.setQuantity(quantity);
            addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
            polymorphs.add(new Polymorph<>(addon, info, Polymorph.State.UNCOMMITTED));
        }
        return polymorphs;
    }
}
