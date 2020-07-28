package com.mcn.honeydew.ui.verify_email.search_email;

import com.mcn.honeydew.data.network.model.response.LocateAccountResponse;
import com.mcn.honeydew.ui.base.MvpView;


/**
 * Created by amit on 16/2/18.
 **/

public interface SearchEmailMvpView extends MvpView {

    void onEmailVerifySuccess(String email);

}
