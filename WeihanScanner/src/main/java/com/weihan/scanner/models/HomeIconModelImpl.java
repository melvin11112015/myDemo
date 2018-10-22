package com.weihan.scanner.models;

import com.weihan.scanner.BaseMVP.IBaseModel;
import com.weihan.scanner.R;
import com.weihan.scanner.activities.Func0Activity;
import com.weihan.scanner.activities.Func10Activity;
import com.weihan.scanner.activities.Func11Activity;
import com.weihan.scanner.activities.Func12Activity;
import com.weihan.scanner.activities.Func13Activity;
import com.weihan.scanner.activities.Func1Activity;
import com.weihan.scanner.activities.Func3Activity;
import com.weihan.scanner.activities.Func4Activity;
import com.weihan.scanner.activities.Func6Activity;
import com.weihan.scanner.activities.Func7Activity;
import com.weihan.scanner.activities.Func8Activity;
import com.weihan.scanner.activities.Func9Activity;
import com.weihan.scanner.activities.SettingsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.weihan.scanner.Constant.KEY_CODE;
import static com.weihan.scanner.Constant.KEY_IMAGE_ID;
import static com.weihan.scanner.Constant.KEY_TITLE;

public class HomeIconModelImpl implements IBaseModel {

    private final List<Map<String, Object>> data = new ArrayList<>();

    public void generateDataList(Callback callback) {

        data.clear();
        Map<String, Object> map;

        map = new HashMap<>();
        map.put(KEY_IMAGE_ID, R.drawable.icon_card);
        map.put(KEY_TITLE, "采购收货");
        map.put(KEY_CODE, 0);
        data.add(map);

        map = new HashMap<>();
        map.put(KEY_IMAGE_ID, R.drawable.icon_parking);
        map.put(KEY_TITLE, "生产领料");
        map.put(KEY_CODE, 1);
        data.add(map);

        map = new HashMap<>();
        map.put(KEY_IMAGE_ID, R.drawable.icon_note);
        map.put(KEY_TITLE, "盘点");
        map.put(KEY_CODE, 2);
        data.add(map);


        map = new HashMap<>();
        map.put(KEY_IMAGE_ID, R.drawable.icon_man);
        map.put(KEY_TITLE, "收货上架");
        map.put(KEY_CODE, 3);
        data.add(map);


        map = new HashMap<>();
        map.put(KEY_IMAGE_ID, R.drawable.icon_paper);
        map.put(KEY_TITLE, "领料确认");
        map.put(KEY_CODE, 4);
        data.add(map);

        map = new HashMap<>();
        map.put(KEY_IMAGE_ID, R.drawable.icon_set);
        map.put(KEY_TITLE, "复盘");
        map.put(KEY_CODE, 5);
        data.add(map);

        map = new HashMap<>();
        map.put(KEY_IMAGE_ID, R.drawable.icon_staff);
        map.put(KEY_TITLE, "产出入库");
        map.put(KEY_CODE, 6);
        data.add(map);

        map = new HashMap<>();
        map.put(KEY_IMAGE_ID, R.drawable.icon_pack);
        map.put(KEY_TITLE, "仓库发货");
        map.put(KEY_CODE, 7);
        data.add(map);

        map = new HashMap<>();
        map.put(KEY_IMAGE_ID, R.drawable.icon_card);
        map.put(KEY_TITLE, "单个移库");
        map.put(KEY_CODE, 8);
        data.add(map);


        map = new HashMap<>();
        map.put(KEY_IMAGE_ID, R.drawable.icon_survey);
        map.put(KEY_TITLE, "产出上架");
        map.put(KEY_CODE, 9);
        data.add(map);

        map = new HashMap<>();
        map.put(KEY_IMAGE_ID, R.drawable.icon_tick);
        map.put(KEY_TITLE, "包装确认");
        map.put(KEY_CODE, 10);
        data.add(map);

        map = new HashMap<>();
        map.put(KEY_IMAGE_ID, R.drawable.icon_tick);
        map.put(KEY_TITLE, "SKU查询");
        map.put(KEY_CODE, 11);
        data.add(map);

        map = new HashMap<>();
        map.put(KEY_IMAGE_ID, R.drawable.icon_tick);
        map.put(KEY_TITLE, "批量下架");
        map.put(KEY_CODE, 12);
        data.add(map);

        map = new HashMap<>();
        map.put(KEY_IMAGE_ID, R.drawable.icon_tick);
        map.put(KEY_TITLE, "批量上架");
        map.put(KEY_CODE, 13);
        data.add(map);

        map = new HashMap<>();
        map.put(KEY_IMAGE_ID, R.drawable.icon_tick);
        map.put(KEY_TITLE, "系统设置");
        map.put(KEY_CODE, 14);
        data.add(map);

        callback.onListComplete(data);
    }

    public void findClass(Callback callback, int position) {
        switch ((int) data.get(position).get(KEY_CODE)) {
            case 0:
                callback.onClassFound(Func0Activity.class);
                break;
            case 1:
                callback.onClassFound(Func1Activity.class);
                break;
            case 3:
                callback.onClassFound(Func3Activity.class);
                break;
            case 4:
                callback.onClassFound(Func4Activity.class);
                break;
            case 6:
                callback.onClassFound(Func6Activity.class);
                break;
            case 7:
                callback.onClassFound(Func7Activity.class);
                break;
            case 8:
                callback.onClassFound(Func8Activity.class);
                break;
            case 9:
                callback.onClassFound(Func9Activity.class);
                break;
            case 10:
                callback.onClassFound(Func10Activity.class);
                break;
            case 11:
                callback.onClassFound(Func11Activity.class);
                break;
            case 12:
                callback.onClassFound(Func12Activity.class);
                break;
            case 13:
                callback.onClassFound(Func13Activity.class);
                break;
            case 14:
                callback.onClassFound(SettingsActivity.class);
                break;
            default:
                break;
        }
    }

    public interface Callback {
        void onListComplete(List<Map<String, Object>> data);

        void onClassFound(Class<?> clazz);
    }


}
