package com.mcn.honeydew.ui.sharelist;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.response.GetUserSettingResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by atiwari on 14/3/18.
 */

public class SharedUserListAdapter extends RecyclerView.Adapter<SharedUserListAdapter.SharedUserViewHolder> {

    private List<GetUserSettingResponse.Result> usersList;
    private Context mContext;
    private SharedUserListCallback callback;

    public SharedUserListAdapter() {
    }

    public void setList(Activity activity, List<GetUserSettingResponse.Result> usersList, SharedUserListAdapter.SharedUserListCallback callback) {
        this.usersList = usersList;
        this.mContext = activity;
        this.callback = callback;
    }

    @Override
    public SharedUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.list_item_shared_user, parent, false);
        return new SharedUserListAdapter.SharedUserViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(SharedUserViewHolder holder, int position) {
        GetUserSettingResponse.Result result = usersList.get(position);

        holder.AllowDeleteSwitch.setChecked(result.isIsAllowDeleteItemOnCompletion());
        holder.notificationSwitch.setChecked(result.isIsSendPushNotification());
        holder.contactNameTextView.setText(result.getLabelText());
        holder.mobileNoTextView.setText(result.getShareUserMobile());

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void removeItem(int pos) {
        usersList.remove(pos);
        notifyDataSetChanged();
    }

    class SharedUserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_user_name)
        TextView contactNameTextView;

        @BindView(R.id.text_mobile_no)
        TextView mobileNoTextView;

        @BindView(R.id.switch_notification)
        Switch notificationSwitch;

        @BindView(R.id.switch_allow_delete)
        Switch AllowDeleteSwitch;

        @BindView(R.id.image_delete)
        ImageView deleteImageView;


        public SharedUserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.image_delete)
        void onDeleteClicked() {
            GetUserSettingResponse.Result result = usersList.get(getLayoutPosition());
            callback.onDeleteClicked(result, getLayoutPosition());
        }

        @OnClick(R.id.switch_notification)
        void onNotificationSwitchClicked() {
            boolean isChecked = notificationSwitch.isChecked();
            GetUserSettingResponse.Result result = usersList.get(getLayoutPosition());
            usersList.get(getLayoutPosition()).setIsSendPushNotification(isChecked);
            notifyDataSetChanged();
            callback.onNotificationSwitchChanged(result, isChecked);
        }

        @OnClick(R.id.switch_allow_delete)
        void onAllowAutoDeleteClicked() {
            boolean isChecked = AllowDeleteSwitch.isChecked();
            GetUserSettingResponse.Result result = usersList.get(getLayoutPosition());
            usersList.get(getLayoutPosition()).setIsAllowDeleteItemOnCompletion(isChecked);
            notifyDataSetChanged();
            callback.onAllowDeleteSwitchChanged(result, isChecked);
        }

        /*@OnCheckedChanged(R.id.switch_notification)
        void onNotificationSwitchChanged(CompoundButton button, boolean isChecked) {
            GetUserSettingResponse.Result result = usersList.get(getLayoutPosition());
            callback.onNotificationSwitchChanged(result, isChecked);
        }*/

    }

    interface SharedUserListCallback {
        void onDeleteClicked(GetUserSettingResponse.Result contact, int position);

        void onNotificationSwitchChanged(GetUserSettingResponse.Result contact, boolean isChecked);

        void onAllowDeleteSwitchChanged(GetUserSettingResponse.Result contact, boolean isChecked);
    }
}
