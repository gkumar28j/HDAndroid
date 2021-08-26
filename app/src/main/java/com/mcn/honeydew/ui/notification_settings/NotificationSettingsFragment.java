package com.mcn.honeydew.ui.notification_settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.response.SystemNotifcationPrefData;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.ui.common_app_settings.CommonAppSettingsFragment;
import com.mcn.honeydew.ui.notifications.settings.NotificationSettingsMvpPresenter;
import com.mcn.honeydew.ui.notifications.settings.NotificationSettingsPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationSettingsFragment extends BaseFragment implements SystemNotificationSettingsMvpView {
    @Inject
    SystemNotificationSettingMvpPresenter<SystemNotificationSettingsMvpView> mPresenter;


    int selectedItemId = 1;
    String selectedItems = "1 week";

    @BindView(R.id.system_notification_text)
    TextView systemNotificationTextView;
    String[] arr = {"1 week", "2 weeks", "1 month", "3 months", "6 months"};

    int searchedPosition = 0;

    public static NotificationSettingsFragment newInstance() {
        NotificationSettingsFragment fragment = new NotificationSettingsFragment();
        Bundle args = new Bundle();
       /* args.putInt("listId", listId);
        args.putString("colorCode", colorCode);*/
        //  fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        getBaseActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        View view = inflater.inflate(R.layout.notification_settings, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
            setUp(view);
        }
        return view;
    }

    @Override
    protected void setUp(View view) {

    mPresenter.getNotificationPrefrences();

    }

    @OnClick(R.id.keep_notif_lay)
    public void onNotificationClicked() {

        showDropdown();


    }


    void showDropdown() {


        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getBaseActivity());
        builder.setTitle("HoneyDew");
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setSingleChoiceItems(arr, searchedPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos) {

                selectedItems = arr[pos];
                selectedItemId = pos + 1;
                systemNotificationTextView.setText(selectedItems);
                mPresenter.setNotificationPref(selectedItems);
                dialog.dismiss();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onNotificationPrefFetched(SystemNotifcationPrefData.Result data) {

        String prefrence = data.getMessage().trim();

        for (int i = 0; i <arr.length ; i++) {

            if(arr[i].equals(prefrence)){

                searchedPosition = i;
                break;
            }
        }

        systemNotificationTextView.setText(prefrence);

    }

    @Override
    public void onSuccessfullySetPref() {
        mPresenter.getNotificationPrefrences();
    }
}
