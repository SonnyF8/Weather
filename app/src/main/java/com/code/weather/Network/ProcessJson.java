package com.code.weather.Network;

import android.app.Activity;
import android.app.Application;

import com.code.weather.Model.DataClasses.City;
import com.code.weather.Model.DataClasses.List;
import com.code.weather.Model.DataClasses.Main;
import com.code.weather.Model.DataClasses.RootJSON;
import com.code.weather.Model.DataClasses.Weather;
import com.code.weather.Model.WeatherModel;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class ProcessJson {

    public ArrayList<WeatherModel> parseJson(String httpResponse) {
        final String DATE_TIME_IN = "yyyy-MM-dd HH:mm:ss";
        final String DATE_OUT = "E, MMM d";
        final String TIME_OUT = "h a";

        String lastDate = "";
        String firstTime = "";
        String weatherDesc = "";
        String timeOut = "";
        String dateOut = "";
        String tempOut = "";

        double tempFahrenheit;

        int interval = 0;

        Date dateTime = null;
        Gson gson = new Gson();

        WeatherModel weatherModel = null;

        ArrayList<String> timeList = null;
        ArrayList<String> tempList = null;
        ArrayList<String> skyList = null;

        ArrayList<WeatherModel> weatherModelList = new ArrayList<>();

        SimpleDateFormat formatDateTimeIn = new SimpleDateFormat(DATE_TIME_IN);
        SimpleDateFormat formatDateOut = new SimpleDateFormat(DATE_OUT);
        SimpleDateFormat formatTimeOut = new SimpleDateFormat(TIME_OUT);

        formatDateTimeIn.setTimeZone(TimeZone.getTimeZone("UTC"));
        formatDateOut.setTimeZone(TimeZone.getDefault());
        formatTimeOut.setTimeZone(TimeZone.getDefault());

        RootJSON rootJSON = gson.fromJson(httpResponse, RootJSON.class);

        do {
            List list = rootJSON.getList().get(interval);
            Main main = list.getMain();
            tempFahrenheit = main.getTemp() * 9 / 5 - 459.67;
            java.util.List<Weather> weather = list.getWeather();

            weatherDesc = weather.get(0).getDescription();
            tempOut = String.format("%.1f", Double.valueOf(tempFahrenheit));

            try {
                dateTime = formatDateTimeIn.parse(list.getDtTxt());
                timeOut = formatTimeOut.format(dateTime).toString();
                dateOut = formatDateOut.format(dateTime).toString();
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }

            if (interval == 0) {
                firstTime = timeOut;
            }

            if (lastDate.length() == 0) {
                weatherModel = new WeatherModel();

                timeList = new ArrayList<>();
                tempList = new ArrayList<>();
                skyList = new ArrayList<>();

                lastDate = dateOut;
            }

            if (lastDate.equals(dateOut)) {
                if (firstTime.equals(timeOut)) {
                    weatherModel.setSky(weatherDesc);
                    weatherModel.setDate(dateOut);
                    weatherModel.setTemp(tempOut);
                }

                timeList.add(timeOut);
                tempList.add(tempOut);
                skyList.add(weather.get(0).getMain());

                interval++;
            } else {
                if (weatherModelList.size() < 1) {
                    while (timeList.size() < 8) {
                        timeList.add(0, new String());
                        tempList.add(0, new String());
                        skyList.add(0, new String());
                    }
                }

                weatherModel.setTimeList(timeList);
                weatherModel.setTempList(tempList);
                weatherModel.setSkyList(skyList);
                weatherModelList.add(weatherModel);

                lastDate = "";
            }

        } while (interval < rootJSON.getList().size());

        if (weatherModel.getDate() == null) {
            weatherModel.setSky(weatherDesc);
            weatherModel.setDate(dateOut);
            weatherModel.setTemp(tempOut);
        }

        if (timeList != null) {
            while (timeList.size() < 8) {
                timeList.add(new String());
                tempList.add(new String());
                skyList.add(new String());
            }

            weatherModel.setTimeList(timeList);
            weatherModel.setTempList(tempList);
            weatherModel.setSkyList(skyList);
            weatherModelList.add(weatherModel);
        }

        return weatherModelList;
    }

}
