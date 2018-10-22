package com.weihan.scanner.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.weihan.scanner.BaseMVP.BaseActivity;
import com.weihan.scanner.R;
import com.weihan.scanner.mvpviews.SettingsMvpView;
import com.weihan.scanner.presenters.SettingsPresenterImpl;

import static com.weihan.scanner.Constant.KEY_SPREF_SETTINGS;
import static com.weihan.scanner.Constant.SHAREDPREF_NAME;

public class SettingsActivity extends BaseActivity<SettingsPresenterImpl> implements SettingsMvpView {

    SharedPreferences sharedPreferences;

    private Button button;
    private EditText etserverIp, etmachineCode, ettempArea, etmoCodeLength, etwarehouseCodeLength, etbatchCodeLength, etpoCodeLength, etbatchCodePrefixLength, etmaterialCodeLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findView();
        sharedPreferences = getSharedPreferences(SHAREDPREF_NAME, MODE_PRIVATE);
        presenter.initSettings(sharedPreferences.getString(KEY_SPREF_SETTINGS, ""));
    }

    @Override
    protected SettingsPresenterImpl buildPresenter() {
        return new SettingsPresenterImpl();
    }

    @Override
    public void showSettingsParam(String serverIp, String machineCode, String tempArea, String moCodeLength, String warehouseCodeLength, String batchCodeLength, String poCodeLength, String batchCodePrefixLength, String materialCodeLength) {
        etserverIp.setText(serverIp);
        etmachineCode.setText(machineCode);
        ettempArea.setText(tempArea);
        etmoCodeLength.setText(moCodeLength);
        etwarehouseCodeLength.setText(warehouseCodeLength);
        etbatchCodeLength.setText(batchCodeLength);
        etpoCodeLength.setText(poCodeLength);
        etbatchCodePrefixLength.setText(batchCodePrefixLength);
        etmaterialCodeLength.setText(materialCodeLength);
    }

    @Override
    public void showMessage(String msg) {

    }

    @Override
    public void showErrorMessage(int id) {
        Toast.makeText(this, getString(id) + "格式错误", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveToSharedPref(String settingsJson) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SPREF_SETTINGS, settingsJson);
        editor.apply();
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void findView() {
        etserverIp = findViewById(R.id.et_setting_serverip);
        etmachineCode = findViewById(R.id.et_setting_machineCode);
        ettempArea = findViewById(R.id.et_setting_tempArea);
        etmoCodeLength = findViewById(R.id.et_setting_moCodeLength);
        etwarehouseCodeLength = findViewById(R.id.et_setting_warehouseCodeLength);
        etbatchCodeLength = findViewById(R.id.et_setting_batchCodeLength);
        etpoCodeLength = findViewById(R.id.et_setting_poCodeLength);
        etbatchCodePrefixLength = findViewById(R.id.et_setting_batchCodePrefixLength);
        etmaterialCodeLength = findViewById(R.id.et_setting_materialCodeLength);
        button = findViewById(R.id.button_setting_save);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.goSaving(etserverIp.getText().toString(),
                        etmachineCode.getText().toString(),
                        ettempArea.getText().toString(),
                        etmoCodeLength.getText().toString(),
                        etwarehouseCodeLength.getText().toString(),
                        etbatchCodeLength.getText().toString(),
                        etpoCodeLength.getText().toString(),
                        etbatchCodePrefixLength.getText().toString(),
                        etmaterialCodeLength.getText().toString());
            }
        });
    }
}