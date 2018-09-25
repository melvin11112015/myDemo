package com.weihan.ligong.mvpviews;

import com.weihan.ligong.BaseMVP.IBaseView;
import com.weihan.ligong.entities.OutstandingPurchLineInfo;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.entities.WarehouseReceiptAddon;

import java.util.List;

public interface Func0MvpView extends IBaseView {
    void initWidget();

    void fillRecycler(List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>> datas);

    void toast(String msg);

    void notifyAdapter();

    void clearDatas();

    void loadPref();

    void savePref(boolean isToClear);
}
