package com.mcn.honeydew.ui.myListDetail;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.OnOutsidePhotoTapListener;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.mcn.honeydew.R;
import com.mcn.honeydew.utils.AppConstants;
import com.mcn.honeydew.utils.SwipeBackActivity;
import com.mcn.honeydew.utils.SwipeBackLayout;

public class MyListDetailImageActivity extends SwipeBackActivity {

    PhotoView imageView;

    SwipeBackLayout swipeBackLayout;

    String url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list_detail_image);

      /*  if(getSupportActionBar()!=null){

            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }*/

        swipeBackLayout = (SwipeBackLayout) findViewById(R.id.swipe_layout);
        imageView = findViewById(R.id.pdf_image);

        if (getIntent() != null) {
            String desc = getIntent().getStringExtra("url");
            url = AppConstants.BASE_URL + desc;
        }

        initViews();

    }


    private void initViews() {
        if(url==null){
            return;
        }

        Glide.with(this).load(url).into(imageView);
        swipeBackLayout.setEnableFlingBack(true);
        imageView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {

                finish();

            }
        });

        imageView.setOnOutsidePhotoTapListener(new OnOutsidePhotoTapListener() {
            @Override
            public void onOutsidePhotoTap(ImageView imageView) {
                finish();
            }
        });


        setDragDirectMode(SwipeBackLayout.DragDirectMode.VERTICAL);
        swipeBackLayout.setOnPullToBackListener(new SwipeBackLayout.SwipeBackListener() {
            @Override
            public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
                //  progressBar.setProgress((int) (progressBar.getMax() * fractionAnchor));
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
