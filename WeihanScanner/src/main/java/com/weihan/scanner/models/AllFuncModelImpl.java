package com.weihan.scanner.models;

import android.annotation.SuppressLint;

import com.common.utils.ToastUtils;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.net.BaseOdataCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AllFuncModelImpl {

    private int taskCount;

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDatetime() {
        Date date = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        return sdf1.format(date) + "T" + sdf2.format(date);
    }

    public <T, K> void processList(List<Polymorph<T, K>> datas, PolyChangeListener<T, K> listener) {
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

    private StringBuilder stringBuilder = new StringBuilder();

    public static boolean checkEmptyList(List datas) {
        if (datas.isEmpty()) {
            ToastUtils.show("没有提交数据");
            return false;
        }
        ToastUtils.show("提交中");
        return true;
    }

    public void buildingResultMsg(boolean isFinished, String msg) {
        if (msg != null) stringBuilder.append('\n').append("错误:").append(msg);
        if (isFinished) {
            ToastUtils.showToastLong("提交完成" + stringBuilder.toString());
            stringBuilder = new StringBuilder();
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