package com.weihan.scanner.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weihan.scanner.BaseMVP.BaseFuncActivity;
import com.weihan.scanner.Constant;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehousePutAwayAddon;
import com.weihan.scanner.mvpviews.Func3MvpView;
import com.weihan.scanner.presenters.Func3PresenterImpl;
import com.weihan.scanner.utils.AdapterHelper;
import com.weihan.scanner.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.scanner.Constant.KEY_CODE;
import static com.weihan.scanner.Constant.KEY_CODE2;
import static com.weihan.scanner.Constant.KEY_SPREF_FUNC3_DATA;
import static com.weihan.scanner.Constant.KEY_TITLE;
import static com.weihan.scanner.Constant.REQUEST_RECOMMAND;
import static com.weihan.scanner.Constant.RESULT_SUCCESS;

public class Func3Activity extends BaseFuncActivity<Func3PresenterImpl> implements Func3MvpView, View.OnClickListener {

    EditText etItemno, etWBcode;
    Button buttonAdd, buttonSubmit, buttonRecommand;
    RecyclerView recyclerView;

    private List<Polymorph<WarehousePutAwayAddon, BinContentInfo>> datas = new ArrayList<>();
    private Func3PresenterImpl.WarehousePutAwayListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func3);

        findView();
        initWidget();
    }

    @Override
    protected Func3PresenterImpl buildPresenter() {
        return new Func3PresenterImpl();
    }

    @Override
    public void initWidget() {
        buttonSubmit.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        buttonRecommand.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Func3PresenterImpl.WarehousePutAwayListAdapter(datas);
        AdapterHelper.setAdapterEmpty(this, adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_item_func3_delete) {
                    buildDeleteDialog(adapter, position);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        etItemno.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    doRecommanding();
                    //return true;
                }
                return false;
            }
        });
        etWBcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    doAdding();
                    return true;
                }
                return false;
            }
        });
        loadPref();
        ViewHelper.initEdittextInputState(this, etItemno);
    }

    @Override
    public void findView() {
        etItemno = findViewById(R.id.et_func3_itemno);
        etWBcode = findViewById(R.id.et_func3_bincode);
        buttonAdd = findViewById(R.id.button_func3_add);
        buttonSubmit = findViewById(R.id.button_func3_submit);
        buttonRecommand = findViewById(R.id.button_func3_recommand);
        recyclerView = findViewById(R.id.recycler_func3);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonAdd) {
            doAdding();
        } else if (view == buttonSubmit) {
            etItemno.requestFocus();
            presenter.submitDatas(datas);
        } else if (view == buttonRecommand) {
            doRecommanding();
        }
    }

    private void doAdding() {
        presenter.attemptToAddPoly(datas, etItemno.getText().toString(), etWBcode.getText().toString());
    }

    private void doRecommanding() {
        String itemno = etItemno.getText().toString();
        if (itemno.isEmpty()) {
            ToastUtils.show("物料条码不能为空");
            return;
        }
        Intent intent = new Intent(Func3Activity.this, ChooseListActivity.class);
        intent.putExtra(KEY_CODE, itemno);
        intent.putExtra(KEY_TITLE, getString(R.string.text_recommand_bin));
        startActivityForResult(intent, REQUEST_RECOMMAND);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RECOMMAND && resultCode == RESULT_SUCCESS) {
            String locationCode = data.getStringExtra(KEY_CODE);
            String binCode = data.getStringExtra(KEY_CODE2);
            if (locationCode != null && binCode != null) {
                etWBcode.setText(locationCode + binCode);
                doAdding();
            }
        }
    }

    @Override
    protected void savePref(boolean isToClear) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String prefJson;

        if (datas.isEmpty() || isToClear)
            prefJson = "";
        else
            prefJson = new Gson().toJson(datas);

        editor.putString(KEY_SPREF_FUNC3_DATA, prefJson);
        editor.apply();
    }

    @Override
    protected void loadPref() {
        sharedPreferences = getSharedPreferences(Constant.SHAREDPREF_NAME, MODE_PRIVATE);
        String prefJson = sharedPreferences.getString(KEY_SPREF_FUNC3_DATA, "");
        if (!prefJson.isEmpty()) {
            List<Polymorph<WarehousePutAwayAddon, BinContentInfo>> tmpList = new Gson()
                    .fromJson(prefJson, new TypeToken<List<Polymorph<WarehousePutAwayAddon, BinContentInfo>>>() {
                    }.getType());
            fillRecycler(tmpList);
        }
    }

    @Override
    protected void clearDatas() {
        savePref(true);
        etWBcode.setText("");
        etItemno.setText("");
        datas.clear();
        notifyAdapter();
    }

    @Override
    public void fillRecycler(List<Polymorph<WarehousePutAwayAddon, BinContentInfo>> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }



}
