package com.weihan.scanner.models;

import com.weihan.scanner.BaseMVP.IBaseModel;
import com.weihan.scanner.WApp;
import com.weihan.scanner.entities.PhysicalInvtAddon;
import com.weihan.scanner.entities.Polymorph;

import java.util.Random;

public class Func2ModelImpl implements IBaseModel {

    private Func2ModelImpl() {
    }

    public static Polymorph<PhysicalInvtAddon, PhysicalInvtAddon> createPoly(String bincode, String itemno) {

        PhysicalInvtAddon addon = new PhysicalInvtAddon();

        addon.setItemNo(itemno);
        addon.setBinCode(bincode);
        addon.setTerminalID(WApp.userInfo.getUserid());
        addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
        addon.setLineNo(Math.abs(new Random().nextInt()));
        addon.setQuantity("1");

        return new Polymorph<>(addon, null, Polymorph.State.UNCOMMITTED);
    }

}
