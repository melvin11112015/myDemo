package com.weihan.ligong.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weihan.ligong.BaseMVP.BaseFuncActivity;
import com.weihan.ligong.Constant;
import com.weihan.ligong.R;
import com.weihan.ligong.entities.OutstandingPurchLineInfo;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.entities.WarehouseReceiptAddon;
import com.weihan.ligong.mvpviews.Func0MvpView;
import com.weihan.ligong.presenters.Func0PresenterImpl;
import com.weihan.ligong.utils.AdapterHelper;
import com.weihan.ligong.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.ligong.Constant.KEY_SPREF_FUNC0_DATA;

public class Func0Activity extends BaseFuncActivity<Func0PresenterImpl> implements Func0MvpView, View.OnClickListener {

    RecyclerView recyclerView;
    EditText etCheck;
    Button btCheck, btSubmit;
    TextView tvCode;

    private PurchaseListAdapter adapter;
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
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter = new PurchaseListAdapter(datas);
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

        loadPref();
    }


    @Override
    public void fillRecycler(List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>> datas) {
        if (!datas.isEmpty()) tvCode.setText(datas.get(0).getInfoEntity().getDocument_No());
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
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
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void clearDatas() {
        savePref(true);
        tvCode.setText("");
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

    private static class PurchaseListAdapter extends BaseQuickAdapter<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>, BaseViewHolder> {

        InputFilter[] filters = new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                if (!TextUtils.isIntString(charSequence.toString()))
                    return "";
                else
                    return null;
            }
        }};


        public PurchaseListAdapter(@Nullable List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>> datas) {
            super(R.layout.item_func0, datas);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo> item) {
            helper.setText(R.id.tv_item_func0_mcn, item.getInfoEntity().getNo());
            helper.setText(R.id.tv_item_func0_name, item.getInfoEntity().getDescription());
            helper.setText(R.id.tv_item_func0_count0, item.getInfoEntity().getOutstanding_Quantity());
            helper.setText(R.id.et_item_func0_count1, item.getAddonEntity().getQuantity());
            EditText et = helper.getView(R.id.et_item_func0_count1);

            et.setFilters(filters);

            final Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo> polymorphItem = item;

            View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isFocused) {
                    if (!isFocused) {
                        //((EditText)view).setText( polymorphItem.getAddonEntity().getQuantity());
                        String s = ((EditText) view).getText().toString();
                        if (TextUtils.isIntString(s) && Integer.valueOf(s) <= Integer.valueOf(polymorphItem.getInfoEntity().getOutstanding_Quantity())) {
                            polymorphItem.getAddonEntity().setQuantity(s);
                        } else {
                            ((EditText) view).setText(polymorphItem.getAddonEntity().getQuantity());
                            ToastUtils.show(R.string.toast_reach_upper_limit);
                        }
                    }
                }
            };
            et.setOnFocusChangeListener(focusChangeListener);

            helper.addOnClickListener(R.id.tv_item_func0_delete);
            switch (item.getState()) {
                case FAILURE:
                    helper.setBackgroundColor(R.id.view_item_func0_state, Color.RED);
                    helper.setTextColor(R.id.tv_item_func0_state, Color.RED);
                    helper.setText(R.id.tv_item_func0_state, R.string.text_commit_fail);
                    break;
                case COMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func0_state, Color.GREEN);
                    helper.setTextColor(R.id.tv_item_func0_state, Color.GREEN);
                    helper.setText(R.id.tv_item_func0_state, R.string.text_committed);
                    break;
                case UNCOMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func0_state, Color.argb(0Xff, 0xff, 0x90, 0x40));
                    helper.setTextColor(R.id.tv_item_func0_state, Color.WHITE);
                    helper.setText(R.id.tv_item_func0_state, "");
                    break;
            }
        }
    }
}
