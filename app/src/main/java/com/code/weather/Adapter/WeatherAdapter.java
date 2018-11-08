package com.code.weather.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.code.weather.BR;
import com.code.weather.Model.WeatherModel;
import com.code.weather.R;

import java.util.ArrayList;

public class WeatherAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<WeatherModel> weatherModelList;

    public WeatherAdapter(Context context, ArrayList<WeatherModel> weatherModelList) {
        this.context = context;
        this.weatherModelList = weatherModelList;
    }

    @Override
    public int getCount() {
        return weatherModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding rowViewBinding;

        if (convertView == null) {
            rowViewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.weather_row, parent, false);
        } else {
            rowViewBinding = DataBindingUtil.bind(convertView);
        }

        rowViewBinding.setVariable(BR.weatherModel, weatherModelList.get(position));
        return rowViewBinding.getRoot();
    }

}
