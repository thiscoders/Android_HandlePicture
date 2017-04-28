package meitu.android.handlepicture;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

/**
 * Created by ye on 2017/4/12.
 */

public class AboutActivity extends AppCompatActivity {
    private final String TAG = AboutActivity.class.getSimpleName();
    private NumberPicker np_test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        np_test = (NumberPicker) findViewById(R.id.np_test);

        np_test.setMinValue(2);
        np_test.setMaxValue(60);
        np_test.setValue(6);

       /* np_test.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                Log.i(TAG, "format..." + value);
                return value + "";
            }
        });

        np_test.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.i(TAG, "valuechange..." + oldVal + "..." + newVal);
            }
        });

        np_test.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                Log.i(TAG, "onscroll..." + scrollState);
            }
        });*/
    }

    public void getValue(View view) {
        Toast.makeText(AboutActivity.this, np_test.getValue() + "", Toast.LENGTH_SHORT).show();
    }
}
