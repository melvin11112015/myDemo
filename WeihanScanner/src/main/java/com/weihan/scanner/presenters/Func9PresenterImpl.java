package com.weihan.scanner.presenters;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.ToastUtils;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.OutputPutAwayAddon;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func9ModelImpl;
import com.weihan.scanner.mvpviews.Func9MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;
import com.weihan.scanner.utils.ViewHelper;

import java.util.List;
import java.util.Random;


public class Func9PresenterImpl extends BasePresenter<Func9MvpView> {

    private GenericOdataCallback<BinContentInfo> callback2 = new GenericOdataCallback<BinContentInfo>() {
        @Override
        public void onDataAvailable(List<BinContentInfo> datas) {

            List<BinContentInfo> tempList = Func9ModelImpl.filtStoreIssueItem(datas);

            if (tempList.isEmpty()) {
                getView().exitActivity();
                ToastUtils.show(R.string.toast_no_record);
            } else
                getView().fillChooseListRecycler(tempList);
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
            getView().exitActivity();
        }
    };
    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();
    private AllFuncModelImpl.PolyChangeListener<OutputPutAwayAddon, OutputPutAwayAddon> listener
            = new AllFuncModelImpl.PolyChangeListener<OutputPutAwayAddon, OutputPutAwayAddon>() {

        private Random random = new Random();

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            getView().notifyAdapter();
            allFuncModel.buildingResultMsg(isFinished, msg);
        }

        @Override
        public void goCommitting(Polymorph<OutputPutAwayAddon, OutputPutAwayAddon> poly) {
            OutputPutAwayAddon addon = poly.getAddonEntity();
            addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setSubmitDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setLineNo(Math.abs(random.nextInt()));
            ApiTool.addOutputPutAwayBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, listener));
        }

    };

    public void acquireBincontentWithStoreIssue(String itemNo) {

        if (itemNo.isEmpty()) {
            ToastUtils.showToastLong("物料条码不能为空");
            return;
        }
        String filter = "Item_No eq '" + itemNo + "'";

        ApiTool.callBinContent(filter, callback2);
    }

    public void submitDatas(List<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }

    public void attemptToAddPoly(List<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>> datas, String itemno, String bincode, String quantity) {

        for (Polymorph<OutputPutAwayAddon, OutputPutAwayAddon> polymorph : datas)
            if (polymorph.getAddonEntity().getBinCode().equals(bincode) && polymorph.getAddonEntity().getItemNo().equals(itemno))
                return;

        datas.add(Func9ModelImpl.createPoly(itemno, bincode, quantity));

        getView().notifyAdapter();
    }

    public static class OutputPutAwayListAdapter extends BaseQuickAdapter<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>, BaseViewHolder> {

        public OutputPutAwayListAdapter(@Nullable List<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>> datas) {
            super(R.layout.item_func9, datas);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<OutputPutAwayAddon, OutputPutAwayAddon> item) {
            helper.setText(R.id.tv_item_func9_mcn, item.getAddonEntity().getItemNo());
            helper.setText(R.id.tv_item_func9_bincode, item.getAddonEntity().getBinCode());
            helper.setText(R.id.et_item_func9_quantity1, item.getAddonEntity().getQuantity());
            EditText et = helper.getView(R.id.et_item_func9_quantity1);
            ViewHelper.setIntOnlyInputFilterForEditText(et);

            final Polymorph<OutputPutAwayAddon, OutputPutAwayAddon> polymorphItem = item;

            View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isFocused) {
                    if (!isFocused) {
                        //((EditText)view).setText( polymorphItem.getAddonEntity().getQuantity());
                        String s = ((EditText) view).getText().toString();
                        //if (TextUtils.isIntString(s) && Integer.valueOf(s) <= Integer.valueOf(polymorphItem.getInfoEntity().getQuantity_Base())) {
                        polymorphItem.getAddonEntity().setQuantity(s);
                       /* } else {
                            ((EditText) view).setText(polymorphItem.getAddonEntity().getQuantity());
                            ToastUtils.show(R.string.toast_reach_upper_limit);
                        }*/
                    }
                }
            };
            et.setOnFocusChangeListener(focusChangeListener);

            helper.addOnClickListener(R.id.tv_item_func9_delete);
            switch (item.getState()) {
                case FAILURE:
                    helper.setBackgroundColor(R.id.view_item_func9_state, Color.RED);
                    helper.setTextColor(R.id.tv_item_func9_state, Color.RED);
                    helper.setText(R.id.tv_item_func9_state, R.string.text_commit_fail);
                    break;
                case COMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func9_state, Color.GREEN);
                    helper.setTextColor(R.id.tv_item_func9_state, Color.GREEN);
                    helper.setText(R.id.tv_item_func9_state, R.string.text_committed);
                    break;
                case UNCOMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func9_state, Color.argb(0Xff, 0xff, 0x90, 0x40));
                    helper.setTextColor(R.id.tv_item_func9_state, Color.WHITE);
                    helper.setText(R.id.tv_item_func9_state, "");
                    break;
            }
        }
    }

}
