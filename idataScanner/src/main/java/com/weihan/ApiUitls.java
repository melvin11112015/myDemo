package com.weihan;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weihan.bean.GeneralResult;
import com.weihan.bean.MaterialValue;
import com.weihan.bean.PacakgeScanRec;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiUitls {

    public static void addTag(String packTagJson) throws Exception {
        OkHttpClient client = new OkHttpClient();
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
        Request request = new Request.Builder()
                .url("http://139.159.253.196:7132/FTK_Demo/OData/Company('FTK')/PacakgeScanRec?$format=json")
                .header("Authorization", "Basic VGltOlF3ZXJ0ITIzNDU=")
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(MEDIA_TYPE_JSON, packTagJson))
                .build();

        Response response = client.newCall(request).execute();//同步
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response + "\n body" + response.body().string());
        System.out.println(response.body().string());
    }

    public static void addTag2(String packTagJson) throws Exception {
        OkHttpClient client = new OkHttpClient();
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
        Request request = new Request.Builder()
                .url("http://139.159.253.196:7132/FTK_Demo/OData/Company('FTK')/PackageScanReceConfirm?$format=json")
                .header("Authorization", "Basic VGltOlF3ZXJ0ITIzNDU=")
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(MEDIA_TYPE_JSON, packTagJson))
                .build();

        Response response = client.newCall(request).execute();//同步
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response + "\n body" + response.body().string());
        System.out.println(response.body().string());
    }

    public static GeneralResult<MaterialValue> checkMaterial(String materialtag) throws Exception {
        OkHttpClient client = new OkHttpClient();
        String url = "http://139.159.253.196:7132/FTK_Demo/OData/Company('FTK')/BinContent?$format=json&$filter=Item_No eq '" + materialtag + "'";
        System.out.println(url);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Basic VGltOlF3ZXJ0ITIzNDU=")
                .build();

        Response response = client.newCall(request).execute();//同步
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response + "\n body" + response.body().string());
        String responseJson = response.body().string();
        System.out.println(responseJson);
        Gson gson = new Gson();
        return gson.fromJson(responseJson, new TypeToken<GeneralResult<MaterialValue>>() {
        }.getType());
    }

    public static GeneralResult<MaterialValue> checkWarehouse(String warehousetag) throws Exception {
        OkHttpClient client = new OkHttpClient();
        String url = "http://139.159.253.196:7132/FTK_Demo/OData/Company('FTK')/BinContent?$format=json&$filter=Bin_Code eq '" + warehousetag + "'";
        System.out.println(url);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Basic VGltOlF3ZXJ0ITIzNDU=")
                .build();

        Response response = client.newCall(request).execute();//同步
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response + "\n body" + response.body().string());
        String responseJson = response.body().string();
        System.out.println(responseJson);
        Gson gson = new Gson();
        return gson.fromJson(responseJson, new TypeToken<GeneralResult<MaterialValue>>() {
        }.getType());
    }

    public static GeneralResult<PacakgeScanRec> checkPack(String packCode, String packType, String littleType) throws Exception {
        OkHttpClient client = new OkHttpClient();
        String url = "http://139.159.253.196:7132/FTK_Demo/OData/Company('FTK')/PacakgeScanRec?$format=json&$filter=BigBarcode eq '" +
                packCode +
                "'  and BigType eq '" +
                packType +
                "' and LittleType eq '" +
                littleType + "'";
        System.out.println(url);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Basic VGltOlF3ZXJ0ITIzNDU=")
                .build();

        Response response = client.newCall(request).execute();//同步
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response + "\n body" + response.body().string());
        String responseJson = response.body().string();
        System.out.println(responseJson);
        Gson gson = new Gson();
        return gson.fromJson(responseJson, new TypeToken<GeneralResult<PacakgeScanRec>>() {
        }.getType());
    }
}
