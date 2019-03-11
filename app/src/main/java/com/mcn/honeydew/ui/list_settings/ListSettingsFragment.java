package com.mcn.honeydew.ui.list_settings;


import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.response.GetListSettingsResponse;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ListSettingsFragment extends BaseFragment implements ListSettingsMvpView {

    @Inject
    ListSettingsMvpPresenter<ListSettingsMvpView> mPresenter;
    private int mListId;

    @BindView(R.id.switch_in_progress)
    SwitchCompat mInProgressSwitchCompat;

    @BindView(R.id.layout_switch)
    RelativeLayout mSwitchRelativeLayout;

    public ListSettingsFragment() {
        // Required empty public constructor
    }

    public static ListSettingsFragment newInstance(int listId, String colorCode) {
        ListSettingsFragment fragment = new ListSettingsFragment();
        Bundle args = new Bundle();
        args.putInt("listId", listId);
        args.putString("colorCode", colorCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mListId = getArguments().getInt("listId");
        }
    }

    @Override
    protected void setUp(View view) {
        mPresenter.getListSettings(mListId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_settings, container, false);
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
    public void onListSettingFetched(GetListSettingsResponse.ListSettings settings) {

        mInProgressSwitchCompat.setChecked(settings.isInProgress());

       /* if (((MainActivity) getBaseActivity()).isOwner) {
            mInProgressSwitchCompat.setEnabled(true);
            mSwitchRelativeLayout.setAlpha(1f);
        } else {
            mInProgressSwitchCompat.setEnabled(false);
            mSwitchRelativeLayout.setAlpha(0.5f);
        }
*/
    }


    @OnClick(R.id.switch_in_progress)
    void onSwitchClicked() {
        mPresenter.updateSettings(mListId, mInProgressSwitchCompat.isChecked());
    }
}
