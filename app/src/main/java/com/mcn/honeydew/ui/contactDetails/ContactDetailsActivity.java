package com.mcn.honeydew.ui.contactDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.AllContact;
import com.mcn.honeydew.data.network.model.SelectedContact;
import com.mcn.honeydew.ui.base.BaseActivity;
import com.mcn.honeydew.ui.shareToContacts.ShareToContactsActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactDetailsActivity extends BaseActivity {

    private final int ITEM_ID_DONE = 12;
    private final int REQUEST_CODE_OPEN_SHARE = 1233;
    private int listId;

    @BindView(R.id.numbers_layout)
    LinearLayout numberLayout;

    @BindView(R.id.text_indicator)
    TextView indicatorTextView;

    @BindView(R.id.contactName)
    TextView contactNameTextView;


    @BindView(R.id.title)
    TextView title;

    private AllContact contact;
    private boolean isAnyChecked = false;
    private List<String> numbersList = new ArrayList<>();
    private ArrayList<SelectedContact> selectedContactList;


    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ContactDetailsActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            title.setText("Contacts");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                contact = bundle.getParcelable("contact");
                selectedContactList = bundle.getParcelableArrayList("selected");
                listId = bundle.getInt("list_id");
                boolean isChecked = bundle.getBoolean("isChecked");

                char first = contact.getContactName().charAt(0);
                indicatorTextView.setText(String.valueOf(first).toUpperCase());
                contactNameTextView.setText(contact.getContactName());

                numbersList = contact.getContactList();
                // Removing duplicate mobile numbers
                Set<String> hs = new HashSet<>();
                hs.addAll(numbersList);
                numbersList.clear();
                numbersList.addAll(hs);

                CheckBox[] checkBoxes = new CheckBox[numbersList.size()];

                for (int i = 0; i < numbersList.size(); i++) {

                    final String number = numbersList.get(i);


                    checkBoxes[i] = new CheckBox(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    checkBoxes[i].setLayoutParams(layoutParams);
                    checkBoxes[i].setTextSize(20);
                    checkBoxes[i].setPadding(40, 10, 10, 10);

                    //if(selectedContactList.get(i).getEmailorPhoneNumber().equals(numbersList.get(i)))
                    checkBoxes[i].setChecked(isExist(number));
                    //if(contact.getContactList().get)

                    checkBoxes[i].setText(number);
                    numberLayout.addView(checkBoxes[i]);

                    checkBoxes[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            isAnyChecked = isChecked;

                            if (isChecked) {
                                selectedContactList.add(new SelectedContact(number, contact.getContactName(), isChecked));
                            }
                            else {
                                selectedContactList.remove(new SelectedContact(number, contact.getContactName(), isChecked));
                            }
                        }
                    });

                  /*  checkBoxes[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            boolean isChecked = ((CheckBox) v).isChecked();
                            isAnyChecked = isChecked;

                            if (isChecked) {
                                selectedContactList.add(new SelectedContact(number, contact.getContactName(), isChecked));
                            }
                        }
                    });*/

                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                Bundle b = new Bundle();

                isAnyChecked = isAnyChecked(numberLayout);

                b.putBoolean("isChecked", isAnyChecked);
                b.putParcelableArrayList("selected", selectedContactList);
                b.putInt("list_id", listId);

                intent.putExtras(b);
                setResult(RESULT_CANCELED, intent);
                finish();
                return true;

            case ITEM_ID_DONE:

                if (selectedContactList.isEmpty()) {
                    Toast.makeText(this, "Please select a contact number", Toast.LENGTH_SHORT).show();
                    return false;
                } else {

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("selected", selectedContactList);
                    bundle.putInt("list_id", listId);
                    startActivityForResult(ShareToContactsActivity.getStartIntent(this, bundle), REQUEST_CODE_OPEN_SHARE);
                    setResult(RESULT_OK);

                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_OPEN_SHARE) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            } else {

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add(0, ITEM_ID_DONE, 0, "DONE");
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void setUp() {

    }


    public boolean isAnyChecked(LinearLayout layout) {
        boolean isChecked = false;
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v instanceof CheckBox) {
                //validate your EditText here
                isChecked = ((CheckBox) v).isChecked();
                if (isChecked)
                    return true;
            }
        }
        return isChecked;
    }

    private boolean isExist(String number) {
        for (SelectedContact c : selectedContactList
                ) {
            if (number.equals(c.getEmailorPhoneNumber())) {
                return true;
            }
        }

        return false;
    }
}
