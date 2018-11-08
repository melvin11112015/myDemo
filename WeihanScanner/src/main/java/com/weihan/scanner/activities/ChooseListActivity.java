package com.weihan.scanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.weihan.scanner.BaseMVP.BaseActivity;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.mvpviews.ChooseListMvpView;
import com.weihan.scanner.presenters.ChooseListPresenterImpl;
import com.weihan.scanner.presenters.Func11PresenterImpl;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.scanner.Constant.KEY_CODE;
import static com.weihan.scanner.Constant.KEY_CODE2;
import static com.weihan.scanner.Constant.KEY_PARAM;
import static com.weihan.scanner.Constant.KEY_PARAM2;
import static com.weihan.scanner.Constant.RESULT_SUCCESS;

public class ChooseListActivity extends BaseActivity<ChooseListPresenterImpl> implements ChooseListMvpView {

    Intent resultIntent = new Intent();
    private Button buttonSelect;
    private EditText etSelect;
    private Func11PresenterImpl.BinContentListAdapter adapter;
    private List<BinContentInfo> datas = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_list);

        findView();
        initWidget();

        presenter.acquireDatas(getIntent().getStringExtra(KEY_CODE));

    }

    @Override
    protected ChooseListPresenterImpl buildPresenter() {
        return new ChooseListPresenterImpl();
    }

    @Override
    public void exitActivity() {
        finish();
    }

    @Override
    public void fillChooseListRecycler(List<BinContentInfo> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
        BinContentInfo firstItem = datas.get(0);
        String WBcode = firstItem.getLocation_Code() + firstItem.getBin_Code();
        etSelect.setText(WBcode);
    }

    @Override
    public void foundItem(BinContentInfo info) {
        resultIntent.putExtra(KEY_CODE, info.getLocation_Code());
        resultIntent.putExtra(KEY_CODE2, info.getBin_Code());
        resultIntent.putExtra(KEY_PARAM, info.getQuantity_Base());
        resultIntent.putExtra(KEY_PARAM2, info);
        setResult(RESULT_SUCCESS, resultIntent);
        finish();
    }

    private void doSelecting() {
        presenter.lookupDatas(datas, etSelect.getText().toString());
    }

    @Override
    public void initWidget() {
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSelecting();
            }
        });

        etSelect.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    doSelecting();
                    return true;
                }
                return false;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(ChooseListActivity.this));
        //recyclerView.addItemDecoration(new DividerItemDecoration(ChooseListActivity.this, DividerItemDecoration.VERTICAL));
        adapter = new Func11PresenterImpl.BinContentListAdapter(datas);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BinContentInfo info = datas.get(position);
                foundItem(info);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void findView() {
        recyclerView = findViewById(R.id.recycler_choose_list);
        etSelect = findViewById(R.id.et_choose_list_select);
        buttonSelect = findViewById(R.id.button_choose_list_select);
    }

}
