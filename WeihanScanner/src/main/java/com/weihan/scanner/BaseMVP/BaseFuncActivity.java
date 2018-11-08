package com.weihan.scanner.BaseMVP;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.weihan.scanner.R;


public abstract class BaseFuncActivity<T extends BasePresenter> extends BaseActivity<T> {

    protected SharedPreferences sharedPreferences;

    @Override
    protected void onStop() {
        super.onStop();
        savePref(false);
    }

    protected abstract void savePref(boolean isToClear);

    protected abstract void loadPref();

    protected abstract void clearDatas();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_func, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_clear) {
            clearDatas();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void buildDeleteDialog(final BaseQuickAdapter mAdapter, final int deleteIndex) {
        new AlertDialog
                .Builder(this)
                .setMessage(R.string.text_delete_confirmation)
                .setPositiveButton(R.string.text_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAdapter.getData().remove(deleteIndex);
                        mAdapter.notifyItemRemoved(deleteIndex);
                    }
                }).setNegativeButton(R.string.text_cancel, null)
                .setCancelable(true)
                .create()
                .show();
    }

}
