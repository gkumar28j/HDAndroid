package com.mcn.honeydew.ui.forgotPassword.locateAccountFragment;

import com.mcn.honeydew.data.network.model.response.LocateAccountResponse;
import com.mcn.honeydew.ui.base.MvpView;


/**
 * Created by amit on 16/2/18.
 **/

public interface LocateAccountMvpView extends MvpView {
    void onLocateAccountSuccess(LocateAccountResponse.Detail detail);
}
