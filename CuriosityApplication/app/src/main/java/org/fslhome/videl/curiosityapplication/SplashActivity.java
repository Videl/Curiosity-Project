package org.fslhome.videl.curiosityapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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

        boolean bootSoundValue = prefs.getBoolean("general_boot_sound_checkbox", false);

        Log.i("Videl", "Splash preference value: " + splashSettingValue);

        if(bootSoundValue)
        {
            startActivity(nextActivity);
        }
        else
        {
            final Context forThread = this;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    MediaPlayer mediaPlayer = MediaPlayer.create(forThread, R.raw.the_tardis);

                    mediaPlayer.start();
                }
            }).start();
        }



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
