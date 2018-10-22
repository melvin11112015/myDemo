package com.weihan.scanner.mvpviews;


import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.OutputPutAwayAddon;
import com.weihan.scanner.entities.Polymorph;

import java.util.List;

public interface Func9MvpView extends BaseFuncMvpView {

    void fillChooseListRecycler(List<BinContentInfo> datas);

    void fillRecycler(List<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>> datas);

    void notifyAdapter();

    void clearDatas();

    void exitActivity();

}
