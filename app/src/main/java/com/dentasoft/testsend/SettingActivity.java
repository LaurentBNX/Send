package com.dentasoft.testsend;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toolbar;

public class SettingActivity extends Activity {
   // private Button
   // private Toolbar setting_toolbar;
    private Switch autoSend;
    private RadioButton french_button, dutch_button, english_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //setting_toolbar = (Toolbar)findViewById(R.id.toolbar_setting);
        autoSend = (Switch)findViewById(R.id.language_switch);
        french_button = (RadioButton) findViewById(R.id.French_button);
        dutch_button = (RadioButton)findViewById(R.id.Dutch_button);
        english_button = (RadioButton)findViewById(R.id.English_button);

    }
}
