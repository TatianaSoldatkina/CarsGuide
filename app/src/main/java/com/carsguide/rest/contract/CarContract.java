package com.carsguide.rest.contract;

import android.net.Uri;

public class CarContract extends BaseContract {

    public static final String TABLE_NAME = "cars";
    public static final String CAR_IMAGE_URL = "car_image_url";
    public static final String CAR_URL = "car_url";
    public static final String CAR_TITLE = "car_title";
    public static final String IS_FAVORITE = "is_favorite";

    public static final String AUTHORITY = "com.carsguide.rest.CarsProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
}