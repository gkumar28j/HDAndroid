package com.mcn.honeydew.ui.addItems.addItemsRecentChild;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.response.RecentItemsResponse;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.addItems.AddItemsFragment;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.utils.AppConstants;
import com.mcn.honeydew.utils.ImageUtils;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import static androidx.core.content.ContextCompat.checkSelfPermission;

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
    private static final int REQUEST_CAMERA = 124;
    private static final int SELECT_FILE = 125;
    private boolean mKeyboardVisible = false;

    ArrayList<String> mList = new ArrayList<>();

    String currentPhotoPath = "";


    @Inject
    AddRecentItemsMvpPresenter<AddRecentItemsMvpView> mPresenter;

    @BindView(R.id.voice_recognition_image)
    ImageView voiceRecognitionImage;

    private TextFilter mFilter;
    @BindView(R.id.search_edit_text)
    EditText mEditText;

    @BindView(R.id.delete_image_view)
    ImageView deleteImageView;


    @BindView(R.id.image_view)
    ImageView captureImageView;

    @BindView(R.id.image_view_loopview)
    ImageView imageLoopView; // this image is shown when keyboard open up


    @BindView(R.id.cardview)
    CardView cardSpaceView;


   /* @BindView(R.id.cardview_loop)
    CardView cardLoopView;*/

    @Inject
    LinearLayoutManager mLayoutManager;

    @Inject
    AddRecentItemsAdapter mAdapter;

    private View view;

    @BindView(R.id.empty_recent_data)
    TextView emptyView;

    @BindView(R.id.camera_imageview)
    ImageView cameraImageView;

    @BindView(R.id.loopView)
    LoopView mLoopView;

    @BindView(R.id.loop_layout)
    LinearLayout linearLayout;

    @BindView(R.id.add_item_title_textview)
    TextView mHeadingTextView;

    @BindView(R.id.view_lay)
    RelativeLayout imageLayout;

    @BindView(R.id.scroll_view)
    ScrollView scrollView;

    int heightImageView = 0;

    @BindView(R.id.loop_cardview)
    CardView loopCardView;

    @BindView(R.id.card_loop_lay)
    LinearLayout loopLayout;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


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

       /* int totalHeight = ScreenUtils.getScreenHeight(getActivity());
        int availiableHeight = (int) (totalHeight - (ScreenUtils.getStatusBarHeight(getActivity()) + (2 * (ScreenUtils.getActionBarHeight(getActivity())))));

        int height = (int) ((availiableHeight * 3.0) / 10.0);
        heightImageView = height;
        int width = height + (height / 3);
        captureImageView.getLayoutParams().height = height;
        captureImageView.getLayoutParams().width = width;

        captureImageView.requestLayout();
        captureImageView.setVisibility(View.VISIBLE);*/

        AddItemsFragment fragment = ((AddItemsFragment) getParentFragment());


        if (fragment != null) {

            if (fragment.getMyListData().getItemName() != null) {
                mEditText.setText(((AddItemsFragment) getParentFragment()).getMyListData().getItemName());
                ((AddItemsFragment) getParentFragment()).setItemName(mEditText.getText().toString().trim());
                mHeadingTextView.setText("Edit Items");
            } else {
                mHeadingTextView.setText(getString(R.string.recent_items_actionbar_heading));
            }

            if (fragment.getFilePath() != null) {
                imageLayout.setVisibility(View.VISIBLE);
                cardSpaceView.setVisibility(View.VISIBLE);
                File newFile = new File(((AddItemsFragment) getParentFragment()).getFilePath());
                captureImageView.setImageURI(Uri.fromFile(newFile));
                imageLoopView.setImageURI(Uri.fromFile(newFile));
            } else if (fragment.getPhoto() != null && !fragment.getPhoto().equals("")) {
                imageLayout.setVisibility(View.VISIBLE);


                progressBar.setVisibility(View.VISIBLE);

                Glide.with(this)
                        .load(AppConstants.BASE_URL + fragment.getPhoto())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                progressBar.setVisibility(View.GONE);
                                cardSpaceView.setVisibility(View.VISIBLE);
                                captureImageView.setImageDrawable(resource);
                                return false;
                            }
                        }).into(captureImageView);

                Glide.with(getBaseActivity()).load(AppConstants.BASE_URL + fragment.getPhoto()).into(imageLoopView);
            }

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

        } else if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {
            onSelectFromGalleryResult(data);
        } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            onCaptureImageResult(data);
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
      /*  AddItemsFragment mParentFragment = ((AddItemsFragment) getParentFragment());
        if (mParentFragment.getMyListData().getItemId() != 0) {
            mParentFragment.setActionbarTitle(getResources().getString(R.string.recent_items_fragment_edit_title));

        }*/

    }

    @Override
    public void onPause() {
        super.onPause();

        if (getActivity() != null) {
            hideKeyboard();
            ((MainActivity) getActivity()).showTabs();
            ((MainActivity)getActivity()).showHideTitle(true);
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
        imageLayout.setVisibility(View.GONE);
        ((MainActivity)getBaseActivity()).showHideTitle(false);

        if (imageLoopView.getDrawable() != null) {
            linearLayout.setBackgroundColor(Color.parseColor("#BF000000"));  // BF - 75% // 80 - 50%
            mLoopView.setCenterTextColor(Color.parseColor("#FFFFFF"));
            loopLayout.setVisibility(View.VISIBLE);
        }

        ((MainActivity) getActivity()).hideTabs();
    }


    private void onKeyboardHidden() {
        ((MainActivity)getBaseActivity()).showHideTitle(true);
        imageLayout.setVisibility(View.VISIBLE);
        loopLayout.setVisibility(View.GONE);
        linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        mLoopView.setCenterTextColor(Color.parseColor("#313131"));
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
                    fragment.getMyListData().getStatusId(), fragment.getFilePath());
        } else {

            // Creating new item
            mPresenter.onAddItems(0, mEditText.getText().toString().trim(),
                    "", "", fragment.getMyListData().getListId(),
                    fragment.getMyListData().getListName(), "", "", 0, fragment.getFilePath());
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


    @OnClick(R.id.camera_imageview)
    public void onCameraClick() {

        if (mEditText.getText().toString().trim().equals("")) {
            showDialog();
            return;
        }


        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(getBaseActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                selectImage();

            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 126);

            }
        } else {
            selectImage();
        }


    }


    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery",};
        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseActivity());
        builder.setTitle("Select Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Camera")) {
                    dispatchTakePictureIntent();
                } else if (items[item].equals("Gallery")) {
                    galleryIntent();
                }
            }
        });
        builder.show();
    }


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    private void onCaptureImageResult(Intent data) {

        if (currentPhotoPath.equals("")) {
            return;
        }
        imageLayout.setVisibility(View.VISIBLE);
        cardSpaceView.setVisibility(View.VISIBLE);
        //   cardLoopView.setVisibility(View.VISIBLE);
        File file = new File(currentPhotoPath);

        if (!file.exists()) {
            return;
        }
        Uri uri = Uri.fromFile(file);

        ImageUtils imageUtils = new ImageUtils(getBaseActivity(), captureImageView);

        String imagePath = imageUtils.compressImage(uri);


        if (imageUtils.getBitmap() != null) {
            captureImageView.setImageBitmap(imageUtils.getBitmap());
            imageLoopView.setImageBitmap(imageUtils.getBitmap());
        } else {
            captureImageView.setImageURI(uri);
            imageLoopView.setImageURI(uri);
        }

        if (((AddItemsFragment) getParentFragment()) != null) {
            ((AddItemsFragment) getParentFragment()).setFilePath(imagePath);
        }

    }

    private void onSelectFromGalleryResult(Intent data) {
        imageLayout.setVisibility(View.VISIBLE);
        cardSpaceView.setVisibility(View.VISIBLE);
        //    cardLoopView.setVisibility(View.VISIBLE);
        Uri picUri = data.getData();

        ImageUtils imageUtils = new ImageUtils(getBaseActivity(), captureImageView);

        String imagePath = imageUtils.compressImage(picUri);

        if (imageUtils.getBitmap() != null) {
            captureImageView.setImageBitmap(imageUtils.getBitmap());
            imageLoopView.setImageBitmap(imageUtils.getBitmap());
        } else {
            captureImageView.setImageURI(picUri);
            imageLoopView.setImageURI(picUri);
        }

        if (((AddItemsFragment) getParentFragment()) != null) {
            ((AddItemsFragment) getParentFragment()).setFilePath(imagePath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getBaseActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getBaseActivity().getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getBaseActivity(),
                        "com.mcn.honeydew.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }
}
