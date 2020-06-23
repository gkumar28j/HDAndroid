package com.mcn.honeydew.ui.common_app_settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcn.honeydew.R;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommonAppSettingsFragment extends BaseFragment implements CommonAppSettingsMvpView {

    @Inject
    CommonAppSettingsMvpPresenter<CommonAppSettingsMvpView> mPresenter;

    @Override
    protected void setUp(View view) {

    }

    public static CommonAppSettingsFragment newInstance() {
        CommonAppSettingsFragment fragment = new CommonAppSettingsFragment();
        Bundle args = new Bundle();
       /* args.putInt("listId", listId);
        args.putString("colorCode", colorCode);*/
      //  fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (getArguments() != null) {
            mListId = getArguments().getInt("listId");
            mColorCode = getArguments().getString("colorCode");

            if (!TextUtils.isEmpty(mColorCode) && !mColorCode.contains("#")) {
                mColorCode = "#".concat(mColorCode);
            }

        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_common_app_settings, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);

            setUp(view);

        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.account_layout)
    public void onAccountLayoutclicked(){

        ((MainActivity)getBaseActivity()).onAccountSettingclicked();

    }

    @OnClick(R.id.reminder_layout)
    public void onReminderLayoutClicked(){

        ((MainActivity)getBaseActivity()).onRemindersClicked();

    }

    @OnClick(R.id.app_support_layout)
    public void onAppSupportLayoutClicked(){

    }

    @OnClick(R.id.suggestions_layout)
    public void onSuggestionLayoutClicked(){

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getBaseActivity()).changeTitle("App Settings");
    }
}
