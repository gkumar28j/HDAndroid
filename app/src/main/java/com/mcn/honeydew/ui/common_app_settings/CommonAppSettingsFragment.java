package com.mcn.honeydew.ui.common_app_settings;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcn.honeydew.BuildConfig;
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

        String url = "http://www.myhoneydew.us/support";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

    }

    @OnClick(R.id.suggestions_layout)
    public void onSuggestionLayoutClicked(){

        startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:support@myhoneydew.us")));

    }

   /* @OnClick(R.id.write_review_lay)
    public void onWriteReviewApp(){

        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }

    }*/

    /*@OnClick(R.id.share_app_lay)
    public void onShareAppClicked(){

        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "HoneyDew List");
            String shareMessage= "\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }

    }*/

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getBaseActivity()).changeTitle("App Settings");
    }


    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getBaseActivity().getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }
}
