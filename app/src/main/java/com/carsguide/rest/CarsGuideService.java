package com.carsguide.rest;

import android.app.IntentService;
import android.content.Intent;

import com.carsguide.Constants;
import com.carsguide.rest.CarsProcessor;

public class CarsGuideService extends IntentService {

    public static final int REQUEST_TYPE_CARS_DATA = 1;

    public CarsGuideService() {
        super("CarsGuideService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int resourceType = intent.getIntExtra(Constants.EXTRAS_REQUEST_TYPE, -1);

        switch (resourceType) {
            case REQUEST_TYPE_CARS_DATA:
                CarsProcessor.getCarsData(intent.getExtras());
                break;
        }
    }


}
