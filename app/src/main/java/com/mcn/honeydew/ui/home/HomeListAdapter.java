package com.mcn.honeydew.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.utils.ScreenUtils;
import com.mcn.honeydew.utils.ViewUtils;
import com.mcn.honeydew.utils.draghelper.ItemTouchHelperAdapter;
import com.mcn.honeydew.utils.draghelper.ItemTouchHelperViewHolder;
import com.mcn.honeydew.utils.draghelper.MyDisabledRecyclerView;
import com.mcn.honeydew.utils.draghelper.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.grantland.widget.AutofitTextView;


/**
 * Created by gkumar on 20/2/18.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private ArrayList<MyHomeListData> list = new ArrayList<>();
    private Context mContext;
    private Callback mCallback;
    private OnStartDragListener mDragStartListener;
    int maxItemCount;

    public HomeListAdapter() {
        // setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.home_adapter_list_items, parent, false);
        return new ViewHolder(mView);

    }

    /*@Override
    public long getItemId(int position) {
        return position;
    }*/

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        //   holder.setItem(position, holder);
        final MyHomeListData data = list.get(position);


        if (data.isIsOwner()) {
            holder.teamNameTextView.setFocusableInTouchMode(true);
            holder.homeImageView.setVisibility(View.VISIBLE);
            if (data.isIsSharedByOwner()) {

                holder.homeImageView.setImageResource(R.drawable.ic_issharebyowner);

            } else {
                holder.homeImageView.setVisibility(View.GONE);

            }
        } else {
            holder.homeImageView.setVisibility(View.VISIBLE);
            holder.homeImageView.setImageResource(R.drawable.shared_list_home);
            holder.teamNameEditText.setFocusableInTouchMode(false);
        }


        holder.teamNameEditText.setText(data.getListName());
        holder.teamNameTextView.setText(data.getListName());


        if (data.getListHeaderColor().contains("#")) {
            holder.mHeaderLayout.setBackgroundColor(Color.parseColor(data.getListHeaderColor()));
        } else {
            holder.mHeaderLayout.setBackgroundColor(Color.parseColor("#" + data.getListHeaderColor()));
        }

        ArrayList<MyHomeListData.MyHomeChildData> templist = new ArrayList<>();
        templist.addAll(data.getItemsByList());


        MyHomeListData.MyHomeChildData tempData1 = new MyHomeListData.MyHomeChildData();
        tempData1.setItemId(786);
        tempData1.setItemName("Android test one");
        templist.add(tempData1);

        MyHomeListData.MyHomeChildData tempData2 = new MyHomeListData.MyHomeChildData();
        tempData2.setItemId(786);
        tempData2.setItemName("Android test two");
        templist.add(tempData2);

        MyHomeListData.MyHomeChildData tempData3 = new MyHomeListData.MyHomeChildData();
        tempData3.setItemId(786);
        tempData3.setItemName("Android test three young cubs rolled over");
        templist.add(tempData3);

        MyHomeListData.MyHomeChildData tempData4 = new MyHomeListData.MyHomeChildData();
        tempData4.setItemId(786);
        tempData4.setItemName("Android test four");
        templist.add(tempData4);

        MyHomeListData.MyHomeChildData tempData5 = new MyHomeListData.MyHomeChildData();
        tempData5.setItemId(786);
        tempData5.setItemName("Android test five");
        templist.add(tempData5);

        MyHomeListData.MyHomeChildData tempData6 = new MyHomeListData.MyHomeChildData();
        tempData6.setItemId(786);
        tempData6.setItemName("Android test six");
        templist.add(tempData6);

        ArrayList<MyHomeListData.MyHomeChildData> newList = new ArrayList<>();

        if(templist.size()>maxItemCount){
            newList = new ArrayList<MyHomeListData.MyHomeChildData>(templist.subList(0, maxItemCount));
            holder.mAdapter = new HomeListChildAdapter(mContext, newList);
        }else {
            holder.mAdapter = new HomeListChildAdapter(mContext, templist);
        }

        holder.mRecyclerView.setAdapter(holder.mAdapter);

        if (data.getItemsByList().size() > 0) {
            holder.emptyView.setVisibility(View.GONE);
        } else {
            holder.emptyView.setVisibility(View.VISIBLE);
        }

        if (data.isLongSelected()) {

            holder.deleteImageView.setVisibility(View.VISIBLE);

        } else {
            holder.deleteImageView.setVisibility(View.GONE);
        }

        holder.mCardMainView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                mCallback.editOnProgress(true, position);
                list.get(position).setLongSelected(true);

                mDragStartListener.onStartDrag(holder);


                return true;
            }
        });

        holder.mCardMainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).isLongSelected()) {

                    list.get(position).setLongSelected(false);
                    notifyItemChanged(position);
                    mCallback.editOnProgress(false, position);

                } else {
                    mCallback.onItemClick(data);

                }

            }
        });

        holder.teamNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {

                        if (holder.teamNameEditText.getText().toString().trim().equals("")) {
                            Toast.makeText(mContext, "List Name Can't be Empty.", Toast.LENGTH_SHORT).show();

                        } else {

                            holder.mHeaderLayout.requestFocus();
                            if (!holder.teamNameEditText.getText().toString().trim().equals(list.get(position).getListName())) {
                                list.get(position).setListName(holder.teamNameEditText.getText().toString().trim());
                                notifyDataSetChanged();
                                mCallback.onEditListName(holder.teamNameEditText.getText().toString().trim(),
                                        list.get(position).getListId(), list.get(position).getListHeaderColor(), list.get(position).getListFontSize());
                            } else {
                                notifyDataSetChanged();
                                mCallback.onHideSoftInput();
                            }

                        }
                        return true;
                    }
                }
                return false;
            }
        });

        holder.mAdapter.setListener((HomeListChildAdapter.Listener) mCallback);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void replaceData(ArrayList<MyHomeListData> response) {
        if (!list.isEmpty()) {
            list.clear();
        }
        list = response;
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

        if (list.get(toPosition).isLongSelected()) {
            list.get(toPosition).setLongSelected(false);

        }

        mCallback.onReorderData(list);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        Toast.makeText(mContext, "delete::" + position, Toast.LENGTH_SHORT).show();
        list.remove(position);
        notifyDataSetChanged();
    }

    public void changeDeleteIcon(int layoutPosition) {
        list.get(layoutPosition).setLongSelected(false);
        notifyItemChanged(layoutPosition);
    }

    public void removeSelectedItem(int pos) {
        list.remove(pos);
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder, View.OnFocusChangeListener {

        @BindView(R.id.home_list__imgview)
        ImageView homeImageView;

        @BindView(R.id.team_name_text_view)
        AutofitTextView teamNameTextView;

        @BindView(R.id.team_name_edit_text)
        EditText teamNameEditText;

        @BindView(R.id.home_list_child_recyclerView)
        MyDisabledRecyclerView mRecyclerView;

        @BindView(R.id.card_main)
        CardView mCardMainView;

        @BindView(R.id.header_lay)
        RelativeLayout mHeaderLayout;


        @BindView(R.id.empty_lay)
        LinearLayout emptyView;

        @BindView(R.id.delete_card_imageview)
        ImageView deleteImageView;

        HomeListChildAdapter mAdapter;

        private LinearLayoutManager manager;

        int totalHeight = 0;
        int availiableHeight = 0;

        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            manager = new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.setHasFixedSize(true);
            teamNameEditText.setOnFocusChangeListener(this);
            totalHeight = ScreenUtils.getScreenHeight(mContext);
            availiableHeight = (int) (totalHeight - ((3 * (mContext.getResources().getDimension(R.dimen.padding_home_cards_list))) + ScreenUtils.getStatusBarHeight(mContext) + (2 * (ScreenUtils.getActionBarHeight(mContext)))));

            int childHeight = mContext.getResources().getDimensionPixelSize(R.dimen.home_child_recycler_textview_height);

            int viewHeight = availiableHeight / 2;
            mCardMainView.getLayoutParams().height = viewHeight;


            int cardtitleHeight = mContext.getResources().getDimensionPixelSize(R.dimen.home_card_list_header_height);
            maxItemCount = (viewHeight - cardtitleHeight) / childHeight;
            Log.e("child count", String.valueOf(maxItemCount));

            mCardMainView.requestLayout();

            teamNameTextView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getActionMasked() ==
                            MotionEvent.ACTION_DOWN) {
                        onTeamNameTextViewClicked();
                    }
                    return false;
                }
            });
        }

        @OnClick(R.id.delete_card_imageview)
        public void onDeleteClicked() {

            if (!list.get(getLayoutPosition()).isIsOwner()) {
                mCallback.onItemUnshare(list.get(getLayoutPosition()), getLayoutPosition());
            } else {
                mCallback.onItemDelete(list.get(getLayoutPosition()), getLayoutPosition());
            }


        }

        // @OnClick({R.id.header_lay, R.id.team_name_text_view})
        //@OnClick(R.id.team_name_text_view)
        void onTeamNameTextViewClicked() {

            if (!list.get(getLayoutPosition()).isIsOwner()) return;

            mCallback.editOnProgress(true, getLayoutPosition());

            teamNameTextView.setVisibility(View.INVISIBLE);

            teamNameEditText.setVisibility(View.VISIBLE);

            teamNameEditText.setFocusable(true);
            teamNameEditText.setFocusableInTouchMode(true);
            teamNameEditText.requestFocus();
            teamNameEditText.setSelection(teamNameEditText.getText().length());
            teamNameEditText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager keyboard = (InputMethodManager)
                            mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(teamNameEditText, 0);


                }
            }, 300);


        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {
            if (list.get(getLayoutPosition()).isLongSelected()) {
                //deleteImageView.setVisibility(View.VISIBLE);
                notifyItemChanged(getLayoutPosition());

            }


        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (hasFocus) {
                mCallback.editOnProgress(true, getLayoutPosition());
                teamNameTextView.setVisibility(View.INVISIBLE);
                v.setVisibility(View.VISIBLE);
            } else {
                mCallback.editOnProgress(false, getLayoutPosition());
                teamNameTextView.setVisibility(View.VISIBLE);
                v.setVisibility(View.INVISIBLE);

            }

        }
    }


    public interface Callback {

        void onItemClick(MyHomeListData data);

        void onItemDelete(MyHomeListData data, int layoutPosition);

        void onItemUnshare(MyHomeListData data, int layoutPosition);

        void onReorderData(ArrayList<MyHomeListData> list);

        void onEditListName(String name, int listId, String color, int fontSize);

        void onHideSoftInput();

        void editOnProgress(boolean edit, int layoutPosition);

    }

    public void setCallback(Callback callback, OnStartDragListener listener) {
        mCallback = callback;
        mDragStartListener = listener;
    }


    public void onSoftInputHide(ViewHolder holder, int position) {

        if (holder == null) {
            return;
        }

        if (holder.teamNameEditText.getText().toString().trim().equals("")) {
            Toast.makeText(mContext, "List Name Can't be Empty.", Toast.LENGTH_SHORT).show();

        } else {

            holder.mHeaderLayout.requestFocus();
            if (!holder.teamNameEditText.getText().toString().trim().equals(list.get(position).getListName())) {
                list.get(position).setListName(holder.teamNameEditText.getText().toString().trim());
                notifyDataSetChanged();
                mCallback.onEditListName(holder.teamNameEditText.getText().toString().trim(),
                        list.get(position).getListId(), list.get(position).getListHeaderColor(), list.get(position).getListFontSize());
            } else {
                notifyDataSetChanged();
                mCallback.onHideSoftInput();
            }

        }

    }

    public ArrayList<MyHomeListData> getUpdatedList() {
        return list;
    }


}
