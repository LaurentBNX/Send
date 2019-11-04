package com.dentasoft.testsend.formatter;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class DateXAxisFormatter extends ValueFormatter {
    private SimpleDateFormat mFormatter;
    private HashMap<Integer,String> mDictionary;

    public DateXAxisFormatter(HashMap<Integer,String> dic){
        mFormatter = new SimpleDateFormat("dd-MM");
        this.mDictionary = dic;
    }
    @Override
    public String getFormattedValue(float value) {
      return mDictionary.get((int)value).substring(0,5);
    }




}
