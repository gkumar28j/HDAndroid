package com.mcn.honeydew.ui.notifications;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.response.NotificationListResponse;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.BluetoothDescActivity;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.utils.EndlessRecyclerOnScrollListener;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amit on 20/2/18.
 */

public class NotificationsFragment extends BaseFragment implements NotificationsMvpView,
        SwipeRefreshLayout.OnRefreshListener, NotificationAdapter.ContentItemListener {
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy, hh:mm aa", Locale.ENGLISH);
    public static final String TAG = "NotificationsFragment";
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;

    @Inject
    NotificationsMvpPresenter<NotificationsMvpView> mPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;

    //@Inject
    NotificationAdapter mListAdapter;


    public static NotificationsFragment newInstance() {
        Bundle args = new Bundle();
        NotificationsFragment fragment = new NotificationsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

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
        int color = getResources().getColor(R.color.colorPrimary);
        refreshLayout.setColorSchemeColors(color, color, color, color);
        refreshLayout.setOnRefreshListener(this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {

                if (!mListAdapter.isLoading()) {
                    mPresenter.loadMoreData();
                }

            }
        };
        mListAdapter = null;
        mPresenter.loadData();
        mPresenter.resetNotification();
        mRecyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);


    }


    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        mEndlessRecyclerOnScrollListener.reset();
        mPresenter.refreshData();
    }

    @Override
    public void onContentClick(NotificationListResponse.NotificationListData clickedData) {

        if (clickedData.getNotificationId() == 0) {

            Intent intent = new Intent(getBaseActivity(), BluetoothDescActivity.class);
            intent.putExtra("description", clickedData.getMessage());
            startActivity(intent);


        } else {
            if (!clickedData.isRead()) {
                mPresenter.setIsRead(clickedData.getNotificationId());
            }

            getBaseActivity().onNotificationClicked(clickedData.getListHeaderColor(),
                    clickedData.getListName(), clickedData.getListId(), clickedData.isOwner(), clickedData.getInProgress());


        }


    }


    @Override
    public void showContentLoading(boolean loading) {

        if (mListAdapter != null) {
            mListAdapter.setLoading(loading);
        }

    }

    @Override
    public void showContentList(List<NotificationListResponse.NotificationListData> contentDataModelList, String duration) {

        refreshLayout.setRefreshing(false);

        if (duration != null) {
            ArrayList<NotificationListResponse.NotificationListData> templist = new ArrayList<>();

            DateTime newDate =  new DateTime().minusDays(7);
            // Date newDate = new Date(Calendar.getInstance().getTimeInMillis() - 604800000L); // 7 * 24 * 60 * 60 * 1000


            if (duration.equals("1 week")) {


              newDate =  new DateTime().minusDays(7);

            } else if (duration.equals("2 weeks")) {
                newDate =  new DateTime().minusDays(14);

            } else if (duration.equals("1 month")) {
                newDate =  new DateTime().minusMonths(1);

            } else if (duration.equals("3 months")) {
                newDate =  new DateTime().minusMonths(3);

            } else if (duration.equals("6 months")) {
                newDate =  new DateTime().minusMonths(6);


            }



            Date tempLocaldate = newDate.toDate();

            String newDateFormatted = sdf.format(tempLocaldate);

            for (int i = 0; i <contentDataModelList.size() ; i++) {

                String time = convertTimeInLocal(contentDataModelList.get(i).getCreatedDate());

                try {
                    Date comingDate = sdf.parse(time);
                    Date newDatefinal = sdf.parse(newDateFormatted);

                    boolean result = comingDate.after(newDatefinal);

                    if(result){ // server record date  is less than 2/1months  weeks

                        templist.add(contentDataModelList.get(i));

                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }



            if (mListAdapter == null) {
                mListAdapter = new NotificationAdapter(new ArrayList<>(templist), this);
                mRecyclerView.setAdapter(mListAdapter);
            }
            mListAdapter.setLoading(false);
            mListAdapter.replaceData(new ArrayList<>(templist));

            return;

        }


        if (mListAdapter == null) {
            mListAdapter = new NotificationAdapter(new ArrayList<>(contentDataModelList), this);
            mRecyclerView.setAdapter(mListAdapter);
        }
        mListAdapter.setLoading(false);
        mListAdapter.replaceData(new ArrayList<>(contentDataModelList));

    }

    @Override
    public void showEmptyView(boolean b) {

    }

    @Override
    public void onResetNotification() {


        getBaseActivity().onResetNotification();

    }

    @Override
    public void onResume() {
        super.onResume();
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

        String formattedDate = "";


        if (date != null) {

            formattedDate = toShow.format(date);
            return formattedDate;

        }



        return formattedDate;

    }
}
