package com.weihan.scanner.mvpviews;


import com.weihan.scanner.entities.BinContentInfo;

import java.util.List;

public interface Func11MvpView extends BaseFuncMvpView {

    void fillRecycler(List<BinContentInfo> datas);

    void clearDatas();


}
