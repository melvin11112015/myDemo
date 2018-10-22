package com.weihan.scanner.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.ToastUtils;
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
import com.weihan.scanner.utils.TextUtils;
import com.weihan.scanner.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.scanner.Constant.KEY_SPREF_FUNC12_DATA;

public class Func12Activity extends BaseFuncActivity<Func12PresenterImpl> implements Func12MvpView, View.OnClickListener {

    EditText etFromBincode, etItemno;
    Button buttonCheck, buttonSubmit;
    RecyclerView recyclerView;

    private BinContentListAdapter adapter;
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
        buttonSubmit.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BinContentListAdapter(datas);
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

        loadPref();
    }

    @Override
    public void findView() {
        etFromBincode = findViewById(R.id.et_func12_from_bincode);
        etItemno = findViewById(R.id.et_func12_itemno);
        buttonCheck = findViewById(R.id.button_func12_check);
        buttonSubmit = findViewById(R.id.button_func12_submit);
        recyclerView = findViewById(R.id.recycler_func12);
    }

    @Override
    protected Func12PresenterImpl buildPresenter() {
        return new Func12PresenterImpl();
    }

    @Override
    public void onClick(View view) {
        if (view == buttonCheck) {
            presenter.acquireDatas(etItemno.getText().toString(), etFromBincode.getText().toString());
        } else if (view == buttonSubmit) {
            etFromBincode.requestFocus();
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
        etFromBincode.setText("");
        etItemno.setText("");
        datas.clear();
        notifyAdapter();
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

    private static class BinContentListAdapter extends BaseQuickAdapter<Polymorph<WarehouseTransferMultiAddon, BinContentInfo>, BaseViewHolder> {

        public BinContentListAdapter(@Nullable List<Polymorph<WarehouseTransferMultiAddon, BinContentInfo>> datas) {
            super(R.layout.item_func12, datas);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<WarehouseTransferMultiAddon, BinContentInfo> item) {
            helper.setText(R.id.tv_item_func12_mcn, item.getInfoEntity().getItem_No());
            helper.setText(R.id.tv_item_func12_from_binname, item.getInfoEntity().getLocation_Code());
            helper.setText(R.id.tv_item_func12_from_bincode, item.getInfoEntity().getBin_Code());
            helper.setText(R.id.tv_item_func12_quantity0, item.getInfoEntity().getQuantity_Base());
            helper.setText(R.id.et_item_func12_quantity1, item.getAddonEntity().getQuantity());
            EditText et = helper.getView(R.id.et_item_func12_quantity1);
            ViewHelper.setIntOnlyInputFilterForEditText(et);

            final Polymorph<WarehouseTransferMultiAddon, BinContentInfo> polymorphItem = item;

            View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isFocused) {
                    if (!isFocused) {
                        //((EditText)view).setText( polymorphItem.getAddonEntity().getQuantity());
                        String s = ((EditText) view).getText().toString();
                        if (TextUtils.isIntString(s) && Integer.valueOf(s) <= Integer.valueOf(polymorphItem.getInfoEntity().getQuantity_Base())) {
                            polymorphItem.getAddonEntity().setQuantity(s);
                        } else {
                            ((EditText) view).setText(polymorphItem.getAddonEntity().getQuantity());
                            ToastUtils.show(R.string.toast_reach_upper_limit);
                        }
                    }
                }
            };
            et.setOnFocusChangeListener(focusChangeListener);

            helper.addOnClickListener(R.id.tv_item_func12_delete);
            switch (item.getState()) {
                case FAILURE:
                    helper.setBackgroundColor(R.id.view_item_func12_state, Color.RED);
                    helper.setTextColor(R.id.tv_item_func12_state, Color.RED);
                    helper.setText(R.id.tv_item_func12_state, R.string.text_commit_fail);
                    break;
                case COMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func12_state, Color.GREEN);
                    helper.setTextColor(R.id.tv_item_func12_state, Color.GREEN);
                    helper.setText(R.id.tv_item_func12_state, R.string.text_committed);
                    break;
                case UNCOMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func12_state, Color.argb(0Xff, 0xff, 0x90, 0x40));
                    helper.setTextColor(R.id.tv_item_func12_state, Color.WHITE);
                    helper.setText(R.id.tv_item_func12_state, "");
                    break;
            }
        }
    }
}
