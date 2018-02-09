package in.codepeaker.notely.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;

import in.codepeaker.notely.R;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                finish();
                ActivityOptionsCompat activityOptionsCompat = makeSceneTransitionAnimation(SplashScreenActivity.this
                        , findViewById(R.id.textview_splash)
                        , ViewCompat.getTransitionName(findViewById(R.id.textview_splash)));
                startActivity(new Intent(SplashScreenActivity.this, NotesListActivity.class), activityOptionsCompat.toBundle());
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(runnable, 1000);
    }
}
