package com.weihan.scanner.activities;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weihan.scanner.BaseMVP.BaseFuncActivity;
import com.weihan.scanner.Constant;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.ProdOutputAddon;
import com.weihan.scanner.mvpviews.Func6MvpView;
import com.weihan.scanner.presenters.Func6PresenterImpl;
import com.weihan.scanner.utils.AdapterHelper;
import com.weihan.scanner.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.scanner.Constant.KEY_SPREF_FUNC6_DATA;

/**
 * 产出入库
 */

public class Func6Activity extends BaseFuncActivity<Func6PresenterImpl> implements Func6MvpView, View.OnClickListener {

    RecyclerView recyclerView;
    EditText etImportCode, etItemno;
    Button btSave;

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
    protected void submitDatas() {
        etItemno.requestFocus();
        presenter.submitDatas(datas);
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initWidget() {
        btSave.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter = new Func6PresenterImpl.ProdOutputAdapter(datas);
        AdapterHelper.setAdapterEmpty(this, adapter);

        recyclerView.setAdapter(adapter);
        etItemno.setOnKeyListener(new View.OnKeyListener() {
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
        ViewHelper.initEdittextInputState(this, etImportCode);
    }

    @Override
    public void findView() {
        recyclerView = findViewById(R.id.recycler_func6);
        btSave = findViewById(R.id.button_func6_save);
        etImportCode = findViewById(R.id.et_func6_importcode);
        etItemno = findViewById(R.id.et_func6_itemno);
    }

    @Override
    public void onClick(View view) {
        if (view == btSave) {
            doAdding();
        }
    }

    private void doAdding() {
        presenter.attemptToAddPoly(datas, etImportCode.getText().toString(), etItemno.getText().toString());
    }
}
