package com.dentasoft.testsend;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class MyAccountFragment extends Fragment {

    private EditText mIdentifier;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public int i = 0;
    public static int counter_sms = 0;
    ArrayList<String> sendMessage = new ArrayList<>();
    ArrayList<String> sendNumber = new ArrayList<>();
    ArrayList<String> sendTime = new ArrayList<>();

    public MyAccountFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_account,container,false);
        getActivity().setTitle("My account");
        Init(v);
        DisplaySavedValues(v);
        return v;
    }

    private void Init(View v) {
        Button save_button = v.findViewById(R.id.my_account_save_button);
        Button sms_button = v.findViewById(R.id.my_account_sms_button);
        Button quit_button = v.findViewById(R.id.my_account_quit_button);
        mPreferences = v.getContext().getSharedPreferences("user_setting", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mIdentifier = v.findViewById(R.id.my_account_edit_identifier);


        save_button.setOnClickListener(v1 -> {
            String id = mIdentifier.getText().toString();
            if (!id.equals("")) {
                mEditor.putString("user_my_account_identifier",id).commit();
            } else {
                Toast.makeText(v1.getContext(), "No identifier is entered!", Toast.LENGTH_SHORT).show();
            }
        });
        quit_button.setOnClickListener(v1 -> {
            getActivity().finish();
        });
        new Thread() {
            @Override
            public void run() {
                    //  InitFTPServerSetting(v);
                    System.out.println("Enter send SMS thread!");
                    FtpService service = new FtpService(v,Constants.IP);
                    Constants.SendFiles = service.fetchSMSToSend("/test");
                    //add
                    Constants.SendContent = new ArrayList<>();

            }
        }.start();

        while (Constants.SendFiles == null){}
        new Thread() {
            @Override
            public void run() {
                FtpService service = new FtpService(v,Constants.IP);
                Constants.SendContent = new ArrayList<>();
                for (String file: Constants.SendFiles) {
                    //System.out.println("Fetched file name !!: "+file);
                    Constants.SMSContent = service.fetchSMSText("/test",file);
                    System.out.println("SendFiles:  "+ Constants.SMSContent);
                    Constants.SendContent.add(Constants.SMSContent);
                }
            }
            }.start();
//        while (Constants.SendContent == null){}
//        while (Constants.SendContent.size()!=5){}
        sms_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick", "button_Send clicked send All SMS");
                try {
                    sendMessage(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void DisplaySavedValues(View v) {
        if (!mPreferences.getString("user_my_account_identifier","").equals("")) {

            mIdentifier.setText(mPreferences.getString("user_my_account_identifier",""));
        }
    }

    public void sendMessage(View v) throws IOException {


        for (String line:Constants.SendContent)
        {
            String[] s = line.split("=");
            String[] ss = s[0].split("\"");
            String[] contentandtime = s[1].split("->");

            sendNumber.add(ss[ss.length-1]);
            sendMessage.add(contentandtime[0]);
            Date currentTime = Calendar.getInstance().getTime();
            sendTime.add(String.valueOf(currentTime));

            System.out.println("Message part: "+ contentandtime[0]);
            System.out.println("Send to this number: "+ss[ss.length-1]);
            System.out.println("Send time: "+ String.valueOf(currentTime));

            try {
                SmsManager smsManager = SmsManager.getDefault();
                System.out.println();
                smsManager.sendTextMessage(ss[ss.length-1],null,contentandtime[0],null,null);
                Toast toast = Toast.makeText(getContext(),"Sms sent!!!",Toast.LENGTH_LONG);
                toast.show();

            }catch (Exception e){
                Toast.makeText(getContext(),"SMS sent failed, please try again!!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            if (i == 10) {break;}
            if (counter_sms==3){
                Log.e("Notification", "Reach 8000 sms limitation, please change SIM card.");
                counter_sms = 0;
                break;
            }

            i++;
            counter_sms++;
        }
    }
}


