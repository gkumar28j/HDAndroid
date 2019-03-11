package com.mcn.honeydew.ui.colorSettings;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.request.UpdateHeaderColorRequest;
import com.mcn.honeydew.data.network.model.response.UpdateHeaderColorResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by amit on 16/2/18.
 */

public class ColorSettingsPresenter<V extends ColorSettingsMvpView> extends BasePresenter<V> implements ColorSettingsMvpPresenter<V> {

    private static final String TAG = ColorSettingsPresenter.class.getSimpleName();

    @Inject
    public ColorSettingsPresenter(DataManager dataManager,
                                  SchedulerProvider schedulerProvider,
                                  CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }




    @Override
    public void updateListColor(final String colorCode, int listId) {
        //getMvpView().showLoading();
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        UpdateHeaderColorRequest request = new UpdateHeaderColorRequest();
        request.setListId(listId);
        request.setListHeaderColor(colorCode);
        getDataManager().doUpdateHeaderColorCall(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UpdateHeaderColorResponse>() {
                    @Override
                    public void accept(UpdateHeaderColorResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();

                        if (response.getResult().getStatus() == 1) {
                            getDataManager().setSelectedColor(colorCode);
                        }


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
