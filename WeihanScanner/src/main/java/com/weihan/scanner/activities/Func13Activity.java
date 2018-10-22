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
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseTransferMultiAddon;
import com.weihan.scanner.entities.WhseTransferMultiInfo;
import com.weihan.scanner.mvpviews.Func13MvpView;
import com.weihan.scanner.presenters.Func13PresenterImpl;
import com.weihan.scanner.utils.AdapterHelper;
import com.weihan.scanner.utils.TextUtils;
import com.weihan.scanner.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.scanner.Constant.KEY_SPREF_FUNC13_DATA;

public class Func13Activity extends BaseFuncActivity<Func13PresenterImpl> implements Func13MvpView, View.OnClickListener {

    EditText etToBincode, etItemno;
    Button buttonCheck, buttonSubmit;
    RecyclerView recyclerView;

    private List<Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo>> datas = new ArrayList<>();
    private WhseTransferMultiListAdapter adapter;

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
        adapter = new WhseTransferMultiListAdapter(datas);
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

        loadPref();
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
            presenter.acquireDatas(etItemno.getText().toString(), etToBincode.getText().toString());
        } else if (view == buttonSubmit) {
            etToBincode.requestFocus();
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

    private static class WhseTransferMultiListAdapter extends BaseQuickAdapter<Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo>, BaseViewHolder> {

        public WhseTransferMultiListAdapter(@Nullable List<Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo>> datas) {
            super(R.layout.item_func13, datas);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo> item) {
            helper.setText(R.id.tv_item_func13_mcn, item.getInfoEntity().getItemNo());
            helper.setText(R.id.tv_item_func13_to_binname, item.getAddonEntity().getToLocationCode());
            helper.setText(R.id.tv_item_func13_to_bincode, item.getAddonEntity().getToBinCode());
            helper.setText(R.id.tv_item_func13_quantity0, item.getInfoEntity().getQuantity());
            helper.setText(R.id.et_item_func13_quantity1, item.getAddonEntity().getQuantity());
            EditText et = helper.getView(R.id.et_item_func13_quantity1);
            ViewHelper.setIntOnlyInputFilterForEditText(et);

            final Polymorph<WarehouseTransferMultiAddon, WhseTransferMultiInfo> polymorphItem = item;

            View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isFocused) {
                    if (!isFocused) {
                        //((EditText)view).setText( polymorphItem.getAddonEntity().getQuantity());
                        String s = ((EditText) view).getText().toString();
                        if (TextUtils.isIntString(s) && Integer.valueOf(s) <= Integer.valueOf(polymorphItem.getInfoEntity().getQuantity())) {
                            polymorphItem.getAddonEntity().setQuantity(s);
                        } else {
                            ((EditText) view).setText(polymorphItem.getAddonEntity().getQuantity());
                            ToastUtils.show(R.string.toast_reach_upper_limit);
                        }
                    }
                }
            };
            et.setOnFocusChangeListener(focusChangeListener);

            helper.addOnClickListener(R.id.tv_item_func13_delete);
            switch (item.getState()) {
                case FAILURE:
                    helper.setBackgroundColor(R.id.view_item_func13_state, Color.RED);
                    helper.setTextColor(R.id.tv_item_func13_state, Color.RED);
                    helper.setText(R.id.tv_item_func13_state, R.string.text_commit_fail);
                    break;
                case COMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func13_state, Color.GREEN);
                    helper.setTextColor(R.id.tv_item_func13_state, Color.GREEN);
                    helper.setText(R.id.tv_item_func13_state, R.string.text_committed);
                    break;
                case UNCOMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func13_state, Color.argb(0Xff, 0xff, 0x90, 0x40));
                    helper.setTextColor(R.id.tv_item_func13_state, Color.WHITE);
                    helper.setText(R.id.tv_item_func13_state, "");
                    break;
            }
        }
    }

}
