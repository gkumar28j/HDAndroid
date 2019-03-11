package com.mcn.honeydew.ui.base;

import com.mcn.honeydew.data.network.model.ErrorObject;

/**
 * Created by amit on 15/2/18.
 */

public interface MvpPresenter<V extends MvpView> {

    void onAttach(V mvpView);

    void onDetach();

    void handleApiError(ErrorObject errorObject);

    void handleApiError(Throwable throwable);

    void setUserAsLoggedOut();
}
