package com.mcn.honeydew.ui.addItems.addItemsRecentChild;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.response.RecentItemsResponse;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.addItems.AddItemsFragment;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.utils.ScreenUtils;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by gkumar on 9/3/18.
 */

public class AddRecentItemsChildFragment extends BaseFragment implements AddRecentItemsMvpView,
        AddRecentItemsAdapter.Callback, Filterable {

    public static AddRecentItemsChildFragment newInstance() {
        Bundle args = new Bundle();
        AddRecentItemsChildFragment fragment = new AddRecentItemsChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private static final int SPEECH_REQUEST_CODE = 104;

    private boolean mKeyboardVisible = false;

    ArrayList<String> mList = new ArrayList<>();


    @Inject
    AddRecentItemsMvpPresenter<AddRecentItemsMvpView> mPresenter;

    @BindView(R.id.voice_recognition_image)
    ImageView voiceRecognitionImage;

    private TextFilter mFilter;
    @BindView(R.id.search_edit_text)
    EditText mEditText;

    @BindView(R.id.delete_image_view)
    ImageView deleteImageView;


    @BindView(R.id.empty_space_view)
    View emptySpaceView;

    @Inject
    LinearLayoutManager mLayoutManager;

    @Inject
    AddRecentItemsAdapter mAdapter;

    private View view;

    @BindView(R.id.empty_recent_data)
    TextView emptyView;

    @BindView(R.id.loopView)
    LoopView mLoopView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_items_recent_child_fragment, container, false);
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
        int totalHeight = ScreenUtils.getScreenHeight(getActivity());
        int availiableHeight = (int) (totalHeight - (ScreenUtils.getStatusBarHeight(getActivity()) + (2 * (ScreenUtils.getActionBarHeight(getActivity())))));
        emptySpaceView.getLayoutParams().height = (int) ((availiableHeight * 2.0) / 5.0);
        emptySpaceView.requestLayout();
        if (((AddItemsFragment) getParentFragment()).getMyListData().getItemName() != null) {
            mEditText.setText(((AddItemsFragment) getParentFragment()).getMyListData().getItemName());
            ((AddItemsFragment) getParentFragment()).setItemName(mEditText.getText().toString().trim());
        }

        mEditText.setTag(false);
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mEditText.setTag(true);
            }
        });
        mEditText.addTextChangedListener(mTextWatcher);

        mPresenter.onViewPrepared();
        mLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (mList.size() == 0 || mEditText == null) {
                    return;
                }

                mEditText.removeTextChangedListener(mTextWatcher);

                mEditText.setText(mList.get(index).trim());

                ((AddItemsFragment) getParentFragment()).setItemName(mList.get(index).trim());
                mEditText.addTextChangedListener(mTextWatcher);
            }
        });

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {
                        onDoneRecentClicked();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub

            if ((Boolean) mEditText.getTag()) {
                getFilter().filter(s);
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

            ((AddItemsFragment) getParentFragment()).setItemName(s.toString().trim());

        }
    };


    @Override
    public void onItemClick(RecentItemsResponse.RecentItemsData data) {

    }


    /**
     * "Record" button click
     */
    @OnClick(R.id.voice_recognition_image)
    public void onRecordVoice() {

        PackageManager pm = getBaseActivity().getPackageManager();

        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(

                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

        if (activities.size() == 0) {
            Toast.makeText(getActivity(), "Recognizer Not Found", Toast.LENGTH_SHORT).show();

        } else {
            voiceRecognitionImage.setImageResource(R.drawable.ic_voice_recognition);
            displaySpeechRecognizer();
        }

    }

    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "HoneyDew\nPlease speak something!");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @OnClick(R.id.delete_image_view)
    public void onDeleteCalled() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle(getActivity().getResources().getString(R.string.app_title));
        dialog.setMessage("Are you sure that you want to delete your popular items?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mPresenter.deleteRecentItems();
                dialog.dismiss();
            }
        })
                .setNegativeButton("No ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                        dialog.dismiss();

                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        voiceRecognitionImage.setImageResource(R.drawable.ic_voice_recognition_disable);
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            String spokenText = capitalize(results.get(0).toString().trim());
            mEditText.setText("");

            mEditText.setText(spokenText);
            getFilter().filter(spokenText);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void updateView(List<RecentItemsResponse.RecentItemsData> mlists) {
        if (!mList.isEmpty()) {
            mList.clear();

        }

        for (int i = 0; i < mlists.size(); i++) {
            mList.add(mlists.get(i).getItemName());

        }
        mLoopView.setItems(mList);

        int pos = 0;
        if (mList.size() > 0 && mEditText.getText().toString().trim().length() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).toLowerCase().equals(mEditText.getText().toString().trim().toLowerCase())) {
                    pos = i;
                }
            }

        }


        mLoopView.setInitPosition(pos);

        mLoopView.setNotLoop();

        if (mList.size() > 0) {

            emptyView.setVisibility(View.GONE);
            deleteImageView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
            deleteImageView.setVisibility(View.GONE);
        }

    }

    @Override
    public void itemSuccesfullyAdded() {

        AddItemsFragment mParentFragment = ((AddItemsFragment) getParentFragment());
        mParentFragment.itemSuccesfullyAdded();
        ((MainActivity) getActivity()).syncItems();

    }

    @Override
    public void recentItemsDeleted(int status) {

        if (status == 1) {
            mList.clear();
            //  mAdapter.replaceData(mList);
            mLoopView.replaceData(mList);

            emptyView.setVisibility(View.VISIBLE);
            deleteImageView.setVisibility(View.GONE);


        }

    }

    @Override
    public void addItemsCallFailed() {
        AddItemsFragment mParentFragment = ((AddItemsFragment) getParentFragment());
        if (mParentFragment != null) {
            mParentFragment.enableDisableDoneButton(true);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        view.getViewTreeObserver()
                .addOnGlobalLayoutListener(mLayoutKeyboardVisibilityListener);
        AddItemsFragment mParentFragment = ((AddItemsFragment) getParentFragment());
        if (mParentFragment.getMyListData().getItemId() != 0) {
            mParentFragment.setActionbarTitle(getResources().getString(R.string.recent_items_fragment_edit_title));

        } else {
            mParentFragment.setActionbarTitle(getResources().getString(R.string.recent_items_actionbar_heading));
        }

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

        emptySpaceView.setVisibility(View.GONE);
        ((MainActivity) getActivity()).hideTabs();
    }

    private void onKeyboardHidden() {
        emptySpaceView.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).showTabs();
    }

    public void onDoneRecentClicked() {

        if (mEditText.getText().toString().trim().equals("")) {
            showDialog();
            return;
        }
        AddItemsFragment fragment = ((AddItemsFragment) getParentFragment());
        fragment.enableDisableDoneButton(false);
        hideKeyboard();

        fragment.setItemName(mEditText.getText().toString().trim());

        if (fragment.getMyListData().getItemId() != 0) {

            // Editing item
            mPresenter.onAddItems(fragment.getMyListData().getItemId(), mEditText.getText().toString().trim(),
                    fragment.getDateTimeText(), fragment.getMyListData().getLatitude(), fragment.getMyListData().getListId(), fragment.getMyListData().getListName(),
                    fragment.getMyListData().getLocation(), fragment.getMyListData().getLongitude(),
                    fragment.getMyListData().getStatusId());
        } else {

            // Creating new item
            mPresenter.onAddItems(0, mEditText.getText().toString().trim(),
                    "", "", fragment.getMyListData().getListId(), fragment.getMyListData().getListName(), "", "", 0);
        }


    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle(getActivity().getResources().getString(R.string.app_title));
        dialog.setMessage("Please type or choose an item from most popular items list.");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });


        final AlertDialog alert = dialog.create();
        alert.show();

    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new TextFilter(mList);
        }
        return mFilter;
    }

    public class TextFilter extends Filter {

        private final List<String> originalList;

        private final List<String> filteredList;

        public TextFilter(ArrayList<String> parentList) {

            this.originalList = new LinkedList<>(parentList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase();

                for (final String dataModel : originalList) {
                    if (dataModel.toLowerCase().contains(filterPattern)) {
                        filteredList.add(dataModel);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mList.clear();
            mList.addAll((ArrayList<String>) results.values);
            mLoopView.replaceData(mList);

        }
    }


    /**
     * making the first letter capital of the spoken text results
     **/
    private String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }
}
