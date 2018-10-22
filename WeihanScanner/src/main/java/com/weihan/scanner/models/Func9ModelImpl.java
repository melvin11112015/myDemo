package com.weihan.scanner.models;

import com.weihan.scanner.BaseMVP.IBaseModel;
import com.weihan.scanner.WApp;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.OutputPutAwayAddon;
import com.weihan.scanner.entities.Polymorph;

import java.util.ArrayList;
import java.util.List;

public class Func9ModelImpl implements IBaseModel {

    private Func9ModelImpl() {
    }

    public static Polymorph<OutputPutAwayAddon, OutputPutAwayAddon> createPoly(String itemno, String bincode, String quantity) {

        OutputPutAwayAddon addon = new OutputPutAwayAddon();

        addon.setItemNo(itemno);
        addon.setBinCode(bincode);
        addon.setTerminalID(WApp.userInfo.getUserid());
        addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
        addon.setQuantity(quantity);

        return new Polymorph<>(addon, null, Polymorph.State.UNCOMMITTED);
    }

    public static List<BinContentInfo> filtStoreIssueItem(List<BinContentInfo> datas) {
        List<BinContentInfo> tempList = new ArrayList<>();
        for (BinContentInfo info : datas)
            if (info.isStore_Issue()) tempList.add(info);
        return tempList;
    }

}
