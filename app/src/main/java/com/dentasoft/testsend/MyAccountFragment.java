package com.dentasoft.testsend;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MyAccountFragment extends Fragment {

    private EditText mIdentifier;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public MyAccountFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_account,container,false);
        getActivity().setTitle("My account");
        Init(v);
        DisplaySavedValues(v);
        return v;
    }

    private void Init(View v) {
        Button save_button = v.findViewById(R.id.my_account_save_button);
        Button sms_button = v.findViewById(R.id.my_account_sms_button);
        Button quit_button = v.findViewById(R.id.my_account_quit_button);
        mPreferences = v.getContext().getSharedPreferences("user_setting", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mIdentifier = v.findViewById(R.id.my_account_edit_identifier);


        save_button.setOnClickListener(v1 -> {
            String id = mIdentifier.getText().toString();
            if (!id.equals("")) {
                mEditor.putString("user_my_account_identifier",id).commit();
            } else {
                Toast.makeText(v1.getContext(), "No identifier is entered!", Toast.LENGTH_SHORT).show();
            }
        });
        quit_button.setOnClickListener(v1 -> {
            getActivity().finish();
        });
    }

    private void DisplaySavedValues(View v) {
        if (!mPreferences.getString("user_my_account_identifier","").equals("")) {

            mIdentifier.setText(mPreferences.getString("user_my_account_identifier",""));
        }
    }
}
