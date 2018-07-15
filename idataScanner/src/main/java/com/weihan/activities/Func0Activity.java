package com.weihan.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.weihan.R;
import com.weihan.fragments.Func0Fragment;

public class Func0Activity extends BaseFuncPagerActivity {

    public static final String KEY_SHAREPREF_FUNC0_PACK = "KEY_SHAREPREF_FUNC0_PACK_";
    public static final String KEY_SHAREPREF_FUNC0_LIST = "KEY_SHAREPREF_FUNC0_LIST_";


    protected void initFragmentData() {
        titleList.clear();
        titleList.add(String.format("%s%s", getString(R.string.text_small), getString(R.string.text_pack)));
        titleList.add(String.format("%s%s", getString(R.string.text_big), getString(R.string.text_pack)));
        titleList.add(getString(R.string.text_cardboard));

        fragmentList.clear();

        for (int index = 0; index < titleList.size(); index++) {
            tabLayout.addTab(tabLayout.newTab().setText(titleList.get(index)));
            String tempPackCode = sharedPreferences.getString(KEY_SHAREPREF_FUNC0_PACK + index, "");
            String listdataJson = sharedPreferences.getString(KEY_SHAREPREF_FUNC0_LIST + index, "");
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
            fragmentList.add(Func0Fragment.newInstance(titleList.get(index), tag1, index, tempPackCode, listdataJson));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func0);

        findView();
        initSharedKeySet(KEY_SHAREPREF_FUNC0_PACK, KEY_SHAREPREF_FUNC0_LIST);
        initFragmentData();
        initViewPager();

    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int index = 0; index < fragmentList.size(); index++) {
            Func0Fragment fragment = (Func0Fragment) fragmentList.get(index);
            editor.putString(KEY_SHAREPREF_FUNC0_PACK + index, fragment.getCurrentTag0Str());
            editor.putString(KEY_SHAREPREF_FUNC0_LIST + index, fragment.getListdataJson());
        }
        editor.apply();
    }


}
