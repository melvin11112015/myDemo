package com.weihan.scanner.presenters;

import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.models.HomeIconModelImpl;
import com.weihan.scanner.mvpviews.HomeMvpView;

import java.util.List;
import java.util.Map;

public class HomePresenterImpl extends BasePresenter<HomeMvpView> implements HomeIconModelImpl.Callback {

    private HomeIconModelImpl model = new HomeIconModelImpl();

    private int title;

    public void getData() {
        model.generateDataList(this);
    }

    public void processClickEvent(int position, int titleResId) {
        this.title = titleResId;
        model.findClass(this, position);
    }

    @Override
    public void onListComplete(List<Map<String, Integer>> data) {
        getView().initGridAdapter(data);
    }

    @Override
    public void onClassFound(Class<?> clazz) {
        getView().toCorrespondingActivity(clazz, title);
    }


}
