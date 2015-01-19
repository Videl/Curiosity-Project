package org.fslhome.videl.curiosityapplication;

import android.content.Intent;
import android.media.audiofx.BassBoost;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout mLayout = (LinearLayout) findViewById(R.id.homepage_wall);
        List<Button> list_buttons = getDeepButtons(mLayout);

        for(Button button : list_buttons)
        {
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent myIntent = new Intent(MainActivity.this, CuriositiesActivity.class);
                    CharSequence text = ((Button) arg0).getText();
                    myIntent.putExtra("org.fslhome.curiosity.curiosities.button_clicked", text); // Optional parameters
                    Log.i("Videl", "Clicked on button: " + text);
                    startActivity(myIntent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent myIntent = new Intent(this, SettingsActivity.class);
            startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    private List<Button> getDeepButtons(LinearLayout mLayout)
    {
        List<Button> list_buttons = new ArrayList<Button>();

        for(int i = 0; i < mLayout.getChildCount(); i++)
        {
            View aview = mLayout.getChildAt(i);
            if(aview instanceof Button)
            {
                // Add click listener to each of them
                Log.i("Videl", "Button detected!");
                list_buttons.add((Button) aview);
            }
            else if(aview instanceof LinearLayout)
            {
                // Recursive
                list_buttons.addAll(getDeepButtons((LinearLayout) aview));
            }
        }

        return list_buttons;
    }

}
