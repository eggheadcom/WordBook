package jp.kenschool.tango1;


import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

/**
 * A custom application class, which performs setup of the Typefaces used as Icon Sets.
 */
public class BootStrapApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        // setup default typefaces
        TypefaceProvider.registerDefaultIconSets();
    }

}