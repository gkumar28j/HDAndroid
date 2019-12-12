package com.mcn.honeydew.ui.addItems.addItemsWhere;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.response.CustomLocationData;
import com.mcn.honeydew.data.network.model.response.RecentLocationAddItemsResponse;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.addItems.AddItemsFragment;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.utils.AppConstants;
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

public class AddItemsWhereFragment extends BaseFragment implements AddItemsWhereMvpView,
        AddItemsRecentLocationAdapter.Callback, Filterable {

    public static AddItemsWhereFragment newInstance(Double lat, Double lng, int listId, String listName) {
        Bundle args = new Bundle();
        args.putDouble(AddItemsFragment.KEY_LATITUDE, lat);
        args.putDouble(AddItemsFragment.KEY_LONGITUDE, lng);
        args.putInt(AppConstants.KEY_LIST_ID, listId);
        args.putString(AppConstants.KEY_LIST_NAME, listName);
        AddItemsWhereFragment fragment = new AddItemsWhereFragment();
        fragment.setArguments(args);
        return fragment;
    }

    ArrayList<String> tempRecentList = new ArrayList<>();
    ArrayList<String> tempNearByList = new ArrayList<>();
    private boolean mKeyboardVisible = false;
    private static final int SPEECH_REQUEST_CODE = 104;
    @BindView(R.id.delete_image_view)
    ImageView mDeleteData;
    private TextFilter mFilter;
    private TextFilterNearby textFilterNearby;
    ArrayList<CustomLocationData> mRecentLocationList = new ArrayList<>();
    ArrayList<CustomLocationData> mNearbyLocationsList = new ArrayList<>();
    private boolean mIsRecentLocation = true;

    @Inject
    AddItemsWhereMvpPresenter<AddItemsWhereMvpView> mPresenter;

    @BindView(R.id.bottom_voice_lay)
    LinearLayout mVoiceLayout;

    @BindView(R.id.voice_recognition_image)
    ImageView voiceRecognitionImageView;


    @BindView(R.id.search_edit_text)
    EditText mEditText;

    @BindView(R.id.recent_search_heading_text_view)
    TextView searchHeadingTextView;

    @BindView(R.id.empty_space_view)
    View emptySpaceView;

    @Inject
    LinearLayoutManager mLayoutManager;


    @Inject
    AddItemsRecentLocationAdapter mAdapter;


    Double latitude, longitude;

    private int listId;
    private String listName = "";


    @BindView(R.id.empty_location_data)
    TextView emptyTextView;


    @BindView(R.id.loopView)
    LoopView mLoopView;

    @BindView(R.id.add_item_title_textview)
    TextView headingTextView;

    String previousLocation = "";
    String tempLocation = "";
    View view;
    double screenInches;
    // int pos = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_items_where_fragment, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {

            if (getArguments() != null) {

                latitude = getArguments().getDouble(AddItemsFragment.KEY_LATITUDE);
                longitude = getArguments().getDouble(AddItemsFragment.KEY_LONGITUDE);
                listId = getArguments().getInt(AppConstants.KEY_LIST_ID);
                listName = getArguments().getString(AppConstants.KEY_LIST_NAME);
            }
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
            setUp(view);
        }
        return view;
    }

    @Override
    protected void setUp(View view) {

        screenInches = ScreenUtils.getScreenSizeInInches(getBaseActivity());


       /* int availiableHeight = (int) (totalHeight - (ScreenUtils.getStatusBarHeight(getActivity()) + (2 * (ScreenUtils.getActionBarHeight(getActivity())))));
        emptySpaceView.getLayoutParams().height = (int) ((availiableHeight * 2.0) / 5.0);
        emptySpaceView.requestLayout();*/

        if (screenInches >= 5.5) {
            emptySpaceView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.add_items_where_fragment_empty_space_height_large_screen);
            emptySpaceView.requestLayout();
        } else {
            emptySpaceView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.add_items_where_fragment_empty_space_height_small_screen);
            emptySpaceView.requestLayout();
        }


        if (((AddItemsFragment) getParentFragment()).getMyListData().getLocation() != null) {
            String loc = ((AddItemsFragment) getParentFragment()).getMyListData().getLocation();
            previousLocation = loc;
            mEditText.setText(loc);
            ((AddItemsFragment) getParentFragment()).setLocation(loc);
        }
        if (((AddItemsFragment) getParentFragment()).getMyListData().getItemName() != null) {
            headingTextView.setText("Edit Items");
        } else {
            headingTextView.setText(getString(R.string.recent_items_actionbar_heading));
        }
        mEditText.setTag(false);
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mEditText.setTag(true);
            }
        });
        mEditText.addTextChangedListener(mTextWatcher);

        mPresenter.fetchRecentLocations(screenInches);
        mLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {

                String locationName;

                if (searchHeadingTextView.getText().toString().equalsIgnoreCase("nearby locations")) {
                    if (mNearbyLocationsList.size() == 0) {
                        return;
                    }

                    locationName = mNearbyLocationsList.get(index).getMainData();
                } else {
                    if (mRecentLocationList.size() == 0) {
                        return;
                    }

                    locationName = mRecentLocationList.get(index).getMainData();
                }


                mEditText.removeTextChangedListener(mTextWatcher);

                mEditText.setText(locationName);

                ((AddItemsFragment) getParentFragment()).setLocation(locationName);
                mEditText.addTextChangedListener(mTextWatcher);
               /* if (!searchHeadingTextView.getText().toString().equalsIgnoreCase("nearby locations")) {
                    pos = index

                }*/
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
                        onDoneClicked();
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
            if ((Boolean) mEditText.getTag())
                getFilter().filter(s);

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

            ((AddItemsFragment) getParentFragment()).setLocation(s.toString().trim());
            if (!searchHeadingTextView.getText().toString().equalsIgnoreCase("nearby locations")) {

                tempLocation = s.toString().trim();

            }


        }
    };

    /**
     * "Delete" button click
     */
    @OnClick(R.id.delete_image_view)
    public void onDeleteClicked() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle(getActivity().getResources().getString(R.string.app_title));
        dialog.setMessage("Are you sure that you want to delete your popular items?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                mPresenter.deleteRecentLocation();
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
    public void updateAdapterViews(List<CustomLocationData> mlists) {
        mRecentLocationList.clear();

        mRecentLocationList = (ArrayList<CustomLocationData>) mlists;

        for (int i = 0; i < mRecentLocationList.size(); i++) {

            tempRecentList.add(mRecentLocationList.get(i).getModifiedData());

        }


        setRecentLocationListData();
    }

    private void setRecentLocationListData() {

        mLoopView.setItems(tempRecentList);

        int pos = -1;

        if (mRecentLocationList.size() > 0 && previousLocation.trim().length() > 0) {
            for (int i = 0; i < mRecentLocationList.size(); i++) {
                if (mRecentLocationList.get(i).getMainData().toLowerCase().equals(previousLocation.trim().toLowerCase())) {
                    pos = i;
                    break;
                }
            }

        }

        if (pos == -1 && !previousLocation.equals("")) {
            mLoopView.setInitPosition(0);
            mEditText.setText(previousLocation);
        } else if (pos == -1 && previousLocation.equals("")) {
            mLoopView.setInitPosition(0);
            mEditText.setText("");
        } else {
            mLoopView.setInitPosition(pos);
            mEditText.setText(mRecentLocationList.get(pos).getMainData());
        }


        mLoopView.setNotLoop();

        if (mRecentLocationList.size() > 0) {
            mDeleteData.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        } else {
            emptyTextView.setVisibility(View.VISIBLE);
            mDeleteData.setVisibility(View.GONE);
        }
    }

    @Override
    public void receiveLatLngs(List<RecentLocationAddItemsResponse.LocationResult> results) {
        AddItemsFragment fragment = ((AddItemsFragment) getParentFragment());
        if (results.isEmpty()) {
            showMessage("We are not getting address on google map. Please enter valid address.");
            fragment.enableDisableDoneButton(true);
            return;
        }


        String DateTimeText = fragment.getDateTimeText();

        String itemName = fragment.getItemName();

        int itemId = fragment.getMyListData().getItemId();

        StringBuilder latitude = new StringBuilder();
        StringBuilder longitude = new StringBuilder();

        for (int i = 0; i < results.size(); i++) {
            latitude.append(results.get(i).getGeometry().getLocation().getLat());
            latitude.append(",");
            longitude.append(results.get(i).getGeometry().getLocation().getLng());
            longitude.append(",");


        }
        String lat = latitude.substring(0, latitude.length() - 1);
        String lng = longitude.substring(0, longitude.length() - 1);

        String url = fragment.getFilePath();

        mPresenter.onAddItems(itemId, itemName, DateTimeText, lat, listId, listName,
                mEditText.getText().toString().trim(), lng, fragment.getMyListData().getStatusId(), url);


    }

    @Override
    public void onReceivedNearbySearch(List<CustomLocationData> nearBySearch) {
        mNearbyLocationsList.clear();

        mNearbyLocationsList = (ArrayList<CustomLocationData>) nearBySearch;
        tempNearByList.clear();

        for (int i = 0; i < mNearbyLocationsList.size(); i++) {

            tempNearByList.add(mNearbyLocationsList.get(i).getModifiedData());

        }
        setNearbyListData();

    }


    private void setNearbyListData() {

        mLoopView.setItems(tempNearByList);
        mLoopView.setNotLoop();

        if (mNearbyLocationsList.size() > 0) {
            emptyTextView.setVisibility(View.GONE);

        } else {
            emptyTextView.setVisibility(View.VISIBLE);
        }

        mEditText.getText().clear();

        if (mNearbyLocationsList.size() > 0) {
            mLoopView.setCurrentPosition(0);
            mLoopView.setInitPosition(0);
            mEditText.setText(mNearbyLocationsList.get(0).getMainData());
        }
        mEditText.addTextChangedListener(mTextWatcher);
    }

    @Override
    public void itemSuccesfullyAdded() {

        ((AddItemsFragment) getParentFragment()).itemSuccesfullyAdded();
        ((MainActivity) getActivity()).syncItems();

    }

    @Override
    public void recentLocationDelted(int status) {
        if (status == 1) {
            mRecentLocationList.clear();
            tempRecentList.clear();
            mLoopView.replaceData(tempRecentList);
            emptyTextView.setVisibility(View.VISIBLE);
            mDeleteData.setVisibility(View.GONE);
        }

    }

    @Override
    public void addItemsCallFailed() {
        AddItemsFragment fragment = ((AddItemsFragment) getParentFragment());
        if (fragment != null) {
            fragment.enableDisableDoneButton(true);
        }

    }

    public void onLocationImageViewClicked(Location location) {

        if (searchHeadingTextView.getText().toString().equalsIgnoreCase("nearby locations")) {
            // Show recent location list
            mEditText.removeTextChangedListener(mTextWatcher);
            searchHeadingTextView.setText(getString(R.string.your_most_popular_locations));
            mDeleteData.setVisibility(View.VISIBLE);
            mLoopView.setItems(tempRecentList);
            mLoopView.setNotLoop();

            if (tempLocation.length() > 0) {
                String loc = tempLocation;
                mLoopView.setCurrentPosition(0);
                mLoopView.setInitPosition(0);
                mEditText.setText(loc);
                tempLocation = mEditText.getText().toString().trim();
            } else {
                mLoopView.setCurrentPosition(0);
                mLoopView.setInitPosition(0);

                if (mRecentLocationList != null && mRecentLocationList.size() > 0) {
                    mDeleteData.setVisibility(View.VISIBLE);
                    mEditText.setText(mRecentLocationList.get(0).getMainData());
                } else {
                    mDeleteData.setVisibility(View.GONE);
                }

            }

            mEditText.addTextChangedListener(mTextWatcher);

        } else {
            // show nearby location list
            mEditText.removeTextChangedListener(mTextWatcher);
            mDeleteData.setVisibility(View.GONE);
            changeTitleHeadingText();

            if (!mNearbyLocationsList.isEmpty()) {
                setNearbyListData();
                return;
            }

            if (latitude != 0.0 && longitude != 0.0) {
                mPresenter.getNearbySearchResults(latitude, longitude, AppConstants.GOOGLE_NEARBY_SEARCH_RADIUS, AppConstants.PLACE_KEY, "store", screenInches);
            } else {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                mPresenter.getNearbySearchResults(latitude, longitude, AppConstants.GOOGLE_NEARBY_SEARCH_RADIUS, AppConstants.PLACE_KEY, "store", screenInches);

            }
        }

    }

    public void onDoneClicked() {

        if (mEditText.getText().toString().trim().equals("")) {
            showDialog();
            return;
        }
        AddItemsFragment fragment = ((AddItemsFragment) getParentFragment());
        fragment.enableDisableDoneButton(false);
        hideKeyboard();

        String DateTimeText = fragment.getDateTimeText();

        String itemName = fragment.getItemName();

        int itemId = fragment.getMyListData().getItemId();

        String url = fragment.getFilePath();

        if (fragment.getMyListData().getLocation() != null && fragment.getMyListData().getLocation().equals(mEditText.getText().toString().trim())) {
            mPresenter.onAddItems(itemId, itemName, DateTimeText, fragment.getMyListData().getLatitude(), listId, listName,
                    mEditText.getText().toString().trim(), fragment.getMyListData().getLongitude(), fragment.getMyListData().getStatusId(), url);
        } else {
            mPresenter.fetchLatLng(mEditText.getText().toString().trim(), latitude, longitude, AppConstants.GOOGLE_NEARBY_SEARCH_RADIUS, AppConstants.PLACE_KEY);
        }


    }


    @Override
    public void onItemClick(String data, int layoutPosition) {

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
            voiceRecognitionImageView.setImageResource(R.drawable.ic_voice_recognition);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        voiceRecognitionImageView.setImageResource(R.drawable.ic_voice_recognition_disable);

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

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle(getActivity().getResources().getString(R.string.app_title));
        dialog.setMessage(R.string.location_selection_text_empty_message);
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
    public void onResume() {
        super.onResume();
        view.getViewTreeObserver()
                .addOnGlobalLayoutListener(mLayoutKeyboardVisibilityListener);
        //   ((AddItemsFragment) getParentFragment()).setActionbarTitle(getResources().getString(R.string.recent_location_actionbar_heading));
        ((AddItemsFragment) getParentFragment()).showLocationState();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (getActivity() != null) {
            hideKeyboard();
            ((MainActivity) getActivity()).showHideTitle(true);
            ((MainActivity) getActivity()).showTabs();
        }

        view.getViewTreeObserver()
                .removeOnGlobalLayoutListener(mLayoutKeyboardVisibilityListener);
        //  ((AddItemsFragment) getParentFragment()).setActionbarTitle(getResources().getString(R.string.recent_items_actionbar_heading));

    }

    @Override
    public Filter getFilter() {

        if (searchHeadingTextView.getText().toString().equalsIgnoreCase("nearby locations")) {

            if (textFilterNearby == null) {
                textFilterNearby = new TextFilterNearby(mNearbyLocationsList);
                return textFilterNearby;

            } else {

                return textFilterNearby;
            }


        } else {

            if (mFilter == null) {
                mFilter = new TextFilter(mRecentLocationList);
                return mFilter;
            } else {
                return mFilter;
            }

        }


    }

    public class TextFilter extends Filter {

        private final List<CustomLocationData> originalList;

        private final List<CustomLocationData> filteredList;

        public TextFilter(ArrayList<CustomLocationData> parentList) {

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

                for (final CustomLocationData dataModel : originalList) {
                    if (dataModel.getMainData().toLowerCase().contains(filterPattern)) {
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
/*            if (searchHeadingTextView.getText().toString().equalsIgnoreCase("nearby locations")) {

                mNearbyLocationsList.clear();
                mNearbyLocationsList.addAll((ArrayList<CustomLocationData>) results.values);
                tempNearByList.clear();
                for (int i = 0; i < mNearbyLocationsList.size(); i++) {

                    tempNearByList.add(mNearbyLocationsList.get(i).getModifiedData());

                }
                mLoopView.replaceData(tempNearByList);

            }else {*/
            mRecentLocationList.clear();
            mRecentLocationList.addAll((ArrayList<CustomLocationData>) results.values);
            tempRecentList.clear();
            for (int i = 0; i < mRecentLocationList.size(); i++) {

                tempRecentList.add(mRecentLocationList.get(i).getModifiedData());

            }
            mLoopView.replaceData(tempRecentList);
            //   }


        }
    }
// new filter class

    public class TextFilterNearby extends Filter {

        private final List<CustomLocationData> originalList;

        private final List<CustomLocationData> filteredList;

        public TextFilterNearby(ArrayList<CustomLocationData> parentList) {

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

                for (final CustomLocationData dataModel : originalList) {
                    if (dataModel.getMainData().toLowerCase().contains(filterPattern)) {
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
            if (searchHeadingTextView.getText().toString().equalsIgnoreCase("nearby locations")) {

                mNearbyLocationsList.clear();
                mNearbyLocationsList.addAll((ArrayList<CustomLocationData>) results.values);
                tempNearByList.clear();
                for (int i = 0; i < mNearbyLocationsList.size(); i++) {

                    tempNearByList.add(mNearbyLocationsList.get(i).getModifiedData());

                }
                mLoopView.replaceData(tempNearByList);

            }


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
        ((MainActivity) getBaseActivity()).showHideTitle(false);
        emptySpaceView.setVisibility(View.GONE);
        headingTextView.setVisibility(View.GONE);
        ((MainActivity) getActivity()).hideTabs();
    }

    private void onKeyboardHidden() {
        ((MainActivity) getBaseActivity()).showHideTitle(true);
        emptySpaceView.setVisibility(View.VISIBLE);
        headingTextView.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).showTabs();
    }

    public void changeTitleHeadingText() {
        final SpannableString spannableString1 = new SpannableString(getResources().getString(R.string.nearby_loc_text));
        spannableString1.setSpan(new RelativeSizeSpan(1.2f), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        final SpannableString spannableString2 = new SpannableString(getResources().getString(R.string.nearby_locations_results));
        spannableString2.setSpan(new RelativeSizeSpan(1.2f), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        searchHeadingTextView.setText("");
        searchHeadingTextView.setText(TextUtils.concat(spannableString1, " ", spannableString2), TextView.BufferType.SPANNABLE);
    }
}
