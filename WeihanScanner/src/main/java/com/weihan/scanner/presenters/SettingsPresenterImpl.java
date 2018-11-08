package com.weihan.scanner.presenters;

import com.google.gson.Gson;
import com.weihan.scanner.BaseMVP.BasePresenter;
import com.weihan.scanner.R;
import com.weihan.scanner.entities.BarcodeSettings;
import com.weihan.scanner.mvpviews.SettingsMvpView;
import com.weihan.scanner.net.ApiTool;
import com.weihan.scanner.utils.TextUtils;

import static com.weihan.scanner.Constant.DEFAULT_MACHINE_CODE;
import static com.weihan.scanner.Constant.DEFAULT_SERVER_IP;
import static com.weihan.scanner.Constant.DEFAULT_TEMP_AREA;

public class SettingsPresenterImpl extends BasePresenter<SettingsMvpView> {

    private Gson gson = new Gson();

    public void goSaving(String serverIp,
                         String machineCode,
                         String tempArea,
                         String moCodeLength,
                         String warehouseCodeLength,
                         String batchCodeLength,
                         String poCodeLength,
                         String batchCodePrefixLength,
                         String materialCodeLength) {

        if (!TextUtils.isIntString(materialCodeLength))
            getView().showErrorMessage(R.string.text_materialCodeLength);
        else if (!TextUtils.isIntString(warehouseCodeLength))
            getView().showErrorMessage(R.string.text_warehouseCodeLength);
        else if (!TextUtils.isIntString(batchCodeLength))
            getView().showErrorMessage(R.string.text_batchCodeLength);
        else if (!TextUtils.isIntString(poCodeLength))
            getView().showErrorMessage(R.string.text_poCodeLength);
        else if (!TextUtils.isIntString(batchCodePrefixLength))
            getView().showErrorMessage(R.string.text_batchCodePrefixLength);
        else if (!TextUtils.isIntString(moCodeLength))
            getView().showErrorMessage(R.string.text_moCodeLength);
        else {
            String[] strArray = new String[]{serverIp,
                    machineCode,
                    tempArea,
                    moCodeLength,
                    warehouseCodeLength,
                    batchCodeLength,
                    poCodeLength,
                    batchCodePrefixLength,
                    materialCodeLength
            };

            getView().saveToSharedPref(gson.toJson(strArray));
        }
    }

    public void initSettings(String settingsJson) {
        if (settingsJson.isEmpty()) {
            getView().showSettingsParam(DEFAULT_SERVER_IP,
                    DEFAULT_MACHINE_CODE,
                    DEFAULT_TEMP_AREA,
                    "6",
                    "3",
                    "22",
                    "9",
                    "1",
                    "10");
        } else {
            String[] strArrays = gson.fromJson(settingsJson, String[].class);
            getView().showSettingsParam(strArrays[0], strArrays[1], strArrays[2], strArrays[3], strArrays[4], strArrays[5], strArrays[6], strArrays[7], strArrays[8]);
        }
    }

    public static void applySetting(BarcodeSettings settings) {
        ApiTool.currentApiUrl = settings.getServerip();
    }
}
