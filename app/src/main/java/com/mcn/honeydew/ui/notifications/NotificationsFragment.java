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
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.response.NotificationListResponse;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.BluetoothDescActivity;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.utils.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amit on 20/2/18.
 */

public class NotificationsFragment extends BaseFragment implements NotificationsMvpView,
        SwipeRefreshLayout.OnRefreshListener, NotificationAdapter.ContentItemListener {

    private static final String TAG = "NotificationsFragment";
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
    public void showContentList(List<NotificationListResponse.NotificationListData> contentDataModelList) {

        refreshLayout.setRefreshing(false);
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
}
