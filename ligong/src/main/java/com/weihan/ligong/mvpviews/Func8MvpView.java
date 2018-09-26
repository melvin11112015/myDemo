package com.weihan.ligong.mvpviews;


import com.weihan.ligong.entities.BinContentInfo;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.entities.WarehouseTransferSingleAddon;

import java.util.List;

public interface Func8MvpView extends BaseFuncMvpView {

    void fillRecycler(List<Polymorph<WarehouseTransferSingleAddon, BinContentInfo>> datas);

    void notifyAdapter();

}
