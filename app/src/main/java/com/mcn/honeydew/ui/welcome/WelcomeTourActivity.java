package com.mcn.honeydew.ui.welcome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.mcn.honeydew.R;
import com.mcn.honeydew.ui.base.BaseActivity;
import com.mcn.honeydew.ui.main.MainActivity;

public class WelcomeTourActivity extends BaseActivity {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, WelcomeTourActivity.class);
        return intent;
    }

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private String[] headingText;
    private TextView btnSkip, btnNext;
    TextView takeTourButton;
    RelativeLayout viewPagerlayout;
    RelativeLayout takeTourLayout;
    TextView skipTextView;
    int deviceHeight;
    int devicewidth;
    double screenInches;
    ImageView tourImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome_tour);

      /*  if(getSupportActionBar()!=null){

            getSupportActionBar().setTitle("Version Update");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }*/

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        btnSkip = findViewById(R.id.btn_skip);
        btnNext = findViewById(R.id.btn_next);
        takeTourButton = findViewById(R.id.button_take_tour);
        takeTourLayout = findViewById(R.id.tour_layout);
        viewPagerlayout = findViewById(R.id.view_pager_layout);
        skipTextView = findViewById(R.id.skip_text_view);
        tourImageView = findViewById(R.id.screen_imageview);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        deviceHeight = displayMetrics.heightPixels;
        devicewidth = displayMetrics.widthPixels;

        double wi = (double) devicewidth / (double) displayMetrics.xdpi;
        double hi = (double) deviceHeight / (double) displayMetrics.ydpi;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        screenInches = Math.sqrt(x + y);

        int widthImageView;

        if (screenInches > 5.0) {
            widthImageView = (int) (devicewidth * 0.7f);
        } else {
            widthImageView = (int) (devicewidth * 0.5f);
        }


        tourImageView.getLayoutParams().width = widthImageView;
        tourImageView.getLayoutParams().height = widthImageView;
        tourImageView.requestLayout();

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_screen_1,
                R.layout.welcome_screen_2,
                R.layout.welcome_screen_3,
                R.layout.welcome_screen_4,
                R.layout.welcome_screen_5,
                R.layout.welcome_screen_6};

        headingText = new String[]{

                getString(R.string.screen_1_heading_text),
                getString(R.string.screen_2_heading_text),
                getString(R.string.screen_3_heading_text),
                getString(R.string.screen_4_heading_text),
                getString(R.string.screen_5_heading_text),
                getString(R.string.screen_6_heading_text)


        };

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        //   changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
        takeTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takeTourLayout.setVisibility(View.GONE);
                viewPagerlayout.setVisibility(View.VISIBLE);


            }
        });

        skipTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });
    }

    @Override
    protected void setUp() {

    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        //  prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeTourActivity.this, MainActivity.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText("DONE");
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText("NEXT");
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };


    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);

            ImageView imageView = view.findViewById(R.id.screen_imageview);
            //  int availiableHeight = (int) (height - 2 * (ScreenUtils.getActionBarHeight(getApplicationContext())));

            int widthImageView;

            if (screenInches > 5.0) {
                widthImageView = (int) (devicewidth * 0.7f);
            } else {
                widthImageView = (int) (devicewidth * 0.5f);
            }

            imageView.getLayoutParams().width = widthImageView;
            imageView.getLayoutParams().height = widthImageView;
            imageView.requestLayout();

          /*  TextView headingTextView = view.findViewById(R.id.content_heading_screen);
            TextView descriptionTextview = view.findViewById(R.id.content_description_screen);
            headingTextView.setText(headingText[position]);
            descriptionTextview.setText(headingText[position]);*/

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}