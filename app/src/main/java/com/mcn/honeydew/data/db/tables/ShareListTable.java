package com.mcn.honeydew.data.db.tables;

public class ShareListTable {

    public static final String TABLE_NAME = "share_list_item";
    public static final String COLUMN_LIST_ID = "share_list_id";
    public static final String COLUMN_LIST_RESPONSE = "share_list_response";


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + COLUMN_LIST_ID + " INTEGER " + " PRIMARY KEY,"
                    + COLUMN_LIST_RESPONSE + " TEXT " + " )";

}
