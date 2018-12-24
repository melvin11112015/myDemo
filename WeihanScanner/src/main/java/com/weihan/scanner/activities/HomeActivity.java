package com.weihan.scanner.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.weihan.scanner.BaseMVP.BaseActivity;
import com.weihan.scanner.R;
import com.weihan.scanner.mvpviews.HomeMvpView;
import com.weihan.scanner.presenters.HomePresenterImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.weihan.scanner.Constant.KEY_IMAGE_ID;
import static com.weihan.scanner.Constant.KEY_TITLE;

public class HomeActivity extends BaseActivity<HomePresenterImpl> implements HomeMvpView {

    GridView gridView;

    @Override
    public void findView() {
        gridView = findViewById(R.id.gridview_main);
    }

    @Override
    public void initGridAdapter(final List<Map<String, Integer>> data) {
        List<Map<String, Object>> objatas = new ArrayList<>();
        for (Map<String, Integer> map : data) {
            Map<String, Object> mapObj = new HashMap<>();
            mapObj.put(KEY_IMAGE_ID, map.get(KEY_IMAGE_ID));
            mapObj.put(KEY_TITLE, getString(map.get(KEY_TITLE)));
            objatas.add(mapObj);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                objatas,
                R.layout.item_grid,
                new String[]{KEY_IMAGE_ID, KEY_TITLE},
                new int[]{R.id.iv_grid, R.id.tv_grid});
        gridView.setAdapter(simpleAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                presenter.processClickEvent(position, data.get(position).get(KEY_TITLE));
            }
        });
    }

    @Override
    public void toCorrespondingActivity(Class<?> clazz, @StringRes int title) {
        if (clazz == null) return;
        Intent intent = new Intent(HomeActivity.this, clazz);
        intent.putExtra(KEY_TITLE, title);
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
