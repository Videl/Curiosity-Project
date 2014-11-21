package org.fslhome.curiosity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingsActivity extends ActionBarActivity {


    protected void onCreate(Bundle savedInstanceState) {
    	// http://developer.android.com/guide/topics/ui/layout/listview.html
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        Button b1 = (Button) findViewById(R.id.settings_about_us);
        b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				exemplo();
			}
		});
    }
    
    private void exemplo() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("About Us");
    	builder.setMessage("Developed By Thibaut Smith and Mateus Saunier");
    	
    	builder.create().show();
    }

}
