package com.weihan.scanner;

import android.app.Application;
import android.content.SharedPreferences;

import com.common.utils.ToastUtils;
import com.google.gson.Gson;
import com.weihan.scanner.entities.BarcodeSettings;
import com.weihan.scanner.entities.UserInfo;
import com.weihan.scanner.presenters.SettingsPresenterImpl;

import static com.weihan.scanner.Constant.DEFAULT_MACHINE_CODE;
import static com.weihan.scanner.Constant.DEFAULT_SERVER_IP;
import static com.weihan.scanner.Constant.DEFAULT_TEMP_AREA;
import static com.weihan.scanner.Constant.DEFAULT_WAREHOUSE_CODE_LENGTH;

public class WApp extends Application {

    public static UserInfo userInfo = new UserInfo();
    public static BarcodeSettings barcodeSettings = BarcodeSettings.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
        initBarcodeSetting();
        ToastUtils.init(getApplicationContext());
    }

    private void initBarcodeSetting() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHAREDPREF_NAME, MODE_PRIVATE);
        String settingJson = sharedPreferences.getString(Constant.KEY_SPREF_SETTINGS, "");
        if (settingJson.isEmpty()) {
            barcodeSettings.setServerip(DEFAULT_SERVER_IP);
            barcodeSettings.setMachineCode(DEFAULT_MACHINE_CODE);
            barcodeSettings.setTempArea(DEFAULT_TEMP_AREA);
            barcodeSettings.setMoCodeLength(6);
            barcodeSettings.setWarehouseCodeLength(DEFAULT_WAREHOUSE_CODE_LENGTH);
            barcodeSettings.setBatchCodeLength(22);
            barcodeSettings.setPoCodeLength(9);
            barcodeSettings.setBatchCodePrefixLength(1);
            barcodeSettings.setMaterialCodeLength(10);

        } else {
            Gson gson = new Gson();
            String[] strArrays = gson.fromJson(settingJson, String[].class);
            barcodeSettings.setServerip(strArrays[0]);
            barcodeSettings.setMachineCode(strArrays[1]);
            barcodeSettings.setTempArea(strArrays[2]);
            barcodeSettings.setMoCodeLength(Integer.valueOf(strArrays[3]));
            barcodeSettings.setWarehouseCodeLength(Integer.valueOf(strArrays[4]));
            barcodeSettings.setBatchCodeLength(Integer.valueOf(strArrays[5]));
            barcodeSettings.setPoCodeLength(Integer.valueOf(strArrays[6]));
            barcodeSettings.setBatchCodePrefixLength(Integer.valueOf(strArrays[7]));
            barcodeSettings.setMaterialCodeLength(Integer.valueOf(strArrays[8]));
        }

        SettingsPresenterImpl.applySetting(barcodeSettings);
    }


}
