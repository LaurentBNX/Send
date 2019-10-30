package com.dentasoft.testsend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> sendMessage;
    ArrayList<String> sendNumber;
    LayoutInflater inflater;


    public ListViewAdapter(Context context,ArrayList<String> sendNumber, ArrayList<String>sendMessage) {
        //super();
        this.context = context;
        this.sendMessage = sendMessage;
        this.sendNumber = sendNumber;
        inflater = (LayoutInflater.from(context));

    }


    @Override
    public int getCount() {
        return sendMessage.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView ph_number;
        TextView sms_content;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
//        LayoutInflater inflater = activity.getLayoutInflater();

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_view_row, null);
            holder = new ViewHolder();
            holder.ph_number = (TextView) convertView.findViewById(R.id.FirstText);
            holder.sms_content = (TextView) convertView.findViewById(R.id.SecondText);
            convertView.setTag(holder);

        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position % 2 == 0) {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.beige,null));
        } else {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.white,null));
        }

        holder.ph_number.setText(sendNumber.get(position));
        holder.sms_content.setText(sendMessage.get(position));
        return convertView;
    }
}
