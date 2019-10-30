package com.dentasoft.testsend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class FtpService {
    private String server;
    private String user;
    private String password;
    private View view;

    public FtpService(View view, String server) {
        this.view = view;
        this.server = server;
        this.user = Constants.userName;
        this.password = Constants.passWord;
    }

    public void fetchText(String filePath, String fileName) throws IOException {
        String fullFileName = filePath + "/" +fileName;
        FTPClient client = new FTPClient();
        try {
            client.connect(server);
            // Try to login and return the respective boolean value
            boolean login = client.login(user, password);
            int replyCode = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)){
                client.disconnect();
                throw new IOException("ftp connection failed:" + replyCode);
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        client.makeDirectory(fullFileName);
        client.enterLocalPassiveMode();

        client.setFileType(FTP.BINARY_FILE_TYPE);


        client.changeWorkingDirectory(filePath);
        File file =new File(view.getContext().getFilesDir(),fileName);

        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
        boolean success = client.retrieveFile("msg_LOG.txt", outputStream);


        StringBuilder fileContent = new StringBuilder("");
        FileInputStream fis;
        int ch;
        try {
            fis = view.getContext().openFileInput(fileName);
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
        Constants.fetched = content;






    }


    public Bitmap fetchImage(String filePath, String fileName) throws IOException {
        String fullFileName = filePath + "/" +fileName;
        FTPClient client = new FTPClient();
        try {
            client.connect(server);
            client.login(user, password);
            int replyCode = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)){
                client.disconnect();
                throw new IOException("ftp connection failed:" + replyCode);
            } else {
                client.makeDirectory(fullFileName);
                client.enterLocalPassiveMode();

                client.setFileType(FTP.BINARY_FILE_TYPE);


                client.changeWorkingDirectory(filePath);
                Bitmap bitmap = null;
                InputStream is = client.retrieveFileStream(fullFileName);
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
                client.logout();
                client.disconnect();
                return bitmap;
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
