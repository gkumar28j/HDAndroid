package com.mcn.honeydew.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.MyHomeListData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.grantland.widget.AutofitTextView;

/**
 * Created by gkumar on 21/2/18.
 */

public class HomeListChildAdapter extends RecyclerView.Adapter<HomeListChildAdapter.ViewHolder> {


    private ArrayList<MyHomeListData.MyHomeChildData> list = new ArrayList<>();
    private Context mContext;
    private Listener mCallback;

    public HomeListChildAdapter(Context context, ArrayList<MyHomeListData.MyHomeChildData> data) {

        mContext = context;
        list = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //  mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.home_list_child_item_layout, parent, false);
        return new ViewHolder(mView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.setItem(position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void replaceData(ArrayList<MyHomeListData.MyHomeChildData> itemsByList) {
        if (!list.isEmpty()) {
            list.clear();
        }
        list = itemsByList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.heading_name)
        AutofitTextView taskNameTextView;

        @BindView(R.id.main_lay)
        RelativeLayout mainLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void setItem(int position) {
            taskNameTextView.setText(list.get(position).getItemName());
        }

       /* @OnClick(R.id.main_lay)
        public void onmainViewClicked() {
            mCallback.onChildItemClick(list.get(getLayoutPosition()));
        }*/

    }


    public interface Listener {

        void onChildItemClick(MyHomeListData.MyHomeChildData data);

    }

    public void setListener(Listener listener) {

        mCallback = listener;

    }

}