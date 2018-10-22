package com.weihan.scanner.BaseMVP;

public interface IPresenter<V> {
    void attachView(V view);

    void detachView();
}
