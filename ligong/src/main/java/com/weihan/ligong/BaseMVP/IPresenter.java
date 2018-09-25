package com.weihan.ligong.BaseMVP;

public interface IPresenter<V> {
    void attachView(V view);

    void detachView();
}
