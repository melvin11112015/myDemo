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
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weihan.scanner.BaseMVP.BaseFuncActivity;
import com.weihan.scanner.Constant;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.OutstandingSalesLineInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseShipmentAddon;
import com.weihan.scanner.mvpviews.Func7MvpView;
import com.weihan.scanner.presenters.Func7PresenterImpl;
import com.weihan.scanner.utils.AdapterHelper;
import com.weihan.scanner.utils.ViewHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.weihan.scanner.Constant.KEY_CODE;
import static com.weihan.scanner.Constant.KEY_PARAM2;
import static com.weihan.scanner.Constant.KEY_SPREF_FUNC7_DATA;
import static com.weihan.scanner.Constant.KEY_TITLE;
import static com.weihan.scanner.Constant.REQUEST_RECOMMAND;
import static com.weihan.scanner.Constant.RESULT_SUCCESS;

public class Func7Activity extends BaseFuncActivity<Func7PresenterImpl> implements Func7MvpView, View.OnClickListener {


    RecyclerView recyclerView;
    EditText etCheck;
    Button btCheck, btSubmit;
    TextView tvCode;

    private Func7PresenterImpl.NewOutstandingSalesLineAdapter adapter;
    private List<Polymorph<List<Polymorph<WarehouseShipmentAddon, BinContentInfo>>, OutstandingSalesLineInfo>> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func7);

        findView();
        initWidget();
    }

    @Override
    protected Func7PresenterImpl buildPresenter() {
        return new Func7PresenterImpl();
    }

    @Override
    public void onClick(View view) {
        if (view == btCheck) {
            doChecking();
        } else if (view == btSubmit) {
            etCheck.requestFocus();
            presenter.submitDatas(datas);
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

        editor.putString(KEY_SPREF_FUNC7_DATA, prefJson);
        editor.apply();
    }

    private Polymorph<List<Polymorph<WarehouseShipmentAddon, BinContentInfo>>, OutstandingSalesLineInfo> itemRecommanded;

    @Override
    protected void loadPref() {
        sharedPreferences = getSharedPreferences(Constant.SHAREDPREF_NAME, MODE_PRIVATE);
        String prefJson = sharedPreferences.getString(KEY_SPREF_FUNC7_DATA, "");
        if (!prefJson.isEmpty()) {
            List<Polymorph<List<Polymorph<WarehouseShipmentAddon, BinContentInfo>>, OutstandingSalesLineInfo>> tmpList = new Gson()
                    .fromJson(prefJson, new TypeToken<List<Polymorph<List<Polymorph<WarehouseShipmentAddon, BinContentInfo>>, OutstandingSalesLineInfo>>>() {
                    }.getType());
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
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void fillRecycler(List<Polymorph<List<Polymorph<WarehouseShipmentAddon, BinContentInfo>>, OutstandingSalesLineInfo>> datas) {
        if (!datas.isEmpty()) tvCode.setText(datas.get(0).getInfoEntity().getDocument_No());
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
    }

    private void doRecommanding() {
        if (itemRecommanded == null) return;

        String itemno = itemRecommanded.getInfoEntity().getNo();
        if (itemno.isEmpty()) {
            ToastUtils.show("物料条码不能为空");
            return;
        }
        Intent intent = new Intent(Func7Activity.this, ChooseListActivity.class);
        intent.putExtra(KEY_CODE, itemno);
        intent.putExtra(KEY_TITLE, getString(R.string.text_recommand_bin));
        startActivityForResult(intent, REQUEST_RECOMMAND);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RECOMMAND && resultCode == RESULT_SUCCESS) {
            Serializable s = data.getSerializableExtra(KEY_PARAM2);

            if (s != null && s instanceof BinContentInfo)
                presenter.attemptToAddPoly(itemRecommanded.getAddonEntity(), (BinContentInfo) s, itemRecommanded.getInfoEntity());

            notifyAdapter();
        }
    }

    @Override
    public void initWidget() {
        btCheck.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter = new Func7PresenterImpl.NewOutstandingSalesLineAdapter(datas, "发货");
        AdapterHelper.setAdapterEmpty(this, adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_item_func7_recommand) {
                    itemRecommanded = (Polymorph<List<Polymorph<WarehouseShipmentAddon, BinContentInfo>>, OutstandingSalesLineInfo>) adapter.getItem(position);
                    doRecommanding();
                }
            }
        });
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
        btSubmit = findViewById(R.id.button_func7_submit);
        etCheck = findViewById(R.id.et_func7_barcode);
        tvCode = findViewById(R.id.tv_func7_code);
    }
}