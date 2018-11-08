package com.code.weather.Network;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class NetworkCall extends AsyncTask {

    public interface CallBack {
        void onCallResponse(String response);
    };

    private CallBack callBack;

    public void invoke(CallBack callBack, String url) {
        this.callBack = callBack;
        this.execute(url);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        String line;
        String url = params[0].toString();

        StringBuilder returnVal = new StringBuilder();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try {
            InputStream in = client.newCall(request).execute().body().byteStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while((line = reader.readLine()) != null) {returnVal.append(line);}
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnVal.toString();
    }

    @Override
    protected void onPostExecute(Object obj) {
        super.onPostExecute(obj);
        callBack.onCallResponse(obj.toString());
    }
}
