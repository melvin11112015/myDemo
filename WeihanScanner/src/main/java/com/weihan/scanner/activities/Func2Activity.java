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
import com.weihan.scanner.entities.PhysicalInvtAddon;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.mvpviews.Func6MvpView;
import com.weihan.scanner.presenters.Func2PresenterImpl;
import com.weihan.scanner.utils.AdapterHelper;
import com.weihan.scanner.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.scanner.Constant.KEY_SPREF_FUNC2_DATA;

public class Func2Activity extends BaseFuncActivity<Func2PresenterImpl> implements Func6MvpView, View.OnClickListener {

    RecyclerView recyclerView;
    EditText etWBCode, etItemno;
    Button btSave, btSubmit;

    private Func2PresenterImpl.PhysicalInvtAdapter adapter;
    private List<Polymorph<PhysicalInvtAddon, PhysicalInvtAddon>> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func2);

        findView();
        initWidget();
    }

    @Override
    protected Func2PresenterImpl buildPresenter() {
        return new Func2PresenterImpl();
    }

    @Override
    protected void savePref(boolean isToClear) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String prefJson;

        if (datas.isEmpty() || isToClear)
            prefJson = "";
        else
            prefJson = new Gson().toJson(datas);

        editor.putString(KEY_SPREF_FUNC2_DATA, prefJson);
        editor.apply();
    }

    @Override
    protected void loadPref() {
        sharedPreferences = getSharedPreferences(Constant.SHAREDPREF_NAME, MODE_PRIVATE);
        String prefJson = sharedPreferences.getString(KEY_SPREF_FUNC2_DATA, "");
        if (!prefJson.isEmpty()) {
            List<Polymorph<PhysicalInvtAddon, PhysicalInvtAddon>> tmpList = new Gson()
                    .fromJson(prefJson, new TypeToken<List<Polymorph<PhysicalInvtAddon, PhysicalInvtAddon>>>() {
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
        etWBCode.setText("");
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
        adapter = new Func2PresenterImpl.PhysicalInvtAdapter(datas);
        AdapterHelper.setAdapterEmpty(this, adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_item_func2_delete) {
                    buildDeleteDialog(adapter, position);
                }
            }
        });
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
        ViewHelper.initEdittextInputState(this, etWBCode);
    }

    @Override
    public void findView() {
        recyclerView = findViewById(R.id.recycler_func2);
        btSave = findViewById(R.id.button_func2_save);
        btSubmit = findViewById(R.id.button_func2_submit);
        etWBCode = findViewById(R.id.et_func2_bincode);
        etItemno = findViewById(R.id.et_func2_itemno);
    }

    @Override
    public void onClick(View view) {
        if (view == btSave) {
            doAdding();
        } else if (view == btSubmit) {
            etItemno.requestFocus();
            presenter.submitDatas(datas);
        }
    }

    private void doAdding() {
        presenter.attemptToAddPoly(datas, etWBCode.getText().toString(), etItemno.getText().toString());
    }
}
