package com.weihan.ligong.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.weihan.ligong.BaseMVP.BaseActivity;
import com.weihan.ligong.R;
import com.weihan.ligong.entities.BinContentInfo;
import com.weihan.ligong.mvpviews.Func11MvpView;
import com.weihan.ligong.presenters.Func11PresenterImpl;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.ligong.Constant.KEY_CODE;
import static com.weihan.ligong.Constant.RESULT_SUCCESS;

public class ChooseListActivity extends BaseActivity<Func11PresenterImpl> implements Func11MvpView {

    RecyclerView recyclerView;
    private Func11PresenterImpl.BinContentListAdapter adapter;
    private List<BinContentInfo> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_list);

        findView();
        initWidget();

        presenter.acquireDatas0(getIntent().getStringExtra(KEY_CODE));
    }


    @Override
    protected Func11PresenterImpl buildPresenter() {
        return new Func11PresenterImpl();
    }


    @Override
    public void fillRecycler(List<BinContentInfo> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void clearDatas() {

    }

    @Override
    public void initWidget() {
        recyclerView.setLayoutManager(new LinearLayoutManager(ChooseListActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(ChooseListActivity.this, DividerItemDecoration.VERTICAL));
        adapter = new Func11PresenterImpl.BinContentListAdapter(datas);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra(KEY_CODE, datas.get(position).getBin_Code());
                setResult(RESULT_SUCCESS, intent);
                finish();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void findView() {
        recyclerView = findViewById(R.id.recycler_choose_list);
    }

}
