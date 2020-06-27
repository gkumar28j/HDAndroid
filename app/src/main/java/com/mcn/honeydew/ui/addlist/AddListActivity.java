package com.mcn.honeydew.ui.addlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.ui.base.BaseActivity;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.utils.KeyboardUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddListActivity extends BaseActivity implements AddListMvpView {

    @Inject
    AddListMvpPresenter<AddListMvpView> mPresenter;

    @BindView(R.id.add_list_edit_text)
    EditText mEditText;
    ImageView leftArrowImgView;

    private int listId = 0;

    public static final int SPEECH_REQUEST_CODE = 321;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, AddListActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);
        mEditText = findViewById(R.id.add_list_edit_text);
        leftArrowImgView = findViewById(R.id.left_arrow_image);


        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(AddListActivity.this);

        setUp();

    }


    @Override
    public void openAddItemFragment(int newItemId) {
      //  hideKeyboard();
      //  finish();
        hideKeyboard();
        Calendar cal = Calendar.getInstance();
        Date listCreationDate = cal.getTime();
        // createdDate = "2018-03-20T08:38:35"
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = mFormat.format(listCreationDate).replace(" ", "T");
        mPresenter.createMyListData(date, mEditText.getText().toString().trim(), newItemId, Integer.toHexString(ContextCompat.getColor(this, R.color.colorPrimary)));
        mEditText.setText("");

    }

    @Override
    public void openMyList(MyHomeListData data) {

        Intent intent = new Intent();
        intent.putExtra(MainActivity.ADD_LIST_ACTIVITY_DATA, new Gson().toJson(data));
        setResult(RESULT_OK,intent);
        finish();
       // onListItemClick(data);
    }

    @Override
    protected void setUp() {
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
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @OnClick(R.id.left_arrow_image)
    public void onLeftArrowClicked(){
        finish();
    }


    @OnClick(R.id.voice_recognition_image)
    public void onVoiceRecognitionClicked(){
        PackageManager pm = this.getPackageManager();

        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(

                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

        if (activities.size() == 0) {
            Toast.makeText(this, "Recognizer Not Found", Toast.LENGTH_SHORT).show();

        } else {
          //  voiceRecognitionImage.setImageResource(R.drawable.ic_voice_recognition);
            displaySpeechRecognizer();
        }
    }

    @OnClick(R.id.done_button)
    public void onDoneButtonClicked(){
        if (mEditText.getText().toString().trim().equals("") || mEditText.getText().toString().trim() == null) {
            showMessage(getString(R.string.error_empty_list_name));
        } else {
            mPresenter.onViewPrepared(mEditText.getText().toString().trim(), listId, "#153359", 12);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        KeyboardUtils.showSoftInput(mEditText, this);
    }

    @Override
    public void onPause() {
        super.onPause();
       hideKeyboard();
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
      //  voiceRecognitionImage.setImageResource(R.drawable.ic_voice_recognition_disable);
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            String spokenText = capitalize(results.get(0).toString().trim());
            mEditText.setText("");

            mEditText.setText(spokenText);
           // getFilter().filter(spokenText);

        }

        super.onActivityResult(requestCode, resultCode, data);
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
