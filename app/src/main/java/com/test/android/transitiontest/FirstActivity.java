package com.test.android.transitiontest;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class FirstActivity extends AppCompatActivity {

    ImageView originalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        setWidgets();

        setTransition();
    }

    private void setWidgets() {
        originalView = (ImageView)findViewById(R.id.trans_original);
    }

    private void setTransition() {
        final Intent i = new Intent(FirstActivity.this, SecondActivity.class);

        originalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(FirstActivity.this,
                            originalView, "test");
                    startActivity(i, options.toBundle());
                }
            }
        });
    }
}
