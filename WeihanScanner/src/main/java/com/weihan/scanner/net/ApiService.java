package com.weihan.scanner.net;

import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.ConsumptionPickAddon;
import com.weihan.scanner.entities.ConsumptionPickConfirmAddon;
import com.weihan.scanner.entities.GenericResult;
import com.weihan.scanner.entities.InvPickingInfo;
import com.weihan.scanner.entities.OutputPutAwayAddon;
import com.weihan.scanner.entities.OutstandingPurchLineInfo;
import com.weihan.scanner.entities.OutstandingSalesLineInfo;
import com.weihan.scanner.entities.PhysicalInvtAddon;
import com.weihan.scanner.entities.PhysicalInvtCheckAddon;
import com.weihan.scanner.entities.PhysicalInvtInfo;
import com.weihan.scanner.entities.ProdOutputAddon;
import com.weihan.scanner.entities.WarehousePutAwayAddon;
import com.weihan.scanner.entities.WarehouseReceiptAddon;
import com.weihan.scanner.entities.WarehouseShipmentAddon;
import com.weihan.scanner.entities.WarehouseTransferMultiAddon;
import com.weihan.scanner.entities.WarehouseTransferSingleAddon;
import com.weihan.scanner.entities.WhseTransferMultiInfo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiService {

    String PARAM_JSON = "?$format=json";

    @GET("OutstandingPurchLine" + PARAM_JSON)
    Call<GenericResult<OutstandingPurchLineInfo>> getPurchaseLineList(@Query("$filter") String filter);

    @POST("WarehouseReceiptBuffer" + PARAM_JSON)
    Call<Map<String, Object>> addWarehouseReceipt(@Body WarehouseReceiptAddon addon);

    @POST("WarehouseTransferSingleBuffer" + PARAM_JSON)
    Call<Map<String, Object>> addWarehouseTransferSingle(@Body WarehouseTransferSingleAddon addon);

    @POST("WhseTransferMultiFromBuffer" + PARAM_JSON)
    Call<Map<String, Object>> addWhseTransferMultiFromBuffer(@Body WarehouseTransferMultiAddon addon);

    @POST("WarehousePutAwayBuffer" + PARAM_JSON)
    Call<Map<String, Object>> addWarehousePutAwayBuffer(@Body WarehousePutAwayAddon addon);

    @GET("BinContent" + PARAM_JSON)
    Call<GenericResult<BinContentInfo>> getBinContentList(@Query("$filter") String filter);

    @GET("WhseTransferMultiFromBuffer" + PARAM_JSON)
    Call<GenericResult<WhseTransferMultiInfo>> getWhseTransferMultiList(@Query("$filter") String filter);

    @GET("InvPickingList" + PARAM_JSON)
    Call<GenericResult<InvPickingInfo>> getInvPickingList(@Query("$filter") String filter);

    @POST("ConsumptionPickBuffer" + PARAM_JSON)
    Call<Map<String, Object>> addConsumptionPickBuffer(@Body ConsumptionPickAddon addon);

    @POST("ConsumptionPickConfirm_Buffer" + PARAM_JSON)
    Call<Map<String, Object>> addConsumptionPickConfirm_Buffer(@Body ConsumptionPickConfirmAddon addon);

    @POST("ProdOutputBuffer" + PARAM_JSON)
    Call<Map<String, Object>> addProdOutputBuffer(@Body ProdOutputAddon addon);

    @POST("OutputPutAwayBuffer" + PARAM_JSON)
    Call<Map<String, Object>> addOutputPutAwayBuffer(@Body OutputPutAwayAddon addon);

    @GET("OutstandingSalesLine" + PARAM_JSON)
    Call<GenericResult<OutstandingSalesLineInfo>> getOutstandingSalesLineList(@Query("$filter") String filter);

    @POST("WarehouseShipmentBuffer" + PARAM_JSON)
    Call<Map<String, Object>> addWarehouseShipmentBuffer(@Body WarehouseShipmentAddon addon);

    @POST("WarehouseShptConfirmBuffer" + PARAM_JSON)
    Call<Map<String, Object>> addWarehouseShptConfirmBuffer(@Body WarehouseShipmentAddon addon);

    @POST("PhysicalInvtBuffer" + PARAM_JSON)
    Call<Map<String, Object>> addPhysicalInvtBuffer(@Body PhysicalInvtAddon addon);

    @POST("PhysicalInvtCheckBuffer" + PARAM_JSON)
    Call<Map<String, Object>> addPhysicalInvtCheckBuffer(@Body PhysicalInvtCheckAddon addon);

    @GET("PhysicalInvtInfo" + PARAM_JSON)
    Call<GenericResult<PhysicalInvtInfo>> getPhysicalInvtInfoList(@Query("$filter") String filter);
}
