package com.weihan.ligong.utils;

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
}
