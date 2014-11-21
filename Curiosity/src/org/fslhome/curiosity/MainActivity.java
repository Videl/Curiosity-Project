package org.fslhome.curiosity;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Get references to all places (in buttons)
        // http://stackoverflow.com/questions/5141549/get-reference-to-views-in-my-android-activity
        LinearLayout mLayout = (LinearLayout) findViewById(R.id.homepage_wall);
        List<Button> list_buttons = getDeepButtons(mLayout);
        
        for(Button button : list_buttons)
        {
    		button.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View arg0) {
    				Intent myIntent = new Intent(MainActivity.this, CuriositiesActivity.class);
    				CharSequence text = ((Button) arg0).getText();
    	        	myIntent.putExtra("button_clicked", text); // Optional parameters
            		Log.i("Videl", "Clicked on button: " + text);
    	        	startActivity(myIntent);
    			}
    		});
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
        	//myIntent.putExtra("key", value); //Optional parameters
        	MainActivity.this.startActivity(myIntent);
            return true;
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
