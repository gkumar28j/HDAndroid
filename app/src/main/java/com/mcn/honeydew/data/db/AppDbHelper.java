package com.mcn.honeydew.data.db;


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

}
