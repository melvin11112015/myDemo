package com.weihan.ligong.BaseMVP;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {

    protected P presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = buildPresenter();
        if (this instanceof IBaseView) presenter.attachView((IBaseView) this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
