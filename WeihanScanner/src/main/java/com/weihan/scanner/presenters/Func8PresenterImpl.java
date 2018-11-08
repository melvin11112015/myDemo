package com.weihan.scanner.presenters;

import com.common.utils.ToastUtils;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.WarehouseTransferSingleAddon;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func8ModelImpl;
import com.weihan.scanner.mvpviews.Func8MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;
import com.weihan.scanner.utils.TextUtils;

import java.util.List;

public class Func8PresenterImpl extends BasePresenter<Func8MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();

    private GenericOdataCallback<BinContentInfo> callback1 = new GenericOdataCallback<BinContentInfo>() {
        @Override
        public void onDataAvailable(List<BinContentInfo> datas) {
            if (datas.isEmpty()) {
                ToastUtils.show(R.string.toast_no_record);
                return;
            }
            getView().fillRecycler(Func8ModelImpl.createPolymorphList(datas));
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };

    private AllFuncModelImpl.PolyChangeListener<WarehouseTransferSingleAddon, BinContentInfo> listener
            = new AllFuncModelImpl.PolyChangeListener<WarehouseTransferSingleAddon, BinContentInfo>() {

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            allFuncModel.onAllCommitted(isFinished, msg);
            getView().notifyAdapter();
        }

        @Override
        public void goCommitting(Polymorph<WarehouseTransferSingleAddon, BinContentInfo> poly) {
            WarehouseTransferSingleAddon addon = poly.getAddonEntity();
            addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setSubmitDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setLineNo(AllFuncModelImpl.getTempInt());
            ApiTool.addWarehouseTransferSingle(addon, allFuncModel.new AllFuncOdataCallback(poly, this));
        }

    };

    public void acquireDatas(String itemNo, String WBcode) {

        if (itemNo.isEmpty() || WBcode.isEmpty()) {
            ToastUtils.showToastLong("物料条码和从仓库条码不能为空");
            return;
        }
        String bincode = AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_BIN);
        String locationCode = AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_LOCATION);

        String filter = "Item_No eq '" + itemNo + "' and Bin_Code eq '" + bincode + "' and Location_Code eq '" + locationCode + "' and Quantity_Base ne 0";

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

        WarehouseTransferSingleAddon addon = datas.get(position).getAddonEntity();
        addon.setToBinCode(fullBincode);
        addon.setQuantity(quantity);
        getView().notifyAdapter();

    }

    public void submitDatas(List<Polymorph<WarehouseTransferSingleAddon, BinContentInfo>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }

}
