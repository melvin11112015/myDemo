package com.weihan.ligong.mvpviews;

import com.weihan.ligong.BaseMVP.IBaseView;

public interface LoginSaveView extends IBaseView {
    void initLoginInfo();

    void saveLoginInfo(String username, String password);
}
