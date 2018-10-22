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
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseTransferMultiAddon;
import com.weihan.scanner.entities.WhseTransferMultiInfo;
import com.weihan.scanner.mvpviews.Func13MvpView;
import com.weihan.scanner.presenters.Func13PresenterImpl;
import com.weihan.scanner.utils.AdapterHelper;
import com.weihan.scanner.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.scanner.Constant.KEY_SPREF_FUNC13_DATA;

public class Func13Activity extends BaseFuncActivity<Func13PresenterImpl> implements Func13MvpView, View.OnClickListener {

    EditText etToBincode, etItemno;
    Button buttonCheck, buttonSubmit;
    RecyclerView recyclerView;

    private List<Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo>> datas = new ArrayList<>();
    private Func13PresenterImpl.WhseTransferMultiListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func13);

        findView();
        initWidget();
    }

    @Override
    protected Func13PresenterImpl buildPresenter() {
        return new Func13PresenterImpl();
    }

    @Override
    public void initWidget() {
        buttonCheck.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Func13PresenterImpl.WhseTransferMultiListAdapter(datas);
        AdapterHelper.setAdapterEmpty(this, adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_item_func13_delete) {
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
        ViewHelper.initEdittextInputState(this, etToBincode);
    }

    @Override
    public void findView() {
        etToBincode = findViewById(R.id.et_func13_to_bincode);
        etItemno = findViewById(R.id.et_func13_itemno);
        buttonCheck = findViewById(R.id.button_func13_check);
        buttonSubmit = findViewById(R.id.button_func13_submit);
        recyclerView = findViewById(R.id.recycler_func13);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonCheck) {
            doChecking();
        } else if (view == buttonSubmit) {
            etToBincode.requestFocus();
            presenter.submitDatas(datas);
        }
    }

    private void doChecking() {
        presenter.acquireDatas(etItemno.getText().toString(), etToBincode.getText().toString());
    }

    @Override
    protected void savePref(boolean isToClear) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String prefJson;

        if (datas.isEmpty() || isToClear)
            prefJson = "";
        else
            prefJson = new Gson().toJson(datas);

        editor.putString(KEY_SPREF_FUNC13_DATA, prefJson);
        editor.apply();
    }

    @Override
    protected void loadPref() {
        sharedPreferences = getSharedPreferences(Constant.SHAREDPREF_NAME, MODE_PRIVATE);
        String prefJson = sharedPreferences.getString(KEY_SPREF_FUNC13_DATA, "");
        if (!prefJson.isEmpty()) {
            List<Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo>> tmpList = new Gson()
                    .fromJson(prefJson, new TypeToken<List<Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo>>>() {
                    }.getType());
            fillRecycler(tmpList);
        }
    }

    @Override
    protected void clearDatas() {
        savePref(true);
        etToBincode.setText("");
        etItemno.setText("");
        datas.clear();
        notifyAdapter();
    }

    @Override
    public void fillRecycler(List<Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo>> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }



}
