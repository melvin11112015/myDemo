package com.weihan.scanner.presenters;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.OutputPutAwayAddon;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func9ModelImpl;
import com.weihan.scanner.mvpviews.Func9MvpView;
import com.weihan.scanner.net.ApiTool;

import java.util.List;


public class Func9PresenterImpl extends BasePresenter<Func9MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();
    private AllFuncModelImpl.PolyChangeListener<OutputPutAwayAddon, OutputPutAwayAddon> listener
            = new AllFuncModelImpl.PolyChangeListener<OutputPutAwayAddon, OutputPutAwayAddon>() {

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            allFuncModel.onAllCommitted(isFinished, msg);
            getView().notifyAdapter();
        }

        @Override
        public void goCommitting(Polymorph<OutputPutAwayAddon, OutputPutAwayAddon> poly) {
            OutputPutAwayAddon addon = poly.getAddonEntity();
            addon.setSubmitDate(AllFuncModelImpl.getCurrentDatetime());
            addon.setLineNo(AllFuncModelImpl.getTempInt());
            ApiTool.addOutputPutAwayBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, this));
        }

    };

    public void submitDatas(List<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }

    public void attemptToAddPoly(List<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>> datas, String itemno, String WBcode, String quantity) {

        String bincode = AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_BIN);
        String locationCode = AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_LOCATION);

        for (Polymorph<OutputPutAwayAddon, OutputPutAwayAddon> polymorph : datas)
            if (polymorph.getAddonEntity().getBinCode().equals(bincode) &&
                    polymorph.getAddonEntity().getLocationCode().equals(locationCode) &&
                    polymorph.getAddonEntity().getItemNo().equals(itemno))
                return;

        datas.add(Func9ModelImpl.createPoly(itemno, bincode, locationCode, quantity));

        getView().notifyAdapter();
    }

    public static class OutputPutAwayListAdapter extends BaseQuickAdapter<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>, BaseViewHolder> {

        public OutputPutAwayListAdapter(@Nullable List<Polymorph<OutputPutAwayAddon, OutputPutAwayAddon>> datas) {
            super(R.layout.item_func9, datas);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<OutputPutAwayAddon, OutputPutAwayAddon> item) {
            helper.setText(R.id.tv_item_func9_mcn, item.getAddonEntity().getItemNo());
            helper.setText(R.id.tv_item_func9_location, item.getAddonEntity().getLocationCode());
            helper.setText(R.id.tv_item_func9_bincode, item.getAddonEntity().getBinCode());
            helper.setText(R.id.et_item_func9_quantity1, item.getAddonEntity().getQuantity());
            EditText et = helper.getView(R.id.et_item_func9_quantity1);

            final Polymorph<OutputPutAwayAddon, OutputPutAwayAddon> polymorphItem = item;

            View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isFocused) {
                    if (!isFocused) {
                        String s = ((EditText) view).getText().toString();
                        polymorphItem.getAddonEntity().setQuantity(s);
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
                    et.setEnabled(true);
                    break;
                case COMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func9_state, Color.GREEN);
                    helper.setTextColor(R.id.tv_item_func9_state, Color.GREEN);
                    helper.setText(R.id.tv_item_func9_state, R.string.text_committed);
                    et.setEnabled(false);
                    break;
                case UNCOMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func9_state, Color.argb(0Xff, 0xff, 0x90, 0x40));
                    helper.setTextColor(R.id.tv_item_func9_state, Color.WHITE);
                    helper.setText(R.id.tv_item_func9_state, "");
                    et.setEnabled(true);
                    break;
            }
        }
    }

}
