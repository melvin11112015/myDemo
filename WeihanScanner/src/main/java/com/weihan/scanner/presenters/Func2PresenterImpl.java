package com.weihan.scanner.presenters;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.PhysicalInvtAddon;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func2ModelImpl;
import com.weihan.scanner.mvpviews.Func6MvpView;
import com.weihan.scanner.net.ApiTool;

import java.util.List;

public class Func2PresenterImpl extends BasePresenter<Func6MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();


    private AllFuncModelImpl.PolyChangeListener<PhysicalInvtAddon, PhysicalInvtAddon> listener
            = new AllFuncModelImpl.PolyChangeListener<PhysicalInvtAddon, PhysicalInvtAddon>() {

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            getView().notifyAdapter();
            allFuncModel.buildingResultMsg(isFinished, msg);
        }

        @Override
        public void goCommitting(Polymorph<PhysicalInvtAddon, PhysicalInvtAddon> poly) {
            ApiTool.addPhysicalInvtBuffer(poly.getAddonEntity(), allFuncModel.new AllFuncOdataCallback(poly, listener));
        }

    };

    public void attemptToAddPoly(List<Polymorph<PhysicalInvtAddon, PhysicalInvtAddon>> datas, String bincode, String itemno) {

        for (Polymorph<PhysicalInvtAddon, PhysicalInvtAddon> polymorph : datas)
            if (polymorph.getAddonEntity().getBinCode().equals(bincode) && polymorph.getAddonEntity().getItemNo().equals(itemno))
                return;

        datas.add(Func2ModelImpl.createPoly(bincode, itemno));

        getView().notifyAdapter();
    }

    public void submitDatas(List<Polymorph<PhysicalInvtAddon, PhysicalInvtAddon>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }

    public static class PhysicalInvtAdapter extends BaseQuickAdapter<Polymorph<PhysicalInvtAddon, PhysicalInvtAddon>, BaseViewHolder> {

        public PhysicalInvtAdapter(@Nullable List<Polymorph<PhysicalInvtAddon, PhysicalInvtAddon>> datas) {
            super(R.layout.item_func2, datas);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<PhysicalInvtAddon, PhysicalInvtAddon> item) {
            helper.setText(R.id.tv_item_func2_location, item.getAddonEntity().getBinCode());
            helper.setText(R.id.tv_item_func2_bin, item.getAddonEntity().getBinCode());
            helper.setText(R.id.tv_item_func2_itemno, item.getAddonEntity().getItemNo());
            helper.setText(R.id.et_item_func2_count1, item.getAddonEntity().getQuantity());
            EditText et = helper.getView(R.id.et_item_func2_count1);

            final Polymorph<PhysicalInvtAddon, PhysicalInvtAddon> polymorphItem = item;

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

            helper.addOnClickListener(R.id.tv_item_func2_delete);
            switch (item.getState()) {
                case FAILURE:
                    helper.setBackgroundColor(R.id.view_item_func2_state, Color.RED);
                    helper.setTextColor(R.id.tv_item_func2_state, Color.RED);
                    helper.setText(R.id.tv_item_func2_state, R.string.text_commit_fail);
                    break;
                case COMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func2_state, Color.GREEN);
                    helper.setTextColor(R.id.tv_item_func2_state, Color.GREEN);
                    helper.setText(R.id.tv_item_func2_state, R.string.text_committed);
                    break;
                case UNCOMMITTED:
                    helper.setBackgroundColor(R.id.view_item_func2_state, Color.argb(0Xff, 0xff, 0x90, 0x40));
                    helper.setTextColor(R.id.tv_item_func2_state, Color.WHITE);
                    helper.setText(R.id.tv_item_func2_state, "");
                    break;
            }
        }
    }

}
