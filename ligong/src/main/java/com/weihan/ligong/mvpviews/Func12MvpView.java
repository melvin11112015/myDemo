package com.weihan.ligong.mvpviews;


import com.weihan.ligong.entities.BinContentInfo;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.entities.WarehouseTransferMultiFromAddon;

import java.util.List;

public interface Func12MvpView extends BaseFuncMvpView {

    void fillRecycler(List<Polymorph<WarehouseTransferMultiFromAddon, BinContentInfo>> datas);

    void notifyAdapter();

}
