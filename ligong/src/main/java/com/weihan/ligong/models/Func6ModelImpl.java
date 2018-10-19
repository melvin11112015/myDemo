package com.weihan.ligong.models;

import com.weihan.ligong.BaseMVP.IBaseModel;
import com.weihan.ligong.LiGongApp;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.entities.ProdOutputAddon;

import java.util.Random;

public class Func6ModelImpl implements IBaseModel {

    private Func6ModelImpl() {
    }

    public static Polymorph<ProdOutputAddon, ProdOutputAddon> createPoly(String importCode, String itemno) {

        ProdOutputAddon addon = new ProdOutputAddon();

        addon.setBarcode(itemno);
        addon.setOutputNo(importCode);
        addon.setTerminalID(LiGongApp.userInfo.getUserid());
        addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
        addon.setLineNo(Math.abs(new Random().nextInt()));
        addon.setQuantity("1");

        return new Polymorph<>(addon, null, Polymorph.State.UNCOMMITTED);
    }

}
