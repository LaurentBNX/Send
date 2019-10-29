package com.dentasoft.testsend;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



public class CustomerInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_IMG_SRC = "image_source";
    private static final String ARG_TXT_VAL = "text_value";
    private static final String ARG_TXT_DEF = "text_definition";

    // TODO: Rename and change types of parameters
    private int image_source;
    private int text_value;
    private int text_definition;


    private OnFragmentInteractionListener mListener;

    public CustomerInfoFragment() {
        // Required empty public constructor
    }


    public static CustomerInfoFragment newInstance(int img_src, int txt_def) {
        CustomerInfoFragment fragment = new CustomerInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMG_SRC, img_src);
       // args.putInt(ARG_TXT_VAL,);
        args.putInt(ARG_TXT_DEF,txt_def);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            image_source = getArguments().getInt(ARG_IMG_SRC);
          //  text_value = getArguments().getInt(ARG_TXT_VAL);
            text_definition = getArguments().getInt(ARG_TXT_DEF);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_about_fragment_customer_info, container, false);

        ImageView img = v.findViewById(R.id.about_img_src);
        TextView txt_val = v.findViewById(R.id.about_txt_value);
        TextView txt_def = v.findViewById(R.id.about_txt_definition);

        img.setImageResource(image_source);
        txt_def.setText(text_definition);
        txt_val.setText("95");
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
