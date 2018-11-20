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
import com.weihan.scanner.entities.ConsumptionPickConfirmAddon;
import com.weihan.scanner.entities.InvPickingInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.mvpviews.Func4MvpView;
import com.weihan.scanner.presenters.Func4PresenterImpl;
import com.weihan.scanner.utils.AdapterHelper;
import com.weihan.scanner.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.scanner.Constant.KEY_SPREF_FUNC4_DATA;

public class Func4Activity extends BaseFuncActivity<Func4PresenterImpl> implements Func4MvpView, View.OnClickListener {


    RecyclerView recyclerView;
    EditText etCheck;
    Button btCheck;
    TextView tvCode;

    private Func4PresenterImpl.ConsumptionAdapter adapter;
    private List<Polymorph<ConsumptionPickConfirmAddon, InvPickingInfo>> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func1);

        findView();
        initWidget();
    }

    @Override
    protected Func4PresenterImpl buildPresenter() {
        return new Func4PresenterImpl();
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

        editor.putString(KEY_SPREF_FUNC4_DATA, prefJson);
        editor.apply();
    }

    private Polymorph<ConsumptionPickConfirmAddon, InvPickingInfo> itemRecommanded;

    @Override
    protected void loadPref() {
        sharedPreferences = getSharedPreferences(Constant.SHAREDPREF_NAME, MODE_PRIVATE);
        String prefJson = sharedPreferences.getString(KEY_SPREF_FUNC4_DATA, "");
        if (!prefJson.isEmpty()) {
            List<Polymorph<ConsumptionPickConfirmAddon, InvPickingInfo>> tmpList = null;
            try {
                tmpList = new Gson()
                        .fromJson(prefJson, new TypeToken<List<Polymorph<ConsumptionPickConfirmAddon, InvPickingInfo>>>() {
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
    public void fillRecycler(List<Polymorph<ConsumptionPickConfirmAddon, InvPickingInfo>> datas) {
        if (!datas.isEmpty()) {
            tvCode.setText(datas.get(0).getInfoEntity().getInv_Document_No());
            etCheck.setText(datas.get(0).getInfoEntity().getInv_Document_No());
        }
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void uncheckAdpaterBox() {
        adapter.uncheckAllBoxes(recyclerView);
    }

    /*
    private void doRecommanding() {
        if (itemRecommanded == null) return;

        String itemno = itemRecommanded.getInfoEntity().getItem_No();
        if (itemno.isEmpty()) {
            ToastUtils.show("物料条码不能为空");
            return;
        }
        Intent intent = new Intent(Func4Activity.this, ChooseListActivity.class);
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
    */

    @Override
    public void initWidget() {
        btCheck.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter = new Func4PresenterImpl.ConsumptionAdapter(datas);
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
        recyclerView = findViewById(R.id.recycler_func1);
        btCheck = findViewById(R.id.button_func1_check);
        etCheck = findViewById(R.id.et_func1_barcode);
        tvCode = findViewById(R.id.tv_func1_code);
    }
}
