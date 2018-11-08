package com.weihan.scanner.net;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weihan.scanner.entities.BinContentInfo;
import com.weihan.scanner.entities.ConsumptionPickAddon;
import com.weihan.scanner.entities.GenericResult;
import com.weihan.scanner.entities.InvPickingInfo;
import com.weihan.scanner.entities.OutputPutAwayAddon;
import com.weihan.scanner.entities.OutstandingPurchLineInfo;
import com.weihan.scanner.entities.OutstandingSalesLineInfo;
import com.weihan.scanner.entities.PhysicalInvtAddon;
import com.weihan.scanner.entities.PhysicalInvtCheckAddon;
import com.weihan.scanner.entities.PhysicalInvtInfo;
import com.weihan.scanner.entities.ProdOutputAddon;
import com.weihan.scanner.entities.UserLogin;
import com.weihan.scanner.entities.WarehousePutAwayAddon;
import com.weihan.scanner.entities.WarehouseReceiptAddon;
import com.weihan.scanner.entities.WarehouseShipmentAddon;
import com.weihan.scanner.entities.WarehouseTransferMultiAddon;
import com.weihan.scanner.entities.WarehouseTransferSingleAddon;
import com.weihan.scanner.entities.WhseTransferMultiInfo;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiTool {

    public static final String DEFAULT_API_URL = "http://139.159.253.196:7158/NAV2018/OData/Company('CRONUS%20International%20Ltd.')/";
    public static final String API_URL_PARAM = "?$format=json&$filter=";
    public static final String DEFAULT_AUTHORIZATION = "Basic TW9jZXZlOk1vY2V2ZTEyMw==";
    public static String currentApiUrl = DEFAULT_API_URL;

    private ApiTool() {
    }

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(currentApiUrl)
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.BODY))
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();
                                Request request = original.newBuilder()
                                        .addHeader("Authorization", DEFAULT_AUTHORIZATION)
                                        .method(original.method(), original.body())
                                        .build();
                                return chain.proceed(request);
                            }
                        })
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    private static String getResonseJson(String url) throws Exception {
        OkHttpClient client = new OkHttpClient();
        System.out.println(url);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", DEFAULT_AUTHORIZATION)
                .build();

        Response response = client.newCall(request).execute();//同步
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response + "\n body" + response.body().string());
        String responseJson = response.body().string();
        System.out.println(responseJson);
        return responseJson;
    }

    public static boolean login(String username, String password) throws Exception {

        String url = currentApiUrl +
                "MobileTerminalUser" +
                API_URL_PARAM +
                "User_ID" +
                " eq '" + username + "' and " +
                "Password" +
                " eq '" + password + "'";

        String responseJson = getResonseJson(url);

        Gson gson = new Gson();
        GenericResult<UserLogin> loginResult = gson.fromJson(responseJson, new TypeToken<GenericResult<UserLogin>>() {
        }.getType());
        return loginResult != null &&
                loginResult.getValue() != null &&
                !loginResult.getValue().isEmpty() &&
                loginResult.getValue().get(0).getUser_ID().equals(username) &&
                loginResult.getValue().get(0).getPassword().equals(password);

    }


    public static void callPurchaseLine(String filter, Callback<GenericResult<OutstandingPurchLineInfo>> callback) {
        getRetrofit()
                .create(ApiService.class)
                .getPurchaseLineList(filter)
                .enqueue(callback);
    }

    public static void addWarehouseReceipt(WarehouseReceiptAddon addon, Callback<Map<String, Object>> callback) {
        getRetrofit()
                .create(ApiService.class)
                .addWarehouseReceipt(addon)
                .enqueue(callback);
    }


    public static void callBinContent(String filter, Callback<GenericResult<BinContentInfo>> callback) {
        getRetrofit()
                .create(ApiService.class)
                .getBinContentList(filter)
                .enqueue(callback);
    }

    public static void addWarehouseTransferSingle(WarehouseTransferSingleAddon addon, Callback<Map<String, Object>> callback) {
        getRetrofit()
                .create(ApiService.class)
                .addWarehouseTransferSingle(addon)
                .enqueue(callback);
    }

    public static void addWhseTransferMultiFromBuffer(WarehouseTransferMultiAddon addon, Callback<Map<String, Object>> callback) {
        getRetrofit()
                .create(ApiService.class)
                .addWhseTransferMultiFromBuffer(addon)
                .enqueue(callback);
    }

    public static void addWarehousePutAwayBuffer(WarehousePutAwayAddon addon, Callback<Map<String, Object>> callback) {
        getRetrofit()
                .create(ApiService.class)
                .addWarehousePutAwayBuffer(addon)
                .enqueue(callback);
    }

    public static void callWhseTransferMultiList(String filter, Callback<GenericResult<WhseTransferMultiInfo>> callback) {
        getRetrofit()
                .create(ApiService.class)
                .getWhseTransferMultiList(filter)
                .enqueue(callback);
    }

    public static void callInvPickingList(String filter, Callback<GenericResult<InvPickingInfo>> callback) {
        getRetrofit()
                .create(ApiService.class)
                .getInvPickingList(filter)
                .enqueue(callback);
    }

    public static void addConsumptionPickBuffer(ConsumptionPickAddon addon, Callback<Map<String, Object>> callback) {
        getRetrofit()
                .create(ApiService.class)
                .addConsumptionPickBuffer(addon)
                .enqueue(callback);
    }

    public static void addConsumptionPickConfirm_Buffer(ConsumptionPickAddon addon, Callback<Map<String, Object>> callback) {
        getRetrofit()
                .create(ApiService.class)
                .addConsumptionPickConfirm_Buffer(addon)
                .enqueue(callback);
    }

    public static void addProdOutputBuffer(ProdOutputAddon addon, Callback<Map<String, Object>> callback) {
        getRetrofit()
                .create(ApiService.class)
                .addProdOutputBuffer(addon)
                .enqueue(callback);
    }

    public static void addOutputPutAwayBuffer(OutputPutAwayAddon addon, Callback<Map<String, Object>> callback) {
        getRetrofit()
                .create(ApiService.class)
                .addOutputPutAwayBuffer(addon)
                .enqueue(callback);
    }

    public static void callOutstandingSalesLineList(String filter, Callback<GenericResult<OutstandingSalesLineInfo>> callback) {
        getRetrofit()
                .create(ApiService.class)
                .getOutstandingSalesLineList(filter)
                .enqueue(callback);
    }

    public static void addWarehouseShipmentBuffer(WarehouseShipmentAddon addon, Callback<Map<String, Object>> callback) {
        getRetrofit()
                .create(ApiService.class)
                .addWarehouseShipmentBuffer(addon)
                .enqueue(callback);
    }

    public static void addWarehouseShptConfirmBuffer(WarehouseShipmentAddon addon, Callback<Map<String, Object>> callback) {
        getRetrofit()
                .create(ApiService.class)
                .addWarehouseShptConfirmBuffer(addon)
                .enqueue(callback);
    }

    public static void addPhysicalInvtBuffer(PhysicalInvtAddon addon, Callback<Map<String, Object>> callback) {
        getRetrofit()
                .create(ApiService.class)
                .addPhysicalInvtBuffer(addon)
                .enqueue(callback);
    }

    public static void addPhysicalInvtCheckBuffer(PhysicalInvtCheckAddon addon, Callback<Map<String, Object>> callback) {
        getRetrofit()
                .create(ApiService.class)
                .addPhysicalInvtCheckBuffer(addon)
                .enqueue(callback);
    }

    public static void callPhysicalInvtInfoList(String filter, Callback<GenericResult<PhysicalInvtInfo>> callback) {
        getRetrofit()
                .create(ApiService.class)
                .getPhysicalInvtInfoList(filter)
                .enqueue(callback);
    }
}
