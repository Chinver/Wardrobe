package com.wardrobe.www;

import android.app.Application;

import org.xutils.x;

/**
 * Created by admin on 2016/9/30.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
