package com.weihan.scanner.mvpviews;


import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseTransferSingleAddon;

import java.util.List;

public interface Func8MvpView extends BaseFuncMvpView {

    void fillRecycler(List<Polymorph<WarehouseTransferSingleAddon, BinContentInfo>> datas);

    void notifyAdapter();

}
