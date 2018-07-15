package com.weihan.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Func0Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Func0Fragment extends Fragment {

    public static final String KEY_MAP_CODE = "code";
    public static final String KEY_MAP_NUM = "num";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TAG0_TYPE = "tag0_type";
    private static final String ARG_TAG1_TYPE = "tag1_type";
    private static final String ARG_TYPE_CODE = "type_code";
    private static final String ARG_CODE_PACK = "pack_code";
    private static final String ARG_LIST_JSON = "list_json";
    String tag0Type, tag1Type, tempPackCode, listdataJson;
    int typeCode;

    List<Map<String, Object>> listData = new ArrayList<>();
    Gson gson = new Gson();

    EditText etTag0;
    EditText etTag1;
    TextView tvCount, tvTag0, tvTag1;
    RecyclerView recyclerView;
    Button buttonAdd;

    Func0RecyclerAdapter adapter;


    public Func0Fragment() {
        // Required empty public constructor
    }


    public static Func0Fragment newInstance(String tag0Type, String tag1Type, int typeCode, String tempPackCode, String listdataJson) {
        Func0Fragment fragment = new Func0Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_TAG0_TYPE, tag0Type);
        args.putString(ARG_TAG1_TYPE, tag1Type);
        args.putString(ARG_CODE_PACK, tempPackCode);
        args.putString(ARG_LIST_JSON, listdataJson);
        args.putInt(ARG_TYPE_CODE, typeCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tag0Type = getArguments().getString(ARG_TAG0_TYPE);
            tag1Type = getArguments().getString(ARG_TAG1_TYPE);
            typeCode = getArguments().getInt(ARG_TYPE_CODE, -1);
            tempPackCode = getArguments().getString(ARG_CODE_PACK);
            listdataJson = getArguments().getString(ARG_LIST_JSON);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_func0, container, false);
        etTag0 = view.findViewById(R.id.et_func0_tag0);
        etTag1 = view.findViewById(R.id.et_func0_tag1);
        tvTag0 = view.findViewById(R.id.tv_func0_tag0);
        tvTag1 = view.findViewById(R.id.tv_func0_tag1);
        tvCount = view.findViewById(R.id.tv_func0_count);
        recyclerView = view.findViewById(R.id.recycler_func0);
        buttonAdd = view.findViewById(R.id.button_func0_add);

        tvTag0.setText(String.format("%s%s", tag0Type, getString(R.string.text_tag)));
        tvTag1.setText(String.format("%s%s", tag1Type, getString(R.string.text_tag)));
        etTag0.setHint(getString(R.string.text_input_barcode, tag0Type));
        etTag1.setHint(getString(R.string.text_input_barcode, tag1Type));

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Func0RecyclerAdapter(listData);
        recyclerView.setAdapter(adapter);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //View headerView = LayoutInflater.from(this).inflate(R.layout.item_func0_header, null);
        //adapter.addHeaderView(headerView);

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


        if (!tempPackCode.isEmpty()) {
            etTag0.setText(tempPackCode);
            postFoucus(etTag1);
        }


        if (!listdataJson.isEmpty()) {
            List<Map<String, Object>> tempListData = gson.fromJson(listdataJson, new TypeToken<List<Map<String, Object>>>() {
            }.getType());
            listData.addAll(tempListData);
            adapter.notifyDataSetChanged();
            tvCount.setText(String.valueOf(listData.size()));
        }
        return view;
    }

    private void addData() {

        String packCode = etTag0.getText().toString().trim();
        String mCode = etTag1.getText().toString().trim();

        if (packCode.isEmpty() || mCode.isEmpty()) {
            String toastStr = getString(R.string.toast_func0_input_code, tag0Type, tag1Type);
            Toast.makeText(getContext(), toastStr, Toast.LENGTH_LONG).show();
            if (mCode.isEmpty()) postFoucus(etTag1);
            else if (packCode.isEmpty()) postFoucus(etTag0);
            return;
        }

        Map<String, Object> map;

        map = new HashMap<>();
        map.put(KEY_MAP_CODE, mCode);
        if (mCode.length() > 3) map.put(KEY_MAP_NUM, mCode.subSequence(0, 3));
        listData.add(map);

        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(listData.size() - 1);

        tvCount.setText(String.valueOf(listData.size()));
        etTag1.setText("");
        postFoucus(etTag1);

    }

    private void postFoucus(final EditText editText) {
        editText.postDelayed(new Runnable() {//给他个延迟时间
            @Override
            public void run() {
                editText.requestFocus();
            }
        }, 200);
    }

    public void clearList(boolean isToFocus) {
        listData.clear();
        adapter.notifyDataSetChanged();
        tvCount.setText(String.valueOf(listData.size()));
        etTag1.setText("");
        etTag0.setText("");
        if (isToFocus) postFoucus(etTag0);
    }

    public String getListdataJson() {
        listdataJson = gson.toJson(listData);
        return listdataJson;
    }

    public String getTag0Str() {
        if (etTag0 != null)
            return etTag0.getText().toString();
        else
            return "";
    }

    class Func0RecyclerAdapter extends BaseQuickAdapter<Map<String, Object>, BaseViewHolder> {

        public Func0RecyclerAdapter(@Nullable List<Map<String, Object>> data) {
            super(R.layout.item_func0_line, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Map<String, Object> item) {
            helper.setText(R.id.tv_item_func0_column0, (String) item.get(KEY_MAP_CODE));
            helper.setText(R.id.tv_item_func0_column1, (String) item.get(KEY_MAP_NUM));
        }
    }

}
