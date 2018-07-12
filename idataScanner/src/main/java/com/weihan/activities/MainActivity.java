package com.weihan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.weihan.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> map;

        map = new HashMap<>();
        map.put("imageId", R.drawable.icon_card);
        map.put(KEY_TITLE, "工厂-打包");
        map.put("code", 0);
        data.add(map);

        map = new HashMap<>();
        map.put("imageId", R.drawable.icon_parking);
        map.put(KEY_TITLE, "工厂-成品上架");
        map.put("code", 1);
        data.add(map);

        map = new HashMap<>();
        map.put("imageId", R.drawable.icon_note);
        map.put(KEY_TITLE, "工厂-仓库发货");
        map.put("code", 2);
        data.add(map);

        map = new HashMap<>();
        map.put("imageId", R.drawable.icon_man);
        map.put(KEY_TITLE, "工厂-分拆");
        map.put("code", 3);
        data.add(map);

        map = new HashMap<>();
        map.put("imageId", R.drawable.icon_pack);
        map.put(KEY_TITLE, "工厂-出库打包");
        map.put("code", 4);
        data.add(map);

        map = new HashMap<>();
        map.put("imageId", R.drawable.icon_paper);
        map.put(KEY_TITLE, "海外-成品入库");
        map.put("code", 5);
        data.add(map);

        map = new HashMap<>();
        map.put("imageId", R.drawable.icon_set);
        map.put(KEY_TITLE, "海外-成品上架");
        map.put("code", 6);
        data.add(map);

        map = new HashMap<>();
        map.put("imageId", R.drawable.icon_staff);
        map.put(KEY_TITLE, "海外-出库");
        map.put("code", 7);
        data.add(map);

        map = new HashMap<>();
        map.put("imageId", R.drawable.icon_pack);
        map.put(KEY_TITLE, "海外-打包");
        map.put("code", 8);
        data.add(map);

        map = new HashMap<>();
        map.put("imageId", R.drawable.icon_card);
        map.put(KEY_TITLE, "海外-理货下架");
        map.put("code", 9);
        data.add(map);

        map = new HashMap<>();
        map.put("imageId", R.drawable.icon_ok);
        map.put(KEY_TITLE, "海外-理货上架");
        map.put("code", 10);
        data.add(map);

        map = new HashMap<>();
        map.put("imageId", R.drawable.icon_survey);
        map.put(KEY_TITLE, "海外-盘点");
        map.put("code", 11);
        data.add(map);

        map = new HashMap<>();
        map.put("imageId", R.drawable.icon_tick);
        map.put(KEY_TITLE, "货位查询");
        map.put("code", 12);
        data.add(map);

        GridView gridView = findViewById(R.id.gridview_main);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.item_grid, new String[]{"imageId", KEY_TITLE}, new int[]{R.id.iv_grid, R.id.tv_grid});
        gridView.setAdapter(simpleAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                switch ((int) data.get(i).get("code")) {
                    case 0:
                        intent.setClass(MainActivity.this, Func0Activity.class);
                        break;
                    case 1:
                        intent.setClass(MainActivity.this, Func0Activity.class);
                        break;
                    case 2:
                        intent.setClass(MainActivity.this, Func0Activity.class);
                        break;
                    case 3:
                        intent.setClass(MainActivity.this, Func0Activity.class);
                        break;
                    case 4:
                        intent.setClass(MainActivity.this, Func0Activity.class);
                        break;
                    case 5:
                        intent.setClass(MainActivity.this, Func0Activity.class);
                        break;
                    case 6:
                        intent.setClass(MainActivity.this, Func0Activity.class);
                        break;
                    case 7:
                        intent.setClass(MainActivity.this, Func0Activity.class);
                        break;
                    case 8:
                        intent.setClass(MainActivity.this, Func0Activity.class);
                        break;
                    case 9:
                        intent.setClass(MainActivity.this, Func0Activity.class);
                        break;
                    case 10:
                        intent.setClass(MainActivity.this, Func0Activity.class);
                        break;
                    case 11:
                        intent.setClass(MainActivity.this, Func0Activity.class);
                        break;
                    case 12:
                        intent.setClass(MainActivity.this, Func0Activity.class);
                        break;
                    default:
                        intent = null;
                }
                if (intent != null) {
                    intent.putExtra(KEY_TITLE, (String) data.get(i).get(KEY_TITLE));
                    startActivity(intent);
                }
            }
        });
    }


}
