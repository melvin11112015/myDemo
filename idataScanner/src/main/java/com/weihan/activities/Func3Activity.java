package com.weihan.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.weihan.R;
import com.weihan.fragments.Func3Fragment;

public class Func3Activity extends BaseFuncPagerActivity {

    public static final String KEY_SHAREPREF_FUNC3_PACK = "KEY_SHAREPREF_FUNC3_PACK_";
    public static final String KEY_SHAREPREF_FUNC3_LIST = "KEY_SHAREPREF_FUNC3_LIST_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func08);

        findView();
        initSharedKeySet(KEY_SHAREPREF_FUNC3_PACK, KEY_SHAREPREF_FUNC3_PACK);
        initFragmentData();
        initViewPager();

    }


    protected void initFragmentData() {
        titleList.clear();
        titleList.add(getString(R.string.text_cardboard));
        titleList.add(String.format("%s%s", getString(R.string.text_big), getString(R.string.text_pack)));
        titleList.add(String.format("%s%s", getString(R.string.text_small), getString(R.string.text_pack)));


        fragmentList.clear();

        for (int index = 0; index < titleList.size(); index++) {
            tabLayout.addTab(tabLayout.newTab().setText(titleList.get(index)));
            String tempPackCode = sharedPreferences.getString(KEY_SHAREPREF_FUNC3_PACK + index, "");
            String listdataJson = sharedPreferences.getString(KEY_SHAREPREF_FUNC3_LIST + index, "");
            String tag1;
            switch (index) {
                case 0:
                    tag1 = titleList.get(1);
                    break;
                case 1:
                    tag1 = String.format("%s%s", getString(R.string.text_small), getString(R.string.text_pack));
                    break;
                case 2:
                    tag1 = getString(R.string.text_material);
                    break;
                default:
                    tag1 = "";
                    break;
            }
            fragmentList.add(Func3Fragment.newInstance(titleList.get(index), tag1, index, tempPackCode, listdataJson));
        }

    }


    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int index = 0; index < fragmentList.size(); index++) {
            Func3Fragment fragment = (Func3Fragment) fragmentList.get(index);
            editor.putString(KEY_SHAREPREF_FUNC3_PACK + index, fragment.getCurrentTag0Str());
            editor.putString(KEY_SHAREPREF_FUNC3_LIST + index, fragment.getListdataJson());
        }
        editor.apply();
    }
}
