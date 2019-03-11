package com.mcn.honeydew.ui.register;

import com.mcn.honeydew.data.network.model.Countries;
import com.mcn.honeydew.ui.base.MvpView;

import java.util.List;

/**
 * Created by amit on 16/2/18.
 */

public interface RegisterMvpView extends MvpView {

    void onRegistrationSuccess();

    void onCountriesFetched(List<Countries> countries);

}
