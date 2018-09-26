package com.weihan.ligong.BaseMVP;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import static com.weihan.ligong.Constant.KEY_TITLE;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {

    protected P presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = buildPresenter();
        if (this instanceof IBaseView) presenter.attachView((IBaseView) this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            String title = getIntent().getStringExtra(KEY_TITLE);
            if (title != null) actionBar.setTitle(title);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.detachView();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * 创建一个与之关联的Presenter
     *
     * @return
     */
    protected abstract P buildPresenter();
}
