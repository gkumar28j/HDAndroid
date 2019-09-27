package com.mcn.honeydew.ui.main;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.mcn.honeydew.BuildConfig;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.ApiCall;
import com.mcn.honeydew.data.network.ApiHeader;
import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.data.network.model.request.UpdateDeviceInfoRequest;
import com.mcn.honeydew.data.network.model.response.GetBluetoothItemsListResponse;
import com.mcn.honeydew.data.network.model.response.NotificationCountResponse;
import com.mcn.honeydew.data.network.model.response.UpdateDeviceInfoResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.AppConstants;
import com.mcn.honeydew.utils.CommonUtils;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimeZone;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by amit on 16/2/18.
 */

public class MainPresenter<V extends MainMvpView> extends BasePresenter<V> implements MainMvpPresenter<V> {

    private static final String TAG = MainPresenter.class.getSimpleName();
    private final String RANGE_1_8_MILE = "1/8 mile";
    private final String RANGE_1_4_MILE = "1/4 mile";
    private final String RANGE_1_2_MILE = "1/2 mile";
    private final String RANGE_3_4_MILE = "3/4 mile";
    private final String RANGE_1_MILE = "1 mile";
    private final double DEFAULT_RANGE = 1609.34;


    @Inject
    public MainPresenter(DataManager dataManager,
                         SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public void saveSelectedList(MyHomeListData data) {
        getDataManager().saveList(data);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewPrepared(Context ctx) {

        if (!getMvpView().isNetworkConnected()) {
      //      getMvpView().showMessage(R.string.connection_error);
            return;
        }


       // if (!getDataManager().IsDeviceIdSendToServer() && getDataManager().getDeviceId() != null) {
        if (getDataManager().getDeviceId() != null) {

            getDataManager().doUpdateDeviceInfo(
                    new UpdateDeviceInfoRequest(
                            0.0,
                            0.0,
                            String.valueOf(BuildConfig.VERSION_NAME),
                            ApiCall.API_VERSION, BuildConfig.VERSION_NAME,
                            getDataManager().getDeviceId(),
                            AppConstants.DEVICE_TYPE,
                            CommonUtils.getOffsetTimeZone(),
                            CommonUtils.getOffsetTimeZone(),
                            CommonUtils.getTimeZoneOffsetName()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<UpdateDeviceInfoResponse>() {
                        @Override
                        public void accept(UpdateDeviceInfoResponse response) throws Exception {
                            if (!isViewAttached()) {
                                return;
                            }
                            getDataManager().sendDeviceIdToServer(true);


                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (!isViewAttached()) {
                                return;
                            }
                            handleApiError(throwable);
                        }
                    });
        }

    }


    @Override
    public void checkLoginSession() {

        if (getDataManager().getCurrentUserLoggedInMode()
                == DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
            getMvpView().openLoginActivity();
        } else {
            getMvpView().onResumeSuccess(getDataManager().isJustLoggedIn());
        }

    }

    @Override
    public boolean isIsProximityNotification() {
        if (getDataManager().getCurrentUserLoggedInMode()
                == DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER.getType()
                && getDataManager().getUserData().isIsProximityNotification()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void checkBluetoothConnectivity() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()
                && mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED) {
            Timber.d("Device is pared with Bluetooth");

            getMvpView().onBluetoothFoundConnected();
        } else {
            Timber.d("Device is not pared with Bluetooth");
        }

    }

    @Override
    public void checkAndCallBluetoothApi() {
        // Check app setting for bluetooth notification
        if (getDataManager().getUserData() != null && getDataManager().getUserData().isIsBluetoothNotification()) {
            new SendNotification().execute(String.valueOf(1));
        }

    }

    @SuppressLint("CheckResult")
    @Override
    public void fetchBluetoothItems() {
        if (!getMvpView().isNetworkConnected()) {
            return;
        }

        getDataManager().doGetAllBluetoothItems()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<GetBluetoothItemsListResponse>() {
                    @Override
                    public void accept(GetBluetoothItemsListResponse response) throws Exception {
                        ArrayList<GetBluetoothItemsListResponse.BluetoothItem> newItems = new ArrayList<>(Arrays.asList(response.getResult()));
                        ArrayList<GetBluetoothItemsListResponse.BluetoothItem> savedItems = getDataManager().getSavedBluetoothItems();
                        for (int i = 0; (i < newItems.size()); i++) {
                            GetBluetoothItemsListResponse.BluetoothItem newItem = newItems.get(i);
                            // if an item of the api response is not available in saved list them adding that in saved item and
                            // saving updated saved item in shared pref.
                            if (savedItems.contains(newItem))
                                newItem.setSent(true);
                        }
                        getDataManager().saveBluetoothItemList(new Gson().toJson(newItems));

                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void fetchNotificationCount() {

        if (!getMvpView().isNetworkConnected()) {
         //   getMvpView().showMessage(R.string.connection_error);
            return;
        }

        getDataManager().doGetNotificationCount()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<NotificationCountResponse>() {
                    @Override
                    public void accept(NotificationCountResponse response) throws Exception {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onNotificationFetched(response.getSystemNotificationCount());

                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        handleApiError(throwable);
                    }
                });


    }

    @Override
    public void saveInProgressValue(boolean isProgress) {

        getDataManager().setInProgressValue(isProgress);

    }

    private class SendNotification extends AsyncTask<String, Void, Boolean> {
        String url = AppConstants.BASE_URL + "v1_2/api/Account/BluetoothConnectDisconnet";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {

                HttpPost httppost = new HttpPost(url);
                HttpClient httpclient = new DefaultHttpClient();
                httpclient.getParams().setParameter(
                        CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

                httppost.addHeader(ApiHeader.HEADER_PARAM_API_KEY, getDataManager().getApiHeader().getmApiKey());
                httppost.addHeader(ApiHeader.HEADER_PARAM_AUTHRIZATION, getDataManager().getApiHeader().getmTokenType() + " " + getDataManager().getApiHeader().getmAccessToken());
                httppost.addHeader(ApiHeader.HEADER_PARAM_REFRESH_TOKEN, getDataManager().getApiHeader().getmRefreshToken());


                JSONObject json = new JSONObject();
                json.put("IsConnected", Integer.parseInt(params[0]));
                StringEntity se = new StringEntity(json.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httppost.setEntity(se);

                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    Timber.d("Bluetooth API response: " + data);

                    return true;
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {

                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

        }
    }
}

