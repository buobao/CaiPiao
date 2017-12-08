package com.king.turman.caipiao.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.king.turman.caipiao.R;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by diaoqf on 2017/12/8.
 */

public abstract class BaseActivity extends RxAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        View v = getLayoutInflater().inflate(getLayoutResId(),null);
        FrameLayout flt = (FrameLayout) findViewById(R.id.frame);
        flt.addView(v, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        findViewById(R.id.back).setOnClickListener(v1 -> back());
    }

    protected abstract @LayoutRes int getLayoutResId();

    protected void back() {
        onBackPressed();
    }
}
