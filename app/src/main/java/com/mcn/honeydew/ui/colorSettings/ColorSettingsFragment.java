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
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/*import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerView;*/

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

            colorPickerView.setColorListener(new ColorEnvelopeListener() {
                @Override
                public void onColorSelected(ColorEnvelope colorEnvelope, boolean fromuser) {

                    if (!isAdded())
                        return;

                    int colorInt = colorEnvelope.getColor();


                    String colorString = convertColorToString(colorInt);


                   /* if (!isTouched)
                        return;*/

                    if (colorString.equalsIgnoreCase("FFFFFF") || colorString.equalsIgnoreCase("FFFFFE")) {
                        return;
                    }
                    //   colorPreview.setBackgroundColor(colorEnvelope.getColor());
                    mColorCode = "#".concat(colorString);
                    ((MainActivity) getActivity()).updateColorCode(mColorCode);

                    mPresenter.updateListColor("#" + colorString, mListId);
                }
            });

        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (colorPickerView != null) {
            //   colorPickerView.saveData();
        }

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
            //   colorPickerView.setSavedColor(Color.parseColor(mColorCode));
            colorPickerView.setPureColor(Color.parseColor(mColorCode));
            // colorPreview.setBackgroundColor(Color.parseColor(mColorCode));
        }
    }

    private String convertColorToString(int color) {
        String colorString = null;
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        colorString = String.format(Locale.getDefault(), "%02X%02X%02X", r, g, b);
        return colorString;


    }

}
