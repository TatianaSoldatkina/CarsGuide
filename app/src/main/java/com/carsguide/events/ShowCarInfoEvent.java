package com.carsguide.events;

public class ShowCarInfoEvent {

    private String carInfoUrl;

    public ShowCarInfoEvent(String carInfoUrl) {
        this.carInfoUrl = carInfoUrl;
    }

    public String getCarInfoUrl() {
        return carInfoUrl;
    }

    public void setCarInfoUrl(String carInfoUrl) {
        this.carInfoUrl = carInfoUrl;
    }
}
