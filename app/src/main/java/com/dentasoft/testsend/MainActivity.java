package com.dentasoft.testsend;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dentasoft.testsend.adapters.ListViewAdapter;
import com.dentasoft.testsend.alarm.SmsBroadcastReceiver;
import com.dentasoft.testsend.dialog.SearchHistoryDialog;
import com.dentasoft.testsend.dialog.SearchNumberDialog;
import com.dentasoft.testsend.tasks.FetchSmsTask;
import com.dentasoft.testsend.util.Mapper;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CustomerInfoFragment.OnFragmentInteractionListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    Intent mServiceIntent;
    private SharedPreferences mPreferences;
    private AlarmManager mAlarmManager;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        InitPreferences();
        if (!Constants.IP.equals("")) {
            try {
                new Thread(() -> {
                    DownloadNavHeader();
                    DownloadSliderImages();
                    DownloadAboutImage();
                }
                ).start();
                while (Constants.slider_images == null || Constants.nav_header == null || Constants.about_image == null){}
            } catch (Exception e) {}
        }
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean pre_auto = mPreferences.getBoolean("autoSend",false);
        if (pre_auto){
           InitAutomaticSmsService();
        }

        InitMenu(findViewById(R.id.toolbar),null);
        NavigateToHome();
    }


    public void stopAutomaticService() {
        mTimer.cancel();
    }
    public void InitAutomaticSmsService() {
        mAlarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        mTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    List<String> fileNames = new FetchSmsTask(null).execute().get();

                   for(String fileName: fileNames) {

                       FtpService ftp = new FtpService(MainActivity.this, Constants.IP);
                       final String[] content = new String[]{""};
                       new Thread(new Runnable() {
                           @Override
                           public void run() {
                               content[0] = ftp.fetchText(Constants.USER_ID, fileName, true);
                           }
                       }).start();
                       while (content[0].equals("")) {
                       }
                       String raw_datetime = fileName.split("txt")[0].substring(3, fileName.length() - 4);
                       Calendar date = Calendar.getInstance();
                       LocalDateTime send_date = Mapper.mapStringToDateTime(raw_datetime);
                       date.set(Calendar.DAY_OF_MONTH, send_date.getDayOfMonth());
                       date.set(Calendar.MONTH, send_date.getMonth().getValue() - 1);
                       date.set(Calendar.YEAR, send_date.getYear());
                       date.set(Calendar.HOUR_OF_DAY, send_date.getHour());
                       date.set(Calendar.MINUTE, send_date.getMinute());
                       date.set(Calendar.SECOND, send_date.getSecond());

                       Intent intent = new Intent(MainActivity.this, SmsBroadcastReceiver.class);
                       intent.putExtra("content", content[0]);
                       PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, (int)System.currentTimeMillis()+ new Random().nextInt(10), intent, PendingIntent.FLAG_UPDATE_CURRENT);

                       mAlarmManager.set(AlarmManager.RTC_WAKEUP, date.toInstant().toEpochMilli(), pendingIntent);
                   }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        mTimer.schedule(timerTask,1,Constants.TIME_SLOT * 1000);

    }

    private void InitPreferences() {
        mPreferences = getSharedPreferences("user_setting", Context.MODE_PRIVATE);
        Constants.TIME_SLOT = mPreferences.getInt("auto_sms_timeslot", 10);
        Constants.IP = mPreferences.getString("ftp_ip","193.70.45.74");
        Constants.USERNAME = mPreferences.getString("ftp_username","sms@soft4all.be");
        Constants.PASSWORD = mPreferences.getString("ftp_password","@P_r6CZ#SQ*d");
        Constants.USER_ID = "/".concat(mPreferences.getString("ftp_user_id","test"));
        Constants.SEND_SMS_INTERVAL = mPreferences.getInt("manual_sms_timeslot",1);
    }

    private void DownloadAboutImage() {
        try {
            FtpService ftp = new FtpService(this, Constants.IP);
            Constants.about_image = ftp.fetchImage(Constants.ABOUT_RESOURCES_PATH, Constants.ABOUT_BACKGROUND_FILE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void DownloadSliderImages() {
        try {
            FtpService ftp = new FtpService(this,Constants.IP);
            List<Bitmap> slider_images = new ArrayList<>();
            slider_images.addAll(ftp.fetchImages(Constants.HOME_SLIDER_IMG_PATH));
            Constants.slider_images = slider_images.toArray(new Bitmap[5]);
        } catch (IOException e) {
            e.printStackTrace();}
    }

    public void InitMenu(Toolbar toolbar, ListViewAdapter adapter) {
        if (toolbar == null){
            toolbar = (Toolbar)getLayoutInflater().inflate(R.layout.toolbar_home,null);
        }
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.nav_open,R.string.nav_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        try {
            ImageView search_date = toolbar.findViewById(R.id.history_toolbar_search);
            ImageView search_number = toolbar.findViewById(R.id.history_toolbar_search_number);
            search_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchHistoryDialog shdia = new SearchHistoryDialog(MainActivity.this,adapter);
                    shdia.show();
                }
            });
            search_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchNumberDialog sndia = new SearchNumberDialog(MainActivity.this,adapter);
                    sndia.show();
                }
            });
        } catch (Exception ex) {}
        InitNavHeader();
    }

    public void DownloadNavHeader() {
        FtpService ftp = new FtpService(this,Constants.IP);
        try {
            Constants.nav_header = ftp.fetchImage(Constants.MENU_IMAGES_PATH,Constants.MENU_NAV_HEADER_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void InitNavHeader() {

        ImageView img = navigationView.getHeaderView(0).findViewById(R.id.image_menu);
        img.setImageDrawable(new BitmapDrawable(getResources(),Constants.nav_header));

    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {drawer.closeDrawer(GravityCompat.START);}
        else {super.onBackPressed();}
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        switch(menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
                break;
            case R.id.nav_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HistoryFragment()).commit();
                break;
            case R.id.nav_statistics:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new StatisticsFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SettingsFragment()).commit();
                break;
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AboutFragment()).commit();
                break;
            case R.id.nav_my_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MyAccountFragment()).commit();
                break;
        }

        return true;
    }
    public void NavigateToHome() {
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_home));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        super.onDestroy();
    }

}
