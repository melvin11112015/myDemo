package com.weihan.ligong.net;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weihan.ligong.entities.BinContentInfo;
import com.weihan.ligong.entities.GenericResult;
import com.weihan.ligong.entities.OutstandingPurchLineInfo;
import com.weihan.ligong.entities.UserLogin;
import com.weihan.ligong.entities.WarehouseReceiptAddon;
import com.weihan.ligong.entities.WarehouseTransferMultiFromAddon;
import com.weihan.ligong.entities.WarehouseTransferSingleAddon;

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

    public static void addWhseTransferMultiFromBuffer(WarehouseTransferMultiFromAddon addon, Callback<Map<String, Object>> callback) {
        getRetrofit()
                .create(ApiService.class)
                .addWhseTransferMultiFromBuffer(addon)
                .enqueue(callback);
    }
}
