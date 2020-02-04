package com.omronble.bp_broadcast;

import android.util.Log;

import com.omron.lib.ohc.OHQDeviceManager;

import org.litepal.LitePalApplication;

import static org.litepal.LitePal.initialize;

/**
 * class desc XXX
 *
 * @author zxzhang
 * <p>e-mail : zxzhang@chinaums.com </p>
 * <p>date: 2020/1/31 </p>
 * @version 1.0
 */
public class MyApplication extends LitePalApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        initialize(this);
        OHQDeviceManager.init(this);
        Log.v("OMRONLib", "OHQDeviceManager.init");
    }
}
