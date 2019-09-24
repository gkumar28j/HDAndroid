package com.mcn.honeydew.ui.phoneVerification;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.Countries;
import com.mcn.honeydew.data.network.model.PhoneVerificationRequest;
import com.mcn.honeydew.data.network.model.PhoneVerificationResponse;
import com.mcn.honeydew.data.network.model.UpdateVerificationStatusRequest;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;


/**
 * Created by amit on 16/2/18.
 */

public class PhoneVerificationPresenter<V extends PhoneVerificationMvpView> extends BasePresenter<V> implements PhoneVerificationMvpPresenter<V> {

    private static final String TAG = PhoneVerificationPresenter.class.getSimpleName();

    @Inject
    public PhoneVerificationPresenter(DataManager dataManager,
                                      SchedulerProvider schedulerProvider,
                                      CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public void fetchCountries() {
        String countryJson = getDataManager().getCountryListFromAsset();
        if (!TextUtils.isEmpty(countryJson)) {
            Countries[] countries = new Gson().fromJson(countryJson, Countries[].class);
            List<Countries> countryList = new ArrayList<>(Arrays.asList(countries));
            getMvpView().onCountriesFetched(countryList);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void getVerificationCode(final int callType, String phoneNumber, String countryCode) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        if (TextUtils.isEmpty(countryCode)) {
            getMvpView().onError(R.string.error_empty_country_code);
            return;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            getMvpView().onError(R.string.error_empty_phone);
            return;
        }
        if (phoneNumber.length() != 10) {
            getMvpView().onError(R.string.error_invalid_phone);
            return;
        }

        getMvpView().showLoading();

        PhoneVerificationRequest request = new PhoneVerificationRequest(callType, countryCode + "-" + phoneNumber);

        getDataManager().doPhoneVerificationApiCall(request)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<PhoneVerificationResponse>() {
                    @Override
                    public void accept(PhoneVerificationResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();

                        if (response.getStatus() == 1) {
                            getMvpView().onVerificationCodeSent(response.getVerificationCode(), response.isVerificationStatus());

                            // Showing toast / message in case of text message only.
                            if (callType == 1)
                                getMvpView().showMessage(response.getMessage());
                        } else {
                            getMvpView().onError(response.getMessage());
                        }


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        // handle the login error here
                        handleApiError(throwable);

                    }
                });

    }

    @SuppressLint("CheckResult")
    @Override
    public void updateVerificationStatus(int status, String phoneNumber, String countryCode, boolean isComingFromSettings) {

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        getMvpView().showLoading();
        final String mobileNo = countryCode + "-" + phoneNumber;

        UpdateVerificationStatusRequest request = new UpdateVerificationStatusRequest(status);

        getDataManager().doUpdateVerificationApiCall(request)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<PhoneVerificationResponse>() {
                    @Override
                    public void accept(PhoneVerificationResponse response) throws Exception {

                        getMvpView().hideLoading();

                        if (response.getStatus() == 1) {

                            UserDetailResponse data = getDataManager().getUserData();
                            if (data.getPrimaryEmail().equals(data.getPrimaryMobile())) {
                                data.setPrimaryEmail(mobileNo);
                                data.setPrimaryMobile(mobileNo);
                            } else {
                                data.setPrimaryMobile(mobileNo);
                            }
                            data.setIsPhoneVerified(1);
                            getDataManager().setUserData(data);

                            if(!isComingFromSettings){
                                getDataManager().setFirstTimeLoggedIn(true);
                            }else {
                                getDataManager().setFirstTimeLoggedIn(false);
                            }


                            getMvpView().openMainActivity(getDataManager().isFirstTimeLoggedIn());
                            enableFCM();

                        } else {
                            // TODO Need to handle else part
                        }


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getMvpView().hideLoading();
                        // handle the login error here
                        handleApiError(throwable);

                    }
                });
    }

    private void enableFCM() {
        // Enable FCM via enable Auto-init service which generate new token and receive in FCMService
        //FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        FirebaseInstanceId.getInstance().getToken();
    }
}
