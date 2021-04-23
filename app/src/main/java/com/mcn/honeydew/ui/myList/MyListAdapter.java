package com.mcn.honeydew.ui.myList;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.request.ChangeItemStatusRequest;
import com.mcn.honeydew.data.network.model.response.MyListResponseData;
import com.mcn.honeydew.utils.draghelper.ItemTouchHelperAdapter;
import com.mcn.honeydew.utils.draghelper.OnStartDragListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.grantland.widget.AutofitTextView;


/**
 * Created by gkumar on 26/2/18.
 */

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private Context mContext;
    private Callback mCallback;
    private ArrayList<MyListResponseData> list = new ArrayList<>();
    private OnStartDragListener mDragStartListener;
    private boolean isInProgress;

    private int openedCount = 0;

    SimpleDateFormat formatter = new SimpleDateFormat("M/dd/yyyy hh:mm:ss a");
    SimpleDateFormat newFormat = new SimpleDateFormat("d MMM yy, hh:mm a");
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();

    public MyListAdapter() {
        binderHelper.setOpenOnlyOne(true);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.home_detail_list_items, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder h, int position) {


        final ViewHolder holder = (ViewHolder) h;

      /*  final MyListResponseData data = list.get(position);
        // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
        // put an unique string id as value, can be any string which uniquely define the data
        // Bind your data here
        holder.bind(data, holder);*/

        if (list != null && 0 <= position && position < list.size()) {
            final MyListResponseData data = list.get(position);

            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data
            binderHelper.bind(holder.swipeLayout, String.valueOf(data.getItemId()));

            // Bind your data here
            holder.bind(data, holder);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void replaceList(List<MyListResponseData> myListResponseData, boolean inProgressValue) {

        if (!list.isEmpty()) {

            list.clear();
        }
        list.addAll(myListResponseData);
        isInProgress = inProgressValue;
        notifyDataSetChanged();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        mCallback.onReorderData(list);

        return true;
    }

    @Override
    public void onItemDismiss(int position) {

    }

    public void removeSelectedItem(int pos) {
        list.remove(pos);
        notifyItemRemoved(pos);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, SwipeRevealLayout.SwipeListener {

        @BindView(R.id.heading_text)
        AutofitTextView mAddressHeadingTextView;

        @BindView(R.id.heading_sub_text)
        TextView mAddressTextView;

        @BindView(R.id.navigate_icon)
        ImageView mNavigateImageView;

        @BindView(R.id.detail_icon)
        ImageView mDetailImageView;

        @BindView(R.id.change_status_image)
        ImageView changeStatusImageView;

        @BindView(R.id.swipe_layout)
        SwipeRevealLayout swipeLayout;

        @BindView(R.id.textview_delete)
        TextView deleteTextView;

        @BindView(R.id.textview_edit)
        TextView editTextView;

        @BindView(R.id.time_text)
        TextView timeTextView;

        @BindView(R.id.camera_icon)
        ImageView cameraImageView;

        @BindView(R.id.red_view)
        View redView;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            deleteTextView.setOnClickListener(this);
            editTextView.setOnClickListener(this);
            changeStatusImageView.setOnClickListener(this);
            mNavigateImageView.setOnClickListener(this);
            cameraImageView.setOnClickListener(this);
            swipeLayout.setSwipeListener(this);
        }

        public void bind(MyListResponseData data, final ViewHolder holder) {
            //  3/16/2018 2:05:31 PM;
            mAddressHeadingTextView.setText(data.getItemName());

            if (data.getPhoto() != null) {

                cameraImageView.setVisibility(View.VISIBLE);

            } else {
                cameraImageView.setVisibility(View.GONE);
            }

            if (data.getLocation() != null) {
                mAddressTextView.setVisibility(View.VISIBLE);
                mAddressTextView.setText(data.getLocation());
             //   mNavigateImageView.setVisibility(View.VISIBLE);

            } else {
                mAddressTextView.setVisibility(View.GONE);
                mAddressTextView.setText("");
             //   mNavigateImageView.setVisibility(View.GONE);

            }

            if (data.getItemTime() != null && !data.getItemTime().equals("")) {

                String finalString = convertTimeInLocal(data.getItemTime());
                timeTextView.setVisibility(View.VISIBLE);
                timeTextView.setText(finalString.toUpperCase());


            } else {
                timeTextView.setVisibility(View.GONE);
                timeTextView.setText("");
            }


            if (data.getStatusId() == MyListFragment.STATUS_NEW_LIST) {
                mAddressHeadingTextView.setTextColor(mContext.getResources().getColor(R.color.black));
                changeStatusImageView.setImageResource(R.drawable.ic_uncheck);
                mAddressHeadingTextView.setPaintFlags(0);
            } else if (data.getStatusId() == MyListFragment.STATUS_IN_PROCESS) {
                mAddressHeadingTextView.setTextColor(mContext.getResources().getColor(R.color.black));
                changeStatusImageView.setImageResource(R.drawable.ic_status_in_progress);
                mAddressHeadingTextView.setPaintFlags(0);
            } else if (data.getStatusId() == MyListFragment.STATUS_COMPLETE) {
                changeStatusImageView.setImageResource(R.drawable.ic_check);
                mAddressHeadingTextView.setTextColor(mContext.getResources().getColor(R.color.dark_gray));
                mAddressHeadingTextView.setPaintFlags(holder.mAddressHeadingTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            }

            if (data.isOwner()) {
                if (data.getStatusId() == MyListFragment.STATUS_COMPLETE) {
                    editTextView.setVisibility(View.GONE);
                } else {
                    editTextView.setVisibility(View.VISIBLE);
                }
                deleteTextView.setVisibility(View.VISIBLE);

            } else {
                deleteTextView.setVisibility(View.GONE);
                if (data.isAllowDelete() && data.getStatusId() == MyListFragment.STATUS_COMPLETE) {
                    deleteTextView.setVisibility(View.VISIBLE);
                } else {
                    deleteTextView.setVisibility(View.GONE);
                }

                if (data.getStatusId() == MyListFragment.STATUS_COMPLETE) {
                    editTextView.setVisibility(View.GONE);
                } else {
                    editTextView.setVisibility(View.VISIBLE);
                }
            }

          /*  mDetailImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mDragStartListener.onStartDrag(holder);

                    return false;
                }
            });*/
            holder.mDetailImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getActionMasked() ==
                            MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                }
            });
            // binderHelper.bind(swipeLayout, String.valueOf(data.getItemId()));


            if (data.isShowExpired()) {
                redView.setVisibility(View.VISIBLE);
            } else {
                redView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.textview_delete:
                    mCallback.onItemDelete(list.get(getLayoutPosition()), getLayoutPosition());
                 /*   list.remove(getLayoutPosition());
                    notifyItemRemoved(getLayoutPosition());*/
                    break;
                case R.id.textview_edit:

                    mCallback.onEditItem(list.get(getLayoutPosition()));

                    break;

                case R.id.navigate_icon:
                    mCallback.navigateToMap(list.get(getLayoutPosition()).getLatitude(), list.get(getLayoutPosition()).getLongitude(), list.get(getLayoutPosition()).getLocation());
                    break;

                case R.id.change_status_image:

                    if (swipeLayout.isOpened()) {
                        swipeLayout.close(true);
                    }

                    MyListResponseData toDoItem = list.get(getLayoutPosition());

                    ChangeItemStatusRequest request = new ChangeItemStatusRequest(list.get(getLayoutPosition()).getItemId(),
                            -1, list.get(getLayoutPosition()).getListId(),
                            list.get(getLayoutPosition()).getListName());


                    if (list.get(getLayoutPosition()).getStatusId() == MyListFragment.STATUS_NEW_LIST) {

                        if (isInProgress) {

                            list.get(getLayoutPosition()).setStatusId(MyListFragment.STATUS_IN_PROCESS);
                            request.setStatusId(MyListFragment.STATUS_IN_PROCESS);

                        } else {

                            list.get(getLayoutPosition()).setStatusId(MyListFragment.STATUS_COMPLETE);
                            request.setStatusId(MyListFragment.STATUS_COMPLETE);

                        }

                        mCallback.changeItemStatus(request, toDoItem.getLocation());

                    } else if (list.get(getLayoutPosition()).getStatusId() == MyListFragment.STATUS_IN_PROCESS) {

                        list.get(getLayoutPosition()).setStatusId(MyListFragment.STATUS_COMPLETE);
                        request.setStatusId(MyListFragment.STATUS_COMPLETE);
                        mCallback.changeItemStatus(request, toDoItem.getLocation());


                    } else if (list.get(getLayoutPosition()).getStatusId() == MyListFragment.STATUS_COMPLETE) {

                        list.get(getLayoutPosition()).setStatusId(MyListFragment.STATUS_NEW_LIST);
                        request.setStatusId(MyListFragment.STATUS_NEW_LIST);
                        mCallback.changeItemStatus(request, toDoItem.getLocation());

                    }
                    notifyItemChanged(getLayoutPosition());

                    break;


                case R.id.camera_icon:

                    mCallback.onCameraIconClicked(list.get(getLayoutPosition()), getLayoutPosition());


                    break;

                default:

                    break;

            }

        }

        @Override
        public void onClosed(SwipeRevealLayout view) {

            openedCount = openedCount + 1;
            if (openedCount == 0) {
                mCallback.onStartEditing(false);
            }
        }

        @Override
        public void onOpened(SwipeRevealLayout view) {
            /** if any single swipe opened then stop timer.**/
            openedCount = openedCount - 1;
            if (openedCount < 0) {
                mCallback.onStartEditing(true);
            }

        }

        @Override
        public void onSlide(SwipeRevealLayout view, float slideOffset) {
            TextView textView = view.findViewById(R.id.textview_delete);

            Log.e("", String.valueOf("slide offset " + slideOffset));

        }


    }


    public interface Callback {

        void onItemClick();

        void onItemDelete(MyListResponseData data, int layoutPosition);

        void onReorderData(ArrayList<MyListResponseData> list);

        void changeItemStatus(ChangeItemStatusRequest changeItemStatusRequest, String location);


        void onEditItem(MyListResponseData data);

        void navigateToMap(String latitude, String longitude, String destLocation);

        void onStartEditing(boolean state);

        void onCameraIconClicked(MyListResponseData data, int layoutPosition);

    }

    public void setCallback(Callback callback, OnStartDragListener listener) {
        mCallback = callback;
        mDragStartListener = listener;
    }

    private String convertTimeInLocal(String time) {

        String convertedDate = null;

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

        SimpleDateFormat toShow = new SimpleDateFormat("d MMM yy, hh:mm a");

        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = null;
        try {
            date = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        toShow.setTimeZone(TimeZone.getDefault());


        if (date == null) {

            convertedDate = time;

        } else {
            convertedDate = toShow.format(date);

        }

        return convertedDate;
    }
}
