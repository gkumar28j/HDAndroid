package com.mcn.honeydew.ui.colorSettings;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcn.honeydew.R;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.ui.main.MainActivity;
import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ColorSettingsFragment extends BaseFragment implements ColorSettingsMvpView {

    private int mListId;
    private String mColorCode;

    @BindView(R.id.colorPickerView)
    ColorPickerView colorPickerView;

   /* @BindView(R.id.preview)
    View colorPreview;*/

    private boolean isTouched = false;

    @Inject
    ColorSettingsMvpPresenter<ColorSettingsMvpView> mPresenter;


    public static ColorSettingsFragment newInstance(int listId, String colorCode) {
        ColorSettingsFragment fragment = new ColorSettingsFragment();
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
            mColorCode = getArguments().getString("colorCode");

            if (!TextUtils.isEmpty(mColorCode) && !mColorCode.contains("#")) {
                mColorCode = "#".concat(mColorCode);
            }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_color_settings, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);

            setUp(view);

            /*colorPickerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    isTouched = true;
                    return true;
                }
            });*/

            colorPickerView.setColorListener(new ColorListener() {
                @Override
                public void onColorSelected(ColorEnvelope colorEnvelope) {

                    if (!isAdded())
                        return;

                    String colorString = colorEnvelope.getColorHtml();

                   /* if (!isTouched)
                        return;*/

                    if (colorString.equalsIgnoreCase("FFFFFF") || colorString.equalsIgnoreCase("FFFFFE")) {
                        return;
                    }
                 //   colorPreview.setBackgroundColor(colorEnvelope.getColor());
                    mColorCode = "#".concat(colorPickerView.getColorHtml());
                    ((MainActivity) getActivity()).updateColorCode(mColorCode);

                    mPresenter.updateListColor("#" + colorPickerView.getColorHtml(), mListId);
                }
            });

        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (colorPickerView != null)
            colorPickerView.saveData();
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

    @Override
    protected void setUp(View view) {
        if (!isAdded())
            return;

        colorPickerView.setPreferenceName("list_color");

        // Saving color code
        if (!TextUtils.isEmpty(mColorCode)) {
            colorPickerView.setSavedColor(Color.parseColor(mColorCode));
           // colorPreview.setBackgroundColor(Color.parseColor(mColorCode));
        }
    }

}
