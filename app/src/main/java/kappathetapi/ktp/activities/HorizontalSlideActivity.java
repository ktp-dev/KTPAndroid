package kappathetapi.ktp.activities;

import android.app.Activity;
import android.os.Bundle;

import kappathetapi.ktp.R;


public class HorizontalSlideActivity extends Activity {

    private int mStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStartCount = 1;

        if (savedInstanceState == null) {
            this.overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_out_left
            );
        } else {
            mStartCount = 2;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mStartCount > 1) {
            this.overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_right
            );
        } else if (mStartCount == 1) {
            mStartCount++;
        }

    }
}
