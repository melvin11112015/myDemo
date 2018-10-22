package com.weihan.scanner.activities;

import android.content.Intent;
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
import com.weihan.scanner.entities.OutputPutAwayAddon;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.mvpviews.Func9MvpView;
import com.weihan.scanner.presenters.Func9PresenterImpl;
import com.weihan.scanner.utils.AdapterHelper;
import com.weihan.scanner.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.scanner.Constant.KEY_CODE;
import static com.weihan.scanner.Constant.KEY_PARAM;
import static com.weihan.scanner.Constant.KEY_SPREF_FUNC9_DATA;
import static com.weihan.scanner.Constant.KEY_TITLE;
import static com.weihan.scanner.Constant.REQUEST_RECOMMAND;
import static com.weihan.scanner.Constant.RESULT_SUCCESS;

public class Func9Activity extends BaseFuncActivity<Func9PresenterImpl> implements Func9MvpView, View.OnClickListener {

    EditText etItemno, etBincode;
    Button buttonAdd, buttonSubmit, buttonRecommand;
    RecyclerView recyclerView;

    private List<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>> datas = new ArrayList<>();
    private OutputPutAwayListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func9);

        findView();
        initWidget();
    }

    @Override
    protected Func9PresenterImpl buildPresenter() {
        return new Func9PresenterImpl();
    }

    @Override
    public void initWidget() {
        buttonSubmit.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        buttonRecommand.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OutputPutAwayListAdapter(datas);
        AdapterHelper.setAdapterEmpty(this, adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_item_func9_delete) {
                    buildDeleteDialog(adapter, position);
                }
            }
        });
        recyclerView.setAdapter(adapter);

        loadPref();
    }

    @Override
    public void findView() {
        etItemno = findViewById(R.id.et_func9_itemno);
        etBincode = findViewById(R.id.et_func9_bincode);
        buttonAdd = findViewById(R.id.button_func9_add);
        buttonSubmit = findViewById(R.id.button_func9_submit);
        buttonRecommand = findViewById(R.id.button_func9_recommand);
        recyclerView = findViewById(R.id.recycler_func9);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonAdd) {
            presenter.attemptToAddPoly(datas, etItemno.getText().toString(), etBincode.getText().toString(), "1");
        } else if (view == buttonSubmit) {
            etItemno.requestFocus();
            presenter.submitDatas(datas);
        } else if (view == buttonRecommand) {
            String itemno = etItemno.getText().toString();
            if (itemno.isEmpty()) {
                ToastUtils.show("物料条码不能为空");
                return;
            }
            Intent intent = new Intent(Func9Activity.this, ChooseListActivity.class);
            intent.putExtra(KEY_CODE, itemno);
            intent.putExtra(KEY_TITLE, "选择推荐库位");
            startActivityForResult(intent, REQUEST_RECOMMAND);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RECOMMAND && resultCode == RESULT_SUCCESS) {
            String code = data.getStringExtra(KEY_CODE);
            String quantity = data.getStringExtra(KEY_PARAM);
            if (code != null) etBincode.setText(code);
            if (quantity != null)
                presenter.attemptToAddPoly(datas, etItemno.getText().toString(), etBincode.getText().toString(), quantity);
            else
                presenter.attemptToAddPoly(datas, etItemno.getText().toString(), etBincode.getText().toString(), "1");
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

        editor.putString(KEY_SPREF_FUNC9_DATA, prefJson);
        editor.apply();
    }

    @Override
    protected void loadPref() {
        sharedPreferences = getSharedPreferences(Constant.SHAREDPREF_NAME, MODE_PRIVATE);
        String prefJson = sharedPreferences.getString(KEY_SPREF_FUNC9_DATA, "");
        if (!prefJson.isEmpty()) {
            List<BinContentInfo> tmpList = new Gson()
                    .fromJson(prefJson, new TypeToken<List<BinContentInfo>>() {
                    }.getType());
            fillChooseListRecycler(tmpList);
        }
    }

    @Override
    public void clearDatas() {
        savePref(true);
        etBincode.setText("");
        etItemno.setText("");
        datas.clear();
        notifyAdapter();
    }

    @Override
    public void exitActivity() {

    }

    @Override
    public void fillChooseListRecycler(List<BinContentInfo> datas) {
    }

    @Override
    public void fillRecycler(List<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        notifyAdapter();
    }

    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    private static class OutputPutAwayListAdapter extends BaseQuickAdapter<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>, BaseViewHolder> {

        public OutputPutAwayListAdapter(@Nullable List<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>> datas) {
            super(R.layout.item_func9, datas);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<OutputPutAwayAddon, OutputPutAwayAddon> item) {
            helper.setText(R.id.tv_item_func9_mcn, item.getAddonEntity().getItemNo());
            helper.setText(R.id.tv_item_func9_bincode, item.getAddonEntity().getBinCode());
            helper.setText(R.id.et_item_func9_quantity1, item.getAddonEntity().getQuantity());
            EditText et = helper.getView(R.id.et_item_func9_quantity1);
            ViewHelper.setIntOnlyInputFilterForEditText(et);

            final Polymorph<OutputPutAwayAddon, OutputPutAwayAddon> polymorphItem = item;

            View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isFocused) {
                    if (!isFocused) {
                        //((EditText)view).setText( polymorphItem.getAddonEntity().getQuantity());
                        String s = ((EditText) view).getText().toString();
                        //if (TextUtils.isIntString(s) && Integer.valueOf(s) <= Integer.valueOf(polymorphItem.getInfoEntity().getQuantity_Base())) {
                        polymorphItem.getAddonEntity().setQuantity(s);
                       /* } else {
                            ((EditText) view).setText(polymorphItem.getAddonEntity().getQuantity());
                            ToastUtils.show(R.string.toast_reach_upper_limit);
                        }*/
                    }
                }
            };
            et.setOnFocusChangeListener(focusChangeListener);

            helper.addOnClickListener(R.id.tv_item_func9_delete);
            switch (item.getState()) {
                case FAILURE:
                    helper.setBackgroundColor(R.id.view_item_func9_state, Color.RED);
                    helper.setTextColor(R.id.tv_item_func9_state, Color.RED);
                    helper.setText(R.id.tv_item_func9_state, R.string.text_commit_fail);
                    break;
                case COMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func9_state, Color.GREEN);
                    helper.setTextColor(R.id.tv_item_func9_state, Color.GREEN);
                    helper.setText(R.id.tv_item_func9_state, R.string.text_committed);
                    break;
                case UNCOMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func9_state, Color.argb(0Xff, 0xff, 0x90, 0x40));
                    helper.setTextColor(R.id.tv_item_func9_state, Color.WHITE);
                    helper.setText(R.id.tv_item_func9_state, "");
                    break;
            }
        }
    }

}
