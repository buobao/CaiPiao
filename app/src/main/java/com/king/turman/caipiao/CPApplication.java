package com.king.turman.caipiao;

import android.app.Application;
import android.content.Context;

/**
 * Created by diaoqf on 2017/12/7.
 */

public class CPApplication extends Application {
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
