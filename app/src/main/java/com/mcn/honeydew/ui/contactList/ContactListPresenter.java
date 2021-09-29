package com.mcn.honeydew.ui.contactList;

import android.annotation.SuppressLint;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.SelectedContact;
import com.mcn.honeydew.data.network.model.request.ShareListRequest;
import com.mcn.honeydew.data.network.model.response.ShareListResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import java.util.List;

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
    private int listSize = 0;
    @Inject
    public ContactListPresenter(DataManager dataManager,
                                SchedulerProvider schedulerProvider,
                                CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void shareListToContact(int listId, List<SelectedContact> selectedContacts) {
        getMvpView().showLoading();
        listSize = selectedContacts.size();
        for (int i = 0; i < selectedContacts.size(); i++) {
            shareContact(listId, selectedContacts.get(i), i);
        }


    }


    @SuppressLint("CheckResult")
    private void shareContact(final int listId, SelectedContact contact, final int position) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        ShareListRequest request = new ShareListRequest();
        request.setListId(listId);
        request.setEmailorPhoneNumber(contact.getEmailorPhoneNumber());
        request.setLabelText(contact.getLabelText());

        getDataManager().doShareListCall(request)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<ShareListResponse>() {
                    @Override
                    public void accept(ShareListResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        String successMessage = null;
                        final int status = response.getResult().getStatus();
                        switch (status) {

                            case 1:
                                successMessage = response.getResult().getMessage();
                                getMvpView().onListSharedSuccess(position);
                                break;

                            case 2:
                            case 3:
                            case 0:
                                String errorMessage = response.getResult().getMessage();
                                getMvpView().onListSharedFailure(position, errorMessage);
                                break;

                            default:
                                break;
                        }

                        if (listSize == position + 1) {
                            getMvpView().onSharingFinished(successMessage);
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
}
