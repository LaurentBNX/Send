package com.dentasoft.testsend.adapters;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateLabelFormatter extends StaticLabelsFormatter {

    public DateLabelFormatter(GraphView graphView) {
        super(graphView);
    }

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
}
