package com.dentasoft.testsend;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.dentasoft.testsend.Constants;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SmsService {

    public void sendMessage(String line) throws IOException {
            try{
                String[] s = line.split("=");
                String[] ss = s[0].split("\"");
                String[] contentandtime = s[1].split("->");


                String number = ss[ss.length-1];
                String message = contentandtime[0];
                String time = contentandtime[1];

                SmsManager smsManager = SmsManager.getDefault();
                System.out.println();
                smsManager.sendTextMessage(number,null,message,null,null);


            } catch (ArrayIndexOutOfBoundsException e) {
            }catch (Exception e){
                e.printStackTrace();
            }
    }
}
