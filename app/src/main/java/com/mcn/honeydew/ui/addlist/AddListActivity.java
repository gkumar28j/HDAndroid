package com.mcn.honeydew.ui.addlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.ui.base.BaseActivity;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.utils.KeyboardUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        hideKeyboard();
        finish();
   /*     hideKeyboard();
        Calendar cal = Calendar.getInstance();
        Date listCreationDate = cal.getTime();
        // createdDate = "2018-03-20T08:38:35"
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = mFormat.format(listCreationDate).replace(" ", "T");
        mPresenter.createMyListData(date, mEditText.getText().toString().trim(), newItemId, Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.colorPrimary)));
        mEditText.setText("");*/

    }

    @Override
    public void openMyList(MyHomeListData data) {
      //  onListItemClick(data);
        finish();
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


}
