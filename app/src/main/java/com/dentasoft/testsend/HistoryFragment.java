package com.dentasoft.testsend;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.io.*;
import java.lang.reflect.Array;
import java.net.SocketException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


import com.dentasoft.testsend.adapters.ListViewAdapter;
import com.dentasoft.testsend.dialog.SearchHistoryDialog;
import com.dentasoft.testsend.dialog.SearchNumberDialog;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class HistoryFragment extends Fragment {
    //  private ArrayList<HashMap> list;
    // list = new ArrayList<HashMap>();
    private ListView smsView;
    private TextView amount_of_msg;
    public int i = 0;
    ArrayList<String> sendMessage = new ArrayList<>();
    ArrayList<String> sendNumber = new ArrayList<>();
    ArrayList<String> historyTime = new ArrayList<>();
    private LinearLayout mToolbar_container;
    private ListViewAdapter mAdapter;


    public HistoryFragment(){}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_history, container, false);

        getActivity().setTitle("History");
        LinearLayout tb_container = (LinearLayout)inflater.inflate(R.layout.toolbar_history_normal,container,false);
        smsView = v.findViewById(R.id.smsListView);
        amount_of_msg = v.findViewById(R.id.message_amount_txt);
        DownloadMessages(v);
        InitMessages(v);
        DisplayMessages(v);
        mToolbar_container = getActivity().findViewById(R.id.toolbar_container);
        mToolbar_container.removeAllViews();
        mToolbar_container.addView(tb_container);
        ((MainActivity)getActivity()).InitMenu((Toolbar) tb_container.getChildAt(0),mAdapter);
        return v;
    }



    private void InitMessages(View v) {
        Scanner data = new Scanner(Constants.FtpContent);
        while (data.hasNextLine()) {
            String line = data.nextLine();
            String[] seperated = line.split("\\|");
            if (seperated.length==3)
            {
                sendNumber.add(seperated[0]);
                sendMessage.add(seperated[1]);
                historyTime.add(seperated[2]);

            }
            else {
                Log.e("Damage message", "Check the damage message: "+line);
            }


        }

        Log.e("Number of message", "onCreateView: "+sendMessage.size());

    }

    private void DownloadMessages(View v) {
        new Thread() {
            @Override
            public void run() {
                //  InitFTPServerSetting(v);
                FtpService service = new FtpService(v,Constants.IP);
                Constants.FtpContent = service.fetchText("/test","msg_LOG.txt");
            }
        }.start();
        while (Constants.FtpContent.equals("")){}
    }

    public void DisplayMessages(View v) {
        mAdapter = new ListViewAdapter(this,getContext(),sendNumber,sendMessage,historyTime);

        smsView.setAdapter(mAdapter);
        smsView.setClickable(true);
        amount_of_msg.setText(mAdapter.getCount()+" messages");

    }

    public void filter(String from,String to) {
        ArrayList<String> numbers = new ArrayList<>();
        ArrayList<String> messages = new ArrayList<>();
        ArrayList<String> time = new ArrayList<>();
        try {
            Date from_bound = new SimpleDateFormat("dd/MM/yyyy").parse(from);
            Date to_bound = new SimpleDateFormat("dd/MM/yyyy").parse(to);
                for (int i = 0; i < historyTime.size(); i++) {
                    Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(historyTime.get(i));
                    if (date.after(from_bound) && date.before(to_bound)) {
                        numbers.add(sendNumber.get(i));
                        messages.add(sendMessage.get(i));
                        time.add(historyTime.get(i));
                    }
                }
          } catch (ParseException e) {
              e.printStackTrace();
          }

        ListViewAdapter adapter = new ListViewAdapter(this,getContext(), numbers,messages,time);
        smsView.setAdapter(adapter);
        smsView.setClickable(true);
        amount_of_msg.setText(adapter.getCount()+" messages");
    }

    public void filter(String number) {
        ArrayList<String> numbers = new ArrayList<>();
        ArrayList<String> messages = new ArrayList<>();
        ArrayList<String> time = new ArrayList<>();
            for (int i = 0; i < historyTime.size(); i++) {
                if (sendNumber.get(i).contains(number)) {
                    numbers.add(sendNumber.get(i));
                    messages.add(sendMessage.get(i));
                    time.add(historyTime.get(i));
                }
            }
        ListViewAdapter adapter = new ListViewAdapter(this,getContext(), numbers,messages,time);
        smsView.setAdapter(adapter);
        smsView.setClickable(true);
        amount_of_msg.setText(adapter.getCount()+" messages");
    }

    public void InitHistoryToolbar(LinearLayout toolbar, ListViewAdapter adapter) {

        ImageView search_date = toolbar.findViewById(R.id.history_toolbar_search);
        ImageView search_number = toolbar.findViewById(R.id.history_toolbar_search_number);
        search_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchHistoryDialog shdia = new SearchHistoryDialog(getContext(),adapter);
                shdia.show();
            }
        });
        search_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchNumberDialog sndia = new SearchNumberDialog(getContext(),adapter);
                sndia.show();
            }
        });
        mToolbar_container.addView(toolbar);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        ((LinearLayout)getActivity().findViewById(R.id.toolbar_container)).removeViewAt(0);
        ((MainActivity)getActivity()).InitMenu(null,null);
    }
}
