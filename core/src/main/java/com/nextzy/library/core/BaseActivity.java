package com.nextzy.library.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Akexorcist on 5/1/2017 AD.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutView());
        bindView();
        setupView();
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    restoreArgument(bundle);
                }
            }
            initialize();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restoreInstanceState(savedInstanceState);
        restoreView();
    }

    /**
     * Activity's layout resource id
     */
    public abstract int getLayoutView();

    /**
     * Binding the view after layout inflated
     */
    public abstract void bindView();

    /**
     * Setup the view state
     * e.g. {@link View#setOnClickListener(View.OnClickListener)}  }
     */
    public abstract void setupView();

    /**
     * Retrieve the bundle object from previous activity or fragment
     * This method wouldn't call if bundle object is empty
     * <p>
     * For Fragment, will return the result from {@link Fragment#getArguments()}
     * For Activity, will return the result from {@link Activity#getIntent()}
     *
     * @param bundle Restored bundle
     */
    public abstract void restoreArgument(Bundle bundle);

    /**
     * The logic code to execute after activity was created
     */
    public abstract void initialize();

    /**
     * Restore the instance state after revive from the hell
     *
     * @param savedInstanceState Restored bundle object
     */
    public abstract void restoreInstanceState(Bundle savedInstanceState);

    /**
     * Setup view state after instance state restored
     * This method will running after {@link #restoreInstanceState(Bundle)} was finished
     */
    public abstract void restoreView();

    /**
     * Save the instance state before view destroy then revive from the hell
     *
     * @param outState Bundle object to save the instance state
     */
    public abstract void saveInstanceState(Bundle outState);
}
