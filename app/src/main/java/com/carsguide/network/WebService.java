package com.carsguide.network;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface WebService {

    @GET("/api/feed/?mode=json&q=make")
    void getCarsData(@Query("start") int start, @Query("num") int num, Callback<ResponseModel> callback);
}
