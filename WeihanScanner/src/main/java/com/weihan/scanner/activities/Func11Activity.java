package com.weihan.scanner.activities;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.weihan.scanner.BaseMVP.BaseActivity;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.mvpviews.Func11MvpView;
import com.weihan.scanner.presenters.Func11PresenterImpl;
import com.weihan.scanner.utils.AdapterHelper;
import com.weihan.scanner.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

public class Func11Activity extends BaseActivity<Func11PresenterImpl> implements Func11MvpView, View.OnClickListener {

    EditText etItemNo, etWBcode;
    Button buttonCheck;
    RecyclerView recyclerView;

    private Func11PresenterImpl.BinContentListAdapter adapter;
    private List<BinContentInfo> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func11);

        findView();
        initWidget();
    }

    @Override
    protected Func11PresenterImpl buildPresenter() {
        return new Func11PresenterImpl();
    }

    @Override
    public void findView() {
        etItemNo = findViewById(R.id.et_func11_itemno);
        etWBcode = findViewById(R.id.et_func11_bincode);
        recyclerView = findViewById(R.id.recycler_func11);
        buttonCheck = findViewById(R.id.button_func11_check1);
    }

    @Override
    public void initWidget() {

        buttonCheck.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Func11PresenterImpl.BinContentListAdapter(datas);
        AdapterHelper.setAdapterEmpty(this, adapter);
        recyclerView.setAdapter(adapter);

        View.OnKeyListener onKeyListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    doChecking();
                    return true;
                }
                return false;
            }
        };

        etItemNo.setOnKeyListener(onKeyListener);
        etWBcode.setOnKeyListener(onKeyListener);

        ViewHelper.initEdittextInputState(this, etWBcode);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonCheck) {
            doChecking();
        }
    }

    private void doChecking() {
        presenter.acquireDatas(etItemNo.getText().toString().trim(), etWBcode.getText().toString().trim());
    }



    @Override
    public void clearDatas() {
        etItemNo.setText("");
        etWBcode.setText("");
        datas.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void fillRecycler(List<BinContentInfo> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_func, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_clear) {
            clearDatas();
        }
        return super.onOptionsItemSelected(item);
    }

}
