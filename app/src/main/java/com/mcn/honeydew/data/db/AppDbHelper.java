package com.mcn.honeydew.data.db;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.mcn.honeydew.data.db.tables.MyListItems;
import com.mcn.honeydew.data.network.model.response.MyListResponseData;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * Created by amit on 14/2/18.
 */

@Singleton
public class AppDbHelper implements DbHelper {
    private final DbOpenHelper mdbOpenHelper;

    @Inject
    public AppDbHelper(DbOpenHelper dbOpenHelper) {
        mdbOpenHelper = dbOpenHelper;
    }


    @Override
    public void insertListData(int listId, String response) {

        SQLiteDatabase db = mdbOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MyListItems.COLUMN_LIST_ID, listId);
        contentValues.put(MyListItems.COLUMN_LIST_RESPONSE, response);

        if(getListData(listId).size()>0){


           int row =  db.update(MyListItems.TABLE_NAME,contentValues,"list_id =" + listId,null);
            Log.e("row_update", String.valueOf(row));

        }else {

            long rowId = db.insert(MyListItems.TABLE_NAME, null, contentValues);
            Log.e("rowid_insert", String.valueOf(rowId));

        }

        db.close();

    }

    @Override
    public ArrayList<MyListResponseData> getListData(int listId) {
        ArrayList<MyListResponseData> responseList = new ArrayList<>();
        SQLiteDatabase db = mdbOpenHelper.getWritableDatabase();

        String rawQuery = " SELECT * FROM " + MyListItems.TABLE_NAME + " WHERE list_id = '" + listId + "'";
        Cursor cursor = db.rawQuery(rawQuery, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {

                    String response = cursor.getString(cursor.getColumnIndexOrThrow(MyListItems.COLUMN_LIST_RESPONSE));

                    responseList.addAll(Arrays.asList(new Gson().fromJson(response, MyListResponseData[].class)));

                } while (cursor.moveToNext());
            }


        }

        return responseList;
    }
}
