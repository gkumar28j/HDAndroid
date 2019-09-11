package com.mcn.honeydew.ui.in_progress;


import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcn.honeydew.R;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InProgressFragment extends BaseFragment implements InProgressMvpView {

    public static InProgressFragment newInstance() {
        InProgressFragment fragment = new InProgressFragment();
      /*  Bundle args = new Bundle();
        args.putInt("listId", listId);
        args.putString("colorCode", colorCode);
        fragment.setArguments(args);*/
        return fragment;
    }


    @Inject
    InProgressMvpPresenter<InProgressMvpView> mPresenter;


    @BindView(R.id.toggle_progress)
    SwitchCompat progressToggleSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        /*    mListId = getArguments().getInt("listId");
            mColorCode = getArguments().getString("colorCode");

            if (!TextUtils.isEmpty(mColorCode) && !mColorCode.contains("#")) {
                mColorCode = "#".concat(mColorCode);
            }*/

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_progress, container, false);

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
    protected void setUp(View view) {

    }


    @Override
    public void onDestroyView() {

        mPresenter.onDetach();
        super.onDestroyView();
    }
}