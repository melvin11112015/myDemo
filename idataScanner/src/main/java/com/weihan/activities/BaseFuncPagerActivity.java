package com.weihan.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.weihan.R;
import com.weihan.adapters.MyFragmentPagerAdapter;
import com.weihan.interfaces.FragmentClearInterface;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseFuncPagerActivity extends BaseActivity {

    protected TabLayout tabLayout;
    protected ViewPager viewPager;
    protected MyFragmentPagerAdapter pagerAdapter;
    protected List<String> titleList = new ArrayList<>();// 标题集合
    protected List<Fragment> fragmentList = new ArrayList<>();// 碎片集合

    private String[] sharedKeys;

    protected abstract void initFragmentData();

    protected void findView() {
        tabLayout = findViewById(R.id.tablayout_func0);
        viewPager = findViewById(R.id.viewPager_func0);
    }

    protected void initViewPager() {
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.setCurrentItem(0);
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

    protected void clearList() {
        (new AlertDialog.Builder(this))
                .setMessage(R.string.text_dialog_clear)
                .setPositiveButton(R.string.text_button_clear, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        for (int index = 0; index < fragmentList.size(); index++) {
                            for (String sharedKey : sharedKeys) editor.remove(sharedKey + index);
                            ((FragmentClearInterface) fragmentList.get(index)).clearList(viewPager.getCurrentItem() == index);
                        }
                        editor.apply();


                    }
                })
                .setNegativeButton(R.string.text_button_cancel, null)
                .show();
    }

    protected void initSharedKeySet(String... sharedKeys) {
        this.sharedKeys = sharedKeys;
    }
}
