package com.dentasoft.testsend.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dentasoft.testsend.R;
import com.dentasoft.testsend.adapters.ListViewAdapter;

import java.util.List;

public class SearchHistoryDialog extends Dialog{
    private ListViewAdapter list;
    private Button search_button;
    private Button cancel_button;
    private EditText mTo;
    private EditText mFrom;

    public SearchHistoryDialog(@NonNull Context context,ListViewAdapter adapter) {
        super(context);
        this.list = adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_history_dialog);
        search_button = findViewById(R.id.dialog_search);
        cancel_button = findViewById(R.id.dialog_cancel);
        mFrom = findViewById(R.id.start_date_search);
        mTo = findViewById(R.id.end_date_search);
        InitButtons();
    }

    public void InitButtons() {
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.filter(mFrom.getText().toString(),mTo.getText().toString());
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
