package com.mcn.honeydew.di.module;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mcn.honeydew.di.ActivityContext;
import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.addItems.AddItemsMvpPresenter;
import com.mcn.honeydew.ui.addItems.AddItemsMvpView;
import com.mcn.honeydew.ui.addItems.AddItemsPresenter;
import com.mcn.honeydew.ui.addItems.addItemsRecentChild.AddRecentItemsAdapter;
import com.mcn.honeydew.ui.addItems.addItemsRecentChild.AddRecentItemsMvpPresenter;
import com.mcn.honeydew.ui.addItems.addItemsRecentChild.AddRecentItemsMvpView;
import com.mcn.honeydew.ui.addItems.addItemsRecentChild.AddRecentItemsPresenter;
import com.mcn.honeydew.ui.addItems.addItemsWhen.AddItemsWhenMvpPresenter;
import com.mcn.honeydew.ui.addItems.addItemsWhen.AddItemsWhenMvpView;
import com.mcn.honeydew.ui.addItems.addItemsWhen.AddItemsWhenPresenter;
import com.mcn.honeydew.ui.addItems.addItemsWhere.AddItemsRecentLocationAdapter;
import com.mcn.honeydew.ui.addItems.addItemsWhere.AddItemsWhereMvpPresenter;
import com.mcn.honeydew.ui.addItems.addItemsWhere.AddItemsWhereMvpView;
import com.mcn.honeydew.ui.addItems.addItemsWhere.AddItemsWherePresenter;
import com.mcn.honeydew.ui.addlist.AddListMvpPresenter;
import com.mcn.honeydew.ui.addlist.AddListMvpView;
import com.mcn.honeydew.ui.addlist.AddListPresenter;
import com.mcn.honeydew.ui.changePassword.ChangePasswordMvpPresenter;
import com.mcn.honeydew.ui.changePassword.ChangePasswordMvpView;
import com.mcn.honeydew.ui.changePassword.ChangePasswordPresenter;
import com.mcn.honeydew.ui.colorSettings.ColorSettingsMvpPresenter;
import com.mcn.honeydew.ui.colorSettings.ColorSettingsMvpView;
import com.mcn.honeydew.ui.colorSettings.ColorSettingsPresenter;
import com.mcn.honeydew.ui.contactList.ContactListAdapter;
import com.mcn.honeydew.ui.contactList.ContactListMvpPresenter;
import com.mcn.honeydew.ui.contactList.ContactListMvpView;
import com.mcn.honeydew.ui.contactList.ContactListPresenter;
import com.mcn.honeydew.ui.dummy.DummyMvpPresenter;
import com.mcn.honeydew.ui.dummy.DummyMvpView;
import com.mcn.honeydew.ui.dummy.DummyPresenter;
import com.mcn.honeydew.ui.forgotPassword.ForgotPasswordMvpPresenter;
import com.mcn.honeydew.ui.forgotPassword.ForgotPasswordMvpView;
import com.mcn.honeydew.ui.forgotPassword.ForgotPasswordPresenter;
import com.mcn.honeydew.ui.forgotPassword.locateAccountFragment.LocateAccountMvpPresenter;
import com.mcn.honeydew.ui.forgotPassword.locateAccountFragment.LocateAccountMvpView;
import com.mcn.honeydew.ui.forgotPassword.locateAccountFragment.LocateAccountPresenter;
import com.mcn.honeydew.ui.forgotPassword.resetMethodFragment.ResetMethodMvpPresenter;
import com.mcn.honeydew.ui.forgotPassword.resetMethodFragment.ResetMethodMvpView;
import com.mcn.honeydew.ui.forgotPassword.resetMethodFragment.ResetMethodPresenter;
import com.mcn.honeydew.ui.forgotPassword.resetPasswordFragment.ResetPasswordMvpPresenter;
import com.mcn.honeydew.ui.forgotPassword.resetPasswordFragment.ResetPasswordMvpView;
import com.mcn.honeydew.ui.forgotPassword.resetPasswordFragment.ResetPasswordPresenter;
import com.mcn.honeydew.ui.forgotPassword.verifyOtpFragment.VerifyOtpMvpPresenter;
import com.mcn.honeydew.ui.forgotPassword.verifyOtpFragment.VerifyOtpMvpView;
import com.mcn.honeydew.ui.forgotPassword.verifyOtpFragment.VerifyOtpPresenter;
import com.mcn.honeydew.ui.home.HomeListAdapter;
import com.mcn.honeydew.ui.home.HomeListMvpView;
import com.mcn.honeydew.ui.home.HomeMvpPresenter;
import com.mcn.honeydew.ui.home.HomePresenter;
import com.mcn.honeydew.ui.in_progress.InProgressMvpPresenter;
import com.mcn.honeydew.ui.in_progress.InProgressMvpView;
import com.mcn.honeydew.ui.in_progress.InProgressPresenter;
import com.mcn.honeydew.ui.list_settings.ListSettingsMvpPresenter;
import com.mcn.honeydew.ui.list_settings.ListSettingsMvpView;
import com.mcn.honeydew.ui.list_settings.ListSettingsPresenter;
import com.mcn.honeydew.ui.login.LoginMvpPresenter;
import com.mcn.honeydew.ui.login.LoginMvpView;
import com.mcn.honeydew.ui.login.LoginPresenter;
import com.mcn.honeydew.ui.main.MainMvpPresenter;
import com.mcn.honeydew.ui.main.MainMvpView;
import com.mcn.honeydew.ui.main.MainPresenter;
import com.mcn.honeydew.ui.myList.MyListAdapter;
import com.mcn.honeydew.ui.myList.MyListMvpPresenter;
import com.mcn.honeydew.ui.myList.MyListMvpView;
import com.mcn.honeydew.ui.myList.MyListPresenter;
import com.mcn.honeydew.ui.notifications.NotificationsMvpPresenter;
import com.mcn.honeydew.ui.notifications.NotificationsMvpView;
import com.mcn.honeydew.ui.notifications.NotificationsPresenter;
import com.mcn.honeydew.ui.notifications.settings.NotificationSettingsMvpPresenter;
import com.mcn.honeydew.ui.notifications.settings.NotificationSettingsMvpView;
import com.mcn.honeydew.ui.notifications.settings.NotificationSettingsPresenter;
import com.mcn.honeydew.ui.phoneVerification.PhoneVerificationMvpPresenter;
import com.mcn.honeydew.ui.phoneVerification.PhoneVerificationMvpView;
import com.mcn.honeydew.ui.phoneVerification.PhoneVerificationPresenter;
import com.mcn.honeydew.ui.phoneVerification.SmsReceiver;
import com.mcn.honeydew.ui.register.RegisterMvpPresenter;
import com.mcn.honeydew.ui.register.RegisterMvpView;
import com.mcn.honeydew.ui.register.RegisterPresenter;
import com.mcn.honeydew.ui.settings.SettingsMvpPresenter;
import com.mcn.honeydew.ui.settings.SettingsMvpView;
import com.mcn.honeydew.ui.settings.SettingsPresenter;
import com.mcn.honeydew.ui.settings.editEmail.EditEmailDialogPresenter;
import com.mcn.honeydew.ui.settings.editEmail.EditEmailMvpPresenter;
import com.mcn.honeydew.ui.settings.editEmail.EditEmailMvpView;
import com.mcn.honeydew.ui.settings.editname.EditNameDialogMvpPresenter;
import com.mcn.honeydew.ui.settings.editname.EditNameDialogMvpView;
import com.mcn.honeydew.ui.settings.editname.EditNameDialogPresenter;
import com.mcn.honeydew.ui.settings.timePicker.TimePickerMvpPresenter;
import com.mcn.honeydew.ui.settings.timePicker.TimePickerMvpView;
import com.mcn.honeydew.ui.settings.timePicker.TimePickerPresenter;
import com.mcn.honeydew.ui.shareToContacts.ShareToContactsMvpPresenter;
import com.mcn.honeydew.ui.shareToContacts.ShareToContactsMvpView;
import com.mcn.honeydew.ui.shareToContacts.ShareToContactsPresenter;
import com.mcn.honeydew.ui.shareToContacts.ShareToListAdapter;
import com.mcn.honeydew.ui.sharelist.ShareListMvpPresenter;
import com.mcn.honeydew.ui.sharelist.ShareListMvpView;
import com.mcn.honeydew.ui.sharelist.ShareListPresenter;
import com.mcn.honeydew.ui.sharelist.SharedUserListAdapter;
import com.mcn.honeydew.ui.splash.SplashMvpPresenter;
import com.mcn.honeydew.ui.splash.SplashMvpView;
import com.mcn.honeydew.ui.splash.SplashPresenter;
import com.mcn.honeydew.ui.welcome.WelcomeMvpPresenter;
import com.mcn.honeydew.ui.welcome.WelcomeMvpView;
import com.mcn.honeydew.ui.welcome.WelcomePresenter;
import com.mcn.honeydew.utils.rx.AppSchedulerProvider;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by amit on 14/2/18.
 */

@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    @PerActivity
    SplashMvpPresenter<SplashMvpView> provideSplashPresenter(SplashPresenter<SplashMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    LoginMvpPresenter<LoginMvpView> provideLoginPresenter(LoginPresenter<LoginMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    MainMvpPresenter<MainMvpView> provideMainPresenter(MainPresenter<MainMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    RegisterMvpPresenter<RegisterMvpView> provideRegisterPresenter(RegisterPresenter<RegisterMvpView> presenter) {
        return presenter;
    }

    @Provides
    SettingsMvpPresenter<SettingsMvpView> provideSettingsMvpPresenter(
            SettingsPresenter<SettingsMvpView> presenter) {
        return presenter;
    }

    @Provides
    NotificationsMvpPresenter<NotificationsMvpView> provideNotificationsMvpPresenter(
            NotificationsPresenter<NotificationsMvpView> presenter) {
        return presenter;
    }

    @Provides
    AddListMvpPresenter<AddListMvpView> provideAddListMvpPresenter(
            AddListPresenter<AddListMvpView> presenter) {
        return presenter;
    }

    @Provides
    HomeMvpPresenter<HomeListMvpView> provideHomeMvpPresenter(
            HomePresenter<HomeListMvpView> presenter) {
        return presenter;
    }

    @Provides
    PhoneVerificationMvpPresenter<PhoneVerificationMvpView> providePhoneVerificationPresenter(PhoneVerificationPresenter<PhoneVerificationMvpView> presenter) {
        return presenter;
    }

    @Provides
    SmsReceiver providesSmsReceiver() {
        return new SmsReceiver();
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(AppCompatActivity activity) {

        return new LinearLayoutManager(activity);
    }

    @Provides
    HomeListAdapter providesHomeListAdapter() {
        return new HomeListAdapter();
    }

    @Provides
    EditNameDialogMvpPresenter<EditNameDialogMvpView> provideEditNamePresenter(
            EditNameDialogPresenter<EditNameDialogMvpView> presenter) {
        return presenter;
    }


    @Provides
    ForgotPasswordMvpPresenter<ForgotPasswordMvpView> provideForgotPasswordPresenter(
            ForgotPasswordPresenter<ForgotPasswordMvpView> presenter) {
        return presenter;
    }

    @Provides
    LocateAccountMvpPresenter<LocateAccountMvpView> providesLocateAccountPresenter(LocateAccountPresenter<LocateAccountMvpView> presenter) {
        return presenter;
    }

    @Provides
    ResetMethodMvpPresenter<ResetMethodMvpView> provideResetMethodPresenter(ResetMethodPresenter<ResetMethodMvpView> presenter) {
        return presenter;
    }

    @Provides
    VerifyOtpMvpPresenter<VerifyOtpMvpView> providesVerifyOtpPresenter(VerifyOtpPresenter<VerifyOtpMvpView> presenter) {
        return presenter;
    }


    @Provides
    MyListMvpPresenter<MyListMvpView> providesHomeDetailListMvpPresenter(MyListPresenter<MyListMvpView> mPresenter) {
        return mPresenter;

    }

    @Provides
    MyListAdapter providesHomeDetailListAdapter() {

        return new MyListAdapter();
    }

    @Provides
    ResetPasswordMvpPresenter<ResetPasswordMvpView> provideResetPasswordPresenter(ResetPasswordPresenter<ResetPasswordMvpView> presenter) {
        return presenter;
    }

    @Provides
    ChangePasswordMvpPresenter<ChangePasswordMvpView> provideChangePasswordPresenter(ChangePasswordPresenter<ChangePasswordMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    NotificationSettingsMvpPresenter<NotificationSettingsMvpView> provideNotificationSettingsPresenter(NotificationSettingsPresenter<NotificationSettingsMvpView> presenter) {
        return presenter;
    }

    @Provides
    AddItemsMvpPresenter<AddItemsMvpView> providesAddItemsMvpPresenter(AddItemsPresenter<AddItemsMvpView> presenter) {

        return presenter;
    }

    @Provides
    DummyMvpPresenter<DummyMvpView> provideDummyMvpPresenter(DummyPresenter<DummyMvpView> presenter) {
        return presenter;
    }

    @Provides
    AddRecentItemsAdapter providesAddItemAdapter() {
        return new AddRecentItemsAdapter();
    }

    @Provides
    ColorSettingsMvpPresenter<ColorSettingsMvpView> provideColorSettingsPresenter(ColorSettingsPresenter<ColorSettingsMvpView> presenter) {
        return presenter;
    }

    @Provides
    AddRecentItemsMvpPresenter<AddRecentItemsMvpView> provideAddRecentItemsMvpPresenter(
            AddRecentItemsPresenter<AddRecentItemsMvpView> presenter) {
        return presenter;
    }

    @Provides
    AddItemsWhenMvpPresenter<AddItemsWhenMvpView> provideAddItemWhenMvpPresenter(
            AddItemsWhenPresenter<AddItemsWhenMvpView> presenter) {
        return presenter;
    }


    @Provides
    AddItemsWhereMvpPresenter<AddItemsWhereMvpView> provideAddItemsWhereMvpPresenter(AddItemsWherePresenter<AddItemsWhereMvpView> presenter) {
        return presenter;
    }

    @Provides
    AddItemsRecentLocationAdapter providesAddItemsRecentLocationAdapter() {
        return new AddItemsRecentLocationAdapter();
    }

    @Provides
    ShareListMvpPresenter<ShareListMvpView> provideShareListMvpPresenter(ShareListPresenter<ShareListMvpView> presenter) {
        return presenter;
    }

    @Provides
    ContactListMvpPresenter<ContactListMvpView> provideContactListPresenter(ContactListPresenter<ContactListMvpView> presenter) {
        return presenter;
    }

    @Provides
    ContactListAdapter providesContactListAdapter() {

        return new ContactListAdapter();
    }

    @Provides
    ShareToListAdapter providesShareToContactAdapter() {
        return new ShareToListAdapter();
    }


    @Provides
    SharedUserListAdapter providesSharedUserListAdapter() {
        return new SharedUserListAdapter();
    }

    @Provides
    ShareToContactsMvpPresenter<ShareToContactsMvpView> provideShareToContactPresenter(ShareToContactsPresenter<ShareToContactsMvpView> presenter) {
        return presenter;
    }

    @Provides
    ListSettingsMvpPresenter<ListSettingsMvpView> provideListSettingsPresenter(ListSettingsPresenter<ListSettingsMvpView> presenter) {
        return presenter;
    }

    @Provides
    InProgressMvpPresenter<InProgressMvpView> provideInProgressPresenter(InProgressPresenter<InProgressMvpView> presenter) {
        return presenter;
    }


    @Provides
    EditEmailMvpPresenter<EditEmailMvpView> provideEditEmailDialogPresenter(EditEmailDialogPresenter<EditEmailMvpView> presenter) {
        return presenter;
    }

    @Provides
    TimePickerMvpPresenter<TimePickerMvpView> provideTimePickerPresenter(TimePickerPresenter<TimePickerMvpView> presenter) {
        return presenter;
    }

   /* @Provides
    NotificationAdapter provideNotificationAdapter() {
        return new NotificationAdapter();
    }*/

    @Provides
    WelcomeMvpPresenter<WelcomeMvpView> provideWelcomePresenter(WelcomePresenter<WelcomeMvpView> presenter) {
        return presenter;
    }
}
