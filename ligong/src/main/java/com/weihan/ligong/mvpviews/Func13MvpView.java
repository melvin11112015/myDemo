package com.weihan.ligong.mvpviews;


import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.entities.WarehouseTransferMultiAddon;
import com.weihan.ligong.entities.WhseTransferMultiInfo;

import java.util.List;

public interface Func13MvpView extends BaseFuncMvpView {

    void fillRecycler(List<Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo>> datas);

    void notifyAdapter();

}
