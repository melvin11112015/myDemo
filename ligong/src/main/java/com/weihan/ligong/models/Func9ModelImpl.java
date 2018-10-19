package com.weihan.ligong.models;

import com.weihan.ligong.BaseMVP.IBaseModel;
import com.weihan.ligong.LiGongApp;
import com.weihan.ligong.entities.BinContentInfo;
import com.weihan.ligong.entities.OutputPutAwayAddon;
import com.weihan.ligong.entities.Polymorph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Func9ModelImpl implements IBaseModel {

    private Func9ModelImpl() {
    }

    public static Polymorph<OutputPutAwayAddon, OutputPutAwayAddon> createPoly(String importCode, String itemno) {

        OutputPutAwayAddon addon = new OutputPutAwayAddon();

        addon.setBarcode(itemno);
        addon.setTerminalID(LiGongApp.userInfo.getUserid());
        addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
        addon.setLineNo(Math.abs(new Random().nextInt()));
        addon.setQuantity("1");

        return new Polymorph<>(addon, null, Polymorph.State.UNCOMMITTED);
    }

    public static List<BinContentInfo> filtStoreIssueItem(List<BinContentInfo> datas) {
        List<BinContentInfo> tempList = new ArrayList<>();
        for (BinContentInfo info : datas)
            if (info.isStore_Issue()) tempList.add(info);
        return tempList;
    }

}
