package com.weihan.ligong.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weihan.ligong.BaseMVP.BaseFuncActivity;
import com.weihan.ligong.Constant;
import com.weihan.ligong.R;
import com.weihan.ligong.entities.ConsumptionPickAddon;
import com.weihan.ligong.entities.InvPickingInfo;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.models.Func1ModelImpl;
import com.weihan.ligong.mvpviews.Func1MvpView;
import com.weihan.ligong.presenters.Func1PresenterImpl;
import com.weihan.ligong.utils.AdapterHelper;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.ligong.Constant.KEY_SPREF_FUNC1_DATA;

public class Func1Activity extends BaseFuncActivity<Func1PresenterImpl> implements Func1MvpView, View.OnClickListener {


    RecyclerView recyclerView;
    EditText etCheck;
    Button btCheck, btSubmit;
    TextView tvCode;

    private Func1ModelImpl.InvPickingAdapter adapter;
    private List<Polymorph<ConsumptionPickAddon, InvPickingInfo>> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func1);

        findView();
        initWidget();
    }

    @Override
    protected Func1PresenterImpl buildPresenter() {
        return new Func1PresenterImpl();
    }

    @Override
    public void onClick(View view) {
        if (view == btCheck) {
            presenter.acquireDatas(etCheck.getText().toString());
        } else if (view == btSubmit) {
            etCheck.requestFocus();
            presenter.submitDatas(datas);
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

        editor.putString(KEY_SPREF_FUNC1_DATA, prefJson);
        editor.apply();
    }

    @Override
    protected void loadPref() {
        sharedPreferences = getSharedPreferences(Constant.SHAREDPREF_NAME, MODE_PRIVATE);
        String prefJson = sharedPreferences.getString(KEY_SPREF_FUNC1_DATA, "");
        if (!prefJson.isEmpty()) {
            List<Polymorph<ConsumptionPickAddon, InvPickingInfo>> tmpList = new Gson()
                    .fromJson(prefJson, new TypeToken<List<Polymorph<ConsumptionPickAddon, InvPickingInfo>>>() {
                    }.getType());
            fillRecycler(tmpList);
        }
    }

    @Override
    protected void clearDatas() {
        savePref(true);
        tvCode.setText("");
        datas.clear();
        notifyAdapter();
    }

    @Override
    public void fillRecycler(List<Polymorph<ConsumptionPickAddon, InvPickingInfo>> datas) {
        if (!datas.isEmpty()) tvCode.setText(datas.get(0).getInfoEntity().getInv_Document_No());
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initWidget() {
        btCheck.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter = new Func1ModelImpl.InvPickingAdapter(datas);
        AdapterHelper.setAdapterEmpty(this, adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_item_func1_delete) {
                    buildDeleteDialog(adapter, position);
                }
            }
        });
        recyclerView.setAdapter(adapter);

        loadPref();
    }

    @Override
    public void findView() {
        recyclerView = findViewById(R.id.recycler_func1);
        btCheck = findViewById(R.id.button_func1_check);
        btSubmit = findViewById(R.id.button_func1_submit);
        etCheck = findViewById(R.id.et_func1_barcode);
        tvCode = findViewById(R.id.tv_func1_code);
    }
}
