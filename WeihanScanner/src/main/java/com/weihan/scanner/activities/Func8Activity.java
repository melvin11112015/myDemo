package com.weihan.scanner.activities;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weihan.scanner.BaseMVP.BaseFuncActivity;
import com.weihan.scanner.Constant;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseTransferSingleAddon;
import com.weihan.scanner.mvpviews.Func8MvpView;
import com.weihan.scanner.presenters.Func8PresenterImpl;
import com.weihan.scanner.utils.AdapterHelper;
import com.weihan.scanner.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import static com.weihan.scanner.Constant.KEY_SPREF_FUNC8_DATA;

public class Func8Activity extends BaseFuncActivity<Func8PresenterImpl> implements Func8MvpView, View.OnClickListener {

    EditText etFromWBcode, etItemno, etToWBcode, etQuantity;
    Button buttonMove, buttonCheck;
    RecyclerView recyclerView;

    private BinContentListAdapter adapter;
    private List<Polymorph<WarehouseTransferSingleAddon, BinContentInfo>> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func8);

        findView();
        initWidget();
    }

    @Override
    protected Func8PresenterImpl buildPresenter() {
        return new Func8PresenterImpl();
    }


    @Override
    public void findView() {
        etFromWBcode = findViewById(R.id.et_func8_from_bincode);
        etItemno = findViewById(R.id.et_func8_itemno);
        etToWBcode = findViewById(R.id.et_func8_to_bincode);
        etQuantity = findViewById(R.id.et_func8_quantity);
        buttonCheck = findViewById(R.id.button_func8_check);
        buttonMove = findViewById(R.id.button_func8_move);
        recyclerView = findViewById(R.id.recycler_func8);
    }

    @Override
    public void initWidget() {
        buttonCheck.setOnClickListener(this);
        buttonMove.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BinContentListAdapter(datas);
        AdapterHelper.setAdapterEmpty(this, adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_item_func8_delete) {
                    buildDeleteDialog(adapter, position);
                    ((BinContentListAdapter) adapter).setSelectedPosition(-1);
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ((BinContentListAdapter) adapter).setSelectedPosition(position);
            }
        });
        recyclerView.setAdapter(adapter);

        etItemno.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    doChecking();
                    //return true;
                }
                return false;
            }
        });
        etQuantity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    doMoving();
                    return true;
                }
                return false;
            }
        });
        loadPref();
        ViewHelper.initEdittextInputState(this, etFromWBcode);
    }

    private void doChecking() {
        presenter.acquireDatas(etItemno.getText().toString(), etFromWBcode.getText().toString());
    }

    private void doMoving() {
        presenter.inputAddonData(adapter.getSelectedPosition(),
                datas,
                etQuantity.getText().toString(),
                etToWBcode.getText().toString());
    }

    @Override
    public void onClick(View view) {
        if (view == buttonCheck) {
            doChecking();
        } else if (view == buttonMove) {
            doMoving();
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

        editor.putString(KEY_SPREF_FUNC8_DATA, prefJson);
        editor.apply();
    }

    @Override
    protected void loadPref() {
        sharedPreferences = getSharedPreferences(Constant.SHAREDPREF_NAME, MODE_PRIVATE);
        String prefJson = sharedPreferences.getString(KEY_SPREF_FUNC8_DATA, "");
        if (!prefJson.isEmpty()) {
            List<Polymorph<WarehouseTransferSingleAddon, BinContentInfo>> tmpList = new Gson()
                    .fromJson(prefJson, new TypeToken<List<Polymorph<WarehouseTransferSingleAddon, BinContentInfo>>>() {
                    }.getType());
            fillRecycler(tmpList);
        }
    }

    @Override
    protected void clearDatas() {
        savePref(true);
        etToWBcode.setText("");
        etFromWBcode.setText("");
        etItemno.setText("");
        etQuantity.setText("");
        datas.clear();
        notifyAdapter();
    }

    @Override
    protected void submitDatas() {
        presenter.submitDatas(datas);
    }


    @Override
    public void fillRecycler(List<Polymorph<WarehouseTransferSingleAddon, BinContentInfo>> datas) {
        if (!datas.isEmpty()) {
            BinContentInfo info = datas.get(0).getInfoEntity();
            etItemno.setText(info.getItem_No());
            WarehouseTransferSingleAddon addon = datas.get(0).getAddonEntity();
            etFromWBcode.setText(info.getBin_Code());
            etToWBcode.setText(addon.getToBinCode());
            adapter.setSelectedPosition(0);
        } else {
            adapter.setSelectedPosition(-1);
        }
        this.datas.clear();
        this.datas.addAll(datas);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    private static class BinContentListAdapter extends BaseQuickAdapter<Polymorph<WarehouseTransferSingleAddon, BinContentInfo>, BaseViewHolder> {

        private int selectedPosition = -1;

        public BinContentListAdapter(@Nullable List<Polymorph<WarehouseTransferSingleAddon, BinContentInfo>> datas) {
            super(R.layout.item_func8, datas);
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
            notifyDataSetChanged();
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<WarehouseTransferSingleAddon, BinContentInfo> item) {
            helper.setText(R.id.tv_item_func8_mcn, item.getInfoEntity().getItem_No());
            helper.setText(R.id.tv_item_func8_from_binname, item.getInfoEntity().getLocation_Code());
            helper.setText(R.id.tv_item_func8_from_bincode, item.getInfoEntity().getBin_Code());
            helper.setText(R.id.tv_item_func8_quantity0, item.getInfoEntity().getQuantity_Base());
            helper.setText(R.id.tv_item_func8_quantity1, item.getAddonEntity().getQuantity());
            helper.setText(R.id.tv_item_func8_to_binname, item.getAddonEntity().getToLocationCode());
            helper.setText(R.id.tv_item_func8_to_bincode, item.getAddonEntity().getToBinCode());
            helper.setBackgroundRes(R.id.la_item_func8, selectedPosition == helper.getAdapterPosition() ? R.drawable.melvin_card_shadow_blue : R.drawable.melvin_card_shadow);

            helper.addOnClickListener(R.id.tv_item_func8_delete);
            switch (item.getState()) {
                case FAILURE:
                    helper.setBackgroundColor(R.id.view_item_func8_state, Color.RED);
                    helper.setTextColor(R.id.tv_item_func8_state, Color.RED);
                    helper.setText(R.id.tv_item_func8_state, R.string.text_commit_fail);
                    break;
                case COMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func8_state, Color.GREEN);
                    helper.setTextColor(R.id.tv_item_func8_state, Color.GREEN);
                    helper.setText(R.id.tv_item_func8_state, R.string.text_committed);
                    break;
                case UNCOMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func8_state, Color.argb(0Xff, 0xff, 0x90, 0x40));
                    helper.setTextColor(R.id.tv_item_func8_state, Color.WHITE);
                    helper.setText(R.id.tv_item_func8_state, "");
                    break;
            }
        }
    }
}
