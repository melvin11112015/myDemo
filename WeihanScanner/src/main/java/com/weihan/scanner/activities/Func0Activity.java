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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weihan.scanner.BaseMVP.BaseFuncActivity;
import com.weihan.scanner.Constant;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.OutstandingPurchLineInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseReceiptAddon;
import com.weihan.scanner.mvpviews.Func0MvpView;
import com.weihan.scanner.presenters.Func0PresenterImpl;
import com.weihan.scanner.utils.AdapterHelper;
import com.weihan.scanner.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.scanner.Constant.KEY_SPREF_FUNC0_DATA;

public class Func0Activity extends BaseFuncActivity<Func0PresenterImpl> implements Func0MvpView, View.OnClickListener {

    RecyclerView recyclerView;
    EditText etCheck;
    Button btCheck, btSubmit;
    TextView tvCode;

    private Func0PresenterImpl.PurchaseListAdapter2 adapter;
    private List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func0);

        findView();
        initWidget();
    }

    @Override
    protected Func0PresenterImpl buildPresenter() {
        return new Func0PresenterImpl();
    }

    @Override
    public void findView() {
        recyclerView = findViewById(R.id.recycler_func0);
        btCheck = findViewById(R.id.button_func0_check);
        btSubmit = findViewById(R.id.button_func0_submit);
        etCheck = findViewById(R.id.et_func0_barcode);
        tvCode = findViewById(R.id.tv_func0_code);
    }

    @Override
    public void initWidget() {
        btCheck.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Func0PresenterImpl.PurchaseListAdapter2(datas);
        AdapterHelper.setAdapterEmpty(this, adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_item_func0_delete) {
                    buildDeleteDialog(adapter, position);
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

    private void doChecking() {
        presenter.acquireDatas(etCheck.getText().toString());
    }

    @Override
    public void fillRecycler(List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>> datas) {
        if (!datas.isEmpty()) {
            tvCode.setText(datas.get(0).getInfoEntity().getDocument_No());
            etCheck.setText(datas.get(0).getInfoEntity().getDocument_No());
        }
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
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

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
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
    protected void loadPref() {
        sharedPreferences = getSharedPreferences(Constant.SHAREDPREF_NAME, MODE_PRIVATE);
        String prefJson = sharedPreferences.getString(KEY_SPREF_FUNC0_DATA, "");
        if (!prefJson.isEmpty()) {
            List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>> tmpList = new Gson()
                    .fromJson(prefJson, new TypeToken<List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>>>() {
                    }.getType());
            fillRecycler(tmpList);
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

        editor.putString(KEY_SPREF_FUNC0_DATA, prefJson);
        editor.apply();
    }


}
