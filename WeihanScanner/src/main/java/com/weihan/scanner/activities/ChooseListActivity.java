package com.weihan.scanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.weihan.scanner.BaseMVP.BaseActivity;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.OutputPutAwayAddon;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.mvpviews.Func9MvpView;
import com.weihan.scanner.presenters.Func11PresenterImpl;
import com.weihan.scanner.presenters.Func9PresenterImpl;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.scanner.Constant.KEY_CODE;
import static com.weihan.scanner.Constant.KEY_PARAM;
import static com.weihan.scanner.Constant.RESULT_SUCCESS;

public class ChooseListActivity extends BaseActivity<Func9PresenterImpl> implements Func9MvpView {

    RecyclerView recyclerView;
    private Func11PresenterImpl.BinContentListAdapter adapter;
    private List<BinContentInfo> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_list);

        findView();
        initWidget();

        presenter.acquireBincontentWithStoreIssue(getIntent().getStringExtra(KEY_CODE));
    }


    @Override
    protected Func9PresenterImpl buildPresenter() {
        return new Func9PresenterImpl();
    }


    @Override
    public void fillChooseListRecycler(List<BinContentInfo> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void fillRecycler(List<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>> datas) {

    }

    @Override
    public void notifyAdapter() {

    }

    @Override
    public void clearDatas() {

    }

    @Override
    public void exitActivity() {
        finish();
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
                intent.putExtra(KEY_PARAM, datas.get(position).getQuantity_Base());
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
