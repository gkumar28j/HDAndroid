

package com.mcn.honeydew.utils;

/**
 * Created by amit on 14/2/18.
 */

public final class AppConstants {

    //Database & Pref
    public static final String DB_NAME = "honeydew_list.db";
    public static final String PREF_NAME = "honeydew_list_pref";

    //Api for Client Development/Stagging
    public static final String BASE_URL = "http://hdapi.azurewebsites.net/";
    public static final String API_KEY = "HDKey:205,237,243,32,197,104,71,175,156,112,192,251,247,193,85,240,64,164,6,162,22,101,166,52,37," +
            "197,84,15,88,10,134,139,253,135,170,224,170,98,78,32,36,173,183,75,247,240,21,166,134,43,224,210,175,242,194,238,0,254,233,71," +
            "253,29,254,105,120,0,106,211,154,55,148,137,58,96,98,214,81,73,85,169,41,13,106,35,59,82,246,32,68,100,122,76,198,134,36,157," +
            "207,133,13,223,53,108,242,199,188,140,224,60,33,144,218,37,225,70,174,37,255,251,205,223,176,57,189,85,212,44,124,237";

    //Api for Live
   /* public static final String BASE_URL = "http://hdservices.azurewebsites.net/";
    public static final String API_KEY = "HDKey:205,237,243,32,197,104,71,175,156,112,192,251,247,193,85,240,64,164,6,162,22,101,166,52,37," +
            "197,84,15,88,10,134,139,253,135,170,224,170,98,78,32,36,173,183,75,247,240,21,166,134,43,224,210,175,242,194,238,0,254,233,71," +
            "253,29,254,105,120,0,106,211,154,55,148,137,58,96,98,214,81,73,85,169,41,13,106,35,59,82,246,32,68,100,122,76,198,134,36,157," +
            "207,133,13,223,53,108,242,199,188,140,224,60,33,144,218,37,225,70,174,37,255,251,205,223,176,57,189,85,212,44,124,237";*/
   //Api for Local
   /* public static final String BASE_URL = "http://honeydew.azurewebsites.net/";
    public static final String API_KEY = "HDKey:205,237,243,32,197,104,71,175,156,112,192,251,247,193,85,240,64,164,6,162,22,101,166,52,37," +
            "197,84,15,88,10,134,139,253,135,170,224,170,98,78,32,36,173,183,75,247,240,21,166,134,43,224,210,175,242,194,238,0,254,233,71," +
            "253,29,254,105,120,0,106,211,154,55,148,137,58,96,98,214,81,73,85,169,41,13,106,35,59,82,246,32,68,100,122,76,198,134,36,157," +
            "207,133,13,223,53,108,242,199,188,140,224,60,33,144,218,37,225,70,174,37,255,251,205,223,176,57,189,85,212,44,124,237";*/


    public static final String STATUS_CODE_SUCCESS = "success";
    public static final String STATUS_CODE_FAILED = "failed";
    public static final int API_STATUS_CODE_LOCAL_ERROR = 0;

    public static final long NULL_INDEX = -1L;
    public static final String DEVICE_TYPE = "Android";
    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    public static final int DB_VERSION = 1;
    public static final int MIN_LENGTH_PASSWORD = 6;

    public static final int REQUEST_CODE_SIGN_UP = 1001;
    public static final int REQUEST_CODE_FORGOT_PASSWORD = 1002;

    public static final String KEY_EMAIL_OR_PHONE = "email_or_phone";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_COUNTRY_CODE = "country_code";
    public static final String KEY_PHONE = "phone";

    public static final String KEY_FRAGMENT_DATA = "fragment_data";
    public static String PASSWORD_NUM_REGX = "[0-9]+";
    public static String PASSWORD_CHAR_REGX = "[A-Za-z]+";


    public static final String KEY_LIST_ID = "listid";
    public static final String KEY_LIST_NAME = "listName";

    public static final String KEY_MY_LIST_DATA = "mylistdata";
    public static final int GOOGLE_NEARBY_SEARCH_RADIUS = 1000;

    public static final String PLACE_KEY = "AIzaSyBTswnWoSz48BFB6IT7cTUqcIAaWLULCow"; // key created on 9 march 2018

    public static final String ACTION_ITEM_COMPLETE = "com.mcn.honeydew.ACTION_ITEM_COMPLETE";

    public static final String ACTION_REFRESH_HOME = "com.mcn.honeydew.ACTION_HOME_REFRESH";
    public static final String ACTION_REFRESH_NOTIF_COUNT = "com.mcn.honeydew.ACTION_NOTIFICATION_COUNT_REFRESH";
    public static final String ACTION_REFRESH_MY_LIST = "com.mcn.honeydew.ACTION_LIST_REFRESH";


    private AppConstants() {
        // This utility class is not publicly instantiable
    }


}
