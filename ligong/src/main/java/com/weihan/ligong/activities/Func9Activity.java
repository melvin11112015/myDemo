package com.weihan.ligong.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.weihan.ligong.BaseMVP.BaseFuncActivity;
import com.weihan.ligong.R;
import com.weihan.ligong.entities.BinContentInfo;
import com.weihan.ligong.mvpviews.Func9MvpView;
import com.weihan.ligong.presenters.Func9PresenterImpl;
import com.weihan.ligong.utils.ViewHelper;

import java.util.List;

public class Func9Activity extends BaseFuncActivity<Func9PresenterImpl> implements Func9MvpView, View.OnClickListener {

    EditText etItemno, etBincode, etQuantity;
    Button buttonAdd, buttonSubmit;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func9);

        findView();
        initWidget();
    }

    @Override
    protected Func9PresenterImpl buildPresenter() {
        return new Func9PresenterImpl();
    }

    @Override
    public void initWidget() {
        buttonSubmit.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        /*
        adapter = new WhseTransferMultiListAdapter(datas);
        AdapterHelper.setAdapterEmpty(this, adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_item_func8_delete) {
                    buildDeleteDialog(adapter, position);
                    ((WhseTransferMultiListAdapter) adapter).setSelectedPosition(-1);
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ((WhseTransferMultiListAdapter) adapter).setSelectedPosition(position);
            }
        });
        recyclerView.setAdapter(adapter);
        */

        ViewHelper.setIntOnlyInputFilterForEditText(etQuantity);

        loadPref();
    }

    @Override
    public void findView() {
        etItemno = findViewById(R.id.et_func9_itemno);
        etBincode = findViewById(R.id.et_func9_bincode);
        etQuantity = findViewById(R.id.et_func9_count);
        buttonAdd = findViewById(R.id.button_func9_add);
        buttonSubmit = findViewById(R.id.button_func9_submit);
        recyclerView = findViewById(R.id.recycler_func9);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonAdd) {

        } else if (view == buttonSubmit) {

        }
    }

    @Override
    protected void savePref(boolean isToClear) {

    }

    @Override
    protected void loadPref() {

    }

    @Override
    public void fillRecycler(List<BinContentInfo> datas) {

    }

    @Override
    public void clearDatas() {

    }


}
