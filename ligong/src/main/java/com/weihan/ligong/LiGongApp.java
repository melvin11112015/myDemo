package com.weihan.ligong;

import android.app.Application;
import android.content.SharedPreferences;

import com.common.utils.ToastUtils;
import com.google.gson.Gson;
import com.weihan.ligong.entities.BarcodeSettings;
import com.weihan.ligong.entities.UserInfo;

import static com.weihan.ligong.Constant.DEFAULT_MACHINE_CODE;
import static com.weihan.ligong.Constant.DEFAULT_SERVER_IP;
import static com.weihan.ligong.Constant.DEFAULT_TEMP_AREA;

public class LiGongApp extends Application {

    public static UserInfo userInfo = new UserInfo();

    @Override
    public void onCreate() {
        super.onCreate();
        initBarcodeSetting();
        ToastUtils.init(getApplicationContext());
    }

    private void initBarcodeSetting() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHAREDPREF_NAME, MODE_PRIVATE);
        String settingJson = sharedPreferences.getString(Constant.KEY_SPREF_SETTINGS, "");
        if (settingJson == null || settingJson.isEmpty()) {
            BarcodeSettings.setServerip(DEFAULT_SERVER_IP);
            BarcodeSettings.setMachineCode(DEFAULT_MACHINE_CODE);
            BarcodeSettings.setTempArea(DEFAULT_TEMP_AREA);
            BarcodeSettings.setMoCodeLength(6);
            BarcodeSettings.setWarehouseCodeLength(3);
            BarcodeSettings.setBatchCodeLength(22);
            BarcodeSettings.setPoCodeLength(9);
            BarcodeSettings.setBatchCodePrefixLength(1);
            BarcodeSettings.setMaterialCodeLength(10);

        } else {
            Gson gson = new Gson();
            String[] strArrays = gson.fromJson(settingJson, String[].class);
            BarcodeSettings.setServerip(strArrays[0]);
            BarcodeSettings.setMachineCode(strArrays[1]);
            BarcodeSettings.setTempArea(strArrays[2]);
            BarcodeSettings.setMoCodeLength(Integer.valueOf(strArrays[3]));
            BarcodeSettings.setWarehouseCodeLength(Integer.valueOf(strArrays[4]));
            BarcodeSettings.setBatchCodeLength(Integer.valueOf(strArrays[5]));
            BarcodeSettings.setPoCodeLength(Integer.valueOf(strArrays[6]));
            BarcodeSettings.setBatchCodePrefixLength(Integer.valueOf(strArrays[7]));
            BarcodeSettings.setMaterialCodeLength(Integer.valueOf(strArrays[8]));
        }
    }


}
