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

    protected abstract void submitDatas();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_func, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
            case R.id.menu_clear:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.dialog_title_attention)
                        .setMessage(R.string.dialog_clear_tip)
                        .setNegativeButton(R.string.text_cancel, null)
                        .setPositiveButton(R.string.text_clear, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                clearDatas();
                            }
                        }).show();
                return true;
            case R.id.menu_submit:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.dialog_title_attention)
                        .setMessage(R.string.dialog_submit_tip)
                        .setNegativeButton(R.string.text_cancel, null)
                        .setPositiveButton(R.string.text_submit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                submitDatas();
                            }
                        }).show();
                return true;

        }

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
