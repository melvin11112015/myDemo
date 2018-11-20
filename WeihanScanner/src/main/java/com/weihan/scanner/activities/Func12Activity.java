package com.weihan.scanner.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weihan.scanner.BaseMVP.BaseFuncActivity;
import com.weihan.scanner.Constant;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseTransferMultiAddon;
import com.weihan.scanner.mvpviews.Func12MvpView;
import com.weihan.scanner.presenters.Func12PresenterImpl;
import com.weihan.scanner.utils.AdapterHelper;
import com.weihan.scanner.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.scanner.Constant.KEY_SPREF_FUNC12_DATA;

public class Func12Activity extends BaseFuncActivity<Func12PresenterImpl> implements Func12MvpView, View.OnClickListener {

    EditText etFromWBcode, etItemno;
    Button buttonCheck;
    RecyclerView recyclerView;

    private Func12PresenterImpl.BinContentListAdapter adapter;
    private List<Polymorph<WarehouseTransferMultiAddon, BinContentInfo>> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func12);

        findView();
        initWidget();
    }

    @Override
    public void initWidget() {
        buttonCheck.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Func12PresenterImpl.BinContentListAdapter(datas);
        AdapterHelper.setAdapterEmpty(this, adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_item_func12_delete) {
                    buildDeleteDialog(adapter, position);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        etItemno.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    doChecking();
                    return true;
                }
                return false;
            }
        });
        loadPref();
        ViewHelper.initEdittextInputState(this, etFromWBcode);
    }

    @Override
    public void findView() {
        etFromWBcode = findViewById(R.id.et_func12_from_bincode);
        etItemno = findViewById(R.id.et_func12_itemno);
        buttonCheck = findViewById(R.id.button_func12_check);
        recyclerView = findViewById(R.id.recycler_func12);
    }

    @Override
    protected Func12PresenterImpl buildPresenter() {
        return new Func12PresenterImpl();
    }

    @Override
    public void onClick(View view) {
        if (view == buttonCheck) {
            doChecking();
        }
    }

    private void doChecking() {
        presenter.acquireDatas(etItemno.getText().toString(), etFromWBcode.getText().toString());
    }

    @Override
    protected void savePref(boolean isToClear) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String prefJson;

        if (datas.isEmpty() || isToClear)
            prefJson = "";
        else
            prefJson = new Gson().toJson(datas);

        editor.putString(KEY_SPREF_FUNC12_DATA, prefJson);
        editor.apply();
    }

    @Override
    protected void loadPref() {
        sharedPreferences = getSharedPreferences(Constant.SHAREDPREF_NAME, MODE_PRIVATE);
        String prefJson = sharedPreferences.getString(KEY_SPREF_FUNC12_DATA, "");
        if (!prefJson.isEmpty()) {
            List<Polymorph<WarehouseTransferMultiAddon, BinContentInfo>> tmpList = new Gson()
                    .fromJson(prefJson, new TypeToken<List<Polymorph<WarehouseTransferMultiAddon, BinContentInfo>>>() {
                    }.getType());
            fillRecycler(tmpList);
        }
    }

    @Override
    protected void clearDatas() {
        savePref(true);
        etFromWBcode.setText("");
        etItemno.setText("");
        datas.clear();
        notifyAdapter();
    }

    @Override
    protected void submitDatas() {
        etFromWBcode.requestFocus();
        presenter.submitDatas(datas);
    }

    @Override
    public void fillRecycler(List<Polymorph<WarehouseTransferMultiAddon, BinContentInfo>> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }


}
