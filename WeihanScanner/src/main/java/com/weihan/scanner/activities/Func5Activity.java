package com.weihan.scanner.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weihan.scanner.BaseMVP.BaseFuncActivity;
import com.weihan.scanner.Constant;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.PhysicalInvtCheckAddon;
import com.weihan.scanner.entities.PhysicalInvtInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.mvpviews.Func5MvpView;
import com.weihan.scanner.presenters.Func5PresenterImpl;
import com.weihan.scanner.utils.AdapterHelper;
import com.weihan.scanner.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.scanner.Constant.KEY_SPREF_FUNC5_DATA;

public class Func5Activity extends BaseFuncActivity<Func5PresenterImpl> implements Func5MvpView, View.OnClickListener {

    EditText etWBcode, etItemno;
    Button buttonCheck;
    RecyclerView recyclerView;
    TextView tvClearText;

    private Func5PresenterImpl.PhysicalInvtAdapter adapter;
    private List<Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo>> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func5);

        findView();
        initWidget();
    }

    @Override
    public void initWidget() {
        buttonCheck.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Func5PresenterImpl.PhysicalInvtAdapter(datas);
        AdapterHelper.setAdapterEmpty(this, adapter);

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
        ViewHelper.initEdittextInputState(this, etWBcode);

        etItemno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        tvClearText.setOnClickListener(this);
    }

    @Override
    public void findView() {
        etWBcode = findViewById(R.id.et_func5_bincode);
        etItemno = findViewById(R.id.et_func5_itemno);
        buttonCheck = findViewById(R.id.button_func5_check);
        recyclerView = findViewById(R.id.recycler_func5);
        tvClearText = findViewById(R.id.tv_func5_cleartext);
    }

    @Override
    protected Func5PresenterImpl buildPresenter() {
        return new Func5PresenterImpl();
    }

    @Override
    public void onClick(View view) {
        if (view == buttonCheck) {
            doChecking();
        } else if (view == tvClearText) {
            etItemno.setText("");
        }
    }

    private void doChecking() {
        presenter.acquireDatas(etItemno.getText().toString(), etWBcode.getText().toString());
    }

    @Override
    protected void savePref(boolean isToClear) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String prefJson;

        if (datas.isEmpty() || isToClear)
            prefJson = "";
        else
            prefJson = new Gson().toJson(datas);

        editor.putString(KEY_SPREF_FUNC5_DATA, prefJson);
        editor.apply();
    }

    @Override
    protected void loadPref() {
        sharedPreferences = getSharedPreferences(Constant.SHAREDPREF_NAME, MODE_PRIVATE);
        String prefJson = sharedPreferences.getString(KEY_SPREF_FUNC5_DATA, "");
        if (!prefJson.isEmpty()) {
            List<Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo>> tmpList = new Gson()
                    .fromJson(prefJson, new TypeToken<List<Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo>>>() {
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
        notifyAdapter();
    }

    @Override
    protected void submitDatas() {
        etWBcode.requestFocus();
        presenter.submitDatas(datas);
    }

    @Override
    public void fillRecycler(List<Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo>> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
        if (!datas.isEmpty()) {
            String wbcode = datas.get(0).getAddonEntity().getLocationCode() + datas.get(0).getAddonEntity().getBinCode();
            etWBcode.setText(wbcode);
        }
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }


}