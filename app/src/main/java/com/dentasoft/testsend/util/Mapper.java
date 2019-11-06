package com.dentasoft.testsend.util;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.dentasoft.testsend.adapters.ListViewAdapter;

import java.time.LocalDateTime;
import java.util.List;

public class Mapper {

    public static String mapViewsToMessageContent(List<View> viewList, ListViewAdapter adapter) {
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (View v: viewList) {
            sb.append("Message ").append(index++).append("\n");
            ListViewAdapter.ViewHolder holder = (ListViewAdapter.ViewHolder)v.getTag();
            sb.append(holder.sms_content.getText().toString()).append("\n").append(holder.ph_number.getText().toString()).append("\n").append(holder.history_date.getText().toString()).append(" ").append(holder.history_time.getText().toString()).append("\n\n");
        }
        return sb.toString();
    }

    public static LocalDateTime mapStringToDateTime(String raw) {
        int day = Integer.parseInt(raw.substring(0,2));
        int month = Integer.parseInt(raw.substring(2,4));
        int year = Integer.parseInt(raw.substring(4,8));
        int hour = Integer.parseInt(raw.substring(8,10));
        int minute = Integer.parseInt(raw.substring(10,12));
        int second = Integer.parseInt(raw.substring(12,14));
        return LocalDateTime.of(year,month,day,hour,minute,second);
    }
}
