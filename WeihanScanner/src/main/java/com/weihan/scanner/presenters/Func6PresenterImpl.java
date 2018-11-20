package com.weihan.scanner.presenters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.entities.ProdOutputAddon;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func6ModelImpl;
import com.weihan.scanner.mvpviews.Func6MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.utils.AdapterHelper;

import java.util.List;

public class Func6PresenterImpl extends BasePresenter<Func6MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();
    private AllFuncModelImpl.PolyChangeListener<ProdOutputAddon, ProdOutputAddon> listener
            = new AllFuncModelImpl.PolyChangeListener<ProdOutputAddon, ProdOutputAddon>() {

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            allFuncModel.onAllCommitted(isFinished, msg);
            getView().notifyAdapter();
        }

        @Override
        public void goCommitting(Polymorph<ProdOutputAddon, ProdOutputAddon> poly) {
            ApiTool.addProdOutputBuffer(poly.getAddonEntity(), allFuncModel.new AllFuncOdataCallback(poly, this));
        }

    };

    public void attemptToAddPoly(List<Polymorph<ProdOutputAddon, ProdOutputAddon>> datas, String importCode, String itemno) {

        for (Polymorph<ProdOutputAddon, ProdOutputAddon> polymorph : datas)
            if (polymorph.getAddonEntity().getOutputNo().equals(importCode) && polymorph.getAddonEntity().getBarcode().equals(itemno))
                return;

        datas.add(Func6ModelImpl.createPoly(importCode, itemno));

        getView().notifyAdapter();
    }

    public void submitDatas(List<Polymorph<ProdOutputAddon, ProdOutputAddon>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }

    public static class ProdOutputAdapter extends BaseItemDraggableAdapter<Polymorph<ProdOutputAddon, ProdOutputAddon>, BaseViewHolder> {

        public ProdOutputAdapter(@Nullable List<Polymorph<ProdOutputAddon, ProdOutputAddon>> datas) {
            super(R.layout.item_func6b, datas);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);

            AdapterHelper.initDraggableAdapter(recyclerView, this);
            AdapterHelper.addAdapterHeaderAndItemDivider(recyclerView, this, R.layout.item_func6b_header);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<ProdOutputAddon, ProdOutputAddon> item) {
            helper.setText(R.id.tv_item_func6_mcn, item.getAddonEntity().getOutputNo());
            helper.setText(R.id.tv_item_func6_name, item.getAddonEntity().getBarcode());
            helper.setText(R.id.et_item_func6_count1, item.getAddonEntity().getQuantity());
            EditText et = helper.getView(R.id.et_item_func6_count1);

            final Polymorph<ProdOutputAddon, ProdOutputAddon> polymorphItem = item;

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

            AllFuncModelImpl.setPolyAdapterItemStateColor(R.id.la_item_func6, item.getState(), helper, et);
        }
    }

}
