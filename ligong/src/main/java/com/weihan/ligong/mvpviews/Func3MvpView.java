package com.weihan.ligong.mvpviews;


import com.weihan.ligong.entities.BinContentInfo;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.entities.WarehousePutAwayAddon;

import java.util.List;

public interface Func3MvpView extends BaseFuncMvpView {

    void fillRecycler(List<Polymorph<WarehousePutAwayAddon, BinContentInfo>> datas);

    void notifyAdapter();

}
