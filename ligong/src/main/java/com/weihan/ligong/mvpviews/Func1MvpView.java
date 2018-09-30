package com.weihan.ligong.mvpviews;

import com.weihan.ligong.entities.ConsumptionPickAddon;
import com.weihan.ligong.entities.InvPickingInfo;
import com.weihan.ligong.entities.Polymorph;

import java.util.List;

public interface Func1MvpView extends BaseFuncMvpView {

    void fillRecycler(List<Polymorph<ConsumptionPickAddon, InvPickingInfo>> datas);

    void notifyAdapter();

}
