package com.weihan.ligong.net;

import com.weihan.ligong.entities.GenericResult;
import com.weihan.ligong.entities.OutstandingPurchLineInfo;
import com.weihan.ligong.entities.WarehouseReceiptAddon;

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
}
