package com.weihan.scanner.models;

import android.annotation.SuppressLint;

import com.common.utils.ToastUtils;
import com.weihan.scanner.WApp;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.net.BaseOdataCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AllFuncModelImpl {

    public final static int TYPE_LOCATION = 1;
    public final static int TYPE_BIN = 2;
    private static int tempLine = (int) System.currentTimeMillis();
    private int taskCount;
    private boolean canRemoveItemAfterCommitted;
    private List datas;
    private StringBuilder stringBuilder = new StringBuilder();

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDatetime() {
        Date date = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        return sdf1.format(date) + "T" + sdf2.format(date);
    }

    public static boolean checkEmptyList(List datas) {
        if (datas.isEmpty()) {
            ToastUtils.show("没有提交数据");
            return false;
        }
        ToastUtils.show("提交中");
        return true;
    }

    public static String convertWBcode(String WBcode, int type) {
        if (WBcode == null || WBcode.trim().isEmpty()) return "";
        int length = WApp.barcodeSettings.getWarehouseCodeLength();
        if (type == TYPE_LOCATION) {
            if (WBcode.trim().length() <= length)
                return WBcode;
            else
                return WBcode.trim().substring(0, length);
        } else if (type == TYPE_BIN) {
            if (WBcode.trim().length() <= length)
                return "";
            else
                return WBcode.trim().substring(length, WBcode.trim().length());
        }
        return WBcode;
    }

    public static int getTempInt() {
        return tempLine++;
    }

    public <T, K> void processList(List<Polymorph<T, K>> datas, PolyChangeListener<T, K> listener, boolean canRemoveItemAfterCommitted) {
        this.canRemoveItemAfterCommitted = canRemoveItemAfterCommitted;
        this.datas = datas;
        taskCount = datas.size();
        for (Polymorph<T, K> poly : datas) {
            if (poly.getState() != Polymorph.State.COMMITTED) {
                listener.goCommitting(poly);
            } else {
                taskCount--;
                listener.onPolyChanged(taskCount <= 0, null);
            }
        }
    }

    public <T, K> void processList(List<Polymorph<T, K>> datas, PolyChangeListener<T, K> listener) {
        processList(datas, listener, true);
    }

    public void onAllCommitted(boolean isFinished, String msg) {
        if (msg != null) stringBuilder.append('\n').append("错误:").append(msg);
        if (isFinished) {
            ToastUtils.showToastLong("提交完成" + stringBuilder.toString());
            stringBuilder = new StringBuilder();

            if (canRemoveItemAfterCommitted && datas != null && !datas.isEmpty() && datas.get(0) instanceof Polymorph) {
                Iterator<Polymorph> iterator = datas.iterator();
                while (iterator.hasNext())
                    if (iterator.next().getState() == Polymorph.State.COMMITTED)
                        iterator.remove();
            }

        }
    }

    public interface PolyChangeListener<T, K> {
        void onPolyChanged(boolean isFinished, String msg);

        void goCommitting(Polymorph<T, K> poly);
    }

    public class AllFuncOdataCallback extends BaseOdataCallback<Map<String, Object>> {

        private Polymorph poly;
        private PolyChangeListener listener;

        public AllFuncOdataCallback(Polymorph poly, PolyChangeListener listener) {
            this.poly = poly;
            this.listener = listener;
        }

        @Override
        public void onDataAvailable(Map<String, Object> datas) {
            poly.setState(Polymorph.State.COMMITTED);
            taskCount--;
            listener.onPolyChanged(taskCount <= 0, null);
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            poly.setState(Polymorph.State.FAILURE);
            taskCount--;
            listener.onPolyChanged(taskCount <= 0, msg);
        }
    }
}
