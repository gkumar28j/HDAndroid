package com.mcn.honeydew.ui.forgotPassword.locateAccountFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.response.LocateAccountResponse;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.utils.KeyboardUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocateAccountFragment extends BaseFragment implements LocateAccountMvpView {

    public static String TAG = LocateAccountFragment.class.getSimpleName();
    private OnLocateAccountListener mListener;

    @Inject
    LocateAccountPresenter<LocateAccountMvpView> mPresenter;

    @BindView(R.id.edit_email)
    EditText emailOrPhoneEditText;

    public LocateAccountFragment() {
        // Required empty public constructor
    }

    public static LocateAccountFragment newInstance() {
        LocateAccountFragment fragment = new LocateAccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_locate_account, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);

        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnLocateAccountListener) {
            mListener = (OnLocateAccountListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLocateAccountListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void setUp(View view) {

    }

    @Override
    public void onLocateAccountSuccess(LocateAccountResponse.Detail detail) {
        if (mListener != null) {
            mListener.onAccountLocated(detail);
        }
    }

    /// Clicks

    @OnClick(R.id.text_search)
    void onSearchClicked() {

        KeyboardUtils.hideSoftInput(getActivity());

        String emailOrPhone = emailOrPhoneEditText.getText().toString().toLowerCase().trim();
        mPresenter.locateAccount(emailOrPhone);
    }


    public interface OnLocateAccountListener {
        void onAccountLocated(LocateAccountResponse.Detail detail);
    }
}
