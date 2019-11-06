package com.dentasoft.testsend.tasks;

import android.content.Context;
import android.os.AsyncTask;
import com.dentasoft.testsend.Constants;
import com.dentasoft.testsend.FtpService;
import com.dentasoft.testsend.SmsService;

import java.io.IOException;
import java.util.List;

public class SendSMSTask extends AsyncTask<String,String,String> {
    private final Context context;

    public SendSMSTask(Context c){
        this.context = c;
    }
    @Override
    protected String doInBackground(String... strings) {
        FtpService ftp = new FtpService(context, Constants.IP);
        String sms = ftp.fetchSMSText("/test",strings[0]);
        try {
            new SmsService().sendMessage(sms);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }
}
