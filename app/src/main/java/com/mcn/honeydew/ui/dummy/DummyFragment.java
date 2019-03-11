package com.mcn.honeydew.ui.dummy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcn.honeydew.R;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;


/**
 * Created by amit on 7/3/18.
 */

public class DummyFragment extends BaseFragment implements DummyMvpView {

    private static final String TAG = "ShareListFragment";

    @Inject
    DummyMvpPresenter<DummyMvpView> mPresenter;


    public static DummyFragment newInstance() {
        Bundle args = new Bundle();
        DummyFragment fragment = new DummyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dummy, container, false);

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
