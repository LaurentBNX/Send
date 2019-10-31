package com.dentasoft.testsend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
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

    public String fetchText(String filePath, String fileName) {
        String fullFileName = filePath + "/" + fileName;
        FTPClient client = new FTPClient();
        try {
            client.connect(server);
            // Try to login and return the respective boolean value
            boolean login = client.login(user, password);
            if (login) System.out.println("Login successful!");
            else System.out.println("Login failed");

            int replyCode = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                client.disconnect();
                throw new IOException("ftp connection failed:" + replyCode);
            }
            client.makeDirectory(fullFileName);
            client.enterLocalPassiveMode();

            client.setFileType(FTP.BINARY_FILE_TYPE);


            client.changeWorkingDirectory(filePath);

            InputStream is = client.retrieveFileStream(fileName);
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");


            StringBuilder fileContent = new StringBuilder("");
            FileInputStream fis;
            int ch;
            fis = view.getContext().openFileInput(fileName);
            try {
                while ((ch = isr.read()) != -1)
                    fileContent.append((char) ch);
            } catch (IOException e) {
                e.printStackTrace();

            }
            String content = new String(fileContent);
            return content;
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
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

    public List<Bitmap> fetchImages(String filePath) throws IOException {
        List<Bitmap> result = new ArrayList<>();
        FTPClient client = new FTPClient();
        try {
            client.connect(server);
            client.login(user, password);
            int replyCode = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)){
                client.disconnect();
                throw new IOException("ftp connection failed:" + replyCode);
            } else {
                client.enterLocalPassiveMode();
                client.setFileType(FTP.BINARY_FILE_TYPE);
                client.changeWorkingDirectory(filePath);
                FTPFile[] files = client.listFiles(filePath);
                for (FTPFile file: files) {
                    String fileName = file.getName();
                    if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
                        result.add(fetchImage(filePath,fileName));
                    }
                }
                return result;
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> fetchSMSToSend(String filePath) {
        //String fullFileName = filePath + "/" + fileName;
        FTPClient client = new FTPClient();
        List<String> result = new ArrayList<>();

        try {
            client.connect(server);
            // Try to login and return the respective boolean value
            boolean login = client.login(user, password);
            if (login) System.out.println("Login successful!");
            else System.out.println("Login failed");

            int replyCode = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                client.disconnect();
                throw new IOException("ftp connection failed:" + replyCode);
            }
            client.makeDirectory(filePath);
            client.enterLocalPassiveMode();

            client.setFileType(FTP.BINARY_FILE_TYPE);


            client.changeWorkingDirectory(filePath);

            FTPFile[] files = client.listFiles(filePath);
            for (FTPFile file: files) {
                String fileName = file.getName();
                if (fileName.endsWith(".txt") && !fileName.contains("LOG")) {
                    result.add(fileName);
                }
            }

            return result;
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
