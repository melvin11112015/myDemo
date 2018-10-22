package com.weihan.scanner.mvpviews;

import com.weihan.scanner.entities.OutstandingPurchLineInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseReceiptAddon;

import java.util.List;

public interface Func0MvpView extends BaseFuncMvpView {

    void fillRecycler(List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>> datas);

    void notifyAdapter();

}
