package com.mcn.honeydew;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.ApiHeader;
import com.mcn.honeydew.data.network.model.response.GetBluetoothItemsListResponse;
import com.mcn.honeydew.utils.AppConstants;
import com.mcn.honeydew.utils.NetworkUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import timber.log.Timber;

import static com.facebook.FacebookSdk.getApplicationContext;

public class HoneyDewAppLifecycleObserver implements LifecycleObserver {
    private Context context;
    private DataManager dataManager;

    @Inject
    public HoneyDewAppLifecycleObserver(Context context) {
        this.context = context;
        dataManager = HoneyDewApp.get(context).getDataManager();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onEnterForeground() {
       /* Timber.log(Log.ERROR, "On Start App");
        Toast.makeText(context, "On Start App", Toast.LENGTH_SHORT).show();*/
        /* stopCartAlarmManager();
         stopWalletAlarmManager();*/
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onEnterBackground() {
        Timber.log(Log.ERROR, "On Stop App");

        // TODO: Need to check "user session", "Internet connectivity", and "Bluetooth setting enabled", device connected/disconnected
        if (NetworkUtils.isNetworkConnected(getApplicationContext()) && !(dataManager.getCurrentUserLoggedInMode()
                == DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType())) {
            new GetBluetoothItems().execute();
        }

    }

    private class GetBluetoothItems extends AsyncTask<String, Void, Boolean> {
        String url = AppConstants.BASE_URL + "v1_2/api/Notification/GetAllPushNotification";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {

                Timber.log(Log.DEBUG, "GetAllPushNotification: URL " + url);
                HttpGet httpGet = new HttpGet(url);
                HttpClient httpclient = new DefaultHttpClient();
                httpclient.getParams().setParameter(
                        CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

                httpGet.addHeader(ApiHeader.HEADER_PARAM_API_KEY, dataManager.getApiHeader().getmApiKey());
                httpGet.addHeader(ApiHeader.HEADER_PARAM_AUTHRIZATION, dataManager.getApiHeader().getmTokenType() + " " + dataManager.getApiHeader().getmAccessToken());
                httpGet.addHeader(ApiHeader.HEADER_PARAM_REFRESH_TOKEN, dataManager.getApiHeader().getmRefreshToken());


                /*JSONObject json = new JSONObject();
                json.put("IsConnected", Integer.parseInt(params[0]));
                StringEntity se = new StringEntity(json.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httppost.setEntity(se);*/

                HttpResponse response = httpclient.execute(httpGet);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    GetBluetoothItemsListResponse items = new Gson().fromJson(data, GetBluetoothItemsListResponse.class);
                    ArrayList<GetBluetoothItemsListResponse.BluetoothItem> newItems = new ArrayList<>(Arrays.asList(items.getResult()));
                    if (!TextUtils.isEmpty(data)) {

                        // getting saved items
                        ArrayList<GetBluetoothItemsListResponse.BluetoothItem> savedItems = dataManager.getSavedBluetoothItems();
                        for (int i = 0; (i < items.getResult().length); i++) {
                            GetBluetoothItemsListResponse.BluetoothItem newItem = newItems.get(i);
                            // if an item of the api response is not available in saved list them adding that in saved item and
                            // saving updated saved item in shared pref.
                            if (savedItems.contains(newItem))
                                newItem.setSent(true);
                        }
                        dataManager.saveBluetoothItemList(new Gson().toJson(newItems));
                    }

                    Timber.d("Get Bluetooth ITEM API response: " + data);

                    return true;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

        }
    }
}
