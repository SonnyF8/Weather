package com.code.weather.Model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

public class ViewState extends BaseObservable {
    private boolean isVisible;

    @Bindable
    public boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
        notifyPropertyChanged(BR.isVisible);
    }

}
