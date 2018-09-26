package com.weihan.ligong.mvpviews;

import com.weihan.ligong.entities.OutstandingPurchLineInfo;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.entities.WarehouseReceiptAddon;

import java.util.List;

public interface Func0MvpView extends BaseFuncMvpView {

    void fillRecycler(List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>> datas);

    void notifyAdapter();

}
