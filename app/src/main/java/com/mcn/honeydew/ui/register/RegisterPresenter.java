package com.mcn.honeydew.ui.register;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.Countries;
import com.mcn.honeydew.data.network.model.RegistrationRequest;
import com.mcn.honeydew.data.network.model.RegistrationResponse;
import com.mcn.honeydew.data.network.model.UserModel;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by amit on 16/2/18.
 */

public class RegisterPresenter<V extends RegisterMvpView> extends BasePresenter<V> implements RegisterMvpPresenter<V> {

    private static final String TAG = RegisterPresenter.class.getSimpleName();

    @Inject
    public RegisterPresenter(DataManager dataManager,
                             SchedulerProvider schedulerProvider,
                             CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onSignUpClicked(UserModel user) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        if (!user.isValidUser()) {
            getMvpView().onError(user.getMessageResourceId());
            return;
        }

        getMvpView().showLoading();


        getDataManager().doRegisterApiCall(new RegistrationRequest(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getDialCode() + "-" + user.getPhoneNumber(),
                String.valueOf(user.getDeviceVersion()),
                user.getApiVersion(),
                user.getAppVersion(),
                user.getDeviceId(),
                user.getDeviceType(),
                user.getProfilePhoto(),
                String.valueOf(user.getLatitude()),
                String.valueOf(user.getLongitude()),
                user.getOffsetTimeZone(),
                user.getTimeZoneOffsetName(),
                user.getTimeZone()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RegistrationResponse>() {
                    @Override
                    public void accept(RegistrationResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        if (response.getStatus() == 1) {

                            getMvpView().onRegistrationSuccess();

                        } else {
                            getMvpView().onError(response.getMessage());
                        }

                        getMvpView().hideLoading();

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

    @Override
    public void fetchCountries() {

        String countryJson = getDataManager().getCountryListFromAsset();
        if (!TextUtils.isEmpty(countryJson)) {
            Countries[] countries = new Gson().fromJson(countryJson, Countries[].class);
            List<Countries> countryList = new ArrayList<>(Arrays.asList(countries));
            getMvpView().onCountriesFetched(countryList);
        }
    }


}
