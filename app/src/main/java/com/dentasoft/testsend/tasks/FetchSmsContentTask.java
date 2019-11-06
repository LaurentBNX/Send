package com.dentasoft.testsend.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.dentasoft.testsend.Constants;
import com.dentasoft.testsend.FtpService;

public class FetchSmsContentTask extends AsyncTask<String,String,String> {
    private final Context context;

    public FetchSmsContentTask(Context c){
        this.context = c;
    }
    @Override
    protected String doInBackground(String... strings) {
        FtpService ftp = new FtpService(context, Constants.IP);
        return ftp.fetchSMSText(strings[0],strings[1]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }


}
