package com.dentasoft.testsend;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class StatisticsFragment extends Fragment {

    private List<String[]> mLines = new ArrayList<>();;
    final Calendar myCalendar = Calendar.getInstance();
    private EditText mFromDate;
    private EditText mToDate;

    DatePickerDialog.OnDateSetListener from_date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateFromLabel();
        }

    };
    DatePickerDialog.OnDateSetListener to_date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateToLabel();
        }

    };

    public StatisticsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statistics,container,false);
        Init(v);
        InitDatePicker(v);
        return v;
    }

    private void InitDatePicker(View v) {
        mFromDate = v.findViewById(R.id.datepicker_min_date);
        mToDate = v.findViewById(R.id.datepicker_max_date);

        mFromDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            new DatePickerDialog(v.getContext(),from_date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                            updateFromLabel();
                        }
            }

        });
        mToDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(v.getContext(),from_date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        updateToLabel();
                }
            }

        });

        Button search_date = v.findViewById();
    }

    private void Init(View v) {
        TextView txt_amount_of_sms = v.findViewById(R.id.statistics_txt_amount_of_sms);
        if (Constants.FtpContent.equals("")) {
            new Thread(() -> {   FtpService ftp = new FtpService(v,Constants.IP);
                Constants.FtpContent = ftp.fetchText("/test","msg_LOG.txt");}).start();
        }
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
        GraphView graph = v.findViewById(R.id.statistics_graph);
        DataPoint[] points = extractDataPoints(mLines);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(points);


        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(10);
        series.setAnimated(true);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        //series.setValuesOnTopSize(50);


        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getGridLabelRenderer().setLabelFormatter(new LabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return new SimpleDateFormat("dd/MM").format(new Date((long)Math.round(value)));
                } else {
                    return value+"";
                }
            }

            @Override
            public void setViewport(Viewport viewport) {

            }
        });
        graph.getGridLabelRenderer().setHumanRounding(false);
       // graph.getGridLabelRenderer().setNumHorizontalLabels(5);
        graph.getViewport().setScrollable(true);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(points[0].getX());

        graph.getViewport().setMaxX(points[5].getX());

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(85);

        graph.getGridLabelRenderer().setHorizontalLabelsAngle(135);

        graph.addSeries(series);
    }

    private DataPoint[] extractDataPoints(List<String[]> lines) {
           List<DataPoint> result = new ArrayList<>();
           List<Date> dates = new ArrayList<>();

       for (int i = 0; i < lines.size(); i++) {
           try {
               String rawdate = lines.get(i)[2].trim().split(" ")[0];
               Date date =new SimpleDateFormat("dd/MM/yyyy").parse(rawdate);

             if (dates.stream().noneMatch(d -> d.equals(date))) {
                 dates.add(date);
                 long count = lines.stream().filter(l -> l[2].trim().startsWith(rawdate)).count();
                 result.add(new DataPoint(date,count));

             }
           } catch (ParseException e) {
               e.printStackTrace();
           }

       }

       return result.toArray(new DataPoint[result.size()]);
    }


    private void updateFromLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRENCH);
        mFromDate.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateToLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRENCH);
        mToDate.setText(sdf.format(myCalendar.getTime()));
    }
}
