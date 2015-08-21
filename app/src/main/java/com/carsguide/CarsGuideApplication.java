package com.carsguide;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CarsGuideApplication extends Application {

    private static Context sContext;

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .build();
        ImageLoader.getInstance().init(config);


    }

	/* Global helper methods to work with application resources */

    /**
     * Shared applicaion context
     *
     * @return Safe context
     */
    public static Context getAppContext() {
        return sContext;
    }

    /**
     * Obtains string with resource ID
     *
     * @param resId String resource identifier
     */
    public static String getResourceString(int resId) {
        return sContext.getResources().getString(resId);
    }


}
