package com.weihan.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.common.Utils.ViewHelper.postFoucus;
import static com.weihan.adapters.FuncRecyclerAdapter.KEY_MAP_CODE;
import static com.weihan.adapters.FuncRecyclerAdapter.KEY_MAP_NUM;

public abstract class BaseFuncActivity extends BaseActivity implements View.OnClickListener {

    protected Set<String> shardKeySet = new HashSet<>();
    String listdataJson = "";
    List<Map<String, Object>> listData = new ArrayList<>();
    Gson gson = new Gson();

    EditText etTag0, etTag1, etTag2;
    TextView tvCount, tvCurrentTag0;
    RecyclerView recyclerView;
    Button buttonAdd, buttonSubmit;
    FuncRecyclerAdapter adapter;

    protected abstract void findView();

    protected abstract boolean beforeAdd();

    protected void clearList() {
        (new AlertDialog.Builder(this))
                .setMessage(R.string.text_dialog_clear)
                .setPositiveButton(R.string.text_button_clear, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        for (String sharedKey : shardKeySet) editor.remove(sharedKey);
                        editor.apply();
                        listData.clear();

                        adapter.notifyDataSetChanged();
                        tvCount.setText(String.valueOf(listData.size()));
                        tvCurrentTag0.setText("");
                        etTag1.setText("");
                        etTag0.setText("");
                        etTag2.setText("");
                        postFoucus(etTag0);

                    }
                })
                .setNegativeButton(R.string.text_button_cancel, null)
                .show();
    }

    protected void initGeneralView() {

        buttonAdd.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FuncRecyclerAdapter(listData);
        recyclerView.setAdapter(adapter);

        etTag1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    addData();
                    return true;
                }
                return false;
            }
        });
    }

    protected void initListdata(String sharedPrefKey) {
        shardKeySet.add(sharedPrefKey);
        listdataJson = sharedPreferences.getString(sharedPrefKey, "");
        if (!listdataJson.isEmpty()) {
            List<Map<String, Object>> tempListData = gson.fromJson(listdataJson, new TypeToken<List<Map<String, Object>>>() {
            }.getType());
            listData.addAll(tempListData);
            adapter.notifyDataSetChanged();
            tvCount.setText(String.valueOf(listData.size()));
        }
    }

    protected void initTag0(String sharedPrefKey) {
        shardKeySet.add(sharedPrefKey);
        String tempTag0 = sharedPreferences.getString(sharedPrefKey, "");
        if (!tempTag0.isEmpty()) {
            etTag0.setText(tempTag0);
            tvCurrentTag0.setText(tempTag0);
            postFoucus(etTag1);
        }
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


    protected void addData() {
        if (!beforeAdd()) return;
        addToList();
    }

    protected void addToList() {
        String tag1 = etTag1.getText().toString().trim();

        Map<String, Object> map;

        map = new HashMap<>();
        map.put(KEY_MAP_CODE, tag1);
        // TODO: 7/15/2018 数量编码方法
        if (tag1.length() > 3)
            map.put(KEY_MAP_NUM, tag1.subSequence(0, 3));
        else
            map.put(KEY_MAP_NUM, "");
        listData.add(map);

        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(listData.size() - 1);

        tvCount.setText(String.valueOf(listData.size()));
        etTag1.setText("");
        postFoucus(etTag1);
    }

    protected void submitData() {
        if (listData.isEmpty()) {
            Toast.makeText(this, R.string.toast_list_empty, Toast.LENGTH_LONG).show();
            return;
        }
        getListdataJson();
    }


}
