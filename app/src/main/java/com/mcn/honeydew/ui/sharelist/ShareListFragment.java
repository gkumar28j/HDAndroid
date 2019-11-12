package com.mcn.honeydew.ui.sharelist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.data.network.model.response.GetUserSettingResponse;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.ui.contactList.ContactListActivity;
import com.mcn.honeydew.utils.NetworkUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


/**
 * Created by amit on 7/3/18.
 */

public class ShareListFragment extends BaseFragment implements ShareListMvpView, SharedUserListAdapter.SharedUserListCallback {

    private static final String TAG = "ShareListFragment";
    private static final int REQUEST_CONTACT_LIST = 1122;


    @Inject
    ShareListMvpPresenter<ShareListMvpView> mPresenter;

    @BindView(R.id.layout_other_user)
    LinearLayout otherUserLayout;

    @BindView(R.id.layout_owner)
    LinearLayout ownerLayout;

    @BindView(R.id.text_email)
    TextView emailTextView;

    @BindView(R.id.text_list_name)
    TextView listNameTextView;

    @BindView(R.id.text_message)
    TextView messageTextView;

    @BindView(R.id.text_user_list_label)
    TextView userListLabelTextView;

    @BindView(R.id.switch_notification)
    Switch notificationSwitch;

    @BindView(R.id.recycler_view_users)
    RecyclerView usersRecyclerView;

    private MyHomeListData mSelectedListData;
    private boolean isOwner;
    private int position;

    @Inject
    SharedUserListAdapter adapter;


    public static ShareListFragment newInstance() {
        Bundle args = new Bundle();
        ShareListFragment fragment = new ShareListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share_list, container, false);

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

        mPresenter.onViewPrepared();

    }


    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CONTACT_LIST) {
                getBaseActivity().onListSharedSuccess();
            }
        }
    }

    @Override
    public void onSelectedListLoaded(MyHomeListData data) {
        mSelectedListData = data;
        isOwner = data.isIsOwner();

        mPresenter.getUserSettings(data.getListId());
        if (isOwner) {
            otherUserLayout.setVisibility(View.GONE);
            ownerLayout.setVisibility(View.VISIBLE);

            listNameTextView.setText(getString(R.string.share_list).replace("@", mSelectedListData.getListName()));

        } else {
        //    otherUserLayout.setVisibility(View.VISIBLE);
            ownerLayout.setVisibility(View.GONE);

            /* if (data.isIsSharedByOwner()) {

             *//* messageTextView.setVisibility(View.GONE);
                userListLabelTextView.setVisibility(View.VISIBLE);*//*
            } else {
                *//*messageTextView.setVisibility(View.VISIBLE);
                userListLabelTextView.setVisibility(View.GONE);*//*
            }*/


        }
    }

    @Override
    public void onUserSettingsLoaded(List<GetUserSettingResponse.Result> settings) {
        /*ownerLayout.setVisibility(View.VISIBLE);
        otherUserLayout.setVisibility(View.GONE);*/

        if (settings.isEmpty()) {
            // Hiding list label and showing message text in case of empty list
            userListLabelTextView.setVisibility(View.GONE);
            messageTextView.setVisibility(View.VISIBLE);
        } else {
            messageTextView.setVisibility(View.GONE);
            userListLabelTextView.setVisibility(View.VISIBLE);

            adapter.setList(getActivity(), settings, this);
            usersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            usersRecyclerView.setAdapter(adapter);
            usersRecyclerView.setNestedScrollingEnabled(false);
        }

        if (!isOwner) {
            otherUserLayout.setVisibility(View.VISIBLE);
            emailTextView.setText(settings.get(0).getSharedUserEmail());
            notificationSwitch.setChecked(settings.get(0).isToUserIsSendPushNotification());
        }


    }

    @Override
    public void onUserDeletedFromSharedList() {
        adapter.removeItem(position);

        if (adapter.getItemCount() == 0) {
            userListLabelTextView.setVisibility(View.GONE);
            messageTextView.setVisibility(View.VISIBLE);
        } else {
            messageTextView.setVisibility(View.GONE);
            userListLabelTextView.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.text_list_name)
    void onShareListClicked() {
        if(!NetworkUtils.isNetworkConnected(getBaseActivity())){
           showMessage(R.string.connection_error);
           return;
        }
        startActivityForResult(ContactListActivity.getStartIntent(getActivity(), mSelectedListData.getListId()), REQUEST_CONTACT_LIST);
    }

    @Override
    public void onDeleteClicked(GetUserSettingResponse.Result result, int position) {
        if(!NetworkUtils.isNetworkConnected(getBaseActivity())){
            showMessage(R.string.connection_error);
            return;
        }
        this.position = position;
        showConfirmDialog(result.getLabelText(), result.getSharedUserEmail());
    }

    @Override
    public void onNotificationSwitchChanged(GetUserSettingResponse.Result contact, boolean isChecked) {
        int status = isChecked ? 1 : 0;
        mPresenter.changeNotificationSettingForSharedUser(status, contact.getSharedUserEmail());
    }

    @Override
    public void onAllowDeleteSwitchChanged(GetUserSettingResponse.Result contact, boolean isChecked) {
        int status = isChecked ? 1 : 0;
        mPresenter.changeAutoDeletionSetting(status, contact.getSharedUserEmail());
    }

    @OnClick(R.id.switch_notification)
    void onNotificationSwitchClicked() {
        boolean isChecked = notificationSwitch.isChecked();
        mPresenter.changePushStatusForOthersList(isChecked ? 1 : 0);
    }


    private void showConfirmDialog(String contactName, final String phoneOrEmail) {
        String message;
        if (TextUtils.isEmpty(contactName)) {
            message = getString(R.string.message_confirm_un_share_without_name);
        } else
            message = getString(R.string.message_confirm_un_share).replace("@", contactName);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Unshare List");
        builder.setMessage(message);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.deleteUserFromSharedList(mSelectedListData.getListId(), phoneOrEmail);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
