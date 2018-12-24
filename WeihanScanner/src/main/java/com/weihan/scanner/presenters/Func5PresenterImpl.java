package com.weihan.scanner.presenters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.common.utils.ToastUtils;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.PhysicalInvtCheckAddon;
import com.weihan.scanner.entities.PhysicalInvtInfo;
import com.weihan.scanner.entities.Polymorph;
import com.weihan.scanner.models.AllFuncModelImpl;
import com.weihan.scanner.models.Func5ModelImpl;
import com.weihan.scanner.mvpviews.Func5MvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.net.GenericOdataCallback;
import com.weihan.scanner.utils.AdapterHelper;
import com.weihan.scanner.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Func5PresenterImpl extends BasePresenter<Func5MvpView> {

    private AllFuncModelImpl allFuncModel = new AllFuncModelImpl();

    private GenericOdataCallback<PhysicalInvtInfo> callback1 = new GenericOdataCallback<PhysicalInvtInfo>() {
        @Override
        public void onDataAvailable(List<PhysicalInvtInfo> datas) {
            if (datas.isEmpty())
                ToastUtils.show(R.string.toast_no_record);

            getView().fillRecycler(Func5ModelImpl.createPolymorphList(datas));
        }

        @Override
        public void onDataUnAvailable(String msg, int errorCode) {
            ToastUtils.showToastLong(msg);
        }
    };

    private AllFuncModelImpl.PolyChangeListener<PhysicalInvtCheckAddon, PhysicalInvtInfo> listener
            = new AllFuncModelImpl.PolyChangeListener<PhysicalInvtCheckAddon, PhysicalInvtInfo>() {

        @Override
        public void onPolyChanged(boolean isFinished, String msg) {
            allFuncModel.onAllCommitted(isFinished, msg);
            getView().notifyAdapter();
        }

        @Override
        public void goCommitting(Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo> poly) {
            PhysicalInvtCheckAddon addon = poly.getAddonEntity();
            addon.setCreationDate(AllFuncModelImpl.getCurrentDatetime());
            ApiTool.addPhysicalInvtCheckBuffer(addon, allFuncModel.new AllFuncOdataCallback(poly, this));
        }

    };

    public void acquireDatas(String itemNo, String WBcode) {

        if (WBcode.isEmpty()) {
            ToastUtils.show("库位不能为空");
            return;
        }

        String locationCode = AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_LOCATION);
        String bincode = AllFuncModelImpl.convertWBcode(WBcode, AllFuncModelImpl.TYPE_BIN);

        String filter;

        filter = "Journal_Batch_Name eq '" +
                locationCode +
                "' and Location_Code eq '" +
                locationCode +
                "' and Bin_Code eq '" +
                bincode +
                "'";

        ApiTool.callPhysicalInvtInfoList(filter, callback1);
    }

    public void submitDatas(List<Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo>> datas) {
        if (!AllFuncModelImpl.checkEmptyList(datas)) return;
        allFuncModel.processList(datas, listener);
    }

    public static class PhysicalInvtAdapter extends BaseItemDraggableAdapter<Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo>, BaseViewHolder>
            implements Filterable {

        private List<Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo>> originDatas;
        private Filter mFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence keyword) {
                FilterResults results = new FilterResults();

                if (keyword == null || keyword.length() == 0) {
                    results.values = originDatas;
                    results.count = originDatas.size();
                } else {
                    List<Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo>> resultsValueList = new ArrayList<>();

                    for (Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo> polymorph : originDatas) {
                        if (keyword.length() <= 2 && polymorph.getAddonEntity().getQuantity().contentEquals(keyword)
                                || keyword.length() > 2 && polymorph.getAddonEntity().getItemNo().contains(keyword))
                            resultsValueList.add(polymorph);
                    }

                    results.values = resultsValueList;
                    results.count = resultsValueList.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                setNewData((List<Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo>>) filterResults.values);
                notifyDataSetChanged();
            }
        };

        public PhysicalInvtAdapter(@Nullable List<Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo>> datas) {
            super(R.layout.item_func5b, datas);

            if (datas == null)
                originDatas = new ArrayList<>();
            else
                originDatas = datas;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);

            AdapterHelper.initDraggableAdapter(recyclerView, this);
            AdapterHelper.addAdapterHeaderAndItemDivider(recyclerView, this, R.layout.item_func5b_header);
        }

        @Override
        protected void convert(final BaseViewHolder helper, Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo> item) {
            EditText et = helper.getView(R.id.et_item_func5_count1);
            et.setOnFocusChangeListener(null);

            helper.setText(R.id.tv_item_func5_itemno, item.getAddonEntity().getItemNo());
            helper.setText(R.id.tv_item_func5_wbcode, item.getAddonEntity().getLocationCode() + item.getAddonEntity().getBinCode());
            helper.setText(R.id.et_item_func5_count1, item.getAddonEntity().getQuantity());

            final Polymorph<PhysicalInvtCheckAddon, PhysicalInvtInfo> polymorphItem = item;

            View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isFocused) {
                    if (!isFocused) {
                        //((EditText)view).setText( polymorphItem.getAddonEntity().getQuantity());
                        String s = ((EditText) view).getText().toString();
                        if (TextUtils.isNumeric(s) && Double.valueOf(s) <= Double.valueOf(polymorphItem.getInfoEntity().getQty_Phys_Inventory())) {
                            polymorphItem.getAddonEntity().setQuantity(s);
                        } else {
                            ((EditText) view).setText(polymorphItem.getAddonEntity().getQuantity());
                            ToastUtils.show(R.string.toast_reach_upper_limit);
                        }
                    }
                }
            };
            et.setOnFocusChangeListener(focusChangeListener);

            AllFuncModelImpl.setPolyAdapterItemStateColor(R.id.la_item_func5, item.getState(), helper, et);
        }

        @Override
        public Filter getFilter() {
            return mFilter;
        }
    }

}
