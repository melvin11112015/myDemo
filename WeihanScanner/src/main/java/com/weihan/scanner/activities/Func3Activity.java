package com.weihan.scanner.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weihan.scanner.BaseMVP.BaseFuncActivity;
import com.weihan.scanner.Constant;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehousePutAwayAddon;
import com.weihan.scanner.mvpviews.Func3MvpView;
import com.weihan.scanner.presenters.Func11PresenterImpl;
import com.weihan.scanner.presenters.Func3PresenterImpl;
import com.weihan.scanner.utils.AdapterHelper;
import com.weihan.scanner.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.scanner.Constant.KEY_SPREF_FUNC3_DATA;

/**
 * 收货上架
 */

public class Func3Activity extends BaseFuncActivity<Func3PresenterImpl> implements Func3MvpView, View.OnClickListener {

    EditText etItemno, etWBcode, etQuantity;
    Button buttonAdd, buttonRecommand;
    RecyclerView recyclerView, recyclerViewRecomandInfo;
    ToggleButton toggleButton;

    private List<Polymorph<WarehousePutAwayAddon, BinContentInfo>> datas = new ArrayList<>();
    private Func3PresenterImpl.WarehousePutAwayListAdapter adapter;
    private List<BinContentInfo> datasRecommendInfo = new ArrayList<>();
    private Func11PresenterImpl.BinContentListAdapter adapterRecommendInfo;

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
        buttonAdd.setOnClickListener(this);
        buttonRecommand.setOnClickListener(this);

        adapter = new Func3PresenterImpl.WarehousePutAwayListAdapter(datas);
        AdapterHelper.setAdapterEmpty(this, adapter);
        recyclerView.setAdapter(adapter);

        adapterRecommendInfo = new Func11PresenterImpl.BinContentListAdapter(datasRecommendInfo);
        AdapterHelper.setAdapterEmpty(this, adapterRecommendInfo);
        recyclerViewRecomandInfo.setAdapter(adapterRecommendInfo);

        etItemno.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    doRecommending();
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

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                recyclerViewRecomandInfo.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

    }

    @Override
    public void findView() {
        etItemno = findViewById(R.id.et_func3_itemno);
        etWBcode = findViewById(R.id.et_func3_bincode);
        etQuantity = findViewById(R.id.et_func3_quantity);
        buttonAdd = findViewById(R.id.button_func3_add);
        buttonRecommand = findViewById(R.id.button_func3_recommand);
        recyclerView = findViewById(R.id.recycler_func3);
        recyclerViewRecomandInfo = findViewById(R.id.recycler_func3_recommand);
        toggleButton = findViewById(R.id.toggleButton_func3);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonAdd) {
            doAdding();
        } else if (view == buttonRecommand) {
            doRecommending();
        }
    }

    private void doAdding() {
        presenter.attemptToAddPoly(datas, etItemno.getText().toString().trim(), etWBcode.getText().toString().trim(), etQuantity.getText().toString().trim());
    }

    private void doRecommending() {
        presenter.acquireDatas(etItemno.getText().toString(), "");
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
        datasRecommendInfo.clear();
        toggleButton.setTextOn(getString(R.string.togglebutton_on_default));
        toggleButton.setTextOff(getString(R.string.togglebutton_off_default));
        toggleButton.setChecked(false);
        notifyAdapter();
    }

    @Override
    protected void submitDatas() {
        etItemno.requestFocus();
        presenter.submitDatas(datas);
    }

    @Override
    public void fillRecycler(List<Polymorph<WarehousePutAwayAddon, BinContentInfo>> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void fillRecyclerWithRecommandInfo(List<BinContentInfo> datasRecommendInfo) {
        this.datasRecommendInfo.clear();
        this.datasRecommendInfo.addAll(datasRecommendInfo);
        adapterRecommendInfo.notifyDataSetChanged();
        if (!datasRecommendInfo.isEmpty()) {
            toggleButton.setTextOn(String.format(getString(R.string.formatting_title_recommend_down), datasRecommendInfo.get(0).getItem_No()));
            toggleButton.setTextOff(String.format(getString(R.string.formatting_title_recommend_up), datasRecommendInfo.get(0).getItem_No()));
            toggleButton.setChecked(true);
        }
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
        adapterRecommendInfo.notifyDataSetChanged();
    }

}
