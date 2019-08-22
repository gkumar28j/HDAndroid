package com.mcn.honeydew.di.component;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.di.module.ActivityModule;
import com.mcn.honeydew.ui.addItems.AddItemsFragment;
import com.mcn.honeydew.ui.addItems.addItemsRecentChild.AddRecentItemsChildFragment;
import com.mcn.honeydew.ui.addItems.addItemsWhen.AddItemsWhenFragment;
import com.mcn.honeydew.ui.addItems.addItemsWhere.AddItemsWhereFragment;
import com.mcn.honeydew.ui.addlist.AddListFragment;
import com.mcn.honeydew.ui.changePassword.ChangePasswordActivity;
import com.mcn.honeydew.ui.colorSettings.ColorSettingsFragment;
import com.mcn.honeydew.ui.contactDetails.ContactDetailsActivity;
import com.mcn.honeydew.ui.contactList.ContactListActivity;
import com.mcn.honeydew.ui.dummy.DummyFragment;
import com.mcn.honeydew.ui.forgotPassword.ForgotPasswordActivity;
import com.mcn.honeydew.ui.forgotPassword.locateAccountFragment.LocateAccountFragment;
import com.mcn.honeydew.ui.forgotPassword.resetMethodFragment.ResetMethodFragment;
import com.mcn.honeydew.ui.forgotPassword.resetPasswordFragment.ResetPasswordFragment;
import com.mcn.honeydew.ui.forgotPassword.verifyOtpFragment.VerifyOtpFragment;
import com.mcn.honeydew.ui.home.HomeListFragment;
import com.mcn.honeydew.ui.in_progress.InProgressFragment;
import com.mcn.honeydew.ui.list_settings.ListSettingsFragment;
import com.mcn.honeydew.ui.login.LoginActivity;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.ui.myList.MyListFragment;
import com.mcn.honeydew.ui.notifications.NotificationsFragment;
import com.mcn.honeydew.ui.notifications.settings.NotificationSettingsActivity;
import com.mcn.honeydew.ui.phoneVerification.PhoneVerificationActivity;
import com.mcn.honeydew.ui.register.RegisterActivity;
import com.mcn.honeydew.ui.settings.SettingsFragment;
import com.mcn.honeydew.ui.settings.editEmail.EditEmailDialog;
import com.mcn.honeydew.ui.settings.editname.EditNameDialog;
import com.mcn.honeydew.ui.shareToContacts.ShareToContactsActivity;
import com.mcn.honeydew.ui.sharelist.ShareListFragment;
import com.mcn.honeydew.ui.splash.SplashActivity;

import dagger.Component;

/**
 * Created by amit on 14/2/18.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(SplashActivity activity);

    void inject(LoginActivity activity);

    void inject(RegisterActivity activity);

    void inject(PhoneVerificationActivity activity);

    void inject(SettingsFragment fragment);

    void inject(NotificationsFragment fragment);

    void inject(AddListFragment fragment);

    void inject(HomeListFragment fragment);

    void inject(EditNameDialog dialog);

    /* Forgot Password */
    void inject(ForgotPasswordActivity activity);

    void inject(LocateAccountFragment fragment);

    void inject(ResetMethodFragment fragment);

    void inject(VerifyOtpFragment fragment);

    void inject(MyListFragment fragment);

    void inject(ResetPasswordFragment fragment);

    // Change Password Activity
    void inject(ChangePasswordActivity activity);

    void inject(NotificationSettingsActivity activity);

    void inject(AddItemsFragment fragment);

    void inject(DummyFragment fragment);

    void inject(ColorSettingsFragment fragment);


    void inject(AddRecentItemsChildFragment fragment);

    void inject(AddItemsWhenFragment fragment);

    void inject(AddItemsWhereFragment fragment);

    void inject(ShareListFragment fragment);

    void inject(ListSettingsFragment fragment);

    // Contact List Activity
    void inject(ContactListActivity activity);

    // Contact Detail Activity
    void inject(ContactDetailsActivity activity);

    void inject(ShareToContactsActivity activity);

    void inject(InProgressFragment fragment);

    void inject(EditEmailDialog dialog);

}
