package com.weihan.scanner.mvpviews;

import android.support.annotation.StringRes;

import com.weihan.scanner.BaseMVP.IBaseView;

import java.util.List;
import java.util.Map;

public interface HomeMvpView extends IBaseView {
    void initGridAdapter(final List<Map<String, Integer>> data);

    void toCorrespondingActivity(Class<?> clazz, @StringRes int title);
}
