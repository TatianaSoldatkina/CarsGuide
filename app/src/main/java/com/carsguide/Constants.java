package com.carsguide;

import android.os.Environment;

public final class Constants {

    public static final String STORAGE_PATH_BASE_EXTERNAL = Environment.getExternalStorageDirectory() + "/carsguide/";
    public static final String STORAGE_PATH_IMAGES = STORAGE_PATH_BASE_EXTERNAL + "Uploaded Images/";

    public static final int CARS_PER_PAGE = 15;

    /* Extras */

    public static final String EXTRAS_REQUEST_TYPE = "extras_request_type";
    public static final String EXTRAS_REQUEST_ID = "extras_reauest_id";
    public static final String EXTRAS_PAGE_START = "extras_page_start";
    public static final String EXTRAS_PER_PAGE = "extras_per_page";
    public static final String EXTRAS_CAR_DETAILS_URL = "extras_car_details_url";

    /* SharedPreferences */

    public static final String SHARED_PREFERENCES_NAME = "cars_shared_preferences";
    public static final String SHARED_PREFERENCES_CARS_COUNT = "shared_preferences_cars_count";



}
