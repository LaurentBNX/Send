package com.dentasoft.testsend;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import java.io.*;
import java.net.SocketException;

import java.util.*;


import com.dentasoft.testsend.adapters.ListViewAdapter;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class HistoryFragment extends Fragment {
    //  private ArrayList<HashMap> list;
    // list = new ArrayList<HashMap>();
    private ListView smsView;
    private TextView outPut;
    public int i = 0;
    ArrayList<String> sendMessage = new ArrayList<>();
    ArrayList<String> sendNumber = new ArrayList<>();
    ArrayList<String> historyTime = new ArrayList<>();

    public HistoryFragment(){}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        smsView = v.findViewById(R.id.smsListView);



        // outPut = v.findViewById(R.id.output);
        //populateList();

        new Thread() {
            @Override
            public void run() {
                    //  InitFTPServerSetting(v);
                    FtpService service = new FtpService(v,Constants.IP);
                    Constants.FtpContent = service.fetchText("/test","msg_LOG.txt");
            }
        }.start();
        while (Constants.FtpContent.equals("")){}

        Scanner data = new Scanner(Constants.FtpContent);
        while (data.hasNextLine()) {
            String line = data.nextLine();
            String[] seperated = line.split("\\|");
            //Date currentTime = Calendar.getInstance().getTime();
            //sendTime.add(String.valueOf(currentTime));
           if (seperated.length==3)
           {
               sendNumber.add(seperated[0]);
               sendMessage.add(seperated[1]);
               historyTime.add(seperated[2]);
               System.out.println("Target phone number:   " +i+ sendNumber.get(i));
               System.out.println("Content to be sent:   " + sendMessage.get(i));
               System.out.println("History time:   " + historyTime.get(i));
               i++;
           }
           else {
               Log.e("Damage message", "Check the damage message: "+line);
           }

            //sendTime[i] = String.valueOf(currentTime);

        }

        Log.e("Number of message", "onCreateView: "+sendMessage.size());
        ListViewAdapter adapter = new ListViewAdapter(this,getContext(),sendNumber,sendMessage,historyTime);

        smsView.setAdapter(adapter);
        smsView.setClickable(true);
        smsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = smsView.getItemAtPosition(position);

            }
        });

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    public void InitFTPServerSetting(View v ) throws IOException {
        String server = Constants.IP;

        FTPClient client = new FTPClient();
        try {
            client.connect(server);
            // Try to login and return the respective boolean value
            boolean login = client.login(Constants.userName, Constants.passWord);
            int replyCode = client.getReplyCode();
            if (FTPReply.isPositiveCompletion(replyCode)){
                Log.e("FTPUtils","Logging successful");
            }
            else{
                client.disconnect();
                throw new IOException("ftp connection failed:" + replyCode);
            }

// If login is true notify user

            if (login) {
                System.out.println("Connection established...");
                //Log.e(TAG, "Initialize successful");
            }
            else {
                System.out.println("Connection fail...");
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * Download file from server
         */
        System.out.println("Enter downloading..");
        String remoteFilePath = "/test/msg_LOG.txt";
        String localFilePath = "D:\\DentalSoft\\Download\\msg_LOG.txt";

        client.makeDirectory(remoteFilePath);
        client.enterLocalPassiveMode();
        client.setFileType(FTP.BINARY_FILE_TYPE);

        System.out.println("Remote system is " + client.getSystemType());
        client.changeWorkingDirectory("/test");
        System.out.println("Current directory is " + client.printWorkingDirectory());
        File file =new File(v.getContext().getFilesDir(),"msg_LOG.txt");
        System.out.println("Directory of " + file);
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));

        boolean success = client.retrieveFile("msg_LOG.txt", outputStream);
        if (success) {
            Log.e("FTP","Ftp file successfully download...");
        }

        String FilePath = v.getContext().getFilesDir() + "/" + "msg_LOG.txt";
        System.out.println("File direction:  " + FilePath);

        StringBuilder fileContent = new StringBuilder("");
        FileInputStream fis;
        int ch;
        try {
            fis = v.getContext().openFileInput("msg_LOG.txt");
            Log.e("onClick", "Read successful");
            System.out.println(fis);

            try {
                while ((ch = fis.read()) != -1)
                    fileContent.append((char) ch);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String content = new String(fileContent);
        Log.e("Laurent",content);
        Log.e("fdg",content.split("\n").length+"");
        //smsView.;

        Constants.FtpContent = content;
        //   Arrays.asList(s).stream().forEach(sl -> Log.e("line",sl));

        /**
         * Another difference is that openFileOutputStream opens / creates
         * a file in the the device's "internal" storage.
         * By contrast FileOutputStream allows use of both internal and external storage.
         *
         * Java BufferedOutputStream class is used for buffering an output stream.
         * It internally uses buffer to store data.
         * It adds more efficiency than to write data directly into a stream.
         * So, it makes the performance fast.
         *
         */

        outputStream.close();


    }

}
