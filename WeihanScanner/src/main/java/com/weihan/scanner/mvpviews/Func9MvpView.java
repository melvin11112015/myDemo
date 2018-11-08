package com.weihan.scanner.mvpviews;


import com.weihan.scanner.entities.OutputPutAwayAddon;
import com.weihan.scanner.entities.Polymorph;

import java.util.List;

public interface Func9MvpView extends BaseFuncMvpView {

    void fillRecycler(List<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>> datas);

    void notifyAdapter();

    void clearDatas();


}
