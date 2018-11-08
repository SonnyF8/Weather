package com.code.weather.Activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.code.weather.Adapter.WeatherAdapter;
import com.code.weather.BR;
import com.code.weather.Model.ViewState;
import com.code.weather.Network.NetworkCall;
import com.code.weather.Network.ProcessJson;
import com.code.weather.R;

public class WeatherActivity extends AppCompatActivity implements NetworkCall.CallBack {
    ViewState viewState = new ViewState();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);

        ViewDataBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewDataBinding.setVariable(BR.viewState, viewState);

        viewState.setIsVisible(true);
    }

    @BindingAdapter({"isVisible"})
    public static void setIsVisible(View view, boolean showView) {
        view.setVisibility(showView ? View.VISIBLE : view.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (netCheck()) {
            new NetworkCall().invoke(this, getString(R.string.weather_api));
        }
    }

    @Override
    public void onCallResponse(String response) {
        ListView listView = (ListView) findViewById(R.id.weather_list);
        listView.setAdapter(new WeatherAdapter(this, new ProcessJson().parseJson(response)));

        viewState.setIsVisible(false);
    }

    private boolean netCheck() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null) {
            if (cm.getActiveNetworkInfo().isConnectedOrConnecting()) {return true;}
        }

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.network_error))
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setMessage(getString(R.string.check_settings))
                .setPositiveButton(getString(R.string.exit_app), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
        return false;
    }

}
