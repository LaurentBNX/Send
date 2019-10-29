package com.dentasoft.testsend;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;

public class AboutActivity extends AppCompatActivity implements CustomerInfoFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        LinearLayout customer_info = findViewById(R.id.about_fragment_holder_customer_info);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setId(R.id.about_fragment_holder_customer_info);

        getSupportFragmentManager().beginTransaction().add(ll.getId(),CustomerInfoFragment.newInstance(1,2,3)).commit();
        customer_info.addView(ll);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
