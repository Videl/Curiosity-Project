package org.fslhome.videl.curiosityapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.fslhome.videl.curiosityapplication.model.CuriosityDBAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        first_time_initilisation_checker();

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
                    Log.d("Videl", "Clicked on button: " + text);
                    startActivity(myIntent);
                }
            });
        }
    }

    private void first_time_initilisation_checker() {

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("CurioLog", "First time");

            // first-time task
            insertDataIntoDatabase();

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
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
            return true;
        } else if (id == R.id.action_drop_db) {

            eraseDatabase();

            Toast.makeText(this, "Erased DB", Toast.LENGTH_SHORT).show();

            return true;
        } else if (id == R.id.action_clean_db) {

            insertDataIntoDatabase();

            Toast.makeText(this, "Added default data.", Toast.LENGTH_SHORT).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void eraseDatabase() {
        CuriosityDBAdapter dbAdapter = new CuriosityDBAdapter(this);
        dbAdapter.open_write().completeDatabaseErase();
        dbAdapter.close();
    }

    private void insertDataIntoDatabase() {
        CuriosityDBAdapter dbAdapter = new CuriosityDBAdapter(this);
        dbAdapter.open_write();
        dbAdapter.addNewCuriosityData("Maynooth", "The Roost", "Every Thursday night, this place is rocking it!", 53.38080, -6.59194);
        dbAdapter.addNewCuriosityData("Maynooth", "Home", "Developper's home ;)", 53.37441, -6.58603);
        dbAdapter.addNewCuriosityData("Maynooth", "Swimming pool", "A really nice swimming pool. Open every day for staff and student.", 53.378845, -6.596513);
        dbAdapter.addNewCuriosityData("Maynooth", "Braddys", "Every Tuesday evening from 10 to midnight, free Jazz music!", 53.381371, -6.590436);
        dbAdapter.addNewCuriosityData("Maynooth", "Museum", "For an hour to spare, you can visit the Museum of Antique studying tools in Maynooth University.", 53.378613, -6.598182);
        dbAdapter.addNewCuriosityData("Dublin", "Queen of Tarts", "Delicious pies and whatnot are sold here.", 53.344294, -6.268979);
        dbAdapter.addNewCuriosityData("Dublin", "Oscar Wilde Memorial Statue", "This statue in the Merrion Square will bring you back in time. Enjoy the Square too ;).", 53.340646, -6.250571);
        dbAdapter.addNewCuriosityData("Dublin", "Phoenix Park", "This park is really big. And you will find roaming deers too! Don't scare them though!", 53.356431, -6.331944);
        dbAdapter.addNewCuriosityData("Dublin", "Trinity College", "The famous University of Dublin, Trinity College. The Book of Kells can be seen here too.", 53.344477, -6.259334);
        dbAdapter.addNewCuriosityData("Montpellier", "Antigone", "This is the center of the town. Every building has a Roman architecture.", 43.608196, 3.887617);
        dbAdapter.addNewCuriosityData("Montpellier", "Palavas", "The beach closest to Montpellier. Lots of food are sold by traveling merchant on the beach!", 43.548730, 3.995685);
        dbAdapter.addNewCuriosityData("Montpellier", "Train Station", "The central point that connects the city to every other city in France. Lots of transportation have their stops too.", 43.603357, 3.880650);
        dbAdapter.addNewCuriosityData("Montpellier", "Arc de Triomphe", "The local Arc de Triomphe. It is said it is a relic of the past, from when the Romans were here.", 43.611106, 3.872646);
        dbAdapter.addNewCuriosityData("Montpellier", "Home", "Developper's home ;).", 43.622445, 3.850667);
        dbAdapter.addNewCuriosityData("Versailles", "Palace of Versailles", "Here is the picturesque Palace of Versailles.", 48.804320, 2.122018);
        dbAdapter.addNewCuriosityData("Versailles", "Train Station", "One of the three train stations connecting Versailles to Paris.", 48.800204, 2.129099);
        dbAdapter.addNewCuriosityData("Versailles", "Bassin de Neptune", "Trully an antique bringing us back to the times of the Kings.", 48.808733, 2.122492);
        dbAdapter.addNewCuriosityData("Versailles", "Great Stables of the Palace of Versaille", "You can find many horse shows there. For all Horse-lovers!", 48.803672, 2.128999);
        dbAdapter.close();
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

    public void cleanDB() {
        Toast tt = new Toast(this);
        tt.setText("Restored a clean DB.");
        tt.show();
    }

    public void dropDB() {
        Toast tt = new Toast(this);
        tt.setText("Erased DB.");
        tt.show();
    }

}
