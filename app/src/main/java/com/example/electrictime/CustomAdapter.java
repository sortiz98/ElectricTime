package com.example.electrictime;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<DataModel> dataSet;
    private EditText inputView;
    private Button distanceButton;
    private Button timeButton;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTransport;
        TextView textViewValue;
        TextView textViewUnits;
        TextView textViewVersion;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewTransport = (TextView) itemView.findViewById(R.id.textViewTransport);
            this.textViewValue = (TextView) itemView.findViewById(R.id.textViewValue);
            this.textViewUnits = (TextView) itemView.findViewById(R.id.textViewUnits);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.version);
        }
    }

    public CustomAdapter(ArrayList<DataModel> data, EditText inputView, Button distanceButton, Button timeButton) {
        this.dataSet = data;
        this.inputView = inputView;
        this.distanceButton = distanceButton;
        this.timeButton = timeButton;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewTransport = holder.textViewTransport;
        TextView textViewValue = holder.textViewValue;
        TextView textViewUnits = holder.textViewUnits;
        TextView textViewVersion = holder.textViewVersion;

        String transport = dataSet.get(listPosition).getTransport();
        String version = dataSet.get(listPosition).getVersion();
        double speed = dataSet.get(listPosition).getSpeed();
        int range = dataSet.get(listPosition).getRange();
        double input;
        String value, units;
        int unitsColor, valueColor;
        boolean isOutOfRange = true;

        try {
            input = Double.parseDouble(inputView.getText().toString());
        } catch (Exception e) {
            input = 0;
        }
        if (MainActivity.getDistanceMode()) {
            distanceButton.setPressed(true);
            timeButton.setPressed(false);
        } else {
            distanceButton.setPressed(false);
            timeButton.setPressed(true);
        }
        if (distanceButton.isPressed()) {
            int time = (int)((input / speed) * 60);
            if (time > 59) {
                value = time / 60 + "  " + time % 60;
                units = "hr       min";
            } else {
                value = String.valueOf(time);
                units = "minutes";
            }
            isOutOfRange = input > range;
        } else {
            double dist = speed * (input / 60.0);
            value = String.valueOf(Math.round(dist * 10.0) / 10.0);
            isOutOfRange = dist > range;
            units = "miles";
        }
        if (isOutOfRange) {
            units = "Too Far";
            unitsColor = Color.RED;
            valueColor = Color.RED;
        } else {
            unitsColor = Color.GRAY;
            valueColor = Color.DKGRAY;
        }

        textViewTransport.setText(transport);
        textViewVersion.setText(version);
        textViewValue.setText(value);
        textViewValue.setTextColor(valueColor);
        textViewUnits.setText(units);
        textViewUnits.setTextColor(unitsColor);

        if (listPosition == 0) {
            textViewTransport.setPaintFlags(textViewTransport.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
            textViewValue.setTextSize(36);
            textViewTransport.setTextColor(Color.BLACK);
        } else {
            textViewValue.setTextSize(30);
            textViewTransport.setPaintFlags(textViewTransport.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}