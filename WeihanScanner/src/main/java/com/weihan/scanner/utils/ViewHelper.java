package com.weihan.scanner.utils;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class ViewHelper {

    private static final int MIN_DELAY_TIME = 700;
    private static long lastClickTime;

    /**
     * 连续点击事件检测
     *
     * @return true:点击间隔小于0.7s
     */
    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    public static void setIntOnlyInputFilterForEditText(EditText editText) {
        InputFilter[] filters = new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                if (!TextUtils.isIntString(charSequence.toString()))
                    return "";
                else
                    return null;
            }
        }};
        editText.setFilters(filters);
    }

    /**
     * Edittext延时获取焦点
     *
     * @param editText 需要焦点的EditText
     */
    public static void postFoucus(final EditText editText) {
        editText.postDelayed(new Runnable() {//给他个延迟时间
            @Override
            public void run() {
                editText.requestFocus();
            }
        }, 200);
    }

    public static void initEdittextInputState(Context context, final EditText editText) {
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        editText.postDelayed(new Runnable() {//给他个延迟时间
            @Override
            public void run() {
                editText.requestFocus();
                if (imm != null) imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        }, 200);
    }

}
