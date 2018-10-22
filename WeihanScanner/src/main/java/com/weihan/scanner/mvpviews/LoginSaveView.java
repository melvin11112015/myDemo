package com.weihan.scanner.mvpviews;

import com.weihan.scanner.BaseMVP.IBaseView;

public interface LoginSaveView extends IBaseView {
    void initLoginInfo();

    void saveLoginInfo(String username, String password);
}
