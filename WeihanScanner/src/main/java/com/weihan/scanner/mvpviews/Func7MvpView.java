package com.weihan.scanner.mvpviews;

import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.OutstandingSalesLineInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseShipmentAddon;

import java.util.List;

public interface Func7MvpView extends BaseFuncMvpView {

    void fillRecycler(List<Polymorph<List<Polymorph<WarehouseShipmentAddon, BinContentInfo>>, OutstandingSalesLineInfo>> datas);

    void notifyAdapter();

}
