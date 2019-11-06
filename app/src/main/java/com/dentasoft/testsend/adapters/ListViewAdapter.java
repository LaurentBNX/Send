package com.dentasoft.testsend.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.widget.Toolbar;

import com.dentasoft.testsend.Constants;
import com.dentasoft.testsend.HistoryFragment;
import com.dentasoft.testsend.MainActivity;
import com.dentasoft.testsend.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    public static int counter = 0;
    Context context;
    ArrayList<String> sendMessage;
    ArrayList<String> sendNumber;
    ArrayList<String> historyTime;
    LayoutInflater inflater;
    HistoryFragment parent_view;
    LinearLayout container;
    private LinearLayout history_toolbar;
    private LinearLayout mNormal_toolbar;

    public ListViewAdapter(HistoryFragment parent,Context context, ArrayList<String> sendNumber, ArrayList<String> sendMessage, ArrayList<String> historyTime) {
        this.parent_view = parent;
        this.context = context;
        this.sendMessage = sendMessage;
        this.sendNumber = sendNumber;
        this.historyTime = historyTime;
        inflater = (LayoutInflater.from(context));
        container = parent_view.getActivity().findViewById(R.id.toolbar_container);
        mNormal_toolbar = (LinearLayout)parent.getLayoutInflater().inflate(R.layout.toolbar_history_normal,null,false);

    }


    @Override
    public int getCount() {
        return historyTime.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void deleteSelected() {

    }

    public void unselect() {
        for (int position: Constants.selected_messages) {

        }
    }

    public class ViewHolder{
        public TextView ph_number;
        public TextView sms_content;
        public TextView history_time;
        public TextView history_date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mNormal_toolbar = (LinearLayout) inflater.inflate(R.layout.toolbar_history_normal,null);
        ViewHolder holder;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_view_row, null);
            holder = new ViewHolder();
            holder.ph_number = (TextView) convertView.findViewById(R.id.number_view);
            holder.sms_content = (TextView) convertView.findViewById(R.id.content_view);
            holder.history_date = convertView.findViewById(R.id.date_view);
            holder.history_time =(TextView)convertView.findViewById(R.id.time_view);
            convertView.setTag(holder);


        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        if (Constants.selected_messages.contains(position)) {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.orange,null));
        } else {
            if (position % 2 == 0) {
                convertView.setBackgroundColor(convertView.getResources().getColor(R.color.alternate_row,null));
            } else {
                convertView.setBackgroundColor(convertView.getResources().getColor(R.color.white,null));
            }
        }


        holder.ph_number.setText(sendNumber.get(position));
        holder.sms_content.setText(sendMessage.get(position));
        holder.history_date.setText(historyTime.get(position).trim().split(" ")[0]);
        holder.history_time.setText(historyTime.get(position).trim().split(" ")[1]);
        convertView.setClickable(true);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable d = (ColorDrawable) v.getBackground();
                int color = d.getColor();
                boolean selected = color ==v.getResources().getColor(R.color.orange,null);
                if (selected){
                    v.setBackgroundColor(v.getResources().getColor(position % 2 == 0 ? R.color.alternate_row: R.color.white,null));
                    counter--;
                    Constants.selected_messages.remove(Constants.selected_messages.indexOf(position));
                    Constants.viewItems.remove(v);
                    if (counter == 0) {
                        InitHistoryToolbar();
                    } else {
                       updateToolbarCounter();
                    }
                }
                else {
                    v.setBackgroundColor(v.getResources().getColor( R.color.orange ,null));
                    counter++;
                    Constants.selected_messages.add(position);
                    Constants.viewItems.add(v);
                    if (counter == 1) {
                       InitHistoryToolbarOnItemSelected();
                    }
                    updateToolbarCounter();
                }
            }
        });


        return convertView;
    }

    private void InitHistoryToolbarOnItemSelected() {
        container.removeViewAt(0);
        history_toolbar = (LinearLayout) inflater.inflate(R.layout.toolbar_history_item_selected,null);
        history_toolbar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        container.addView(history_toolbar);
        parent_view.InitHistoryToolbarOnItemSelected(history_toolbar,this);
    }

    private void InitHistoryToolbar() {
        container.removeViewAt(0);

        mNormal_toolbar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        parent_view.InitHistoryToolbar(mNormal_toolbar,this);

        ((MainActivity)parent_view.getActivity()).InitMenu((Toolbar) mNormal_toolbar.getChildAt(0),this);
    }


    public void updateToolbarCounter() {
        TextView txt_counter = history_toolbar.findViewById(R.id.history_select_counter);
        txt_counter.setText(counter+"");
    }

    public void filter(String from,String to) {
        parent_view.filter(from,to);
    }
    public void filter(String number) {
        parent_view.filter(number);
    }
}
