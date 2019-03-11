package com.mcn.honeydew.ui.changePassword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.mcn.honeydew.R;
import com.mcn.honeydew.ui.base.BaseActivity;
import com.mcn.honeydew.utils.KeyboardUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity implements ChangePasswordMvpView {

    @BindView(R.id.edit_old_password)
    EditText oldPasswordEditText;

    @BindView(R.id.edit_new_password)
    EditText newPasswordEditText;

    @BindView(R.id.edit_confirm_password)
    EditText confirmPasswordEditText;

    @Inject
    ChangePasswordPresenter<ChangePasswordMvpView> mPresenter;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ChangePasswordActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setUp() {

    }

    @OnClick(R.id.text_submit)
    void onSubmitClicked()
    {
        KeyboardUtils.hideSoftInput(this);

        String oldPassword = oldPasswordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        mPresenter.onSubmitClicked(oldPassword,newPassword,confirmPassword);
    }

    @Override
    public void onPasswordChanged() {
        finish();
    }


    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }
}
