package com.dentasoft.testsend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dentasoft.testsend.tasks.FetchSmsTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyAccountFragment extends Fragment {

    private EditText mIdentifier;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

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
                mEditor.putString("ftp_user_id",id).commit();
                Constants.USER_ID = id;
                ((MainActivity)getActivity()).NavigateToHome();
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
                    FtpService service = new FtpService(getContext(),Constants.IP);
                    Constants.SendFiles = service.fetchSMSToSend(Constants.USER_ID);
                    //add
                    Constants.SendContent = new ArrayList<>();

            }
        }.start();

        while (Constants.SendFiles == null){}
        new Thread() {
            @Override
            public void run() {
                FtpService service = new FtpService(getContext(),Constants.IP);
                Constants.SendContent = new ArrayList<>();
                for (String file: Constants.SendFiles) {
                    //System.out.println("Fetched file name !!: "+file);
                    Constants.SMSContent = service.fetchSMSText(Constants.USER_ID,file);
                    System.out.println("SendFiles:  "+ Constants.SMSContent);
                    Constants.SendContent.add(Constants.SMSContent);
                }
            }
            }.start();

        sms_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendMessage(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void DisplaySavedValues(View v) {
        mIdentifier.setText(mPreferences.getString("ftp_user_id",""));
    }

    public void sendMessage(View v) throws IOException {
       try {
           List<String> fileNames = new FetchSmsTask(null).execute().get();
           for (String fileName: fileNames) {
               FtpService ftp = new FtpService(getContext(), Constants.IP);
               final String[] content = new String[]{""};
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       content[0] = ftp.fetchText(Constants.USER_ID, fileName, true);

                   }
               }).start();
               while (content[0].equals("")) {
               }
               new SmsService().sendMessage(content[0]);
               Thread.sleep(Constants.SEND_SMS_INTERVAL * 1000);
           }
       } catch (Exception e) {
               e.printStackTrace();
           }

    }
}


