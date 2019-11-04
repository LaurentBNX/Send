package com.dentasoft.testsend;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    private EditText TimeSlot,IP,PassWord,UserName;
    private Button save_setting;
    private Switch autoSend;
    private RadioButton french_button, dutch_button, english_button;
    public SettingsFragment() {}
    public boolean pre_auto;
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Settings");
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        autoSend = (Switch)v.findViewById(R.id.auto_send_switch);
        french_button = (RadioButton) v.findViewById(R.id.French_button);
        dutch_button = (RadioButton)v.findViewById(R.id.Dutch_button);
        english_button = (RadioButton)v.findViewById(R.id.English_button);
        TimeSlot = (EditText)v.findViewById(R.id.edit_timeSlot);
        IP = (EditText) v.findViewById(R.id.edit_IP_address);
        UserName = (EditText)v.findViewById(R.id.edit_username);
        PassWord = (EditText)v.findViewById(R.id.edit_password);
        final RadioGroup rg = (RadioGroup)v.findViewById(R.id.rg_button);
        save_setting = (Button)v.findViewById(R.id.save_setting_button);

        final SharedPreferences preferences= v.getContext().getSharedPreferences("user_setting", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        pre_auto = preferences.getBoolean("autoSend",false);
        int pre_language = preferences.getInt("Language",0);
        System.out.println("previous setting:  "+ pre_auto+"  "+ pre_language);
        if (preferences.getBoolean("autoSend",false)){
            autoSend.setChecked(true);
        }
        if (preferences.getInt("Language",0)==2131230723) english_button.setChecked(true);
        if (preferences.getInt("Language",0)==2131230725) french_button.setChecked(true);
        if (preferences.getInt("Language",0)==2131230722) dutch_button.setChecked(true);

        Constants.time_slot = preferences.getString("TimeSlot", "");
        Constants.IP_edit = preferences.getString("IPAddress","");
        Constants.userName_edit = preferences.getString("UserName","");
        Constants.passWord_edit = preferences.getString("PassWord","");

        TimeSlot.setText(preferences.getString("TimeSlot",""));
        IP.setText(preferences.getString("IPAddress",""));
        UserName.setText(preferences.getString("UserName",""));
        PassWord.setText(preferences.getString("PassWord",""));


        save_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick", "user setting saved");
                Boolean sendAuto;
                Boolean switchState = autoSend.isChecked();
                editor.putBoolean("autoSend",switchState).commit();
                editor.putInt("Language",rg.getCheckedRadioButtonId()).commit();
                //add
                editor.putString("TimeSlot",TimeSlot.getText().toString()).commit();
                editor.putString("IPAddress",IP.getText().toString()).commit();
                editor.putString("UserName",UserName.getText().toString()).commit();
                editor.putString("PassWord",PassWord.getText().toString()).commit();
                Constants.time_slot = preferences.getString("TimeSlot", "");
                Constants.IP_edit = preferences.getString("IPAddress","");
                Constants.userName_edit = preferences.getString("UserName","");
                Constants.passWord_edit = preferences.getString("PassWord","");

                System.out.println(rg.getCheckedRadioButtonId());
                Boolean auto = preferences.getBoolean("autoSend",false);
                int language = preferences.getInt("Language",0);
                System.out.println(auto+"  "+language);
                System.out.println("Saved IP: "+Constants.IP_edit + "  Saved User Name:  "+Constants.userName_edit+"  Saved Password:  "+Constants.passWord_edit);
                System.out.println("Saved time slot: "+ Constants.time_slot);
            }
        });
        return v;
    }
}
