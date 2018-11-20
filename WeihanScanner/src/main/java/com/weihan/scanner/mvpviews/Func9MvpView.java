package com.weihan.scanner.mvpviews;


import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.OutputPutAwayAddon;
import com.weihan.scanner.entities.Polymorph;

import java.util.List;

public interface Func9MvpView extends BaseFuncMvpView {

    void fillRecycler(List<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>> datas);

    void fillRecyclerWithRecommandInfo(List<BinContentInfo> datasRecommandInfo);

    void notifyAdapter();

}
