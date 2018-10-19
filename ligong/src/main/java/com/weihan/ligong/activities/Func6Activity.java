package com.weihan.ligong.activities;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weihan.ligong.BaseMVP.BaseFuncActivity;
import com.weihan.ligong.Constant;
import com.weihan.ligong.R;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.entities.ProdOutputAddon;
import com.weihan.ligong.mvpviews.Func6MvpView;
import com.weihan.ligong.presenters.Func6PresenterImpl;
import com.weihan.ligong.utils.AdapterHelper;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.ligong.Constant.KEY_SPREF_FUNC6_DATA;

public class Func6Activity extends BaseFuncActivity<Func6PresenterImpl> implements Func6MvpView, View.OnClickListener {

    RecyclerView recyclerView;
    EditText etImportCode, etItemno;
    Button btSave, btSubmit;

    private Func6PresenterImpl.ProdOutputAdapter adapter;
    private List<Polymorph<ProdOutputAddon, ProdOutputAddon>> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func6);

        findView();
        initWidget();
    }

    @Override
    protected Func6PresenterImpl buildPresenter() {
        return new Func6PresenterImpl();
    }

    @Override
    protected void savePref(boolean isToClear) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String prefJson;

        if (datas.isEmpty() || isToClear)
            prefJson = "";
        else
            prefJson = new Gson().toJson(datas);

        editor.putString(KEY_SPREF_FUNC6_DATA, prefJson);
        editor.apply();
    }

    @Override
    protected void loadPref() {
        sharedPreferences = getSharedPreferences(Constant.SHAREDPREF_NAME, MODE_PRIVATE);
        String prefJson = sharedPreferences.getString(KEY_SPREF_FUNC6_DATA, "");
        if (!prefJson.isEmpty()) {
            List<Polymorph<ProdOutputAddon, ProdOutputAddon>> tmpList = new Gson()
                    .fromJson(prefJson, new TypeToken<List<Polymorph<ProdOutputAddon, ProdOutputAddon>>>() {
                    }.getType());
            datas.clear();
            datas.addAll(tmpList);
            notifyAdapter();
        }
    }

    @Override
    protected void clearDatas() {
        savePref(true);
        etItemno.setText("");
        etImportCode.setText("");
        datas.clear();
        notifyAdapter();
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initWidget() {
        btSave.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter = new Func6PresenterImpl.ProdOutputAdapter(datas);
        AdapterHelper.setAdapterEmpty(this, adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_item_func6_delete) {
                    buildDeleteDialog(adapter, position);
                }
            }
        });
        recyclerView.setAdapter(adapter);

        loadPref();
    }

    @Override
    public void findView() {
        recyclerView = findViewById(R.id.recycler_func6);
        btSave = findViewById(R.id.button_func6_save);
        btSubmit = findViewById(R.id.button_func6_submit);
        etImportCode = findViewById(R.id.et_func6_importcode);
        etItemno = findViewById(R.id.et_func6_itemno);
    }

    @Override
    public void onClick(View view) {
        if (view == btSave) {
            presenter.attemptToAddPoly(datas, etImportCode.getText().toString(), etItemno.getText().toString());
        } else if (view == btSubmit) {
            etItemno.requestFocus();
            presenter.submitDatas(datas);
        }
    }
}
