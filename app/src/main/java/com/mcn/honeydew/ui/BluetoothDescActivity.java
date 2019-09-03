package com.mcn.honeydew.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.mcn.honeydew.R;

import butterknife.BindView;

public class BluetoothDescActivity extends AppCompatActivity {

    TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_desc);

        if(getSupportActionBar()!=null){

            getSupportActionBar().setTitle("Version Update");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        mTextView = findViewById(R.id.bluetooth_description_textview);
        if(getIntent()!=null){

            String desc = getIntent().getStringExtra("description");
            mTextView.setText(desc);

        }
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
