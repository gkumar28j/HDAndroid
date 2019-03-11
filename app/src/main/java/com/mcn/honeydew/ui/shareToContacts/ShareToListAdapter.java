package com.mcn.honeydew.ui.shareToContacts;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.SelectedContact;
import com.mcn.honeydew.utils.Status;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by atiwari on 13/3/18.
 */

public class ShareToListAdapter extends RecyclerView.Adapter<ShareToListAdapter.ShareViewHolder> {

    private List<SelectedContact> contactList;
    private Context mContext;
    private ShareToListCallback callback;

    public ShareToListAdapter() {
    }

    public void setList(Activity activity, List<SelectedContact> list) {
        this.contactList = list;
        this.mContext = activity;
        this.callback = (ShareToListCallback) activity;
    }

    @Override
    public ShareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.layout_final_list_item, parent, false);
        return new ShareToListAdapter.ShareViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ShareViewHolder holder, int position) {
        SelectedContact contact = contactList.get(position);

        holder.contactNameTextView.setText(contact.getLabelText());
        holder.numberTextView.setText(contact.getEmailorPhoneNumber());

        if (contact.isLoading()) {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.statusImageView.setVisibility(View.GONE);
        } else {
            holder.progressBar.setVisibility(View.GONE);
            holder.statusImageView.setVisibility(View.VISIBLE);
        }

        if (contact.getStatus() == Status.SUCCESS) {
            holder.statusImageView.setImageResource(R.drawable.ic_success);
            holder.deleteImageView.setVisibility(View.GONE);
        } else if (contact.getStatus() == Status.FAILED) {
            holder.statusImageView.setImageResource(R.drawable.ic_info);
            holder.deleteImageView.setVisibility(View.GONE);
        } else if (contact.getStatus() == 0) {
            holder.deleteImageView.setImageResource(R.drawable.ic_cross);
            holder.deleteImageView.setVisibility(View.VISIBLE);
        } else {
            holder.deleteImageView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void showLoadingView(int position) {
        /*contactList.get(position).setLoading(true);
        notifyItemChanged(position);*/

        for (int i = 0; i < contactList.size(); i++) {
            contactList.get(i).setLoading(true);
            contactList.get(i).setStatus(-1);
            notifyItemChanged(i);
        }

    }

    public void hideLoadingView(int position) {
        contactList.get(position).setLoading(false);
        notifyItemChanged(position);
    }

    public void updateStatus(int position, int status, String errorMessage) {
        contactList.get(position).setStatus(status);
        contactList.get(position).setMessage(errorMessage);

        notifyItemChanged(position);
    }

    class ShareViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.contact_name)
        TextView contactNameTextView;

        @BindView(R.id.contact_number)
        TextView numberTextView;

        @BindView(R.id.image_delete)
        ImageView deleteImageView;

        @BindView(R.id.image_status)
        ImageView statusImageView;

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public ShareViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.image_delete)
        void onDeleteClicked() {
            callback.onDeleteClicked(getLayoutPosition());
        }

        @OnClick(R.id.image_status)
        void onInfoClicked(View view) {

            if (contactList.get(getLayoutPosition()).getStatus() == Status.SUCCESS)
                return;

            String message = contactList.get(getLayoutPosition()).getMessage();
            callback.onOnErrorIconClicked(message);

        }
    }

    interface ShareToListCallback {

        void onDeleteClicked(int position);

        void onOnErrorIconClicked(String message);
    }
}
