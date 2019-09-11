package com.mcn.honeydew.ui.myListDetail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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

    ProgressBar progressBar;

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
        progressBar = findViewById(R.id.progress_bar);

        if (getIntent() != null) {
            String desc = getIntent().getStringExtra("url");
            url = AppConstants.BASE_URL + desc;
        }

        initViews();

    }


    private void initViews() {
        if (url == null) {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        // Glide.with(this).load(url).into(imageView);

        Glide.with(this)
                .load(url)
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
                        return false;
                    }
                }).into(imageView);


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
