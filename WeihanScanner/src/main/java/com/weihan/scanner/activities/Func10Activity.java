package com.weihan.scanner.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.weihan.scanner.BaseMVP.BaseFuncActivity;
import com.weihan.scanner.Constant;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.OutstandingSalesLineInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseShipmentAddon;
import com.weihan.scanner.mvpviews.Func10MvpView;
import com.weihan.scanner.presenters.Func10PresenterImpl;
import com.weihan.scanner.utils.AdapterHelper;
import com.weihan.scanner.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.scanner.Constant.KEY_SPREF_FUNC10_DATA;

/**
 * 包装确认
 */

public class Func10Activity extends BaseFuncActivity<Func10PresenterImpl> implements Func10MvpView, View.OnClickListener {


    RecyclerView recyclerView;
    EditText etCheck;
    Button btCheck;
    TextView tvCode;

    private Func10PresenterImpl.NewOutstandingSalesLineAdapter adapter;
    private List<Polymorph<WarehouseShipmentAddon, OutstandingSalesLineInfo>> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func7);

        findView();
        initWidget();
    }

    @Override
    protected Func10PresenterImpl buildPresenter() {
        return new Func10PresenterImpl();
    }

    @Override
    public void onClick(View view) {
        if (view == btCheck) {
            doChecking();
        }
    }

    private void doChecking() {
        presenter.acquireDatas(etCheck.getText().toString());
    }

    @Override
    protected void savePref(boolean isToClear) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String prefJson;

        if (datas.isEmpty() || isToClear)
            prefJson = "";
        else
            prefJson = new Gson().toJson(datas);

        editor.putString(KEY_SPREF_FUNC10_DATA, prefJson);
        editor.apply();
    }


    @Override
    protected void loadPref() {
        sharedPreferences = getSharedPreferences(Constant.SHAREDPREF_NAME, MODE_PRIVATE);
        String prefJson = sharedPreferences.getString(KEY_SPREF_FUNC10_DATA, "");
        if (!prefJson.isEmpty()) {
            List<Polymorph<WarehouseShipmentAddon, OutstandingSalesLineInfo>> tmpList = null;
            try {
                tmpList = new Gson()
                        .fromJson(prefJson, new TypeToken<List<Polymorph<WarehouseShipmentAddon, OutstandingSalesLineInfo>>>() {
                        }.getType());
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                savePref(true);
                return;
            }
            fillRecycler(tmpList);
        }
    }

    @Override
    protected void clearDatas() {
        savePref(true);
        tvCode.setText("");
        etCheck.setText("");
        datas.clear();
        notifyAdapter();
    }

    @Override
    protected void submitDatas() {
        etCheck.requestFocus();
        presenter.submitDatas(datas);
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void uncheckAdpaterBox() {
        adapter.uncheckAllBoxes(recyclerView);
    }

    @Override
    public void fillRecycler(List<Polymorph<WarehouseShipmentAddon, OutstandingSalesLineInfo>> datas) {
        if (!datas.isEmpty()) {
            tvCode.setText(datas.get(0).getInfoEntity().getDocument_No());
            etCheck.setText(datas.get(0).getInfoEntity().getDocument_No());
        }
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void initWidget() {
        btCheck.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter = new Func10PresenterImpl.NewOutstandingSalesLineAdapter(datas);
        AdapterHelper.setAdapterEmpty(this, adapter);

        recyclerView.setAdapter(adapter);

        etCheck.setOnKeyListener(new View.OnKeyListener() {
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
        ViewHelper.initEdittextInputState(this, etCheck);
    }

    @Override
    public void findView() {
        recyclerView = findViewById(R.id.recycler_func7);
        btCheck = findViewById(R.id.button_func7_check);
        etCheck = findViewById(R.id.et_func7_barcode);
        tvCode = findViewById(R.id.tv_func7_code);
    }
}
