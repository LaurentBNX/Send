package com.dentasoft.testsend.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.dentasoft.testsend.Constants;

public class ImageAdapter extends PagerAdapter {
    private Context mContext;
    private Bitmap[] mImages;

    public ImageAdapter(Context context, Bitmap[] images) {
        super();
        this.mContext = context;
        this.mImages = images;
    }
    @Override
    public int getCount() {
        return mImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView img = new ImageView(mContext);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setImageDrawable(new BitmapDrawable(mContext.getResources(), Constants.slider_images[position]));
        container.addView(img,0);
        return img;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }
}
