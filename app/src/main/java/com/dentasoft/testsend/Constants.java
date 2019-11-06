package com.dentasoft.testsend;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static int[][] ABOUT_CUSTOMER_DETAILS = {
            {R.drawable.ic_action_name,R.string.happy_customer},
            {R.drawable.ic_users,R.string.sms_users},
            {R.drawable.ic_computer,R.string.project_in_dev}

    };


    //FTP constants
            public static String IP = "";
            public static String USERNAME = "";
            public static String PASSWORD = "";
            //@P_r6CZ#SQ*d
            public static int TIME_SLOT = 1;
            public static int SEND_SMS_INTERVAL = 1;
            public static String USER_ID = "";
            public static List<View> viewItems = new ArrayList<>();



        public static final String ABOUT_RESOURCES_PATH = "/_webService_/about/images";
            public static final String ABOUT_BACKGROUND_FILE = "backgroud.jpg";

            public static final String HOME_SLIDER_PATH = "/_webService_/slider";
            public static final String HOME_SLIDER_IMG_PATH = HOME_SLIDER_PATH+"/img";
            public static final String HOME_SLIDER_CONFIG_FILE = "slider.json";



            public static final String MENU_IMAGES_PATH = "/_webService_/config/img";

            public static final String MENU_NAV_HEADER_FILE = "nav_header.png";

            public static Bitmap about_image = null;
            public static Bitmap[] slider_images = null;
            public static Bitmap nav_header = null;

            public static String FtpContent = "";
            public static String SMSContent = "";
            public static List<String> SendFiles = null;
            public static List<String> SendContent = null;

            public static List<Integer> selected_messages = new ArrayList<>();


}
