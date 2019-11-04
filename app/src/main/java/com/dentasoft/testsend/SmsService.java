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
    public  int counter_sms =0, i=0;
    public void sendMessage(String line) throws IOException {
            try{
                String[] s = line.split("=");
                String[] ss = s[0].split("\"");
                String[] contentandtime = s[1].split("->");


           // String number = ss[ss.length-1];
            String number = "0032485987031";
            String message = contentandtime[0];
            String time = contentandtime[1];

                SmsManager smsManager = SmsManager.getDefault();
                System.out.println();
                smsManager.sendTextMessage(number,null,message+" "+time,null,null);


            } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(line);
            }catch (Exception e){
                e.printStackTrace();
            }

            if (i == 10) {return;}
            if (counter_sms==3){
                Log.e("Notification", "Reach 8000 sms limitation, please change SIM card.");
                counter_sms = 0;
                return;
            }

            i++;
            counter_sms++;
    }
}
