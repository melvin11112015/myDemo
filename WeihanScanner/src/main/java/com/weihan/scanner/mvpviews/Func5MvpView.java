package com.weihan.scanner.mvpviews;


import com.weihan.scanner.entities.PhysicalInvtCheckAddon;
import com.weihan.scanner.entities.PhysicalInvtInfo;
import com.weihan.scanner.entities.Polymorph;

import java.util.List;

public interface Func5MvpView extends BaseFuncMvpView {

    void fillRecycler(List<Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo>> datas);

    void notifyAdapter();

}
