
package com.mcn.honeydew.ui.splash;

import android.os.Handler;

import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class SplashPresenter<V extends SplashMvpView> extends BasePresenter<V> implements SplashMvpPresenter<V> {

    @Inject
    public SplashPresenter(DataManager dataManager,
                           SchedulerProvider schedulerProvider,
                           CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);

        int secondsDelayed = 2;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(!isViewAttached()){
                    return;
                }
                decideNextActivity();
            }
        }, secondsDelayed * 1000);

    }

    private void decideNextActivity() {
        if (getDataManager().getCurrentUserLoggedInMode()
                == DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
            if(!isViewAttached()){
                return;
            }
            getMvpView().openLoginActivity();
        } else if (getDataManager().getUserData().getIsPhoneVerified() != 1) {
            if(!isViewAttached()){
                return;
            }
            getMvpView().openPhoneVerificationActivity(getDataManager().getUserData().getPrimaryMobile());
        } else {
            if(!isViewAttached()){
                return;
            }
            getMvpView().openMainActivity();
        }
    }
}
