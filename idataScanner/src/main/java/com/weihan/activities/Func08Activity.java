package com.weihan.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.weihan.R;
import com.weihan.fragments.Func08Fragment;

public class Func08Activity extends BaseFuncPagerActivity {

    public static final String KEY_SHAREPREF_FUNC0_PACK = "KEY_SHAREPREF_FUNC0_PACK_";
    public static final String KEY_SHAREPREF_FUNC0_LIST = "KEY_SHAREPREF_FUNC0_LIST_";

    public static final String KEY_SHAREPREF_FUNC8_PACK = "KEY_SHAREPREF_FUNC8_PACK_";
    public static final String KEY_SHAREPREF_FUNC8_LIST = "KEY_SHAREPREF_FUNC8_LIST_";

    private int typeCode = -1;

    protected void initFragmentData() {
        titleList.clear();
        titleList.add(String.format("%s%s", getString(R.string.text_small), getString(R.string.text_pack)));
        titleList.add(String.format("%s%s", getString(R.string.text_big), getString(R.string.text_pack)));
        titleList.add(getString(R.string.text_cardboard));

        fragmentList.clear();

        for (int index = 0; index < titleList.size(); index++) {
            tabLayout.addTab(tabLayout.newTab().setText(titleList.get(index)));
            String tempPackCode = "";
            String listdataJson = "";
            if (typeCode == 0) {
                tempPackCode = sharedPreferences.getString(KEY_SHAREPREF_FUNC0_PACK + index, "");
                listdataJson = sharedPreferences.getString(KEY_SHAREPREF_FUNC0_LIST + index, "");
            } else if (typeCode == 8) {
                tempPackCode = sharedPreferences.getString(KEY_SHAREPREF_FUNC8_PACK + index, "");
                listdataJson = sharedPreferences.getString(KEY_SHAREPREF_FUNC8_LIST + index, "");
            }
            String tag1;
            switch (index) {
                case 0:
                    tag1 = getString(R.string.text_material);
                    break;
                case 1:
                    tag1 = titleList.get(0);
                    break;
                case 2:
                    tag1 = titleList.get(1);
                    break;
                default:
                    tag1 = "";
                    break;
            }
            fragmentList.add(Func08Fragment.newInstance(titleList.get(index), tag1, index + typeCode * 10, tempPackCode, listdataJson));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func08);

        findView();
        typeCode = getIntent().getIntExtra("code", -2);
        if (typeCode == 0) initSharedKeySet(KEY_SHAREPREF_FUNC0_PACK, KEY_SHAREPREF_FUNC0_LIST);
        else if (typeCode == 8)
            initSharedKeySet(KEY_SHAREPREF_FUNC8_PACK, KEY_SHAREPREF_FUNC8_LIST);
        initFragmentData();
        initViewPager();

    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int index = 0; index < fragmentList.size(); index++) {
            Func08Fragment fragment = (Func08Fragment) fragmentList.get(index);
            if (typeCode == 0) {
                editor.putString(KEY_SHAREPREF_FUNC0_PACK + index, fragment.getCurrentTag0Str());
                editor.putString(KEY_SHAREPREF_FUNC0_LIST + index, fragment.getListdataJson());
            } else if (typeCode == 8) {
                editor.putString(KEY_SHAREPREF_FUNC8_PACK + index, fragment.getCurrentTag0Str());
                editor.putString(KEY_SHAREPREF_FUNC8_LIST + index, fragment.getListdataJson());
            } else return;
        }
        editor.apply();
    }


}
