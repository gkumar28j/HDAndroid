package com.mcn.honeydew.ui.shareToContacts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.SelectedContact;
import com.mcn.honeydew.ui.base.BaseActivity;
import com.mcn.honeydew.utils.Status;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareToContactsActivity extends BaseActivity implements ShareToContactsMvpView, ShareToListAdapter.ShareToListCallback {

    private final int ITEM_ID_SHARE = 13;
    private int listId;
    private ArrayList<SelectedContact> selectedContacts;
    private boolean isFailed = false;

    @BindView(R.id.final_contact_list)
    RecyclerView recyclerView;

    @BindView(R.id.text_message)
    TextView messageTextView;

    @BindView(R.id.title)
    TextView title;

    @Inject
    ShareToListAdapter adapter;

    @Inject
    ShareToContactsMvpPresenter<ShareToContactsMvpView> mPresenter;

    public static Intent getStartIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ShareToContactsActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_to_contacts);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            title.setText("Share Contact");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mPresenter.onAttach(this);

        if (getIntent() != null) {

            Bundle bundle = getIntent().getExtras();
            selectedContacts = bundle.getParcelableArrayList("selected");
            listId = bundle.getInt("list_id");

            adapter.setList(this, selectedContacts);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);


        }

    }

    @Override
    protected void setUp() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add(0, ITEM_ID_SHARE, 0, "Share");
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

            case ITEM_ID_SHARE:
                adapter.showLoadingView(0);
                mPresenter.shareListToContact(listId, selectedContacts);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeleteClicked(int position) {
        if (selectedContacts.isEmpty())
            return;

        selectedContacts.remove(position);
        adapter.notifyDataSetChanged();

        if (selectedContacts.isEmpty()) {
            messageTextView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onOnErrorIconClicked(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListSharedSuccess(int position) {
        adapter.hideLoadingView(position);
        adapter.updateStatus(position, Status.SUCCESS, "");
    }

    @Override
    public void onListSharedFailure(int position, String errorMessage) {
        isFailed = true;
        adapter.hideLoadingView(position);
        adapter.updateStatus(position, Status.FAILED, errorMessage);
        adapter.updateStatus(position, Status.FAILED, errorMessage);
    }

    @Override
    public void onSharingFinished(String message) {

        if (!isFailed) {

            if (!TextUtils.isEmpty(message)) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }

            setResult(RESULT_OK);
            this.finish();
        }
    }


}
