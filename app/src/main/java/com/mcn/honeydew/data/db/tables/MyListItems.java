package com.mcn.honeydew.data.db.tables;

public class MyListItems {

    public static final String TABLE_NAME = "list_item";
    public static final String COLUMN_LIST_ID = "list_id";
    public static final String COLUMN_LIST_RESPONSE = "list_response";


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + COLUMN_LIST_ID + " INTEGER " + " PRIMARY KEY,"
                    + COLUMN_LIST_RESPONSE + " TEXT " + " )";

}
