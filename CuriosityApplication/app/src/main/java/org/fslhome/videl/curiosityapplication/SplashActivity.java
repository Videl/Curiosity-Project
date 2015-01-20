package org.fslhome.videl.curiosityapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by videl on 20/01/15.
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent nextActivity = new Intent(this, MainActivity.class);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        boolean splashSettingValue = prefs.getBoolean("general_splash_checkbox", false);

        //"general_splash_checkbox", "false");
        //findPreference();

        Log.i("Videl", "Splash preference value: " + splashSettingValue);


        if(splashSettingValue)
        {
            startActivity(nextActivity);
        }
        else
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("Videl", "End of Splash.");
                            startActivity(nextActivity);
                        }
                    });
                }
            }).start();
        }
    }
}
