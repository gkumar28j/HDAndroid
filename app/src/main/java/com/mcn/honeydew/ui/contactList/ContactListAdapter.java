package com.mcn.honeydew.ui.contactList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.AllContact;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by atiwari on 10/3/18.
 */

public class ContactListAdapter extends BaseAdapter implements SectionIndexer, Filterable {

    List<AllContact> contactList = new ArrayList<>();
    private Context mContext;
    private ContactCallback callback;
    private TextFilter mFilter;


    public ContactListAdapter() {

    }

    public void setList(Activity activity, List<AllContact> allContacts) {
        this.mContext = activity;
        contactList = allContacts;
        callback = (ContactCallback) activity;
    }

    public void setContactList(List<AllContact> list) {
        this.contactList = list;
    }


    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        final AllContact contact = contactList.get(position);

        LayoutInflater vi = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = vi.inflate(R.layout.item_contact,
                parent, false);

        holder = new ViewHolder();


        //holder.header = convertView.findViewById(R.id.section);
        holder.titleTextView = convertView.findViewById(R.id.text_section_title);
        holder.contactNameTextView = convertView
                .findViewById(R.id.text_contact_name);
        holder.checkBox = convertView
                .findViewById(R.id.checkbox_select);


        String label = contact.getContactName().toUpperCase();
        char firstChar = label.toUpperCase().charAt(0);


        if (position == 0) {
            // Setting section title
            holder.titleTextView.setText(label.substring(0, 1).toUpperCase());

        } else {

            String preLabel = contactList.get(position - 1).getContactName();
            char preFirstChar = preLabel.toUpperCase().charAt(0);
            if (firstChar != preFirstChar) {
                // Setting section title
                holder.titleTextView.setText(label.substring(0, 1).toUpperCase());
            } else {
                holder.titleTextView.setVisibility(View.GONE);
            }
        }

        holder.checkBox.setChecked(contact.isChecked());

        holder.contactNameTextView.setText(contact.getContactName());

       /* convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = v.findViewById(R.id.checkbox_select);
                callback.onContactClicked(contact, checkBox.isChecked(), position);
            }
        });*/

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                callback.onContactCheckChanged(isChecked, contact, position);
            }
        });

        return convertView;
    }

    // ----------------------------- Section Indexer Callback ------------------------
    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < contactList.size(); i++) {
            String l = contactList.get(i).getContactName();
            char firstChar = l.toUpperCase().charAt(0);

            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new TextFilter(this, contactList);
        }
        return mFilter;
    }

    public class ViewHolder {

        TextView contactNameTextView;
        TextView titleTextView;
        CheckBox checkBox;
        //LinearLayout header;
    }

    //----------------------- Text Filterable ----------------------------------

    private class TextFilter extends Filter {
        private final ContactListAdapter adapter;
        private final List<AllContact> originalList;

        private final List<AllContact> filteredList;

        public TextFilter(ContactListAdapter adapter, List<AllContact> parentList) {
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

                for (final AllContact dataModel : originalList) {
                    if (dataModel.getContactName().toLowerCase().contains(filterPattern)) {
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
            adapter.contactList.clear();
            adapter.contactList.addAll((ArrayList<AllContact>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

/*    public void updateItem(int position, boolean isChecked) {
        AllContact contact = contactList.get(position);
        contact.setChecked(isChecked);

        contactList.set(position, contact);
        //callback.onContactCheckChanged(isChecked, contact, position);
        this.notifyDataSetChanged();
    }*/

    interface ContactCallback {

        void onContactClicked(AllContact contact, boolean checked, int clickedPosition);

        void onContactCheckChanged(boolean isChecked, AllContact contact, int clickedPosition);
    }
}
