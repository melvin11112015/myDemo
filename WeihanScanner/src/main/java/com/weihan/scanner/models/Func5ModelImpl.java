package com.weihan.scanner.models;

import com.weihan.scanner.BaseMVP.IBaseModel;
import com.weihan.scanner.WApp;
import com.weihan.scanner.entities.PhysicalInvtCheckAddon;
import com.weihan.scanner.entities.PhysicalInvtInfo;
import com.weihan.scanner.entities.Polymorph;

import java.util.ArrayList;
import java.util.List;

public class Func5ModelImpl implements IBaseModel {

    private Func5ModelImpl() {
    }

    public static List<Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo>> createPolymorphList(List<PhysicalInvtInfo> datas) {
        List<Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo>> polymorphs = new ArrayList<>();
        for (PhysicalInvtInfo info : datas) {

            /*
            String quantity = info.getQty_Phys_Inventory();
            if (quantity == null || quantity.isEmpty() || !TextUtils.isNumeric(quantity))
                continue;
            if (Double.valueOf(quantity) == 0) continue;
            */

            PhysicalInvtCheckAddon addon = new PhysicalInvtCheckAddon();
            addon.setItemNo(info.getItem_No());
            addon.setJournalBatch(info.getJournal_Batch_Name());
            addon.setTerminalID(WApp.userInfo.getUserid());
            addon.setLineNo(info.getLine_No());
            addon.setQuantity("0");
            addon.setJournalTemplate(info.getJournal_Template_Name());
            addon.setBinCode(info.getBin_Code());
            addon.setLocationCode(info.getLocation_Code());
            addon.setPostingDate(info.getPosting_Date());
            polymorphs.add(new Polymorph<>(addon, info, Polymorph.State.UNCOMMITTED));
        }
        return polymorphs;
    }
}
