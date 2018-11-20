package com.weihan.scanner.mvpviews;

import com.weihan.scanner.entities.ConsumptionPickConfirmAddon;
import com.weihan.scanner.entities.InvPickingInfo;
import com.weihan.scanner.entities.Polymorph;

import java.util.List;

public interface Func4MvpView extends BaseFuncMvpView {

    void fillRecycler(List<Polymorph<ConsumptionPickConfirmAddon, InvPickingInfo>> datas);

    void notifyAdapter();

    void uncheckAdpaterBox();

}
