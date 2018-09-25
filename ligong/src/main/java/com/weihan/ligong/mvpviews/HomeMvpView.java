package com.weihan.ligong.mvpviews;

import com.weihan.ligong.BaseMVP.IBaseView;

import java.util.List;
import java.util.Map;

public interface HomeMvpView extends IBaseView {
    void initGridAdapter(final List<Map<String, Object>> data);

    void toCorrespondingActivity(Class<?> clazz);
}
