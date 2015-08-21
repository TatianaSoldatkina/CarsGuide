package com.carsguide.rest;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.carsguide.CarsGuideApplication;
import com.carsguide.Constants;
import com.carsguide.rest.contract.CarContract;
import com.carsguide.helper.ImageDownloader;
import com.carsguide.model.Car;
import com.carsguide.network.WebService;
import com.carsguide.network.ResponseModel;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CarsProcessor {

    private static final String BASE_URL = "http://buyersguide.caranddriver.com";

    private static WebService getCarsQuideService() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .build();

        return restAdapter.create(WebService.class);
    }

    public static void getCarsData(Bundle bundle) {
        int start = bundle.getInt(Constants.EXTRAS_PAGE_START);
        int num = bundle.getInt(Constants.EXTRAS_PER_PAGE);
        getCarsQuideService().getCarsData(start, num, new Callback<ResponseModel>() {
            @Override
            public void success(ResponseModel responseModel, Response response) {
                SharedPreferences sharedPreferences = CarsGuideApplication.getAppContext().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                sharedPreferences.edit().putInt(Constants.SHARED_PREFERENCES_CARS_COUNT, responseModel.getCount()).commit();

                updateContentProvider(responseModel.getCars());

                for (Car car : responseModel.getCars()) {
                    String imageUrl = car.getMakeUrl();
                    ImageDownloader.getInstance().downloadImage(CarsGuideApplication.getAppContext(),
                            imageUrl);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(CarsGuideApplication.getAppContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void updateContentProvider(List<Car> cars) {
        Context context = CarsGuideApplication.getAppContext();
        if (cars != null) {
            for (Car car : cars) {
                ContentValues carsValues = new ContentValues();
                carsValues.put(CarContract.ID, car.getId());
                carsValues.put(CarContract.CAR_TITLE, car.getTitle());
                carsValues.put(CarContract.CAR_IMAGE_URL, car.getMakeUrl());
                carsValues.put(CarContract.CAR_URL, car.getUrl());

                Cursor carCursor = context.getContentResolver().query(ContentUris.withAppendedId(CarContract.CONTENT_URI, car.getId()),
                        null, null, null, null);

                if (carCursor.moveToFirst()) {
                    long id = carCursor.getLong(carCursor.getColumnIndexOrThrow(CarContract.ID));
                    context.getContentResolver().update(ContentUris.withAppendedId(CarContract.CONTENT_URI, id), carsValues,
                            null, null);
                } else {
                    carsValues.put(CarContract.IS_FAVORITE, 0);
                    context.getContentResolver().insert(CarContract.CONTENT_URI, carsValues);
                }

                carCursor.close();
            }
        }
    }

    public static void setCarFavorite(int carId, boolean isFavorite) {
        Context context = CarsGuideApplication.getAppContext();

        ContentValues carsValues = new ContentValues();
        carsValues.put(CarContract.IS_FAVORITE, isFavorite ? 1 : 0);

        Cursor carCursor = context.getContentResolver().query(ContentUris.withAppendedId(CarContract.CONTENT_URI, carId),
                null, null, null, null);

        if (carCursor.moveToFirst()) {
            context.getContentResolver().update(ContentUris.withAppendedId(CarContract.CONTENT_URI, carId), carsValues,
                    null, null);
        }

        carCursor.close();
    }
}
