package com.weihan.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.weihan.ApiUitls;
import com.weihan.R;
import com.weihan.bean.GeneralResult;
import com.weihan.bean.MaterialValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.common.Utils.ViewHelper.postFoucus;
import static com.weihan.adapters.FuncRecyclerAdapter.KEY_MAP_CODE;
import static com.weihan.adapters.FuncRecyclerAdapter.KEY_MAP_NUM;
import static com.weihan.adapters.FuncRecyclerAdapter.KEY_MAP_STATUS;


public class Func16Activity extends BaseFuncActivity {

    public static final String KEY_SHAREPREF_FUNC1_WAREHOUSE = "KEY_SHAREPREF_FUNC1_WAREHOUSE";
    public static final String KEY_SHAREPREF_FUNC1_LIST = "KEY_SHAREPREF_FUNC1_LIST_";

    public static final String KEY_SHAREPREF_FUNC6_WAREHOUSE = "KEY_SHAREPREF_FUNC6_WAREHOUSE";
    public static final String KEY_SHAREPREF_FUNC6_LIST = "KEY_SHAREPREF_FUNC6_LIST_";

    Button buttonRecommand;

    private int typeCode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func16);

        typeCode = getIntent().getIntExtra("code", -2);

        findView();
        initGeneralView();

        initTag0(KEY_SHAREPREF_FUNC1_WAREHOUSE);
        initListdata(KEY_SHAREPREF_FUNC1_LIST);
    }

    @Override
    protected void initGeneralView() {
        super.initGeneralView();

        buttonRecommand.setOnClickListener(this);

        etTag0.setHint(getString(R.string.text_input_barcode, getString(R.string.text_warehouse_position)));
        etTag1.setHint(getString(R.string.text_input_barcode, getString(R.string.text_pack)));

        etTag2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    recommandWarehouse();
                    return true;
                }
                return false;
            }
        });
    }

    protected void findView() {
        etTag0 = findViewById(R.id.et_func1_tag0);
        etTag1 = findViewById(R.id.et_func1_tag1);
        etTag2 = findViewById(R.id.et_func1_tag2);
        tvCount = findViewById(R.id.tv_func1_count);
        tvCurrentTag0 = findViewById(R.id.tv_func1_currentTag0);
        recyclerView = findViewById(R.id.recycler_func1);
        buttonAdd = findViewById(R.id.button_func1_add);
        buttonSubmit = findViewById(R.id.button_func1_submit);
        buttonRecommand = findViewById(R.id.button_func1_recommand);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonAdd) addData();
        if (view == buttonRecommand) recommandWarehouse();
        if (view == buttonSubmit) submitData();

    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (typeCode == 1) {
            editor.putString(KEY_SHAREPREF_FUNC1_WAREHOUSE, getCurrentTag0Str());
            editor.putString(KEY_SHAREPREF_FUNC1_LIST, getListdataJson());
        } else if (typeCode == 6) {
            editor.putString(KEY_SHAREPREF_FUNC6_WAREHOUSE, getCurrentTag0Str());
            editor.putString(KEY_SHAREPREF_FUNC6_LIST, getListdataJson());
        } else return;
        editor.apply();
    }

    @Override
    protected void addToList() {
        String tag1 = etTag1.getText().toString().trim();

        Map<String, Object> map;

        map = new HashMap<>();
        map.put(KEY_MAP_CODE, tag1);
        map.put(KEY_MAP_STATUS, getString(R.string.text_status_pending));
        // TODO: 7/15/2018 数量编码方法
        map.put(KEY_MAP_NUM, "1");
        listData.add(map);

        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(listData.size() - 1);

        tvCount.setText(String.valueOf(listData.size()));
        etTag1.setText("");
        postFoucus(etTag1);

    }

    @Override
    protected boolean beforeAdd() {
        String warehouse = etTag0.getText().toString().trim();
        String PackCode = etTag1.getText().toString().trim();

        if (warehouse.isEmpty() || PackCode.isEmpty()) {
            String toastStr = getString(R.string.toast_func_input_code2, getString(R.string.text_warehouse_position), getString(R.string.text_pack));
            Toast.makeText(this, toastStr, Toast.LENGTH_LONG).show();
            if (PackCode.isEmpty()) postFoucus(etTag1);
            else postFoucus(etTag0);
            return false;
        }

        String currentCode = tvCurrentTag0.getText().toString().trim();
        if (!currentCode.equals(warehouse) && !currentCode.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_list_diffenrcetag, getString(R.string.text_warehouse_position)), Toast.LENGTH_LONG).show();
            return false;
        } else tvCurrentTag0.setText(warehouse);

        return true;
    }


    private void recommandWarehouse() {
        final String mCode = etTag2.getText().toString().trim();

        if (mCode.isEmpty()) {
            String toastStr = getString(R.string.toast_func_input_code1, getString(R.string.text_material));
            Toast.makeText(this, toastStr, Toast.LENGTH_LONG).show();
            postFoucus(etTag2);
            return;
        }

        (new Thread() {
            @Override
            public void run() {
                GeneralResult generalResult = null;
                final List<MaterialValue> materialValueList;
                try {
                    generalResult = ApiUitls.checkMaterial(mCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (generalResult != null) {
                    materialValueList = generalResult.getValue();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            if (materialValueList.isEmpty())
                                Toast.makeText(Func16Activity.this, R.string.toast_no_record, Toast.LENGTH_SHORT).show();
                            else {
                                etTag0.setText(materialValueList.get(0).getBin_Code());
                                postFoucus(etTag1);
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Func16Activity.this, R.string.toast_check_fail, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }


}
