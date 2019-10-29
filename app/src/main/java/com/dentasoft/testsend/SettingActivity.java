package com.dentasoft.testsend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SettingActivity extends AppCompatActivity {
    private Button save_setting;
    private Switch autoSend;
    private RadioButton french_button, dutch_button, english_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        autoSend = (Switch)findViewById(R.id.language_switch);
        french_button = (RadioButton) findViewById(R.id.French_button);
        dutch_button = (RadioButton)findViewById(R.id.Dutch_button);
        english_button = (RadioButton)findViewById(R.id.English_button);
        final RadioGroup rg = (RadioGroup)findViewById(R.id.rg_button);

        save_setting = (Button)findViewById(R.id.save_setting_button);


        final SharedPreferences preferences=getSharedPreferences("user_setting",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        Boolean pre_auto = preferences.getBoolean("autoSend",false);
        int pre_language = preferences.getInt("Language",0);
        System.out.println("previous setting:  "+ pre_auto+"  "+ pre_language);
        if (preferences.getBoolean("autoSend",false)){
            autoSend.setChecked(true);
        }
        if (preferences.getInt("Language",0)==2131230723) english_button.setChecked(true);
        if (preferences.getInt("Language",0)==2131230725) french_button.setChecked(true);
        if (preferences.getInt("Language",0)==2131230722) dutch_button.setChecked(true);


        save_setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("onClick", "user setting saved");
                        Boolean sendAuto;
                        Boolean switchState = autoSend.isChecked();
                        editor.putBoolean("autoSend",switchState).commit();
                        editor.putInt("Language",rg.getCheckedRadioButtonId()).commit();
                        System.out.println(rg.getCheckedRadioButtonId());
                        Boolean auto = preferences.getBoolean("autoSend",false);
                        int language = preferences.getInt("Language",0);
                        System.out.println(auto+"  "+language);
                    }
                });
    }
}
