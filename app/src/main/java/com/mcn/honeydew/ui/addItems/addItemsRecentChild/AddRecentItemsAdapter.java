package com.mcn.honeydew.ui.addItems.addItemsRecentChild;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.response.RecentItemsResponse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gkumar on 7/3/18.
 */

public class AddRecentItemsAdapter extends RecyclerView.Adapter<AddRecentItemsAdapter.ViewHolder> implements Filterable{


    private ArrayList<String> list = new ArrayList<>();
    private Context mContext;
    private Callback mCallback;
    private TextFilter mFilter;
    private int row_index =-1;
    public AddRecentItemsAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.add_items_adapter_views, parent, false);
        return new ViewHolder(mView);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.recentItemTextView.setText(list.get(position));
        if(row_index==position){
            holder.mMainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.light_gray));
        }else {
            holder.mMainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void replaceData(ArrayList<String> response) {
        if (!list.isEmpty()) {
            list.clear();
        }
        list = response;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new TextFilter(this, list);
        }
        return mFilter;
    }


    class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.recent_item_textview)
        TextView recentItemTextView;

        @BindView(R.id.add_item_main_lay)
        RelativeLayout mMainLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }



        @OnClick(R.id.add_item_main_lay)
        public void onItemsClicked() {
            row_index=getLayoutPosition();
            notifyDataSetChanged();
          //  mCallback.onItemClick(list.get(getLayoutPosition()));

        }



    }


    public interface Callback {

        void onItemClick(RecentItemsResponse.RecentItemsData data);

    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    private class TextFilter extends Filter {
        private final AddRecentItemsAdapter adapter;
        private final List<String> originalList;

        private final List<String> filteredList;

        public TextFilter(AddRecentItemsAdapter adapter, ArrayList<String> parentList) {
            this.adapter = adapter;
            this.originalList = new LinkedList<>(parentList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase();

                for (final String dataModel : originalList) {
                    if (dataModel.toLowerCase().contains(filterPattern)) {
                        filteredList.add(dataModel);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.list.clear();
            adapter.list.addAll((ArrayList<String>) results.values);
            adapter.notifyDataSetChanged();

        }
    }
}



