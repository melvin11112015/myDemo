package com.weihan.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.weihan.R;
import com.weihan.adapters.MyFragmentPagerAdapter;
import com.weihan.fragments.Func0Fragment;

import java.util.ArrayList;
import java.util.List;

public class Func0Activity extends BaseActivity {

    public static final String KEY_SHAREPREF_FUNC0_PACK = "KEY_SHAREPREF_FUNC0_PACK_";
    public static final String KEY_SHAREPREF_FUNC0_LIST = "KEY_SHAREPREF_FUNC0_LIST_";

    TabLayout tabLayout;
    ViewPager viewPager;
    MyFragmentPagerAdapter pagerAdapter;
    private List<String> titleList = new ArrayList<>();// 标题集合
    private List<Fragment> fragmentList = new ArrayList<>();// 碎片集合

    private void findView() {
        tabLayout = findViewById(R.id.tablayout_func0);
        viewPager = findViewById(R.id.viewPager_func0);
    }

    private void initFragmentTab() {
        titleList.clear();
        titleList.add(String.format("%s%s", getString(R.string.text_small), getString(R.string.text_pack)));
        titleList.add(String.format("%s%s", getString(R.string.text_big), getString(R.string.text_pack)));

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
                default:
                    tag1 = "";
                    break;
            }
            fragmentList.add(Func0Fragment.newInstance(titleList.get(index), tag1, index, tempPackCode, listdataJson));
        }

        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.setCurrentItem(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func0);


        findView();
        initFragmentTab();


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


    private void clearList() {
        (new AlertDialog.Builder(this))
                .setMessage(R.string.text_dialog_clear)
                .setPositiveButton(R.string.text_button_clear, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        for (int index = 0; index < fragmentList.size(); index++) {
                            editor.remove(KEY_SHAREPREF_FUNC0_PACK + index);
                            editor.remove(KEY_SHAREPREF_FUNC0_LIST + index);
                            ((Func0Fragment) fragmentList.get(index)).clearList(viewPager.getCurrentItem() == index);
                        }
                        editor.apply();


                    }
                })
                .setNegativeButton(R.string.text_button_cancel, null)
                .show();
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


}
