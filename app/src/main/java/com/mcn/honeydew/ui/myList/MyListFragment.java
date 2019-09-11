package com.mcn.honeydew.ui.myList;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.request.ChangeItemStatusRequest;
import com.mcn.honeydew.data.network.model.response.MyListResponseData;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.services.GeoFenceFilterService;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.ui.myListDetail.MyListDetailImageActivity;
import com.mcn.honeydew.utils.AppConstants;
import com.mcn.honeydew.utils.draghelper.OnStartDragListener;
import com.mcn.honeydew.utils.draghelper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gkumar on 26/2/18.
 */

public class MyListFragment extends BaseFragment implements MyListMvpView, MyListAdapter.Callback, OnStartDragListener, SwipeRefreshLayout.OnRefreshListener {


    public static MyListFragment newInstance(int Id, String name) {
        Bundle args = new Bundle();
        args.putString(KEY_SHARED_LIST_ID, String.valueOf(Id));
        args.putString(KEY_SHARED_LIST_NAME, name);
        MyListFragment fragment = new MyListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    Handler handler = new Handler();
    Runnable mRunnable;
    private boolean isEditOnProgress = false;
    public static final String KEY_SHARED_LIST_ID = "sharedlistid";
    public static final String KEY_SHARED_LIST_NAME = "sharedlistname";
    public static final int STATUS_NEW_LIST = 1;
    public static final int STATUS_IN_PROCESS = 2;
    public static final int STATUS_COMPLETE = 3;
    public static final int STATUS_IS_DELETE = 4;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Inject
    MyListPresenter<MyListMvpView> mPresenter;

    @BindView(R.id.home_detail_list_recyclerview)
    RecyclerView mRecyclerview;

    @Inject
    LinearLayoutManager mLayoutManager;

    @Inject
    MyListAdapter mAdapter;

    @BindView(R.id.empty_view)
    TextView emptyView;

    private String Id = null;
    private String listName = null;
    ItemTouchHelper mItemTouchHelper;
    private long TIME_DELAY = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_list_detail_layout, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
            if (getArguments() != null) {

                Id = getArguments().getString(KEY_SHARED_LIST_ID);
                listName = getArguments().getString(KEY_SHARED_LIST_NAME);

            }
            setUp(view);

        }

        return view;
    }

    @Override
    protected void setUp(View view) {

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(mAdapter);


        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter, false);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerview);
        mAdapter.setCallback(this, this);
        mPresenter.getData(Id, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mRunnable = new Runnable() {
            @Override
            public void run() {

                if (!isEditOnProgress) {
                    if (TIME_DELAY != 0) {
                        mPresenter.getData(Id, false);
                    } else {
                        mPresenter.getData(Id, true);
                    }
                }

                TIME_DELAY = 4000;
                handler.postDelayed(this, TIME_DELAY);
            }
        };
        handler.postDelayed(mRunnable, TIME_DELAY);
    }

    @Override
    public void onItemClick() {

    }

    @Override
    public void onPause() {
        super.onPause();

        if (handler != null) {
            handler.removeCallbacks(mRunnable);

        }
    }

    @Override
    public void onItemDelete(MyListResponseData data, int layoutPosition) {
        data.setListName(listName);
        mAdapter.removeSelectedItem(layoutPosition);
        if (mAdapter.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
        mPresenter.deleteItem(data, layoutPosition);
        ((MainActivity) getActivity()).syncItems();
        // Fetching bluetooth items
        mPresenter.fetchBluetoothItems();
    }

    @Override
    public void onDeleteComplete() {

        isEditOnProgress = false;

    }

    @Override
    public void onReorderData(ArrayList<MyListResponseData> list) {

        mPresenter.onReorderData(list);

    }

    @Override
    public void changeItemStatus(ChangeItemStatusRequest changeItemStatusRequest, String location) {
        changeItemStatusRequest.setListName(listName);
        mPresenter.changeItemStatus(changeItemStatusRequest);

        if (changeItemStatusRequest.getStatusId() == STATUS_COMPLETE) {
            ((MainActivity) getActivity()).syncItems();

            // Adding request id into an array list because Geo-fence client accepts it as list
            ArrayList<String> ids = new ArrayList<>();
            String requestId = String.valueOf(changeItemStatusRequest.getListId()) +
                    GeoFenceFilterService.SEPARATE_KEY +
                    changeItemStatusRequest.getItemId() +
                    GeoFenceFilterService.SEPARATE_KEY +
                    changeItemStatusRequest.getListName() +
                    GeoFenceFilterService.SEPARATE_KEY +
                    location +
                    GeoFenceFilterService.SEPARATE_KEY +
                    ((MainActivity) getBaseActivity()).headerColor;
            ids.add(requestId);

            Intent intent = new Intent(AppConstants.ACTION_ITEM_COMPLETE);
            intent.putStringArrayListExtra("request_id", ids);

            LocalBroadcastManager.getInstance(getBaseActivity()).sendBroadcast(intent);
        } else if (changeItemStatusRequest.getStatusId() == STATUS_NEW_LIST) {
            ((MainActivity) getActivity()).syncItems();
        }

        // Fetching bluetooth items
        mPresenter.fetchBluetoothItems();


    }

    @Override
    public void onEditItem(MyListResponseData data) {

        data.setListName(listName);
        getBaseActivity().editItemsFromList(data);
        ((MainActivity) getActivity()).syncItems();

    }


    @Override
    public void navigateToMap(String latitude, String longitude, String destLocation) {
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%s,%s (%s)", latitude, longitude, destLocation);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                showMessage("Please install a maps application");
            }
        }
    }

    @Override
    public void onStartEditing(boolean state) {
        if (state) {
            isEditOnProgress = true;
        } else {
            isEditOnProgress = false;
        }

    }

    @Override
    public void onCameraIconClicked(MyListResponseData data, int layoutPosition) {

        Intent intent = new Intent(getBaseActivity(),MyListDetailImageActivity.class);
        intent.putExtra("url",data.getPhoto());
        startActivity(intent);

    }


    @Override
    public void replceData(List<MyListResponseData> myListResponseData) {
        if (!isAdded()) {
            return;
        }
        mSwipeRefreshLayout.setRefreshing(false);
        if (myListResponseData.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
        mAdapter.replaceList(myListResponseData);
    }

    @Override
    public void onErrorOccured() {
        if (!isAdded()) {
            return;
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @OnClick(R.id.empty_view)
    public void onEmptyViewClicked() {

        MyListResponseData data = new MyListResponseData();
        data.setListId(Integer.valueOf(Id));
        data.setListName(listName);

        getBaseActivity().onAddItemsClicked(data);

    }

    @Override
    public void onRefresh() {

        mSwipeRefreshLayout.setRefreshing(true);
        mPresenter.getData(Id, false);
    }
}
