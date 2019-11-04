package com.dentasoft.testsend;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.IOException;


public class AboutFragment extends Fragment {

    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View v = inflater.inflate(R.layout.fragment_about, container, false);
         LinearLayout customer_info = v.findViewById(R.id.about_fragment_holder_customer_info);
        android.widget.LinearLayout ll = new LinearLayout(getActivity());
        ll.setOrientation(android.widget.LinearLayout.VERTICAL);
        ll.setId(R.id.about_fragment_holder_customer_info);
        getActivity().setTitle("About");
        for (int[] customer_detail: Constants.ABOUT_CUSTOMER_DETAILS) {
            getActivity().getSupportFragmentManager().beginTransaction().add(ll.getId(),CustomerInfoFragment.newInstance(customer_detail[0],customer_detail[1])).commit();
        }
        customer_info.addView(ll);
        InitBackgroundPicture(v);
        return v;
    }

    private void InitBackgroundPicture(View v) {
        LinearLayout background = v.findViewById(R.id.company_details_background);
        Drawable bg = new BitmapDrawable(getResources(),Constants.about_image);
        background.setBackground(bg);
    }


}
