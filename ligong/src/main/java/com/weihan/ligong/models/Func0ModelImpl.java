package com.weihan.ligong.models;

import com.weihan.ligong.BaseMVP.IBaseModel;
import com.weihan.ligong.LiGongApp;
import com.weihan.ligong.entities.OutstandingPurchLineInfo;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.entities.WarehouseReceiptAddon;
import com.weihan.ligong.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Func0ModelImpl implements IBaseModel {

    private Func0ModelImpl() {
    }

    public static List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>> createPolymorphList(List<OutstandingPurchLineInfo> datas) {
        List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>> polymorphs = new ArrayList<>();
        for (OutstandingPurchLineInfo info : datas) {

            String quantity = info.getOutstanding_Quantity();
            if (quantity == null || quantity.isEmpty() || !TextUtils.isIntString(quantity))
                continue;
            if (Integer.valueOf(quantity) == 0) continue;

            WarehouseReceiptAddon addon = new WarehouseReceiptAddon();
            addon.setItemNo(info.getNo());
            addon.setPurchOrderNo(info.getDocument_No());
            addon.setTerminalID(LiGongApp.userInfo.getUserid());
            addon.setQuantity(quantity);
            polymorphs.add(new Polymorph<>(addon, info, Polymorph.State.UNCOMMITTED));
        }
        return polymorphs;
    }
}
