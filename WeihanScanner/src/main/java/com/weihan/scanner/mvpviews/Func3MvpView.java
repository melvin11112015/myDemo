package com.weihan.scanner.mvpviews;


import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehousePutAwayAddon;

import java.util.List;

public interface Func3MvpView extends BaseFuncMvpView {

    void fillRecycler(List<Polymorph<WarehousePutAwayAddon, BinContentInfo>> datas);

    void fillRecyclerWithRecommandInfo(List<BinContentInfo> datasRecommandInfo);

    void notifyAdapter();

}
