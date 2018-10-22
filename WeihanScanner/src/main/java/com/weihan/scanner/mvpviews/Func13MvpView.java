package com.weihan.scanner.mvpviews;


import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseTransferMultiAddon;
import com.weihan.scanner.entities.WhseTransferMultiInfo;

import java.util.List;

public interface Func13MvpView extends BaseFuncMvpView {

    void fillRecycler(List<Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo>> datas);

    void notifyAdapter();

}
