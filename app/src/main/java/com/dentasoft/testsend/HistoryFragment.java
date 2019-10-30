package com.dentasoft.testsend;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.io.*;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class HistoryFragment extends Fragment {
    public HistoryFragment(){}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);

            new Thread() {
                @Override
                public void run() {
                    try {
                      //  InitFTPServerSetting(v);
                        FtpService service = new FtpService(v,Constants.IP);
                        service.fetchText("/test","msg_LOG.txt");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();


        return v;
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

        if (success) {
            Log.e("FTP","Ftp file successfully download...");
            System.out.println("Ftp file successfully download.");
        }
    }

}
