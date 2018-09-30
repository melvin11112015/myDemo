package com.weihan.ligong.presenters;

import com.common.utils.ToastUtils;
import com.weihan.ligong.BaseMVP.BasePresenter;
import com.weihan.ligong.entities.BinContentInfo;
import com.weihan.ligong.entities.Polymorph;
import com.weihan.ligong.entities.WarehouseTransferSingleAddon;
import com.weihan.ligong.models.AllFuncModelImpl;
import com.weihan.ligong.models.Func8ModelImpl;
import com.weihan.ligong.mvpviews.Func8MvpView;
import com.weihan.ligong.net.ApiTool;
import com.weihan.ligong.net.GenericOdataCallback;
import com.weihan.ligong.utils.TextUtils;

import java.util.List;
import java.util.Random;

public class Func8PresenterImpl extends BasePresenter<Func8MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();

    private GenericOdataCallback<BinContentInfo> callback1 = new GenericOdataCallback<BinContentInfo>() {
        @Override
        public void onDataAvailable(List<BinContentInfo> datas) {

            getView().fillRecycler(Func8ModelImpl.createPolymorphList(datas));
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };

    private AllFuncModelImpl.PolyChangeListener<WarehouseTransferSingleAddon, BinContentInfo> listener
            = new AllFuncModelImpl.PolyChangeListener<WarehouseTransferSingleAddon, BinContentInfo>() {

        private Random random = new Random();

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            getView().notifyAdapter();
            allFuncModel.buildingResultMsg(isFinished, msg);
        }

        @Override
        public void goCommitting(Polymorph<WarehouseTransferSingleAddon, BinContentInfo> poly) {
            WarehouseTransferSingleAddon addon = poly.getAddonEntity();
            addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setSubmitDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setLineNo(Math.abs(random.nextInt()));
            ApiTool.addWarehouseTransferSingle(addon, allFuncModel.new AllFuncOdataCallback(poly, listener));
        }

    };

    public void acquireDatas(String itemNo, String binCode) {

        if (itemNo.isEmpty() || binCode.isEmpty()) {
            ToastUtils.showToastLong("物料条码和从仓库条码不能为空");
            return;
        }
        String filter = "Item_No eq '" + itemNo + "' and Bin_Code eq '" + binCode + "'";

        ApiTool.callBinContent(filter, callback1);
    }

    public void inputAddonData(int position, List<Polymorph<WarehouseTransferSingleAddon, BinContentInfo>> datas, String quantity, String fullBincode) {
        if (datas.isEmpty() || position == -1 || position >= datas.size()) {
            ToastUtils.showToastLong("未选中记录");
            return;
        }
        if (!TextUtils.isIntString(quantity)) {
            ToastUtils.show("请输入有效数量");
            return;
        }
        int realquantity = 0;

        try {
            realquantity = Integer.valueOf(datas.get(position).getInfoEntity().getQuantity_Base());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            ToastUtils.show("未知数量");
            return;
        }

        if (Integer.valueOf(quantity) > realquantity) {
            ToastUtils.show("请输入有效数量");
            return;
        }
        /*
        if(fullBincode.length()<= BarcodeSettings.getWarehouseCodeLength()){
            ToastUtils.show("仓库编码长度不正确");
            return;
        }
        */
        WarehouseTransferSingleAddon addon = datas.get(position).getAddonEntity();
        /*String locationCode = fullBincode.substring(0,BarcodeSettings.getWarehouseCodeLength());
        String bincode = fullBincode.substring(BarcodeSettings.getWarehouseCodeLength(),fullBincode.length());
        addon.setToBinCode(bincode);
        addon.setToLocationCode(locationCode);
        */
        addon.setToBinCode(fullBincode);
        addon.setQuantity(quantity);
        getView().notifyAdapter();

    }

    public void submitDatas(List<Polymorph<WarehouseTransferSingleAddon, BinContentInfo>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }

}
