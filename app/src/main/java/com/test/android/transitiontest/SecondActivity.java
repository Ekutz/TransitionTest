package com.test.android.transitiontest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        setWidgets();
    }

    private void setWidgets() {

    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
//        super.onBackPressed();
    }
}
