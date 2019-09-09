package com.mcn.honeydew.ui.myListDetail;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.mcn.honeydew.R;
import com.mcn.honeydew.ui.base.BaseActivity;
import com.mcn.honeydew.utils.AppConstants;
import com.mcn.honeydew.utils.TouchImageView;

public class MyListDetailImageActivity extends BaseActivity {


    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list_detail_image);

        if(getSupportActionBar()!=null){

            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        imageView = findViewById(R.id.pdf_image);
        if(getIntent()!=null){

            String desc = getIntent().getStringExtra("url");
            String completeUrl = AppConstants.BASE_URL+desc;
            Glide.with(this).load(completeUrl).skipMemoryCache(true).into(imageView);

        }
    }

    @Override
    protected void setUp() {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
