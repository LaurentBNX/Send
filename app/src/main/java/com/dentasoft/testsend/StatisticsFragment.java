package com.dentasoft.testsend;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dentasoft.testsend.formatter.DateXAxisFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class StatisticsFragment extends Fragment {

    private BarChart mChart;
    private List<String[]> mLines = new ArrayList<>();
    private HashMap<Integer,String> mDates_map;
    private Button mFilterButton;


    private EditText mToDate;
    private EditText mFromDate;
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
    private View mView;
    private BarDataSet mFullDataSet;
    private ArrayList<BarEntry> mAllEntries;

    public StatisticsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_statistics,container,false);
        getActivity().setTitle("Statistics");
        InitViews(mView);
        InitChart(mView);

        return mView;
    }

    private void InitViews(View v) {
        mFilterButton = v.findViewById(R.id.statistics_btn_search);
        mFromDate = v.findViewById(R.id.datepicker_min_date);
        mToDate = v.findViewById(R.id.datepicker_max_date);
        InitDatePicker(v);
        mFilterButton.setOnClickListener(FilterChart);
    }

    private void InitChart(View v) {
        mChart = v.findViewById(R.id.statistics_graph);
        mChart.setPinchZoom(false);
        mChart.getDescription().setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f );
        xAxis.setLabelCount(5,false);

        DownloadMessageData(v);
        HashMap<Integer,String> date_dictionary = InitGraphData(v);
        xAxis.setValueFormatter(new DateXAxisFormatter(date_dictionary));

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setLabelCount(5);
        yAxis.setAxisMinimum(0f);


    }

    private HashMap<Integer,String> InitGraphData(View v) {

        mAllEntries = extractDataPoints(mLines);
        mFullDataSet = new BarDataSet(mAllEntries,"The year 2019");

        BarData data = new BarData(mFullDataSet);
        data.setBarWidth(0.85f);
        if (mChart.getData() != null) {
            mChart.getData().clearValues();
        }
        mChart.setData(data);
        mChart.setFitBars(true);
        mChart.invalidate();

        return mDates_map;
    }

    public void InitDatePicker(View v) {
        mFromDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(v.getContext(),from_date, from_calendar.get(Calendar.YEAR), from_calendar.get(Calendar.MONTH),
                            from_calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }

        });
        mToDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(v.getContext(),to_date, to_calendar.get(Calendar.YEAR), to_calendar.get(Calendar.MONTH),
                            to_calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }

        });
    }

    private void DownloadMessageData(View v) {
        TextView txt_amount_of_sms = v.findViewById(R.id.statistics_txt_amount_of_sms);
        Constants.FtpContent = "";
            new Thread(() -> {   FtpService ftp = new FtpService(getContext(),Constants.IP);
                Constants.FtpContent = ftp.fetchText(Constants.USER_ID,"msg_LOG.txt");}).start();
        while (Constants.FtpContent.equals("")){}

        Scanner data= new Scanner(Constants.FtpContent);

        while (data.hasNextLine()) {
            String line = data.nextLine();
            String [] seperated = line.split("\\|");
            if (seperated.length == 3) {
                mLines.add(seperated);
            }
        }
        txt_amount_of_sms.setText(mLines.size()+"");
    }

    private ArrayList<BarEntry> extractDataPoints(List<String[]> lines) {
        int counter = 0;
        mDates_map = new HashMap<>();
        ArrayList<BarEntry> result = new ArrayList<>();
        List<Date> dates = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            try {
                String rawdate = lines.get(i)[2].trim().split(" ")[0];
                Date date =new SimpleDateFormat("dd/MM/yyyy").parse(rawdate);

                if (dates.stream().noneMatch(d -> d.equals(date))) {
                    dates.add(date);
                    long count = lines.stream().filter(l -> l[2].trim().startsWith(rawdate)).count();
                    result.add(new BarEntry(counter,(float)count));
                    mDates_map.put(counter++,new SimpleDateFormat("dd-MM-yyyy").format(date));

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return result;
    }

    private void updateFromLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mFromDate.setText(sdf.format(from_calendar.getTime()));
    }

    private void updateToLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mToDate.setText(sdf.format(to_calendar.getTime()));
    }

    private ArrayList<BarEntry> filterFullDataSet( ArrayList<BarEntry> allEntries,String start,String end) {
        int startIndex = 0;
        int endIndex = allEntries.size()-1;

        try {
            Date start_date = new SimpleDateFormat("dd/MM/yyyy").parse(start);
            Date end_date = new SimpleDateFormat("dd/MM/yyyy").parse(end);

            for (int i = 1; i < mDates_map.size(); i++) {
                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(mDates_map.get(i));
                Date prev_date = new SimpleDateFormat("dd-MM-yyyy").parse(mDates_map.get(i-1));
                if (start_date.compareTo(date) < 1 && start_date.compareTo(prev_date) > 0) {
                    startIndex = i;
                }

            }
            for (int i = 0; i < mDates_map.size()-1; i++) {
                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(mDates_map.get(i));
                Date next_date = new SimpleDateFormat("dd-MM-yyyy").parse(mDates_map.get(i+1));
                if (end_date.compareTo(date) > -1 && end_date.compareTo(next_date) < 0) {
                    endIndex = i;
                }

            }
        } catch (ParseException ex) {ex.printStackTrace();}
        return new ArrayList<>(allEntries.subList(startIndex,endIndex+1));
    }

    public View.OnClickListener FilterChart = (v) -> {
        String start_date = mFromDate.getText().toString().trim();
        String end_date = mToDate.getText().toString().trim();
        if (start_date.equals("") || end_date.equals("")) {
            Toast.makeText(getContext(),R.string.toast_message_date_invalid,Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<BarEntry> newDataSet = filterFullDataSet(mAllEntries,start_date,end_date);

        BarDataSet dataSet = new BarDataSet(newDataSet, "Custom date range");
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.85f);
        mChart.setData(data);
        mChart.setPinchZoom(false);
        mChart.invalidate();
    };


}
