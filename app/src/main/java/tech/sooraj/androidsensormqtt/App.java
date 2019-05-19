package tech.sooraj.androidsensormqtt;

import android.app.Application;
import android.util.Log;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application {
// Put the onCreate code as you obtained from the post link you reffered

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TEST","APP");
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}