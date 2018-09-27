package com.weihan.ligong.mvpviews;


import com.weihan.ligong.entities.BinContentInfo;

import java.util.List;

public interface Func11MvpView extends BaseFuncMvpView {

    void fillRecycler(List<BinContentInfo> datas);

    void clearDatas();

}
