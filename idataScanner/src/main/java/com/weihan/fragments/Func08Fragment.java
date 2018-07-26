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
import com.weihan.ApiUitls;
import com.weihan.R;
import com.weihan.adapters.FuncRecyclerAdapter;
import com.weihan.bean.PackTag;
import com.weihan.interfaces.FragmentClearInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.common.Utils.ViewHelper.postFoucus;
import static com.weihan.adapters.FuncRecyclerAdapter.KEY_MAP_CODE;
import static com.weihan.adapters.FuncRecyclerAdapter.KEY_MAP_NUM;
import static com.weihan.adapters.FuncRecyclerAdapter.KEY_MAP_STATUS;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Func08Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Func08Fragment extends Fragment implements FragmentClearInterface {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TAG0_TYPE = "tag0_type";
    private static final String ARG_TAG1_TYPE = "tag1_type";
    private static final String ARG_TYPE_CODE = "type_code";
    private static final String ARG_TAG0_CURRENT = "tag0_current";
    private static final String ARG_LIST_JSON = "list_json";

    String tag0Type, tag1Type, listdataJson, sharePrefPackCoe;
    int typeCode;
    int failedCount = 0;

    List<Map<String, Object>> listData = new ArrayList<>();
    Gson gson = new Gson();

    EditText etTag0, etTag1;
    TextView tvCount, tvTag0, tvTag1, tvCurrentTag0;
    RecyclerView recyclerView;
    Button buttonAdd, buttonSubmit;

    FuncRecyclerAdapter adapter;


    public Func08Fragment() {
        // Required empty public constructor
    }


    public static Func08Fragment newInstance(String tag0Type, String tag1Type, int typeCode, String currentTag0, String listdataJson) {
        Func08Fragment fragment = new Func08Fragment();
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
        View view = inflater.inflate(R.layout.fragment_func08, container, false);
        etTag0 = view.findViewById(R.id.et_func0_tag0);
        etTag1 = view.findViewById(R.id.et_func0_tag1);
        tvTag0 = view.findViewById(R.id.tv_func0_tag0);
        tvTag1 = view.findViewById(R.id.tv_func0_tag1);
        tvCount = view.findViewById(R.id.tv_func0_count);
        tvCurrentTag0 = view.findViewById(R.id.tv_func0_currentTag0);
        recyclerView = view.findViewById(R.id.recycler_func0);
        buttonAdd = view.findViewById(R.id.button_func0_add);
        buttonSubmit = view.findViewById(R.id.button_func0_submit);


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
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FuncRecyclerAdapter(listData);
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


        if (!sharePrefPackCoe.isEmpty()) {
            etTag0.setText(sharePrefPackCoe);
            tvCurrentTag0.setText(sharePrefPackCoe);
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

        String toastStr = getString(R.string.toast_func_input_code2, tag0Type, tag1Type);
        if (packCode.isEmpty() || mCode.isEmpty()) {
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

        switch (tag0Type) {
            case "托盘":
                if (!packCode.contains("PBL") || !mCode.contains("BLB")) {
                    Toast.makeText(getContext(), toastStr, Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            case "大包装":
                if (!packCode.contains("BLB") || !mCode.contains("LLB")) {
                    Toast.makeText(getContext(), toastStr, Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            case "小包装":
                if (!packCode.contains("LLB")) {
                    Toast.makeText(getContext(), toastStr, Toast.LENGTH_LONG).show();
                    return;
                }
                break;
        }

        Map<String, Object> map;

        map = new HashMap<>();
        map.put(KEY_MAP_CODE, mCode);
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

        (new Thread() {
            @Override
            public void run() {
                failedCount = 0;
                for (Map<String, Object> map : listData) {
                    String code = (String) map.get(KEY_MAP_CODE);
                    String packtagJson = new PackTag(code, getCurrentTag0Str(), tag1Type, tag0Type).toString();
                    System.out.println(packtagJson);
                    try {
                        //ApiUitls.getList();
                        ApiUitls.addTag(packtagJson);
                        map.put(KEY_MAP_STATUS, getString(R.string.text_status_success));
                    } catch (Exception e) {
                        e.printStackTrace();
                        failedCount++;
                        map.put(KEY_MAP_STATUS, getString(R.string.text_status_failure));
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), String.format(getString(R.string.toast_submit_result), listData.size(), failedCount), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();


    }

}
