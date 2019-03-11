package com.mcn.honeydew.ui.contactList;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.request.ShareListRequest;
import com.mcn.honeydew.data.network.model.response.ShareListResponse;
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

public class ContactListPresenter<V extends ContactListMvpView> extends BasePresenter<V> implements ContactListMvpPresenter<V> {

    private static final String TAG = ContactListPresenter.class.getSimpleName();

    @Inject
    public ContactListPresenter(DataManager dataManager,
                                SchedulerProvider schedulerProvider,
                                CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void doShareList(int listId, String emailOrPhone, String contactName) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        //getMvpView().showLoading();

        ShareListRequest request = new ShareListRequest();
        request.setListId(listId);
        request.setEmailorPhoneNumber(emailOrPhone);
        request.setLabelText(contactName);


        getDataManager().doShareListCall(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ShareListResponse>() {
                    @Override
                    public void accept(ShareListResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        // getMvpView().hideLoading();

                        if (response.getResult().getStatus() == 1) {

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
