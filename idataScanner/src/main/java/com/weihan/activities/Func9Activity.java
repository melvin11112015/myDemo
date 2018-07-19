package com.weihan.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weihan.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.common.Utils.ViewHelper.postFoucus;
import static com.weihan.activities.Func9Activity.Func9RecyclerAdapter.KEY_MAP_CODE;
import static com.weihan.activities.Func9Activity.Func9RecyclerAdapter.KEY_MAP_PUTON;
import static com.weihan.activities.Func9Activity.Func9RecyclerAdapter.KEY_MAP_WITHDRAW;
import static com.weihan.adapters.FuncRecyclerAdapter.KEY_MAP_NUM;

public class Func9Activity extends BaseActivity implements View.OnClickListener {

    public static final String KEY_SHAREPREF_FUNC9_LIST = "KEY_SHAREPREF_FUNC9_LIST_";

    protected TabLayout tabLayout;

    EditText etTag0, etTag1;
    TextView tvCount;
    RecyclerView recyclerView;
    Button buttonPuton, buttonWithdraw, buttonSubmit;

    Func9RecyclerAdapter adapter;
    String listdataJson = "";
    List<Map<String, Object>> listData = new ArrayList<>();
    Gson gson = new Gson();
    Func9RecyclerAdapter.OnItemClickListener itemClickListener = new Func9RecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            ((Func9RecyclerAdapter) adapter).setCheckedPositon(position);
            adapter.notifyDataSetChanged();
            System.out.println(position);
        }
    };
    private int currentTabCode = -1;

    private void findview() {
        tabLayout = findViewById(R.id.tablayout_func9);
        recyclerView = findViewById(R.id.recycler_func9);
        buttonPuton = findViewById(R.id.button_func9_puton);
        buttonWithdraw = findViewById(R.id.button_func9_withdraw);
        tvCount = findViewById(R.id.tv_func9_count);
        etTag0 = findViewById(R.id.et_func9_tag0);
        etTag1 = findViewById(R.id.et_func9_tag1);
        buttonSubmit = findViewById(R.id.button_func9_submit);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func9);

        findview();
        initview();
        initListdata(KEY_SHAREPREF_FUNC9_LIST);
        selectTab(0);
    }

    private void initview() {

        adapter = new Func9RecyclerAdapter(listData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buttonPuton.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);
        buttonWithdraw.setOnClickListener(this);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        etTag1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (currentTabCode == 0) {
                        doWithdraw();
                    } else if (currentTabCode == 1) {
                        doPuton();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void doPuton() {
        String warehouse = etTag0.getText().toString().trim();
        String packCode = etTag1.getText().toString().trim();
        if (warehouse.isEmpty()) {
            String toastStr = getString(R.string.toast_func_input_code1, getString(R.string.text_warehouse_position));
            Toast.makeText(this, toastStr, Toast.LENGTH_LONG).show();
            postFoucus(etTag0);
            return;
        }
        if (packCode.isEmpty() && adapter.getCheckedPositon() < 0) {
            Toast.makeText(this, R.string.toast_puton_hint, Toast.LENGTH_LONG).show();
            return;
        } else {
            for (int i = 0; i < listData.size(); i++)
                if (listData.get(i).get(KEY_MAP_CODE).equals(packCode)) {
                    adapter.setCheckedPositon(i);
                    break;
                }
            if (adapter.getCheckedPositon() < 0) {
                Toast.makeText(this, R.string.toast_no_record, Toast.LENGTH_LONG).show();
                return;
            }
        }
        Map<String, Object> map = listData.get(adapter.getCheckedPositon());
        map.put(KEY_MAP_PUTON, warehouse);
        listData.set(adapter.getCheckedPositon(), map);
        adapter.notifyDataSetChanged();
        etTag1.setText("");
        postFoucus(etTag1);
    }

    private void doWithdraw() {
        if (!beforeAdd()) return;

        String tag1 = etTag1.getText().toString().trim();

        Map<String, Object> map;

        map = new HashMap<>();
        map.put(KEY_MAP_CODE, tag1);

        if (tag1.length() > 3)
            map.put(KEY_MAP_NUM, tag1.subSequence(0, 3));
        else
            map.put(KEY_MAP_NUM, "");
        map.put(KEY_MAP_WITHDRAW, etTag0.getText().toString().trim());
        map.put(KEY_MAP_PUTON, "");
        listData.add(map);

        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(listData.size() - 1);

        tvCount.setText(String.valueOf(listData.size()));
        etTag1.setText("");
        postFoucus(etTag1);

    }

    private boolean beforeAdd() {
        String warehouse = etTag0.getText().toString().trim();
        String PackCode = etTag1.getText().toString().trim();

        if (warehouse.isEmpty() || PackCode.isEmpty()) {
            String toastStr = getString(R.string.toast_func_input_code2, getString(R.string.text_warehouse_position), getString(R.string.text_pack));
            Toast.makeText(this, toastStr, Toast.LENGTH_LONG).show();
            if (PackCode.isEmpty()) postFoucus(etTag1);
            else postFoucus(etTag0);
            return false;
        }

        return true;
    }

    private void selectTab(int i) {
        currentTabCode = i;
        if (i == 0) {
            buttonWithdraw.setVisibility(View.VISIBLE);
            buttonPuton.setVisibility(View.GONE);
            adapter.setCheckedPositon(-1);
            adapter.setOnItemClickListener(null);
        } else if (i == 1) {
            buttonWithdraw.setVisibility(View.GONE);
            buttonPuton.setVisibility(View.VISIBLE);
            adapter.setOnItemClickListener(itemClickListener);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == buttonPuton) doPuton();
        if (view == buttonWithdraw) doWithdraw();
        if (view == buttonSubmit) submitData();
    }

    protected void clearList() {
        (new AlertDialog.Builder(this))
                .setMessage(R.string.text_dialog_clear)
                .setPositiveButton(R.string.text_button_clear, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(KEY_SHAREPREF_FUNC9_LIST);
                        editor.apply();
                        listData.clear();

                        adapter.notifyDataSetChanged();
                        tvCount.setText(String.valueOf(listData.size()));
                        etTag1.setText("");
                        etTag0.setText("");
                        postFoucus(etTag0);

                    }
                })
                .setNegativeButton(R.string.text_button_cancel, null)
                .show();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_SHAREPREF_FUNC9_LIST, getListdataJson());

        editor.apply();
    }

    public String getListdataJson() {
        listdataJson = gson.toJson(listData);
        return listdataJson;
    }

    protected void initListdata(String sharedPrefKey) {

        // TODO: 7/19/2018 从网络获得理货区数据

        listdataJson = sharedPreferences.getString(sharedPrefKey, "");
        if (!listdataJson.isEmpty()) {
            List<Map<String, Object>> tempListData = gson.fromJson(listdataJson, new TypeToken<List<Map<String, Object>>>() {
            }.getType());
            listData.addAll(tempListData);
            adapter.notifyDataSetChanged();
            tvCount.setText(String.valueOf(listData.size()));
        }
    }

    private void submitData() {
        if (listData.isEmpty()) {
            Toast.makeText(this, R.string.toast_list_empty, Toast.LENGTH_LONG).show();
            return;
        }
        getListdataJson();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_func, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuitem_clear) {
            clearList();
        }
        return super.onOptionsItemSelected(item);
    }

    class Func9RecyclerAdapter extends BaseQuickAdapter<Map<String, Object>, BaseViewHolder> {

        public static final String KEY_MAP_CODE = "code";
        public static final String KEY_MAP_NUM = "num";
        public static final String KEY_MAP_WITHDRAW = "withdraw";
        public static final String KEY_MAP_PUTON = "puton";

        private int checkedPositon = -1;

        public Func9RecyclerAdapter(@Nullable List<Map<String, Object>> data) {
            super(R.layout.item_func9_line, data);
        }

        public int getCheckedPositon() {
            return checkedPositon;
        }

        public void setCheckedPositon(int checkedPositon) {
            this.checkedPositon = checkedPositon;
        }

        @Override
        protected void convert(BaseViewHolder helper, Map<String, Object> item) {
            helper.setText(R.id.tv_item_func9_column0, (String) item.get(KEY_MAP_CODE));
            helper.setText(R.id.tv_item_func9_column1, (String) item.get(KEY_MAP_NUM));
            helper.setText(R.id.tv_item_func9_column2, (String) item.get(KEY_MAP_WITHDRAW));
            helper.setText(R.id.tv_item_func9_column3, (String) item.get(KEY_MAP_PUTON));
            helper.setTextColor(R.id.tv_item_func9_column0, helper.getAdapterPosition() == checkedPositon ? Color.RED : Color.BLACK);
            helper.setTextColor(R.id.tv_item_func9_column1, helper.getAdapterPosition() == checkedPositon ? Color.RED : Color.BLACK);
            helper.setTextColor(R.id.tv_item_func9_column2, helper.getAdapterPosition() == checkedPositon ? Color.RED : Color.BLACK);
            helper.setTextColor(R.id.tv_item_func9_column3, helper.getAdapterPosition() == checkedPositon ? Color.RED : Color.BLACK);
        }
    }
}
