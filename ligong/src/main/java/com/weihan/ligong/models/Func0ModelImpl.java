package com.weihan.ligong.models;

import android.annotation.SuppressLint;

import com.weihan.ligong.BaseMVP.IBaseModel;
import com.weihan.ligong.LiGongApp;
import com.weihan.ligong.entities.OutstandingPurchLineInfo;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.entities.WarehouseReceiptAddon;
import com.weihan.ligong.net.ApiTool;
import com.weihan.ligong.net.BaseOdataCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Func0ModelImpl implements IBaseModel {

    private int taskCount;

    public static List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>> createPolymorphList(List<OutstandingPurchLineInfo> datas) {
        List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>> polymorphs = new ArrayList<>();
        for (OutstandingPurchLineInfo info : datas) {
            WarehouseReceiptAddon addon = new WarehouseReceiptAddon();
            addon.setItemNo(info.getNo());
            addon.setPurchOrderNo(info.getDocument_No());
            addon.setTerminalID(LiGongApp.userInfo.getUserid());
            addon.setQuantity("");
            polymorphs.add(new Polymorph<>(addon, info, Polymorph.State.UNCOMMITTED));
        }
        return polymorphs;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDatetime() {
        Date date = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        return sdf1.format(date) + "T" + sdf2.format(date);
    }

    public void processList(List<Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo>> datas, PolyChangeListener listener) {
        taskCount = datas.size();
        for (Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo> poly : datas) {
            if (poly.getState() != Polymorph.State.COMMITTED) {
                WarehouseReceiptAddon addon = poly.getAddonEntity();
                addon.setCreationDate(getCurrentDatetime());
                ApiTool.addWarehouseReceipt(addon, new Func0OdataCallback(poly, listener));
            } else {
                taskCount--;
                listener.onPolyChanged(taskCount <= 0, null);
            }
        }
    }

    public interface PolyChangeListener {
        void onPolyChanged(boolean isFinished, String msg);
    }

    private class Func0OdataCallback extends BaseOdataCallback<Map<String, Object>> {

        private Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo> poly;
        private PolyChangeListener listener;

        public Func0OdataCallback(Polymorph<WarehouseReceiptAddon, OutstandingPurchLineInfo> poly, PolyChangeListener listener) {
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
