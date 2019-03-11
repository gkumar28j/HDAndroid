package com.mcn.honeydew.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mcn.honeydew.di.ApplicationContext;
import com.mcn.honeydew.di.DatabaseInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by amit on 14/2/18.
 */

@Singleton
public class DbOpenHelper extends SQLiteOpenHelper {

    //USER TABLE
    public static final String USER_TABLE_NAME = "user";
    public static final String PROXIMITY_ITEMS_TABLE_NAME = "proximity_items";

    @Inject
    public DbOpenHelper(@ApplicationContext Context context,
                        @DatabaseInfo String dbName,
                        @DatabaseInfo Integer version) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       /* db.execSQL(ProximityItem.CREATE_TABLE);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       /* db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PROXIMITY_ITEMS_TABLE_NAME);
        onCreate(db);*/
    }


    private String getCurrentTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }
}
