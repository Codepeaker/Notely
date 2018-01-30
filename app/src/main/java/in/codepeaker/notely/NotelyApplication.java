package in.codepeaker.notely;

import android.app.Application;
import android.content.Context;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by github.com/codepeaker on 30/1/18.
 */

public class NotelyApplication extends Application {
    static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
//        MediaManager.init(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Muli-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }

}
