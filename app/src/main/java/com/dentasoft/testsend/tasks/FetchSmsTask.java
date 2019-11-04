package com.dentasoft.testsend.tasks;

import android.os.AsyncTask;
import android.view.View;
import com.dentasoft.testsend.Constants;
import com.dentasoft.testsend.FtpService;
import com.dentasoft.testsend.SmsService;

import java.io.IOException;
import java.util.List;

public class FetchSmsTask extends AsyncTask<String, String, List<String>> {

    private final View v;

    public FetchSmsTask(View v){
        this.v = v;
    }
    @Override
    protected List<String> doInBackground(String... strings) {
        FtpService service = new FtpService(v, Constants.IP);
        return service.fetchSMSToSend("/test");
    }

}
