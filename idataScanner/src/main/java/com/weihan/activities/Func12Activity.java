package com.weihan.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.common.Utils.ViewHelper;
import com.weihan.ApiUitls;
import com.weihan.R;
import com.weihan.adapters.FuncRecyclerAdapter;
import com.weihan.bean.GeneralResult;
import com.weihan.bean.MaterialValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.common.Utils.ViewHelper.postFoucus;
import static com.weihan.adapters.FuncRecyclerAdapter.KEY_MAP_CODE;
import static com.weihan.adapters.FuncRecyclerAdapter.KEY_MAP_NUM;

public class Func12Activity extends BaseActivity implements View.OnClickListener {

    EditText etTag0, etTag1;
    RecyclerView recyclerView;
    Button buttonCheck0, buttonCheck1;
    FuncRecyclerAdapter adapter;

    List<Map<String, Object>> listData = new ArrayList<>();

    private void findView() {
        etTag0 = findViewById(R.id.et_func12_tag0);
        etTag1 = findViewById(R.id.et_func12_tag1);
        recyclerView = findViewById(R.id.recycler_func12);
        buttonCheck0 = findViewById(R.id.button_func12_check0);
        buttonCheck1 = findViewById(R.id.button_func12_check1);
    }

    protected void initGeneralView() {
        buttonCheck0.setOnClickListener(this);
        buttonCheck1.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FuncRecyclerAdapter(listData);
        recyclerView.setAdapter(adapter);

        etTag0.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    check0();
                    return true;
                }
                return false;
            }
        });

        etTag1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    check1();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func12);

        findView();
        initGeneralView();
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

    private void clearList() {
        listData.clear();
        adapter.notifyDataSetChanged();
        etTag0.setText("");
        etTag1.setText("");
    }


    private void check0() {
        final String check0tag = etTag0.getText().toString().trim();
        clearList();

        postFoucus(etTag0);
        if (check0tag.isEmpty()) {
            String toastStr = getString(R.string.toast_func_input_code1, getString(R.string.text_material));
            Toast.makeText(this, toastStr, Toast.LENGTH_LONG).show();
            return;
        }


        (new Thread() {
            @Override
            public void run() {
                GeneralResult<MaterialValue> generalResult = null;
                try {
                    generalResult = ApiUitls.checkMaterial(check0tag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (generalResult != null) {
                    for (MaterialValue value : generalResult.getValue()) {
                        Map<String, Object> map;
                        map = new HashMap<>();
                        map.put(KEY_MAP_CODE, value.getBin_Code());
                        map.put(KEY_MAP_NUM, value.getQuantity_Base());
                        listData.add(map);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            if (listData.isEmpty())
                                Toast.makeText(Func12Activity.this, R.string.toast_no_record, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            Toast.makeText(Func12Activity.this, R.string.toast_check_fail, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void check1() {
        final String check1tag = etTag1.getText().toString().trim();
        clearList();

        postFoucus(etTag1);
        if (check1tag.isEmpty()) {
            String toastStr = getString(R.string.toast_func_input_code1, getString(R.string.text_warehouse_position));
            Toast.makeText(this, toastStr, Toast.LENGTH_LONG).show();
            return;
        }


        (new Thread() {
            @Override
            public void run() {
                GeneralResult<MaterialValue> generalResult = null;
                try {
                    generalResult = ApiUitls.checkWarehouse(check1tag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (generalResult != null) {
                    for (MaterialValue value : generalResult.getValue()) {
                        Map<String, Object> map;
                        map = new HashMap<>();
                        map.put(KEY_MAP_CODE, value.getItem_No());
                        map.put(KEY_MAP_NUM, value.getQuantity_Base());
                        listData.add(map);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            if (listData.isEmpty())
                                Toast.makeText(Func12Activity.this, R.string.toast_no_record, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            Toast.makeText(Func12Activity.this, R.string.toast_check_fail, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        if (ViewHelper.isFastClick()) return;
        if (view == buttonCheck0) check0();
        if (view == buttonCheck1) check1();
    }
}
