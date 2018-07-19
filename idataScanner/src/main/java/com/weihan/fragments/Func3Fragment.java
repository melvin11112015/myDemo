package com.weihan.fragments;


import android.os.Bundle;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weihan.R;
import com.weihan.adapters.FuncRecyclerAdapter;
import com.weihan.interfaces.FragmentClearInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.common.Utils.ViewHelper.postFoucus;
import static com.weihan.adapters.FuncRecyclerAdapter.KEY_MAP_CODE;
import static com.weihan.adapters.FuncRecyclerAdapter.KEY_MAP_NUM;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Func3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Func3Fragment extends Fragment implements FragmentClearInterface {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TAG0_TYPE = "tag0_type";
    private static final String ARG_TAG1_TYPE = "tag1_type";
    private static final String ARG_TYPE_CODE = "type_code";
    private static final String ARG_TAG0_CURRENT = "tag0_current";
    private static final String ARG_LIST_JSON = "list_json";

    String tag0Type, tag1Type, listdataJson, sharePrefPackCoe;
    int typeCode;

    List<Map<String, Object>> listData = new ArrayList<>();
    Gson gson = new Gson();

    EditText etTag0, etTag1;
    TextView tvCount, tvTag0, tvTag1, tvCurrentTag0;
    RecyclerView recyclerView;
    Button buttonAdd, buttonRemove, buttonSubmit, buttonAcquire;

    FuncRecyclerAdapter adapter;


    public Func3Fragment() {
        // Required empty public constructor
    }


    public static Func3Fragment newInstance(String tag0Type, String tag1Type, int typeCode, String currentTag0, String listdataJson) {
        Func3Fragment fragment = new Func3Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_TAG0_TYPE, tag0Type);
        args.putString(ARG_TAG1_TYPE, tag1Type);
        args.putString(ARG_TAG0_CURRENT, currentTag0);
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
            sharePrefPackCoe = getArguments().getString(ARG_TAG0_CURRENT);
            listdataJson = getArguments().getString(ARG_LIST_JSON);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_func3, container, false);
        etTag0 = view.findViewById(R.id.et_func3_tag0);
        etTag1 = view.findViewById(R.id.et_func3_tag1);
        tvTag0 = view.findViewById(R.id.tv_func3_tag0);
        tvTag1 = view.findViewById(R.id.tv_func3_tag1);
        tvCount = view.findViewById(R.id.tv_func3_count);
        tvCurrentTag0 = view.findViewById(R.id.tv_func3_currentTag0);
        recyclerView = view.findViewById(R.id.recycler_func3);
        buttonAdd = view.findViewById(R.id.button_func3_add);
        buttonRemove = view.findViewById(R.id.button_func3_remove);
        buttonSubmit = view.findViewById(R.id.button_func3_submit);
        buttonAcquire = view.findViewById(R.id.button_func3_acquire);


        tvTag0.setText(String.format("%s%s", tag0Type, getString(R.string.text_tag)));
        tvTag1.setText(String.format("%s%s", tag1Type, getString(R.string.text_tag)));
        etTag0.setHint(getString(R.string.text_input_barcode, tag0Type));
        etTag1.setHint(getString(R.string.text_input_barcode, tag1Type));

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeData();
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
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

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FuncRecyclerAdapter(listData);
        recyclerView.setAdapter(adapter);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //View headerView = LayoutInflater.from(this).inflate(R.layout.item_func3_header, null);
        //adapter.addHeaderView(headerView);
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
                    removeData();
                    return true;
                }
                return false;
            }
        });


        if (!sharePrefPackCoe.isEmpty()) {
            etTag0.setText(sharePrefPackCoe);
            tvCurrentTag0.setText(sharePrefPackCoe);
            postFoucus(etTag1);
        }

        setList();

        return view;
    }

    private void acquireData() {
        String packCode = etTag0.getText().toString().trim();
        if (packCode.isEmpty()) {
            String toastStr = getString(R.string.toast_func_input_code1, tag0Type);
            Toast.makeText(getContext(), toastStr, Toast.LENGTH_LONG).show();
            postFoucus(etTag0);
            return;
        }
        // TODO: 7/15/2018 获得数据
        clearList(false);
        etTag0.setText(packCode);
        tvCurrentTag0.setText(packCode);
        listdataJson = "[{\"code\":\"1223\",\"num\":\"123\"},{\"code\":\"12b23\",\"num\":\"1c23\"}]";
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

    private void addData() {
        String packCode = etTag0.getText().toString().trim();
        String mCode = etTag1.getText().toString().trim();

        if (packCode.isEmpty() || mCode.isEmpty()) {
            String toastStr = getString(R.string.toast_func_input_code2, tag0Type, tag1Type);
            Toast.makeText(getContext(), toastStr, Toast.LENGTH_LONG).show();
            if (mCode.isEmpty()) postFoucus(etTag1);
            else if (packCode.isEmpty()) postFoucus(etTag0);
            return;
        }

        String currentCode = tvCurrentTag0.getText().toString().trim();
        if (!currentCode.equals(packCode) && !currentCode.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.toast_list_diffenrcetag, tag0Type), Toast.LENGTH_LONG).show();
            return;
        } else tvCurrentTag0.setText(packCode);

        Map<String, Object> map = new HashMap<>();
        map.put(KEY_MAP_CODE, mCode);
        map.put(KEY_MAP_NUM, mCode.length() > 3 ? mCode.substring(0, 3) : "");
        listData.add(map);
        adapter.notifyItemInserted(listData.size() - 1);

        Toast.makeText(getContext(), String.format(getString(R.string.toast_add_success), tag1Type), Toast.LENGTH_SHORT).show();
        recyclerView.scrollToPosition(listData.size() - 1);

    }

    private void removeData() {


        String packCode = etTag0.getText().toString().trim();
        String mCode = etTag1.getText().toString().trim();

        if (packCode.isEmpty() || mCode.isEmpty()) {
            String toastStr = getString(R.string.toast_func_input_code2, tag0Type, tag1Type);
            Toast.makeText(getContext(), toastStr, Toast.LENGTH_LONG).show();
            if (mCode.isEmpty()) postFoucus(etTag1);
            else if (packCode.isEmpty()) postFoucus(etTag0);
            return;
        }

        String currentCode = tvCurrentTag0.getText().toString().trim();
        if (!currentCode.equals(packCode) && !currentCode.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.toast_list_diffenrcetag, tag0Type), Toast.LENGTH_LONG).show();
            return;
        } else tvCurrentTag0.setText(packCode);

        boolean checkedFlag = false;
        for (int index = 0; index < listData.size(); index++) {
            Map<String, Object> map = listData.get(index);
            if (map.get(KEY_MAP_CODE).equals(mCode)) {
                listData.remove(index);
                adapter.notifyItemRemoved(index);
                checkedFlag = true;
                break;
                //recyclerView.scrollToPosition(listData.size() - 1);
                //tvCount.setText(String.valueOf(listData.size()));
            }
        }


        if (!checkedFlag)
            Toast.makeText(getContext(), getString(R.string.toast_no_record), Toast.LENGTH_LONG).show();
        else Toast.makeText(getContext(), R.string.toast_remove_success, Toast.LENGTH_SHORT).show();

        etTag1.setText("");
        postFoucus(etTag1);
    }

    public void clearList(boolean isToFocus) {
        listData.clear();
        if (adapter != null) adapter.notifyDataSetChanged();
        if (tvCount != null) tvCount.setText(String.valueOf(listData.size()));
        if (tvCurrentTag0 != null) tvCurrentTag0.setText("");
        if (etTag1 != null) etTag1.setText("");
        if (etTag0 != null) etTag0.setText("");
        if (isToFocus) postFoucus(etTag0);
    }

    public String getListdataJson() {
        listdataJson = gson.toJson(listData);
        return listdataJson;
    }

    public String getCurrentTag0Str() {
        if (tvCurrentTag0 != null)
            return tvCurrentTag0.getText().toString().trim();
        else
            return "";
    }

    private void submitData() {
        if (listData.isEmpty()) {
            Toast.makeText(getContext(), R.string.toast_list_empty, Toast.LENGTH_LONG).show();
            return;
        }
        getListdataJson();
    }


}
