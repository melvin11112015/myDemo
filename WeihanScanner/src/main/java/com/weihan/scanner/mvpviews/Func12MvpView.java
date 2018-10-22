package com.weihan.scanner.mvpviews;


import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseTransferMultiAddon;

import java.util.List;

public interface Func12MvpView extends BaseFuncMvpView {

    void fillRecycler(List<Polymorph<WarehouseTransferMultiAddon, BinContentInfo>> datas);

    void notifyAdapter();

}
