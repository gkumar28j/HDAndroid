package com.mcn.honeydew.ui.addlist;

import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.utils.KeyboardUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by amit on 20/2/18.
 */

public class AddListFragment extends BaseFragment implements AddListMvpView {

    public static final String TAG = "AddListFragment";

    @Inject
    AddListMvpPresenter<AddListMvpView> mPresenter;

    @BindView(R.id.bottom_layout)
    RelativeLayout mbottomLayout;

    @BindView(R.id.add_list_edit_text)
    EditText mEditText;

    @BindView(R.id.done_text_view)
    TextView mDoneButton;

    @BindView(R.id.cancel_text_view)
    TextView mCancelButton;

    private View view;
    private boolean mKeyboardVisible = false;

    private int listId = 0;

   /* @BindView(R.id.add_items_view)
    TextView mAddItemsTextView;*/

    public static AddListFragment newInstance() {
        Bundle args = new Bundle();
        AddListFragment fragment = new AddListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getBaseActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        view = inflater.inflate(R.layout.fragment_add_list, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);

            setUp(view);
        }
        return view;
    }

    @OnClick(R.id.done_text_view)
    public void onDoneButtonClicked() {

        if (mEditText.getText().toString().trim().equals("") || mEditText.getText().toString().trim() == null) {
            showMessage(getString(R.string.error_empty_list_name));
        } else {
            mPresenter.onViewPrepared(mEditText.getText().toString().trim(), listId, "#153359", 12);
        }


    }


    @Override
    protected void setUp(View view) {
        //  mAddItemsTextView.setVisibility(View.GONE);

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {
                        onDoneButtonClicked();
                        return true;
                    }
                }

                return false;
            }
        });


    }


    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        view.getViewTreeObserver()
                .addOnGlobalLayoutListener(mLayoutKeyboardVisibilityListener);
        KeyboardUtils.showSoftInput(mEditText, getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();

        if (getActivity() != null) {
            hideKeyboard();
            ((MainActivity) getActivity()).showTabs();
        }

        view.getViewTreeObserver()
                .removeOnGlobalLayoutListener(mLayoutKeyboardVisibilityListener);
    }

    private final ViewTreeObserver.OnGlobalLayoutListener mLayoutKeyboardVisibilityListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {

            final Rect rectangle = new Rect();
            final View contentView = view;
            contentView.getWindowVisibleDisplayFrame(rectangle);
            int screenHeight = contentView.getRootView().getHeight();

            // r.bottom is the position above soft keypad or device button.
            // If keypad is shown, the rectangle.bottom is smaller than that before.
            int keypadHeight = screenHeight - rectangle.bottom;
            // 0.15 ratio is perhaps enough to determine keypad height.
            boolean isKeyboardNowVisible = keypadHeight > screenHeight * 0.15;

            if (mKeyboardVisible != isKeyboardNowVisible) {
                if (isKeyboardNowVisible) {
                    onKeyboardShown();
                } else {
                    onKeyboardHidden();
                }
            }

            mKeyboardVisible = isKeyboardNowVisible;
        }
    };

    private void onKeyboardShown() {
        mbottomLayout.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).hideTabs();
    }

    private void onKeyboardHidden() {
        mbottomLayout.setVisibility(View.GONE);
        ((MainActivity) getActivity()).showTabs();
    }

    @Override
    public void openAddItemFragment(int newItemId) {
        getBaseActivity().hideKeyboard();
        Calendar cal = Calendar.getInstance();
        Date listCreationDate = cal.getTime();
        // createdDate = "2018-03-20T08:38:35"
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = mFormat.format(listCreationDate).replace(" ", "T");
        mPresenter.createMyListData(date, mEditText.getText().toString().trim(), newItemId, Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.colorPrimary)));
        mEditText.setText("");
    }

    @Override
    public void openMyList(MyHomeListData data) {
        getBaseActivity().onListItemClick(data);
    }


    @OnClick(R.id.cancel_text_view)
    public void onCancelTextClicked() {
        getBaseActivity().hideKeyboard();

    }

    /*@OnClick(R.id.add_items_view)
    public void onAddItemsClicked() {
      //  getBaseActivity().hideKeyboard();
        getBaseActivity().onAddItemsClicked(listId);

    }*/
}
