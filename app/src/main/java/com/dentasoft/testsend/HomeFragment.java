package com.dentasoft.testsend;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dentasoft.testsend.adapters.ImageAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public HomeFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home,container,false);
    getActivity().setTitle("Home");
   // InitSliderImages(v);
        return v;
    }

    private void InitSliderImages(View v) {
        List<Bitmap> slider_images = new ArrayList<>();
        new Thread(
                () -> {
                    FtpService ftp = new FtpService(v,Constants.IP);
                    try {
                            slider_images.add(ftp.fetchImage(Constants.HOME_SLIDER_PATH,"slide1.png"));
                    } catch (IOException e) {
                        e.printStackTrace();}

                }
        ).start();

        Constants.slider_images = slider_images.toArray(new Bitmap[3]);
        ImageAdapter adapter = new ImageAdapter(v.getContext(),Constants.slider_images);
        ViewPager pager = v.findViewById(R.id.home_viewpager);
        pager.setAdapter(adapter);
    }
}
