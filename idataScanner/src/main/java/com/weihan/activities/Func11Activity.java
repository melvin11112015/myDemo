package com.weihan.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weihan.R;
import com.weihan.adapters.FuncRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.common.Utils.ViewHelper.postFoucus;
import static com.weihan.adapters.FuncRecyclerAdapter.KEY_MAP_CHECKED;
import static com.weihan.adapters.FuncRecyclerAdapter.KEY_MAP_CODE;

public class Func11Activity extends BaseActivity {

    public static final String KEY_SHAREPREF_FUNC11_WAREHOUSE = "KEY_SHAREPREF_FUNC11_WAREHOUSE";
    public static final String KEY_SHAREPREF_FUNC11_LIST = "KEY_SHAREPREF_FUNC11_LIST_";


    List<Map<String, Object>> listData = new ArrayList<>();
    Gson gson = new Gson();

    EditText etTag0, etTag1;
    TextView tvCount, tvCurrentTag0;
    RecyclerView recyclerView;
    Button buttonCheck, buttonSubmit, buttonAcquire;

    FuncRecyclerAdapter adapter;

    String listdataJson = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func11);

        findView();

    }

    protected void findView() {
        etTag0 = findViewById(R.id.et_func11_tag0);
        etTag1 = findViewById(R.id.et_func11_tag1);
        tvCount = findViewById(R.id.tv_func11_count);
        tvCurrentTag0 = findViewById(R.id.tv_func11_currentTag0);
        recyclerView = findViewById(R.id.recycler_func11);
        buttonCheck = findViewById(R.id.button_func11_check);
        buttonSubmit = findViewById(R.id.button_func11_submit);
        buttonAcquire = findViewById(R.id.button_func11_acquire);

        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
        buttonAcquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acquireData();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FuncRecyclerAdapter(listData);
        recyclerView.setAdapter(adapter);

        etTag0.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    acquireData();
                    return true;
                }
                return false;
            }
        });

        etTag1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    checkData();
                    return true;
                }
                return false;
            }
        });

        initTag0(KEY_SHAREPREF_FUNC11_WAREHOUSE);
        initListdata(KEY_SHAREPREF_FUNC11_LIST);
    }

    protected void initTag0(String sharedPrefKey) {
        String tempTag0 = sharedPreferences.getString(sharedPrefKey, "");
        if (!tempTag0.isEmpty()) {
            etTag0.setText(tempTag0);
            tvCurrentTag0.setText(tempTag0);
            postFoucus(etTag1);
        }
    }

    protected void initListdata(String sharedPrefKey) {

        listdataJson = sharedPreferences.getString(sharedPrefKey, "");
        System.out.println(listdataJson);
        if (!listdataJson.isEmpty()) {
            List<Map<String, Object>> tempListData = gson.fromJson(listdataJson, new TypeToken<List<Map<String, Object>>>() {
            }.getType());
            listData.addAll(tempListData);
            adapter.notifyDataSetChanged();
            tvCount.setText(String.valueOf(listData.size()));
        }
    }

    private void acquireData() {
        String packCode = etTag0.getText().toString().trim();
        if (packCode.isEmpty()) {
            String toastStr = getString(R.string.toast_func_input_code1, getString(R.string.text_warehouse_position));
            Toast.makeText(this, toastStr, Toast.LENGTH_LONG).show();
            postFoucus(etTag0);
            return;
        }
        // TODO: 7/15/2018 获得数据
        listData.clear();
        etTag0.setText(packCode);
        tvCurrentTag0.setText(packCode);
        listdataJson = "[{\"code\":\"1223\",\"num\":\"123\",\"checked\":true},{\"code\":\"12b23\",\"num\":\"1c23\",\"checked\":false}]";
        setList();

    }

    private void setList() {
        if (!listdataJson.isEmpty()) {
            List<Map<String, Object>> tempListData = gson.fromJson(listdataJson, new TypeToken<List<Map<String, Object>>>() {
            }.getType());
            listData.addAll(tempListData);
            adapter.notifyDataSetChanged();
            tvCount.setText(String.valueOf(listData.size()));
            postFoucus(etTag1);
        }
    }

    private void checkData() {


        String packCode = etTag0.getText().toString().trim();
        String mCode = etTag1.getText().toString().trim();

        if (packCode.isEmpty() || mCode.isEmpty()) {
            String toastStr = getString(R.string.toast_func_input_code2, getString(R.string.text_warehouse_position), getString(R.string.text_tag));
            Toast.makeText(this, toastStr, Toast.LENGTH_LONG).show();
            if (mCode.isEmpty()) postFoucus(etTag1);
            else if (packCode.isEmpty()) postFoucus(etTag0);
            return;
        }

        String currentCode = tvCurrentTag0.getText().toString().trim();
        if (!currentCode.equals(packCode) && !currentCode.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_list_diffenrcetag, getString(R.string.text_warehouse_position)), Toast.LENGTH_LONG).show();
            return;
        } else tvCurrentTag0.setText(packCode);

        boolean checkedFlag = false;
        for (int index = 0; index < listData.size(); index++) {
            Map<String, Object> map = listData.get(index);
            if (map.get(KEY_MAP_CODE).equals(mCode)) {
                map.put(KEY_MAP_CHECKED, true);
                listData.set(index, map);
                checkedFlag = true;
                adapter.notifyDataSetChanged();
                break;
                //recyclerView.scrollToPosition(listData.size() - 1);
                //tvCount.setText(String.valueOf(listData.size()));
            }
        }

        if (!checkedFlag)
            Toast.makeText(this, getString(R.string.toast_no_record), Toast.LENGTH_LONG).show();

        etTag1.setText("");
        postFoucus(etTag1);
    }

    private void submitData() {
        if (listData.isEmpty()) {
            Toast.makeText(this, R.string.toast_list_empty, Toast.LENGTH_LONG).show();
            return;
        }
        getListdataJson();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_SHAREPREF_FUNC11_LIST, getListdataJson());
        editor.putString(KEY_SHAREPREF_FUNC11_WAREHOUSE, getCurrentTag0Str());

        editor.apply();
    }

    public String getCurrentTag0Str() {
        if (tvCurrentTag0 != null)
            return tvCurrentTag0.getText().toString().trim();
        else
            return "";
    }

    public String getListdataJson() {
        listdataJson = gson.toJson(listData);
        return listdataJson;
    }

    protected void clearList() {
        (new AlertDialog.Builder(this))
                .setMessage(R.string.text_dialog_clear)
                .setPositiveButton(R.string.text_button_clear, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(KEY_SHAREPREF_FUNC11_LIST);
                        editor.remove(KEY_SHAREPREF_FUNC11_WAREHOUSE);
                        editor.apply();
                        listData.clear();

                        adapter.notifyDataSetChanged();
                        tvCount.setText(String.valueOf(listData.size()));
                        tvCurrentTag0.setText("");
                        etTag1.setText("");
                        etTag0.setText("");
                        postFoucus(etTag0);

                    }
                })
                .setNegativeButton(R.string.text_button_cancel, null)
                .show();
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
}
