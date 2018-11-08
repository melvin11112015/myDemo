package com.weihan.scanner.mvpviews;

import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.ConsumptionPickAddon;
import com.weihan.scanner.entities.InvPickingInfo;
import com.weihan.scanner.entities.Polymorph;

import java.util.List;

public interface Func1MvpView extends BaseFuncMvpView {

    void fillRecycler(List<Polymorph<List<Polymorph<ConsumptionPickAddon, BinContentInfo>>, InvPickingInfo>> datas);

    void notifyAdapter();

}
