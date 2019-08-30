package com.mcn.honeydew.ui.notifications;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.response.NotificationListResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends LoadMoreBaseAdapter<NotificationListResponse.NotificationListData, NotificationAdapter.ItemViewHolder> {

    private ContentItemListener mItemListener;
    private Context context;

    public NotificationAdapter(List<NotificationListResponse.NotificationListData> posts, ContentItemListener listener) {

        setList(posts);
        mItemListener = listener;

    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.notification_list_items, parent, false);
        return new ItemViewHolder(postView, mItemListener);
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_DATA) {
            bindPostViewHolder((ItemViewHolder) holder, position);
        }
    }

    private void bindPostViewHolder(ItemViewHolder itemViewHolder, int position) {
        itemViewHolder.setItem(data.get(position), context);

    }

    public void replaceData(List<NotificationListResponse.NotificationListData> dataModelList) {
        data.clear();
        setList(dataModelList);
        notifyDataSetChanged();
    }

    private void setList(List<NotificationListResponse.NotificationListData> dataModelList) {
        this.data = dataModelList;
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private NotificationListResponse.NotificationListData mItem;

        Context context;

        @BindView(R.id.notification_heading_textview)
        TextView titleTextView;

        @BindView(R.id.notification_time_textview)
        TextView subTitleTextView;

        @BindView(R.id.main_notf_lay)
        LinearLayout mainLayout;


        private ContentItemListener mItemListener;


        public ItemViewHolder(View itemView, ContentItemListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mItemListener = listener;
            itemView.setOnClickListener(this);


        }

        public void setItem(NotificationListResponse.NotificationListData data, Context mcontext) {

            mItem = data;
            context = mcontext;


            if(mItem.getItemName()!=null){

                String mainString = mItem.getMessage();
                if(mainString.contains(mItem.getItemName())){

                    int startIndex = mainString.indexOf(mItem.getItemName());
                    int endIndex = startIndex+ mItem.getItemName().length();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(mItem.getMessage());

                    final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD); // Span to make text bold

                    sb.setSpan(bss, startIndex, endIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold


                    titleTextView.setText(sb);

                }else {
                    titleTextView.setText(mainString);
                }

            }else {
                titleTextView.setText(mItem.getMessage());
            }



            if(mItem.getItemExpireTime()!=null){
                String time = convertTimeInLocal(mItem.getItemExpireTime());
                subTitleTextView.setText(time);
                subTitleTextView.setVisibility(View.VISIBLE);

            }else {
                subTitleTextView.setVisibility(View.INVISIBLE);
            }


            if(mItem.isRead()){
                mainLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
            }else {
                mainLayout.setBackgroundColor(context.getResources().getColor(R.color.notification_read_color));
            }

            if(mItem.getNotificationId()==0){
                mainLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
            }


        }

        @Override
        public void onClick(View v) {
            mItemListener.onContentClick(mItem);
        }



        public String convertTimeInLocal(String time) {

            //2019-08-27 11:10:15 +0000

            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss aa", Locale.ENGLISH);

            SimpleDateFormat toShow = new SimpleDateFormat("dd MMM yy, hh:mm aa", Locale.ENGLISH);

            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;
            try {
                date = df.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            toShow.setTimeZone(TimeZone.getDefault());


            String formattedDate = toShow.format(date);

            return formattedDate;

        }

    }

    public interface ContentItemListener {

        void onContentClick(NotificationListResponse.NotificationListData clickedData);

    }



}
