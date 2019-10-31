package com.dentasoft.testsend;

import android.app.DatePickerDialog;
import android.graphics.Color;
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

import com.dentasoft.testsend.adapters.DateLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

public class StatisticsFragment extends Fragment {

    private List<String[]> mLines = new ArrayList<>();;
    final Calendar from_calendar = Calendar.getInstance();
    final Calendar to_calendar = Calendar.getInstance();
    private EditText mFromDate;
    private EditText mToDate;

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

    private DataPoint[] mAllPoints;
    private GraphView mGraph;

    public StatisticsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statistics,container,false);
        getActivity().setTitle("Statistics");
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
                            new DatePickerDialog(v.getContext(),from_date, from_calendar.get(Calendar.YEAR), from_calendar.get(Calendar.MONTH),
                                    from_calendar.get(Calendar.DAY_OF_MONTH)).show();
                            updateFromLabel();
                        }
            }

        });
        mToDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(v.getContext(),to_date, to_calendar.get(Calendar.YEAR), to_calendar.get(Calendar.MONTH),
                            to_calendar.get(Calendar.DAY_OF_MONTH)).show();
                        updateToLabel();
                }
            }

        });

        Button search_date = v.findViewById(R.id.statistics_btn_search);
        search_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start_date = mFromDate.getText().toString();
                String end_date = mToDate.getText().toString();
                int date_gap = 0;
                if (start_date.equals("") || end_date.equals("")) {
                    Toast.makeText(v.getContext(),"Please pick a start and end date!",Toast.LENGTH_SHORT).show();
                    return;
                }
                mGraph.getSeries().remove(0);
                List<DataPoint> newPoints =  Arrays.asList(mAllPoints).stream().filter(point -> {
                    try {
                        Date point_date = new Date((long) point.getX());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(start_date));
                        cal.add(Calendar.HOUR,-1);
                        Date start_bound = cal.getTime();
                        cal.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(end_date));
                        cal.add(Calendar.HOUR,1);
                        Date end_bound = cal.getTime();

                        boolean isafter = point_date.after(start_bound);
                        boolean isbefore = point_date.before(end_bound);
                        return isafter && isbefore;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).collect(Collectors.toList());
                DataPoint[] newSeries = newPoints.toArray(new DataPoint[newPoints.size()]);
                BarGraphSeries series = new BarGraphSeries(newSeries);
                InitSeries(series);
                mGraph.addSeries(series);
                mGraph.getViewport().setMinX(newSeries[0].getX());

                mGraph.getViewport().setMaxX(newSeries[newSeries.length-1].getX());
                InitHorizontalLabels(newSeries);

            }
        });
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
        mGraph = v.findViewById(R.id.statistics_graph);
        mAllPoints = extractDataPoints(mLines);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(mAllPoints);

        InitSeries(series);

        mGraph.getLegendRenderer().setVisible(true);
        mGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        mGraph.getGridLabelRenderer().setHumanRounding(false);
        mGraph.getViewport().setScrollable(true);

        mGraph.getViewport().setXAxisBoundsManual(true);
        mGraph.getViewport().setMinX(mAllPoints[0].getX());

        mGraph.getViewport().setMaxX(mAllPoints[5].getX());
        InitHorizontalLabels(mAllPoints);

        mGraph.getViewport().setYAxisBoundsManual(true);
        mGraph.getViewport().setMinY(0);
        mGraph.getViewport().setMaxY(85);

        mGraph.getGridLabelRenderer().setHorizontalLabelsAngle(135);

        mGraph.addSeries(series);
    }


    public void InitSeries(BarGraphSeries series) {
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
    }

    public void InitHorizontalLabels(DataPoint[] points) {
        DateLabelFormatter labels = new DateLabelFormatter(mGraph);
        String [] lab = new String[points.length];
        for (int i = 0; i < points.length; i++) {
            lab[i] = (long)points[i].getX()+"";
        }
        labels.setHorizontalLabels(lab);
        mGraph.getGridLabelRenderer().setLabelFormatter(labels);
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
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mFromDate.setText(sdf.format(from_calendar.getTime()));
    }
    private void updateToLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mToDate.setText(sdf.format(to_calendar.getTime()));
    }
}
