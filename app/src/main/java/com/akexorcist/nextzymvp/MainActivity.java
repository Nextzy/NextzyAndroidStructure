package com.akexorcist.nextzymvp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.akexorcist.nextzymvp.network.MockManager;

public class MainActivity extends AppCompatActivity {
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
            MockManager
                    .getInstance()
                    .getSomeResult("0926621664")
                    .subscribe(someResult -> {
                        Log.e("Check", "result");
                    });
        };
    }
}
