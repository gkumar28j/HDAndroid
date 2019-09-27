package com.mcn.honeydew.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.icu.text.TimeZoneNames;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.utils.ItemOffsetDecoration;
import com.mcn.honeydew.utils.draghelper.OnStartDragListener;
import com.mcn.honeydew.utils.draghelper.SimpleItemTouchHelperCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by amit on 20/2/18.
 */

public class HomeListFragment extends BaseFragment implements HomeListMvpView, HomeListChildAdapter.Listener,
        HomeListAdapter.Callback, OnStartDragListener {

    private static final String TAG = "HomeListFragment";
    private boolean mKeyboardVisible = false;
    @Inject
    HomeMvpPresenter<HomeListMvpView> mPresenter;

    @BindView(R.id.home_recylcer_view)
    RecyclerView homeRecyclerView;

    @Inject
    LinearLayoutManager mLayoutManager;

    @Inject
    HomeListAdapter mAdapter;


    ArrayList<MyHomeListData> mList = new ArrayList<>();

    ItemTouchHelper mItemTouchHelper;


    @BindView(R.id.empty_view)
    TextView emptyView;

    private long TIME_DELAY = 0;
    Handler handler = new Handler();
    Runnable mRunnable;

    private boolean isEditOnProgress = false;

    private int currentEditPosition = -1;
    View view;

    public static HomeListFragment newInstance() {
        Bundle args = new Bundle();
        HomeListFragment fragment = new HomeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

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
        homeRecyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.card_margin_my_list_home);
        homeRecyclerView.addItemDecoration(itemDecoration);

        homeRecyclerView.setLayoutManager(layoutManager);
        homeRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter, true);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(homeRecyclerView);
        mAdapter.setCallback(this, this);

        homeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:

                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:

                        if (isEditOnProgress) {
                            onKeyboardHidden();
                        }

                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:

                        break;

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        TIME_DELAY = 0;

    }


    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();

        view.getViewTreeObserver()
                .addOnGlobalLayoutListener(mLayoutKeyboardVisibilityListener);
        mRunnable = new Runnable() {
            @Override
            public void run() {

                if (!isEditOnProgress) {
                    if (TIME_DELAY != 0) {
                        mPresenter.onViewPrepared(false);
                    } else {
                        mPresenter.onViewPrepared(true);
                    }
                }

                TIME_DELAY = 4000;
                handler.postDelayed(this, TIME_DELAY);
            }
        };
        handler.postDelayed(mRunnable, TIME_DELAY);

        mPresenter.fetchBluetoothList();
    }

    @Override
    public void replceData(ArrayList<MyHomeListData> response) {
        if (response.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }

        mAdapter.replaceData(response);

    }


    @Override
    public void deleteComplete(String message, int pos) {

        mAdapter.removeSelectedItem(pos);
        if (mAdapter.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
        isEditOnProgress = false;
        ((MainActivity) getActivity()).syncItems();

        mPresenter.fetchBluetoothList();
    }

    @Override
    public void onEditCompleted() {

        isEditOnProgress = false;

    }

    @Override
    public void onReorderData(ArrayList<MyHomeListData> list) {

        getBaseActivity().hideKeyboard();
        mPresenter.onReorderCardsData(list);
    }

    @Override
    public void onReorderComplete() {
        isEditOnProgress = false;
    }

    @Override
    public void onPause() {
        super.onPause();

        getBaseActivity().hideKeyboard();
        if ((getActivity()) != null) {
            ((MainActivity) getActivity()).showTabs();
        }


        view.getViewTreeObserver()
                .removeOnGlobalLayoutListener(mLayoutKeyboardVisibilityListener);
        if (handler != null) {
            handler.removeCallbacks(mRunnable);

        }
    }

    @Override
    public void onItemClick(MyHomeListData data) {
        //   MyListFragment fragment = MyListFragment.newInstance(data.getListId());

        getBaseActivity().onListItemClick(data);
    }

    @Override
    public void onItemDelete(final MyHomeListData data, final int layoutPosition) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle(getActivity().getResources().getString(R.string.app_title));
        dialog.setMessage("Are you sure you want to delete the selected List?");
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mPresenter.onDeleteCard(data, layoutPosition);
                dialog.dismiss();
            }
        })
                .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                        dialog.dismiss();
                        mAdapter.changeDeleteIcon(layoutPosition);
                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();


    }

    @Override
    public void onItemUnshare(final MyHomeListData data, final int layoutPosition) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle(getActivity().getResources().getString(R.string.app_title));
        dialog.setMessage("Are you sure you want to unshare the selected List?");
        dialog.setPositiveButton("Unshare List", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mPresenter.onUnshareCard(data, layoutPosition);
                dialog.dismiss();
            }
        })
                .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                        dialog.dismiss();
                        mAdapter.changeDeleteIcon(layoutPosition);
                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public void onEditListName(String name, int listId, String color, int fontSize) {

        getBaseActivity().hideKeyboard();
        mPresenter.onEditListName(name, listId, color, fontSize);

    }

    @Override
    public void onHideSoftInput() {
        getBaseActivity().hideKeyboard();
        isEditOnProgress = false;
    }

    @Override
    public void editOnProgress(boolean state, int layoutPosition) {
        currentEditPosition = layoutPosition;
        isEditOnProgress = state;

    }

    @Override
    public void onChildItemClick(MyHomeListData.MyHomeChildData data) {

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @OnClick(R.id.empty_view)
    public void onEmptyViewClicked() {
        // open add list fragment

        getBaseActivity().onEmptyViewClicked();

    }


    private final ViewTreeObserver.OnGlobalLayoutListener mLayoutKeyboardVisibilityListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {

            final Rect rectangle = new Rect();
            final View contentView = view;
            contentView.getWindowVisibleDisplayFrame(rectangle);
            int screenHeight = contentView.getRootView().getHeight();

            // r.bottom is the position above soft keypad or device button.
            // If keypad is shown, the rectangle.bottom is smaller than that before.
            int keypadHeight = screenHeight - rectangle.bottom;
            // 0.15 ratio is perhaps enough to determine keypad height.
            boolean isKeyboardNowVisible = keypadHeight > screenHeight * 0.15;

            if (mKeyboardVisible != isKeyboardNowVisible) {
                if (isKeyboardNowVisible) {
                    onKeyboardShown();
                } else {
                    onKeyboardHidden();
                }
            }

            mKeyboardVisible = isKeyboardNowVisible;
        }
    };

    private void onKeyboardShown() {

        ((MainActivity) getActivity()).hideTabs();
    }

    private void onKeyboardHidden() {
        // isEditOnProgress = false;

        if (currentEditPosition != -1) {
            try {
                HomeListAdapter.ViewHolder holder = (HomeListAdapter.ViewHolder) homeRecyclerView.findViewHolderForAdapterPosition(currentEditPosition);
                mAdapter.onSoftInputHide(holder, currentEditPosition);
            } catch (Exception e) {
            }

        }
        ((MainActivity) getActivity()).showTabs();
    }


}
