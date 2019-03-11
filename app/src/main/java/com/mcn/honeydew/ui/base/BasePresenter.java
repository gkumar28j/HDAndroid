package com.mcn.honeydew.ui.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.ApiError;
import com.mcn.honeydew.data.network.model.ErrorObject;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.HttpException;

/**
 * Created by amit on 15/2/18.
 */

public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private static final String TAG = "BasePresenter";

    private final DataManager mDataManager;
    private final SchedulerProvider mSchedulerProvider;
    private final CompositeDisposable mCompositeDisposable;

    private V mMvpView;

    @Inject
    public BasePresenter(DataManager dataManager,
                         SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
        this.mDataManager = dataManager;
        this.mSchedulerProvider = schedulerProvider;
        this.mCompositeDisposable = compositeDisposable;
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mCompositeDisposable.dispose();
        mMvpView = null;
    }


    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    @Override
    public void handleApiError(ErrorObject errorObject) {

    }

    @Override
    public void handleApiError(Throwable throwable) {
        if(!isViewAttached()) return;
        try {

            if (!(throwable instanceof HttpException)) {
                getMvpView().onError(R.string.api_default_error);
                return;
            }

            HttpException httpException = (HttpException) throwable;

            final GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
            final Gson gson = builder.create();

            ApiError apiError = gson.fromJson(httpException.response().errorBody().string(), ApiError.class);
            if (apiError == null) {
                getMvpView().onError(R.string.api_default_error);
                return;
            }
            if (apiError.getError() == null && apiError.getMessage() == null) {
                getMvpView().onError(R.string.api_default_error);
                return;
            }
            switch (httpException.code()) {
                case HttpsURLConnection.HTTP_UNAUTHORIZED:
                    getDataManager().setUserAsLoggedOut();
                    getMvpView().openActivityOnTokenExpire();
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                case HttpURLConnection.HTTP_NOT_FOUND:
                default:
                    if (apiError.getMessage() != null)
                        getMvpView().onError(apiError.getMessage());
                    else if (apiError.getError() != null)
                        getMvpView().onError(apiError.getError());
            }
        } catch (IOException | JsonSyntaxException | NullPointerException e) {
            e.printStackTrace();
            if(!isViewAttached()) return;
            getMvpView().onError(R.string.api_default_error);
        }
    }

    @Override
    public void setUserAsLoggedOut() {
        getDataManager().setAccessToken(null);
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.onAttach(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}
