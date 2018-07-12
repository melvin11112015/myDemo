package com.weihan.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.Utils.ViewHelper;
import com.weihan.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Func0Activity extends BaseActivity implements View.OnClickListener {


    List<Map<String, Object>> listData = new ArrayList<>();
    EditText etPackCode;
    EditText etMaterial;
    TextView tvCount;
    RecyclerView recyclerView;
    Func0Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func0);

        etPackCode = findViewById(R.id.et_tag_pack_s);
        etMaterial = findViewById(R.id.et_tag_material);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        tvCount = findViewById(R.id.tv_func0_count);
        Button buttonAdd = findViewById(R.id.button_func0_add);
        buttonAdd.setOnClickListener(this);

        recyclerView = findViewById(R.id.recycler_func0);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Func0Adapter(listData);
        recyclerView.setAdapter(adapter);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //View headerView = LayoutInflater.from(this).inflate(R.layout.item_func0_header, null);
        //adapter.addHeaderView(headerView);

        etMaterial.setOnKeyListener(new View.OnKeyListener() {
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

    private void addData() {

        String packCode = etPackCode.getText().toString().trim();
        String mCode = etMaterial.getText().toString().trim();

        if (packCode.isEmpty() || mCode.isEmpty()) {
            Toast.makeText(this, "请输入物料编码和包装编码", Toast.LENGTH_LONG).show();
            if (mCode.isEmpty()) postFoucus(etMaterial);
            else if (packCode.isEmpty()) postFoucus(etPackCode);
            return;
        }

        Map<String, Object> map;

        map = new HashMap<>();
        map.put("code", mCode);
        if (mCode.length() > 3) map.put("num", mCode.subSequence(0, 3));
        listData.add(map);

        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(listData.size() - 1);

        tvCount.setText(String.valueOf(listData.size()));
        etMaterial.setText("");
        postFoucus(etMaterial);

    }

    private void clearList() {
        listData.clear();
        adapter.notifyDataSetChanged();
        tvCount.setText(String.valueOf(listData.size()));
        etMaterial.setText("");
        etPackCode.setText("");
        postFoucus(etPackCode);
    }

    @Override
    public void onClick(View view) {
        if (ViewHelper.isFastClick()) return;
        switch (view.getId()) {
            case R.id.button_func0_add:
                addData();
                break;
            case R.id.button_func0_submit:
                break;
            default:
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_func0, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuitem_clear) {
            clearList();
        }
        return super.onOptionsItemSelected(item);
    }

    private void postFoucus(final EditText editText) {
        editText.postDelayed(new Runnable() {//给他个延迟时间
            @Override
            public void run() {
                editText.requestFocus();
            }
        }, 200);
    }

    class Func0Adapter extends BaseQuickAdapter<Map<String, Object>, BaseViewHolder> {

        public Func0Adapter(@Nullable List<Map<String, Object>> data) {
            super(R.layout.item_func0_line, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Map<String, Object> item) {
            helper.setText(R.id.tv_item_func0_column0, (String) item.get("code"));
            helper.setText(R.id.tv_item_func0_column1, (String) item.get("num"));
        }
    }


}
