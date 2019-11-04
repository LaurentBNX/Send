package com.dentasoft.testsend.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dentasoft.testsend.R;
import com.dentasoft.testsend.adapters.ListViewAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SearchHistoryDialog extends Dialog{
    private ListViewAdapter list;
    private Button search_button;
    private Button cancel_button;
    private EditText mTo;
    private EditText mFrom;
    final Calendar from_calendar = Calendar.getInstance();
    final Calendar to_calendar = Calendar.getInstance();


    DatePickerDialog.OnDateSetListener from_date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            from_calendar.set(Calendar.YEAR, year);
            from_calendar.set(Calendar.MONTH, monthOfYear);
            from_calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateFromLabel();
        }

    };


    DatePickerDialog.OnDateSetListener to_date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            to_calendar.set(Calendar.YEAR, year);
            to_calendar.set(Calendar.MONTH, monthOfYear);
            to_calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateToLabel();
        }

    };

    public SearchHistoryDialog(@NonNull Context context,ListViewAdapter adapter) {
        super(context);
        this.list = adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_search_by_date);
        search_button = findViewById(R.id.dialog_search);
        cancel_button = findViewById(R.id.dialog_cancel);
        mFrom = findViewById(R.id.start_date_search);
        mTo = findViewById(R.id.end_date_search);
        InitButtons();
        InitDatePicker();
    }

    public void InitButtons() {
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFrom.getText().toString().trim().equals("") || mTo.getText().toString().trim().equals("")) {
                    Toast.makeText(getContext(),R.string.toast_message_date_invalid,Toast.LENGTH_SHORT).show();
                    return;
                }
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
    public void InitDatePicker() {
        mFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(v.getContext(),from_date, from_calendar.get(Calendar.YEAR), from_calendar.get(Calendar.MONTH),
                            from_calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }

        });
        mTo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(v.getContext(),to_date, to_calendar.get(Calendar.YEAR), to_calendar.get(Calendar.MONTH),
                            to_calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }

        });
    }

    private void updateFromLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mFrom.setText(sdf.format(from_calendar.getTime()));
    }

    private void updateToLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mTo.setText(sdf.format(to_calendar.getTime()));
    }


}
