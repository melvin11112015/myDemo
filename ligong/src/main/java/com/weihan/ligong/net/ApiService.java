package com.weihan.ligong.net;

import com.weihan.ligong.entities.BinContentInfo;
import com.weihan.ligong.entities.GenericResult;
import com.weihan.ligong.entities.OutstandingPurchLineInfo;
import com.weihan.ligong.entities.WarehouseReceiptAddon;
import com.weihan.ligong.entities.WarehouseTransferMultiFromAddon;
import com.weihan.ligong.entities.WarehouseTransferSingleAddon;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiService {

    String PARAM_JSON = "?$format=json";

    String PARAM_COMPANY = "&company='CRONUS%20International%20Ltd.'";

    @GET("OutstandingPurchLine" + PARAM_JSON)
    Call<GenericResult<OutstandingPurchLineInfo>> getPurchaseLineList(@Query("$filter") String filter);

    @POST("WarehouseReceiptBuffer" + PARAM_JSON)
    Call<Map<String, Object>> addWarehouseReceipt(@Body WarehouseReceiptAddon addon);

    @POST("WarehouseTransferSingleBuffer" + PARAM_JSON)
    Call<Map<String, Object>> addWarehouseTransferSingle(@Body WarehouseTransferSingleAddon addon);

    @POST("WhseTransferMultiFromBuffer" + PARAM_JSON)
    Call<Map<String, Object>> addWhseTransferMultiFromBuffer(@Body WarehouseTransferMultiFromAddon addon);

    @GET("BinContent" + PARAM_JSON)
    Call<GenericResult<BinContentInfo>> getBinContentList(@Query("$filter") String filter);
}
