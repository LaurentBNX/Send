package com.dentasoft.testsend.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import com.dentasoft.testsend.Constants;
import com.dentasoft.testsend.FtpService;
import com.dentasoft.testsend.SmsService;

import java.io.IOException;
import java.util.List;

public class FetchSmsTask extends AsyncTask<String, String, List<String>> {

    private final Context context;

    public FetchSmsTask(Context c){
        this.context = c;
    }
    @Override
    protected List<String> doInBackground(String... strings) {
        FtpService service = new FtpService(context, Constants.IP);
        return service.fetchSMSToSend(Constants.USER_ID);
    }

}
