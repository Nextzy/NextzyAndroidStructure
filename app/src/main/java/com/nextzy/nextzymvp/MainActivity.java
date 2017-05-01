package com.nextzy.nextzymvp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nextzy.nextzymvp.network.response.SomeResult;

import io.reactivex.observers.DisposableSingleObserver;


public class MainActivity extends AppCompatActivity implements ActivityContractor {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(onButtonClick());

    }

    private View.OnClickListener onButtonClick() {
        return v -> {
            MockNetworkCacheManager.getInstance(this)
                    .getSomeResultService()
                    .subscribe(new DisposableSingleObserver<SomeResult>() {
                        @Override
                        public void onSuccess(SomeResult someResult) {
                            Log.e("Check", "Result : " + someResult.getName());
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });

//            MockManager
//                    .getInstance()
//                    .getSomeResult("0926621664")
//                    .subscribe(someResult -> {
//                        Log.e("Check", "result");
//                    });
        };
    }

    boolean isActivityActive;

    @Override
    protected void onStart() {
        super.onStart();
        isActivityActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityActive = false;
    }

    @Override
    public boolean isActive() {
        return isActivityActive;
    }

    @Override
    public boolean isInactive() {
        return isActivityActive;
    }

    private static final String KEY_IS_ACTIVITY_ACTIVE = "is_activity_active";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_ACTIVITY_ACTIVE, isActivityActive);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isActivityActive = savedInstanceState.getBoolean(KEY_IS_ACTIVITY_ACTIVE);
    }
}
