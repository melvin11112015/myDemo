package com.weihan.scanner.mvpviews;


import com.weihan.scanner.entities.BinContentInfo;

import java.util.List;

public interface ChooseListMvpView extends BaseFuncMvpView {

    void fillChooseListRecycler(List<BinContentInfo> datas);

    void exitActivity();

    void foundItem(BinContentInfo info);

}
