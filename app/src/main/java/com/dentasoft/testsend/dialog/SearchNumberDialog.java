package com.dentasoft.testsend.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.dentasoft.testsend.R;
import com.dentasoft.testsend.adapters.ListViewAdapter;

public class SearchNumberDialog extends Dialog {
    private ListViewAdapter list;
    private Button search_button;
    private Button cancel_button;
    private EditText number;

    public SearchNumberDialog(@NonNull Context context, ListViewAdapter adapter) {
        super(context);
        this.list = adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_search_number);
        search_button = findViewById(R.id.dialog_search);
        cancel_button = findViewById(R.id.dialog_cancel);
        number = findViewById(R.id.edit_search_number);
        InitButtons();
    }

    public void InitButtons() {
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.filter(number.getText().toString());
                dismiss();
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
