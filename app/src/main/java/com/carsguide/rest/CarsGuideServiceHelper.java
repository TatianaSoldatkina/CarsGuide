package com.carsguide.rest;

import android.content.Context;
import android.content.Intent;
import com.carsguide.CarsGuideApplication;
import com.carsguide.Constants;

import java.util.UUID;

public class CarsGuideServiceHelper {

    private static CarsGuideServiceHelper instance;

    private Context ctx;

    private CarsGuideServiceHelper() {
        this.ctx = CarsGuideApplication.getAppContext();
    }


    public static CarsGuideServiceHelper getInstance() {
        if (instance == null) {
            instance = new CarsGuideServiceHelper();
        }

        return instance;
    }

    public long getCars(int start, int perPage) {

        long requestId = generateRequestID();
        Intent intent = new Intent(this.ctx, CarsGuideService.class);
        intent.putExtra(Constants.EXTRAS_REQUEST_TYPE, CarsGuideService.REQUEST_TYPE_CARS_DATA);
        intent.putExtra(Constants.EXTRAS_PAGE_START, start);
        intent.putExtra(Constants.EXTRAS_PER_PAGE, perPage);
        intent.putExtra(Constants.EXTRAS_REQUEST_ID, requestId);

        this.ctx.startService(intent);

        return requestId;
    }

    private long generateRequestID() {
        long requestId = UUID.randomUUID().getLeastSignificantBits();
        return requestId;
    }
}
