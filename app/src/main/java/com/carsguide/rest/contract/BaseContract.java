package com.carsguide.rest.contract;

public class BaseContract {

    public static final String ID = "_id";

    public static final String SYNC_STATUS = "sync_status";

    protected static String getFullColumnName(String tableName, String columnName) {
        return tableName + "." + columnName;
    }

}