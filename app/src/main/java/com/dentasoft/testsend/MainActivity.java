package com.dentasoft.testsend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dentasoft.testsend.adapters.ImageAdapter;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CustomerInfoFragment.OnFragmentInteractionListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

           new Thread(() -> {
               DownloadNavHeader();
               DownloadSliderImages();}
               ).start();
        } catch (Exception e) {}
        while (Constants.slider_images == null || Constants.nav_header == null){}
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitMenu();
        InitNavHeader();


    }

    private void DownloadSliderImages() {
        try {
            FtpService ftp = new FtpService(navigationView,Constants.IP);
            List<Bitmap> slider_images = new ArrayList<>();
            slider_images.addAll(ftp.fetchImages(Constants.HOME_SLIDER_IMG_PATH));
            Constants.slider_images = slider_images.toArray(new Bitmap[5]);
        } catch (IOException e) {
            e.printStackTrace();}
    }

    public void InitMenu() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.nav_open,R.string.nav_close);

        drawer.addDrawerListener(toggle);
        navigationView.setCheckedItem(R.id.nav_home);
        toggle.syncState();




    }


    public void DownloadNavHeader() {
        FtpService ftp = new FtpService(navigationView,Constants.IP);
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
        drawer.closeDrawer(GravityCompat.START);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
