package com.weihan.scanner.models;

import com.weihan.scanner.BaseMVP.IBaseModel;
import com.weihan.scanner.WApp;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.ProdOutputAddon;

import java.util.Random;

public class Func6ModelImpl implements IBaseModel {

    private Func6ModelImpl() {
    }

    public static Polymorph<ProdOutputAddon, ProdOutputAddon> createPoly(String importCode, String itemno) {

        ProdOutputAddon addon = new ProdOutputAddon();

        addon.setBarcode(itemno);
        addon.setOutputNo(importCode);
        addon.setTerminalID(WApp.userInfo.getUserid());
        addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
        addon.setLineNo(Math.abs(new Random().nextInt()));
        addon.setQuantity("1");

        return new Polymorph<>(addon, null, Polymorph.State.UNCOMMITTED);
    }

}
