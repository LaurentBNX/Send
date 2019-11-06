package com.dentasoft.testsend;


import android.app.Notification;
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
    private EditText TimeSlot,IP,PassWord,UserName,SmsInterval;
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
        SmsInterval = v.findViewById(R.id.edit_send_sms_interval);
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


        TimeSlot.setText(Constants.TIME_SLOT+"");
        IP.setText(Constants.IP);
        UserName.setText(Constants.USERNAME);
        PassWord.setText(Constants.PASSWORD);
        SmsInterval.setText(Constants.SEND_SMS_INTERVAL+"");


        save_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick", "user setting saved");
                Boolean switchState = autoSend.isChecked();
                boolean switchToSmsServer = !preferences.getBoolean("autoSend",false) && switchState;
                boolean switchToManual = preferences.getBoolean("autoSend",false) && !switchState;
                editor.putBoolean("autoSend",switchState).commit();
                editor.putInt("Language",rg.getCheckedRadioButtonId()).commit();
                //add
                try {
                    editor.putInt("auto_sms_timeslot",Integer.parseInt(TimeSlot.getText().toString())).commit();
                    editor.putInt("manual_sms_timeslot",Integer.parseInt(SmsInterval.getText().toString())).commit();
                } catch (Exception e) {
                    Toast.makeText(getContext(),"Please fill in a number as timeslot!",Toast.LENGTH_SHORT).show();
                }

                editor.putString("ftp_ip",IP.getText().toString()).commit();
                editor.putString("ftp_username",UserName.getText().toString()).commit();
                editor.putString("ftp_password",PassWord.getText().toString()).commit();
                Constants.SEND_SMS_INTERVAL = preferences.getInt("manual_sms_timeslot",1);
                Constants.TIME_SLOT = preferences.getInt("auto_sms_timeslot", 0);
                Constants.IP = preferences.getString("ftp_ip","");
                Constants.USERNAME = preferences.getString("ftp_username","");
                Constants.PASSWORD = preferences.getString("ftp_password","");
                Constants.SEND_SMS_INTERVAL = preferences.getInt("manual_sms_timeslot",1);
               if (switchToSmsServer) {
                   Toast.makeText(getContext(),"SMS server launched!",Toast.LENGTH_LONG).show();
                   ((MainActivity)getActivity()).InitAutomaticSmsService();

               }
               if (switchToManual) {
                   Toast.makeText(getContext(),"SMS server stopped!",Toast.LENGTH_LONG).show();
                   ((MainActivity)getActivity()).stopAutomaticService();
               }
                ((MainActivity)getActivity()).NavigateToHome();
            }
        });
        return v;
    }
}
