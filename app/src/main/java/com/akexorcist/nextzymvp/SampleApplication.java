package com.akexorcist.nextzymvp;

import android.app.Application;

import com.nextzy.library.box.Box;
import com.nextzy.library.box.BoxConfiguration;
import com.nextzy.library.nextwork.cookie.NextworkWebkitCookieJar;

/**
 * Created by Akexorcist on 3/26/2017 AD.
 */

public class SampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NextworkWebkitCookieJar.getInstance().init(getApplicationContext());
        BoxConfiguration.init(getApplicationContext(), Box.getInstance().getConfigBuilder());
    }
}
