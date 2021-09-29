package com.mcn.honeydew.ui.contactList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.AllContact;
import com.mcn.honeydew.data.network.model.SelectedContact;
import com.mcn.honeydew.ui.base.BaseActivity;
import com.mcn.honeydew.ui.contactDetails.ContactDetailsActivity;
import com.mcn.honeydew.ui.shareToContacts.ShareToContactsActivity;
import com.mcn.honeydew.utils.sidebar.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ContactListActivity extends BaseActivity implements ContactListMvpView, ContactListAdapter.ContactCallback {
    private final int ITEM_ID_NEXT = 13;
    private final int REQUEST_CODE_OPEN_SHARE = 123;
    private final static int REQUEST_ID_READ_CONTACT_PERMISSIONS = 103;
    private final static int REQUEST_CODE_CONTACT_DETAIL = 1044;

    private boolean isAnyChecked;
    private int listId;
    private int clickedPosition;
    //private ArrayList<SelectedContact> selectedContactList = new ArrayList<>();
    private List<Character> characterList = new ArrayList<>();
    List<AllContact> allContacts = new ArrayList<>();
    ArrayList<SelectedContact> selectedContacts = new ArrayList<>();

    @Inject
    ContactListMvpPresenter<ContactListMvpView> mPresenter;

    @Inject
    ContactListAdapter mAdapter;

   /* @BindView(R.id.recycler_contact_list)
    RecyclerView contactRecyclerView;*/

    @BindView(R.id.contact_list)
    ListView listView;

    @BindView(R.id.sideBar)
    SideBar sideBar;

   /* @BindView(R.id.text_cancel)
    TextView cancelTextView;*/

    @BindView(R.id.edit_search)
    EditText searchEditText;

    @BindView(R.id.title)
    TextView title;

    public static Intent getStartIntent(Context context, int listId) {
        Intent intent = new Intent(context, ContactListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("list_id", listId);
        intent.putExtras(bundle);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            title.setText("Contact List");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent() != null) {
            listId = getIntent().getExtras().getInt("list_id");
        }

        mPresenter.onAttach(this);
        searchEditText.addTextChangedListener(mTextWatcher);
        setUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add(0, ITEM_ID_NEXT, 0, "Share");
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //setResult(RESULT_CANCELED);
                finish();
                return true;

            case ITEM_ID_NEXT:
                if (selectedContacts.size() == 0) {
                    Toast.makeText(this, "Please select a contact number", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                  /*  Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("selected", selectedContacts);
                    bundle.putInt("list_id", listId);
                    startActivityForResult(ShareToContactsActivity.getStartIntent(this, bundle), REQUEST_CODE_OPEN_SHARE);*/
                    hideKeyboard();
                    mPresenter.shareListToContact(listId,selectedContacts);
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (hasPermission(Manifest.permission.READ_CONTACTS)) {
            //mPresenter.getContacts(getContentResolver());
            new GetContactsTask().execute();
        }
    }

    @Override
    protected void setUp() {
        checkPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OPEN_SHARE) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        } else if (requestCode == REQUEST_CODE_CONTACT_DETAIL) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            } else if (resultCode == RESULT_CANCELED) {

                if (data == null)
                    return;

                Bundle bundle = data.getExtras();
                boolean checked = bundle.getBoolean("isChecked");
                listId = bundle.getInt("list_id");
                ArrayList<SelectedContact> selected = bundle.getParcelableArrayList("selected");

                if (checked && selected != null && !selected.isEmpty()) {
                    isAnyChecked = true;

                    for (SelectedContact c : selected) {
                        if (!selectedContacts.contains(c)) {
                            selectedContacts.add(c);
                        }
                    }
                }

                allContacts.get(clickedPosition).setChecked(checked);
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    private void checkPermission() {

        String[] permission = {Manifest.permission.READ_CONTACTS};

        if (hasPermission(permission[0])) {
            //mPresenter.getContacts(getContentResolver());
            new GetContactsTask().execute();
        } else {
            requestPermissions(permission, REQUEST_ID_READ_CONTACT_PERMISSIONS);
        }
    }


    private List<AllContact> getContacts() {


        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {

                // Initializing list object
                List<String> phoneList = new ArrayList<>();
                AllContact contact = new AllContact();


                // If contact has at least one phone no.
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    createIndexer(name.toUpperCase());

                    // Capitalizing first character of contact name to create common group
                    String capitalizeName = name.substring(0, 1).toUpperCase() + name.substring(1);
                    Log.d("Name: ", capitalizeName);
                    contact.setContactName(capitalizeName);

                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {

                        // Getting phone number and adding it to list
                        String phoneNo = getCodeRemovedNumber(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

                        if (!phoneList.contains(phoneNo)) {
                            phoneList.add(phoneNo);
                        }

                    }

                    // Adding phone list to contact
                    contact.setContactList(phoneList);
                    allContacts.add(contact);
                    pCur.close();
                }
            }
        }


        cur.close();
        Collections.sort(allContacts, new Comparator<AllContact>() {
            @Override
            public int compare(AllContact lhs, AllContact rhs) {
                return lhs.getContactName().compareTo(rhs.getContactName());
            }
        });
        return allContacts;
    }

    @Override
    public void onContactClicked(AllContact contact, boolean checked, int clickedPosition) {
        this.clickedPosition = clickedPosition;

        Bundle bundle = new Bundle();
        bundle.putParcelable("contact", contact);
        bundle.putParcelableArrayList("selected", selectedContacts);
        bundle.putInt("list_id", listId);
        bundle.putBoolean("isChecked", checked);

        if (contact.getContactList().size() >= 1) {
            startActivityForResult(ContactDetailsActivity.getStartIntent(this, bundle), REQUEST_CODE_CONTACT_DETAIL);
        }
    }

    @Override
    public void onContactCheckChanged(boolean isChecked, AllContact contact, int clickedPosition) {
        if (contact.getContactList().size() == 0)
            return;

        this.clickedPosition = clickedPosition;


     //   if (contact.getContactList().size() > 1) {

            if (isChecked) {

               /* Bundle bundle = new Bundle();
                bundle.putParcelable("contact", contact);
                bundle.putParcelableArrayList("selected", selectedContacts);
                bundle.putInt("list_id", listId);
                startActivityForResult(ContactDetailsActivity.getStartIntent(this, bundle), REQUEST_CODE_CONTACT_DETAIL);*/
                for (String no : contact.getContactList()) {
                    SelectedContact c = new SelectedContact(no, contact.getContactName(), isChecked);
                    selectedContacts.add(c);
                }

            } else {

                for (String no : contact.getContactList()) {
                    SelectedContact c = new SelectedContact(no, contact.getContactName(), isChecked);
                    selectedContacts.remove(c);
                }
            }
       /* } else {

            SelectedContact c = new SelectedContact(contact.getContactList().get(0), contact.getContactName(), isChecked);
            if (isChecked) {
                isAnyChecked = isChecked;
                selectedContacts.add(c);
            } else {
                selectedContacts.remove(c);
            }

        }

        contact.setChecked(isChecked);
        allContacts.set(clickedPosition, contact);
        mAdapter.notifyDataSetChanged();*/
      //  mPresenter.shareListToContact(listId,selectedContacts);
    }

    TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            if (s.length() == 0) {
                // cancelTextView.setEnabled(false);
            } else {
                // filterData(s);
                // cancelTextView.setEnabled(true);

            }
            mAdapter.getFilter().filter(s);

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };



    class GetContactsTask extends AsyncTask<Void, Void, List<AllContact>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading();
        }

        @Override
        protected List<AllContact> doInBackground(Void... voids) {
            return getContacts();
        }

        @Override
        protected void onPostExecute(List<AllContact> allContacts) {
          //  super.onPostExecute(allContacts);
            if (ContactListActivity.this.isDestroyed()) { // or call isFinishing() if min sdk version < 17
                return;
            }
            hideLoading();

            Collections.sort(characterList);
            sideBar.init(characterList.toArray(new Character[characterList.size()]));

            mAdapter.setList(ContactListActivity.this, allContacts);

            sideBar.setSize((int) getResources().getDimension(R.dimen.textview_size_s3), (int) getResources().getDimension(R.dimen.side_bar_letter_height));
            sideBar.setListView(listView);
            //mAdapter.setContactCallback(ContactListActivity.this);

            //listView.setLayoutManager(new LinearLayoutManager(ContactListActivity.this));
            listView.setAdapter(mAdapter);
        }
    }

    private String getCodeRemovedNumber(String mobileNo) {

        if (!TextUtils.isEmpty(mobileNo)) {

            if (mobileNo.startsWith("0")) {
                mobileNo = mobileNo.substring(1, mobileNo.length());
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return PhoneNumberUtils.normalizeNumber(mobileNo);
            } else {
                String num;
                int spaceIndex;

                if (mobileNo.contains("-")) {
                    spaceIndex = mobileNo.indexOf("-");
                    num = mobileNo.substring(spaceIndex + 1, mobileNo.length()).replaceAll("\\s+", "");
                    return num;
                } else if (mobileNo.contains(" ")) {
                    spaceIndex = mobileNo.indexOf(" ");

                    if (spaceIndex < 3)
                        num = mobileNo.substring(spaceIndex + 1, mobileNo.length()).replaceAll("\\s+", "");
                    else
                        num = mobileNo.replaceAll("\\s+", "");

                    return num;
                }

            }
        }

        return mobileNo;
    }

    @SuppressLint("TimberArgCount")
    private void createIndexer(String name) {
        Character character;

        if (name.contains(" ")) {
            int spaceIndex = name.indexOf(" ");
            character = name.charAt(spaceIndex + 1);

        } else {
            character = name.charAt(0);
        }

        if (characterList.size() > 0) {
            if (!characterList.contains(character))
                characterList.add(character);
        } else {
            characterList.add(character);
        }

        Timber.d("Character ", character.toString());

    }

    @Override
    protected void onDestroy() {
        hideLoading();
        super.onDestroy();
    }

    @Override
    public void onListSharedSuccess(int position) {

    }

    @Override
    public void onListSharedFailure(int position, String errorMessage) {
       /* if (!TextUtils.isEmpty(errorMessage)) {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK);
        finish();*/
    }

    @Override
    public void onSharingFinished(String message) {

     //   if (!isFailed) {

            if (!TextUtils.isEmpty(message)) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }

            setResult(RESULT_OK);
            finish();
      //  }
    }

}
