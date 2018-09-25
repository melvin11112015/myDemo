package com.weihan.ligong.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.weihan.ligong.BaseMVP.BaseActivity;
import com.weihan.ligong.R;
import com.weihan.ligong.mvpviews.HomeMvpView;
import com.weihan.ligong.presenters.HomePresenterImpl;

import java.util.List;
import java.util.Map;

import static com.weihan.ligong.Constant.KEY_IMAGE_ID;
import static com.weihan.ligong.Constant.KEY_TITLE;

public class HomeActivity extends BaseActivity<HomePresenterImpl> implements HomeMvpView {

    GridView gridView;

    @Override
    public void findView() {
        gridView = findViewById(R.id.gridview_main);
    }

    @Override
    public void initGridAdapter(final List<Map<String, Object>> data) {
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                data,
                R.layout.item_grid,
                new String[]{KEY_IMAGE_ID, KEY_TITLE},
                new int[]{R.id.iv_grid, R.id.tv_grid});
        gridView.setAdapter(simpleAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                presenter.processClickEvent(position);
            }
        });
    }

    @Override
    public void toCorrespondingActivity(Class<?> clazz) {
        if (clazz == null) return;
        Intent intent = new Intent(HomeActivity.this, clazz);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findView();
        presenter.getData();
    }

    @Override
    protected HomePresenterImpl buildPresenter() {
        return new HomePresenterImpl();
    }

}
