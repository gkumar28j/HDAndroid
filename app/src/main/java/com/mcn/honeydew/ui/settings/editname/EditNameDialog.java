package com.mcn.honeydew.ui.settings.editname;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by amit on 23/2/18.
 */

public class EditNameDialog extends BaseDialog implements EditNameDialogMvpView {

    private static final String TAG = "RateUsDialog";

    @Inject
    EditNameDialogMvpPresenter<EditNameDialogMvpView> mPresenter;


    @BindView(R.id.et_message)
    EditText mMessage;


    @BindView(R.id.btn_submit)
    Button mSubmitButton;

    @BindView(R.id.btn_cancel)
    Button mCancelButton;

    private RefreshListener mListener;

    public void setListener(RefreshListener listener) {
        mListener = listener;
    }


    public static EditNameDialog newInstance() {
        EditNameDialog fragment = new EditNameDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_edit_name, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {

            component.inject(this);

            setUnBinder(ButterKnife.bind(this, view));

            mPresenter.onAttach(this);
        }

        return view;
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }


    @Override
    protected void setUp(View view) {

        mPresenter.onViewPrepared();
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onNameSubmitted(mMessage.getText().toString());
            }
        });

    }

    @OnClick(R.id.btn_cancel)
    void onCancelClick() {
        mPresenter.onCancelClicked();
    }


    @Override
    public void dismissDialog() {
        super.dismissDialog(TAG);
    }

    @Override
    public void showUserName(UserDetailResponse userData) {
        mMessage.setText(userData.getUserName());
    }

    @Override
    public void refreshData() {
        mListener.refreshData();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    public static interface RefreshListener {
        void refreshData();
    }

}
